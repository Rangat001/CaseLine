package com.example.rgt.CaseLine.Controller;

import com.example.rgt.CaseLine.DTO.Post_DTO;
import com.example.rgt.CaseLine.Repository.UserRepository;
import com.example.rgt.CaseLine.Service.CaseService;
import com.example.rgt.CaseLine.Service.UserService;
import com.example.rgt.CaseLine.entity.Case_Group;
import com.example.rgt.CaseLine.entity.User;
import com.example.rgt.CaseLine.entity.post;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("Case")
public class CaseController {

    @Autowired
    private CaseService caseService;

    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;

    @Operation(summary = "Get case details", description = "Retrieve detailed information about a specific case", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Case details retrieved successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized access"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @Parameter(name = "id", description = "Case ID", required = true)
    @GetMapping("{id}")
    public ResponseEntity<?> getCaseDetails(@PathVariable int id) {
        try{
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if(authentication == null || !authentication.isAuthenticated()) {
                return ResponseEntity.status(401).body("Unauthorized access");
            }
            return ResponseEntity.ok(caseService.getCaseDetails(id));
        }catch (Exception e){
            return ResponseEntity.status(500).body("Error retrieving case details: " + e.getMessage());
        }
    }

    //                                  Check Member
    @Operation(summary = "Check case membership", description = "Check if authenticated user is a member of the specified case", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Membership status returned",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Boolean.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    @Parameter(name = "caseId", description = "Case ID", required = true)
    @GetMapping("/{caseId}/check-membership")
    public ResponseEntity<Boolean> checkCaseMembership(@PathVariable int caseId) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            User user = userRepository.findByName(authentication.getName()); // Extract user ID from JWT token
            boolean isMember = caseService.partOfCase(user.getUser_id(),user.getOrg_id(), caseId);
            return ResponseEntity.ok(isMember);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(false);
        }
    }

    //                              Create Post
    @Operation(summary = "Create post in case", description = "Create a new post/update in a specific case", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Post created successfully"),
            @ApiResponse(responseCode = "400", description = "Post content cannot be empty"),
            @ApiResponse(responseCode = "401", description = "Unauthorized access"),
            @ApiResponse(responseCode = "403", description = "User not part of this case"),
            @ApiResponse(responseCode = "500", description = "Error creating post")
    })
    @Parameter(name = "caseId", description = "Case ID", required = true)
    @PostMapping("post/{caseId}")
    public ResponseEntity<?> createPost(@PathVariable int caseId, @RequestBody post postContent) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            User user = userRepository.findByName(authentication.getName());
            if(authentication == null || !authentication.isAuthenticated()) {
                return ResponseEntity.status(401).body("Unauthorized access");
            }
            if(postContent == null || postContent.getContent() == null || postContent.getContent().isEmpty()) {
                return ResponseEntity.badRequest().body("Post content cannot be empty");
            }
            if(caseService.partOfCase(user.getUser_id(),user.getOrg_id(),caseId)){
                postContent.setCase_id(caseId);
                postContent.setPosted_by(user.getUser_id());
                caseService.createPost(caseId, postContent);
            }
            else {
                return ResponseEntity.status(403).body("You are not part of this case");
            }
            return ResponseEntity.ok("Post created successfully");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error creating post: " + e.getMessage());
        }
    }

    //                                  Edit Post
    @Operation(summary = "Update post", description = "Update an existing post in a case", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Post updated successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized access"),
            @ApiResponse(responseCode = "403", description = "User not part of this case"),
            @ApiResponse(responseCode = "500", description = "Error updating post")
    })
    @Parameter(name = "Case_id", description = "Case ID", required = true)
    @PutMapping("post/{Case_id}")
    public ResponseEntity<?> updatePost(@PathVariable int Case_id, @RequestBody post postContent) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            User user = userRepository.findByName(authentication.getName());
            if(authentication == null || !authentication.isAuthenticated()) {
                return ResponseEntity.status(401).body("Unauthorized access");
            }
            if(caseService.partOfCase(user.getUser_id(),user.getOrg_id(),Case_id)){
                postContent.setCase_id(Case_id);
                postContent.setPosted_by(user.getUser_id());
                caseService.updatePost(Case_id, postContent);
            }
            else {
                return ResponseEntity.status(403).body("You are not part of this case");
            }
            return ResponseEntity.ok("Post updated successfully");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error updating post: " + e.getMessage());
        }
    }

    //                                  Delete Post
    @Operation(summary = "Delete post", description = "Delete a post from a case", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Post deleted successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized access"),
            @ApiResponse(responseCode = "500", description = "Error deleting post")
    })
    @Parameters({
            @Parameter(name = "caseId", description = "Case ID", required = true),
            @Parameter(name = "postId", description = "Post ID", required = true)
    })
    @DeleteMapping("post/{caseId}/{postId}")
    public ResponseEntity<?> deletePost(@PathVariable int caseId, @PathVariable int postId) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            User user = userRepository.findByName(authentication.getName());

            if (authentication == null || !authentication.isAuthenticated()) {
                return ResponseEntity.status(401).body("Unauthorized access");
            }
            caseService.deletePost(caseId, user.getUser_id(), user.getOrg_id(), postId);

            return ResponseEntity.ok("Post deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error deleting post: " + e.getMessage());
        }
    }


    //    These two methods check Editability and Check deleatibility is used for check authority so not nned to check authority in editpost and delete post mathods
    //     call these function first befor calling editpost and delete post.


    //                                        check authority to edit the post
    @Operation(summary = "Check post edit permission", description = "Check if user has permission to edit a specific post", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User can edit the post"),
            @ApiResponse(responseCode = "401", description = "Unauthorized access"),
            @ApiResponse(responseCode = "403", description = "User not eligible to edit this post"),
            @ApiResponse(responseCode = "500", description = "Error checking post editability")
    })
    @Parameters({
            @Parameter(name = "caseId", description = "Case ID", required = true),
            @Parameter(name = "postId", description = "Post ID", required = true)
    })
    @GetMapping("editablility/{caseId}/{postId}")
    public ResponseEntity<?> checkeditability(@PathVariable int caseId, @PathVariable int postId) {
        // If the user ownes the Post or is an editor in the case
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            User user = userRepository.findByName(authentication.getName());
            if(authentication == null || !authentication.isAuthenticated()) {
                return ResponseEntity.status(401).body("Unauthorized access");
            }
            if(caseService.IsPostOwner(user.getUser_id(),postId) || caseService.userRole_IN_case(user.getUser_id(), user.getOrg_id(), caseId) == Case_Group.role.editor) {
                return ResponseEntity.ok("User can edit the post");

            } else {
                return ResponseEntity.status(403).body("You are not eligible to edit this post");
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error checking post editability: " + e.getMessage());
        }
    }


    //                                          Check ability to delete the post
    @Operation(summary = "Check post delete permission", description = "Check if user has permission to delete a specific post", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User can delete the post"),
            @ApiResponse(responseCode = "401", description = "Unauthorized access"),
            @ApiResponse(responseCode = "403", description = "User not eligible to delete this post"),
            @ApiResponse(responseCode = "500", description = "Error checking post deletability")
    })
    @Parameters({
            @Parameter(name = "caseId", description = "Case ID", required = true),
            @Parameter(name = "postId", description = "Post ID", required = true)
    })
    @GetMapping("deletability/{caseId}/{postId}")
    public ResponseEntity<?> checkdeletability(@PathVariable int caseId, @PathVariable int postId) {
        // If the user ownes the Post
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            User user = userRepository.findByName(authentication.getName());
            if(authentication == null || !authentication.isAuthenticated()) {
                return ResponseEntity.status(401).body("Unauthorized access");
            }
            if(caseService.IsPostOwner(user.getUser_id(),postId)) {
                return ResponseEntity.ok("User can delte the post");

            } else {
                return ResponseEntity.status(403).body("You are not eligible to edit this post");
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error checking post editability: " + e.getMessage());
        }
    }


    @Operation(summary = "Get all posts in case", description = "Retrieve all posts/updates for a specific case", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Posts retrieved successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = List.class))),
            @ApiResponse(responseCode = "500", description = "Error retrieving case posts")
    })
    @Parameter(name = "caseId", description = "Case ID", required = true)
    @GetMapping("{caseId}/posts")
    public ResponseEntity<?> getCasePosts(@PathVariable int caseId) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            if(authentication == null || !authentication.isAuthenticated()) {
                return ResponseEntity.status(401).body("Unauthorized access");
            }

            List<post> posts = caseService.getCasePosts(caseId,authentication.getName());
            if (posts == null || posts.isEmpty()) {
                return ResponseEntity.status(404).body("No posts found for this case");
            }
            //                    Need to find an efficient solution, but for now it is ok

            List<Post_DTO> postDTOList = new ArrayList<>();
            for (post p : posts) {
                Post_DTO postDTO = new Post_DTO(p);
                postDTO.posted_by = userRepository.findNameById(p.getPosted_by());
                postDTOList.add(postDTO);
            }
            return ResponseEntity.ok(postDTOList);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error retrieving case posts: " + e.getMessage());
        }
    }
}
