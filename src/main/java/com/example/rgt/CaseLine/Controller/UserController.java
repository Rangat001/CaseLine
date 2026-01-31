package com.example.rgt.CaseLine.Controller;

import com.example.rgt.CaseLine.Repository.Case_GrupRepository;
import com.example.rgt.CaseLine.Repository.UserRepository;
import com.example.rgt.CaseLine.entity.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/User")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private Case_GrupRepository caseGrupRepository;

    //  Check Role of user in Group
    @Operation(summary = "Check user role in case", description = "Get the authenticated user's role in a specific case", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Role retrieved successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "403", description = "User not found"),
            @ApiResponse(responseCode = "404", description = "Role not found for user in this case")
    })
    @Parameter(name = "CaseId", description = "Case ID", required = true)
    @GetMapping("/checkrole/{CaseId}")
    public ResponseEntity<?> checkRole(@PathVariable int CaseId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            User user = userRepository.findByName(authentication.getName());
            String role = caseGrupRepository.findRoleByUserIdAndCaseId(user.getUser_id(), CaseId);
            if (role != null) {
                return ResponseEntity.ok(role);
            }
            return ResponseEntity.status(404).body("Role not found for user in this case");
        }
        return ResponseEntity.status(403).body("User not found");
    }


    // Check Role of User in Org.
    @Operation(summary = "Check user role in organization", description = "Get the authenticated user's role in their organization", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Role retrieved successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = User.Role.class))),
            @ApiResponse(responseCode = "403", description = "User not found"),
            @ApiResponse(responseCode = "404", description = "Role not found in Organization")
    })
    @GetMapping("/checkrole")
    public ResponseEntity<?> checkRole() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            User user = userRepository.findByName(authentication.getName());
            User.Role role = user.getRole();
            if (role != null) {
                return ResponseEntity.ok(role);
            }
            return ResponseEntity.status(404).body("Role not found in Organization");
        }
        return ResponseEntity.status(403).body("User not found");
    }


    //  get the case details by emp for dashboard

}
