package com.example.rgt.CaseLine.Controller;

import com.example.rgt.CaseLine.DTO.Add_memberDTO;
import com.example.rgt.CaseLine.DTO.AdminDashboardDTO;
import com.example.rgt.CaseLine.DTO.Case_Member_DTO;
import com.example.rgt.CaseLine.Repository.CaseRepository;
import com.example.rgt.CaseLine.Repository.UserRepository;
import com.example.rgt.CaseLine.Service.AdminService;
import com.example.rgt.CaseLine.Service.UserService;
import com.example.rgt.CaseLine.entity.Case;
import com.example.rgt.CaseLine.entity.CaseGroupId;
import com.example.rgt.CaseLine.entity.Case_Group;
import com.example.rgt.CaseLine.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.reactor.ReactorEnvironmentPostProcessor;
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
    @Autowired
    private UserService userService;

    @Autowired
    private CaseRepository caseRepository;


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

                String org_name = adminService.getOrgName(user.getOrg_id());
                int totalCase = adminService.countOfCases(user.getUser_id());
                int totalPost = adminService.countOfPosts(user.getUser_id());
                int totalMembers = adminService.countMembers(user.getUser_id());
                int[] ratio = adminService.Active_Close_ratio(user.getUser_id());
                String acive_close_ratio = ratio[0] + "/" + ratio[1];
                AdminDashboardDTO dashboardData = new AdminDashboardDTO(org_name,totalCase, totalPost, totalMembers, acive_close_ratio);

                return new ResponseEntity<>(dashboardData, HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/add_member")
    public ResponseEntity<?> addMember(@RequestBody Add_memberDTO addMemberDTO){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication != null && authentication.isAuthenticated()){
            User user = userRepository.findByName(authentication.getName());
            if(user.getRole()==User.Role.admin || user.getRole()==User.Role.owner){
                int admin_id = user.getUser_id();
                int case_id = addMemberDTO.getCase_id();
                //             need to find the use_id
                String email = addMemberDTO.getEmail();
                Case_Group.role role = addMemberDTO.getRole();
                Case_Group caseGroup = new Case_Group();
                CaseGroupId caseGroupId = new CaseGroupId();

                //    Only run when the first member added to the Case_Grp
                if(adminService.check_intial_grp(admin_id,case_id)){
                    adminService.intial_grp(admin_id,case_id);
                }

                caseGroupId.setCaseId(case_id);
                caseGroupId.setUserId(userService.findByMail(email).getUser_id());

                caseGroup.setId(caseGroupId);
                caseGroup.setRole(role);
                adminService.Add_member(caseGroup);
                return new ResponseEntity<>(HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/Case/{id}/members")
    public ResponseEntity<?> getCaseMembers(@PathVariable int id) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if(authentication != null && authentication.isAuthenticated()){

            User user = userRepository.findByName(authentication.getName());
            // Check if the user is an admin or owner
            if(user.getRole()==User.Role.admin || user.getRole()==User.Role.owner){

                List<Case_Member_DTO> member = adminService.getMembersByCaseId(id);
                return new ResponseEntity<>(member, HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @PutMapping("/Case/{caseId}/member/{userId}/role")
    public ResponseEntity<?> updateMemberRole(@PathVariable int caseId, @PathVariable int userId, @RequestBody Case_Group.role role) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if(authentication != null && authentication.isAuthenticated()){

            User user = userRepository.findByName(authentication.getName());

            if(user.getRole()==User.Role.admin || user.getRole()==User.Role.owner){
                adminService.editMemberRole(caseId, userId, role);
                return new ResponseEntity<>(HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @PutMapping("/Case/{id}")
    public ResponseEntity<?> updateCase(@PathVariable int id, @RequestBody Case entity) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if(authentication != null && authentication.isAuthenticated()){

            User user = userRepository.findByName(authentication.getName());

            if(user.getRole()==User.Role.admin || user.getRole()==User.Role.owner){
                Case org = caseRepository.findById(id);
                entity.setCaseId(id);
                entity.setUpdatedAt(LocalDateTime.now());
                entity.setCreatedAt(org.getCreatedAt());
                entity.setCreatedBy(org.getCreatedBy());
                entity.setOrgId(org.getOrgId());
                adminService.editCase(entity);
                return new ResponseEntity<>(HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }


    @DeleteMapping("/Case/{caseId}/member/{userId}")
    public ResponseEntity<?> deleteMember(@PathVariable int caseId, @PathVariable int userId) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if(authentication != null && authentication.isAuthenticated()){

            User user = userRepository.findByName(authentication.getName());

            if(user.getRole()==User.Role.admin || user.getRole()==User.Role.owner){
                adminService.deleteMemberFromCase(caseId, userId);
                return new ResponseEntity<>(HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

}
