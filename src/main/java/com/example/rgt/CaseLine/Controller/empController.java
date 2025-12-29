package com.example.rgt.CaseLine.Controller;

import com.example.rgt.CaseLine.DTO.AdminDashboardDTO;
import com.example.rgt.CaseLine.DTO.emp_dashboard_DTO;
import com.example.rgt.CaseLine.Repository.UserRepository;
import com.example.rgt.CaseLine.Service.empService;
import com.example.rgt.CaseLine.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("emp")
public class empController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private empService empService;


    @GetMapping("/dashboard")
    public ResponseEntity<?> getInfo(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication != null && authentication.isAuthenticated()){
            User user = userRepository.findByName(authentication.getName());
            if(user.getRole() !=User.Role.admin || user.getRole() !=User.Role.owner){

//                String org_name = adminService.getOrgName(user.getOrg_id());
                int totalCase = empService.countOfCases(user.getUser_id());
                int totalPost = empService.countOfPosts(user.getUser_id());
                int[] ratio = empService.Active_Close_ratio(user.getUser_id());
                emp_dashboard_DTO dashboardData = new emp_dashboard_DTO(
                        totalCase,
                        totalPost,
                        ratio[0],
                        ratio[1]
                );
                return new ResponseEntity<>(dashboardData, HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/cases")
    public ResponseEntity<?> getCasesByEmp() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication != null && authentication.isAuthenticated()){
            User user = userRepository.findByName(authentication.getName());
            if(user.getRole() !=User.Role.admin || user.getRole()!= User.Role.owner){
                return new ResponseEntity<>(empService.empCases(user.getUser_id()), HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
}
