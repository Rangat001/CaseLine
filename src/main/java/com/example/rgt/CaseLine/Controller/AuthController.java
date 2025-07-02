package com.example.rgt.CaseLine.Controller;

import com.example.rgt.CaseLine.DTO.OrgLoginDTO;
import com.example.rgt.CaseLine.Repository.OrgRepository;
import com.example.rgt.CaseLine.Repository.UserRepository;
import com.example.rgt.CaseLine.Service.*;
import com.example.rgt.CaseLine.entity.Organization;
import com.example.rgt.CaseLine.entity.User;
import com.example.rgt.CaseLine.util.JWTutil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailServiceImpl userDetailService;

//    @Autowired
//    private OrganizationUserDetailsService OrgDetailService;

//    //                                    Org Login Only
//    @Autowired
//    private DaoAuthenticationProvider orgAuthProvider;
//
//    //                                   User(Emp) only
//    @Autowired
//    private DaoAuthenticationProvider userAuthProvider;

    @Autowired
    private OrgRepository orgRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JWTutil jwTutil;

    @Autowired
    private UserService  userService;

    @Autowired
    private OrgService orgService;


    @PostMapping("/signup")
    public ResponseEntity<User> createUser(@RequestBody User userEntity) {
        try{
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            if (authentication != null && authentication.isAuthenticated()) {
                int org_id = userRepository.findOrgId_ByName(authentication.getName());
                userEntity.setOrg_id(org_id);
            } else {
                throw new Exception("Not Valid Admin");
            }
            userService.SaveNewUser(userEntity);
            return new ResponseEntity<>(userEntity, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User userEntity){
        try{
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(userEntity.getEmail(),userEntity.getPassword()));
            User temp = userRepository.findByEmail(userEntity.getEmail());
            if(temp.getRole() != User.Role.owner){
                String token = jwTutil.generateToken(userEntity.getEmail());
                return new ResponseEntity<>(token,HttpStatus.OK);
            }
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>("Incorrect Credential", HttpStatus.BAD_REQUEST);
        }
    }

    //                                          Organization

    @PostMapping("/org_signup")
    public ResponseEntity<Organization> createOrg(@RequestBody OrgLoginDTO org) {
        try {
            Organization temp = new Organization();
            temp.setOrg_name(org.getOrg_name());
            temp.setOwner_name(org.getOwner_name());
            temp.setEmail(org.getEmail());
            temp.setOrg_type(org.getOrg_type());
            boolean result = orgService.createOrg(temp);
            if(result){
                Organization ORG = orgRepository.findByEmail(org.getEmail());
                userService.SaveOwner(ORG.getOrg_id(),org.getOwner_name(),org.getEmail(),org.getPassword());
            }else{
                throw new RuntimeException("Error while creating org");
            }
            return new ResponseEntity<>(temp, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/org_login")
    public ResponseEntity<?> login_org(@RequestBody User user){
        try{
            long start = System.currentTimeMillis();
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(user.getEmail(),user.getPassword()));
            System.out.println("Auth Time: " + (System.currentTimeMillis() - start) + "ms");
            // check the user is owner or not
            User temp = userRepository.findByEmail(user.getEmail());
            if(temp.getRole() == User.Role.owner){
                String token = jwTutil.generateToken(user.getEmail());
                return new ResponseEntity<>(token,HttpStatus.OK);
            }
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>("Incorrect Credential", HttpStatus.BAD_REQUEST);
        }
    }
}
