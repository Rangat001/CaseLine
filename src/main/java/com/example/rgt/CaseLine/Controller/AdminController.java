package com.example.rgt.CaseLine.Controller;

import com.example.rgt.CaseLine.Repository.UserRepository;
import com.example.rgt.CaseLine.Service.AdminService;
import com.example.rgt.CaseLine.entity.Case;
import com.example.rgt.CaseLine.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.TimeZone;

@RestController
@RequestMapping("Admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @Autowired
    private UserRepository userRepository;


    @PostMapping("/create_case")
    public ResponseEntity<?>  create_case(@RequestBody Case entity, TimeZone timeZone){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            //           Need to load from Name not from Email;
            User user = userRepository.findByName(authentication.getName());
            if(user.getRole()==User.Role.admin || user.getRole()==User.Role.owner){
                // need to assign org_id and created by and Created_At
                entity.setCreatedBy(user.getUser_id());
                entity.setOrgId(user.getOrg_id());
                entity.setCreatedAt(LocalDateTime.now());
                entity.setUpdatedAt(LocalDateTime.now());
                adminService.createCase(entity);
                return new ResponseEntity<>(HttpStatus.OK);
            }else {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/cases")
    public ResponseEntity<?> getCasesByAdmin() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication != null && authentication.isAuthenticated()){
            User user = userRepository.findByName(authentication.getName());
            if(user.getRole()==User.Role.admin || user.getRole()==User.Role.owner){
                return new ResponseEntity<>(adminService.getCasesByAdminId(user.getUser_id()), HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    // Admin' data Total cases, total posts,Team member under Their case,Active Close Ratio
    @GetMapping("/data")
    public ResponseEntity<?> getInfo(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication != null && authentication.isAuthenticated()){
            User user = userRepository.findByName(authentication.getName());
            if(user.getRole()==User.Role.admin || user.getRole()==User.Role.owner){
                return new ResponseEntity<>(HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
}
