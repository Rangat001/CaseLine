package com.example.rgt.CaseLine.Controller;

import com.example.rgt.CaseLine.DTO.OrgLoginRequest;
import com.example.rgt.CaseLine.Repository.OrgRepository;
import com.example.rgt.CaseLine.Service.OrgService;
import com.example.rgt.CaseLine.Service.OrganizationUserDetailsService;
import com.example.rgt.CaseLine.Service.UserDetailServiceImpl;
import com.example.rgt.CaseLine.Service.UserService;
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
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailServiceImpl userDetailService;

    @Autowired
    private OrganizationUserDetailsService OrgDetailService;

    //                                    Org Login Only
    @Autowired
    private DaoAuthenticationProvider orgAuthProvider;

    @Autowired
    private JWTutil jwTutil;

    @Autowired
    private UserService  userService;

    @Autowired
    private OrgService orgService;


    @PostMapping("/signup")
    public ResponseEntity<User> createUser(@RequestBody User userEntity) {
        try {
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
            UserDetails user = userDetailService.loadUserByUsername(userEntity.getEmail());
            String token = jwTutil.generateToken(user.getUsername());
            return new ResponseEntity<>(token,HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Incorrect Credential", HttpStatus.BAD_REQUEST);
        }
    }

    //                                          Organization

    @PostMapping("/org_signup")
    public ResponseEntity<Organization> createOrg(@RequestBody Organization org) {
        try {
            orgService.createOrg(org);
            return new ResponseEntity<>(org, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/org_login")
    public ResponseEntity<?> login_org(@RequestBody OrgLoginRequest org){
        try{
            long start = System.currentTimeMillis();
            Authentication auth = orgAuthProvider.authenticate(
                    new UsernamePasswordAuthenticationToken(org.getEmail(), org.getPassword()));
            System.out.println("Auth Time: " + (System.currentTimeMillis() - start) + "ms");
            String token = jwTutil.generateToken(auth.getName());
            return new ResponseEntity<>(token,HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Incorrect Credential", HttpStatus.BAD_REQUEST);
        }
    }
}
