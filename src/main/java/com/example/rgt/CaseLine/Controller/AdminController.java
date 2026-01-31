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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
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

    @Operation(summary = "Create new case", description = "Admin/Owner creates a new case in the organization", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Case created successfully"),
            @ApiResponse(responseCode = "400", description = "Unauthorized or invalid request")
    })
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

    @Operation(summary = "Get all cases", description = "Retrieve all cases managed by the authenticated admin/owner", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cases retrieved successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = List.class))),
            @ApiResponse(responseCode = "400", description = "Unauthorized access")
    })
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
    @Operation(summary = "Get admin dashboard data", description = "Get dashboard statistics including total cases, posts, team members, and active/close ratio", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Dashboard data retrieved successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = AdminDashboardDTO.class))),
            @ApiResponse(responseCode = "400", description = "Unauthorized access")
    })
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

    @Operation(summary = "Add member to case", description = "Admin/Owner adds a team member to a specific case", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Member added successfully"),
            @ApiResponse(responseCode = "400", description = "User not found or not in same organization")
    })
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

                //             Check if user exists in same organization
                User member = userRepository.findByEmail(email);
                
                if(member == null || member.getOrg_id() != user.getOrg_id()) {
                    return new ResponseEntity<>("User not found or not in the same organization", HttpStatus.BAD_REQUEST);
                }

                caseGroupId.setCaseId(case_id);
                caseGroupId.setUserId(member.getUser_id());

                caseGroup.setId(caseGroupId);
                caseGroup.setRole(role);
                adminService.Add_member(caseGroup);
                return new ResponseEntity<>(HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @Operation(summary = "Get case members", description = "Retrieve all members of a specific case", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Members retrieved successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = List.class))),
            @ApiResponse(responseCode = "400", description = "Unauthorized access")
    })
    @Parameter(name = "id", description = "Case ID", required = true)
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

    @Operation(summary = "Update member role in case", description = "Admin/Owner updates a team member's role in a specific case", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Role updated successfully"),
            @ApiResponse(responseCode = "400", description = "Unauthorized access")
    })
    @Parameters({
            @Parameter(name = "caseId", description = "Case ID", required = true),
            @Parameter(name = "userId", description = "User ID", required = true)
    })
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

    @Operation(summary = "Update case details", description = "Admin/Owner updates case information", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Case updated successfully"),
            @ApiResponse(responseCode = "400", description = "Unauthorized access")
    })
    @Parameter(name = "id", description = "Case ID", required = true)
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

    @Operation(summary = "Remove member from case", description = "Admin/Owner removes a team member from a specific case", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Member removed successfully"),
            @ApiResponse(responseCode = "400", description = "Unauthorized access")
    })
    @Parameters({
            @Parameter(name = "caseId", description = "Case ID", required = true),
            @Parameter(name = "userId", description = "User ID to remove", required = true)
    })
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
