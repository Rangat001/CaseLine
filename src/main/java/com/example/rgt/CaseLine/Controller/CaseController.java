package com.example.rgt.CaseLine.Controller;

import com.example.rgt.CaseLine.DTO.Post_DTO;
import com.example.rgt.CaseLine.Repository.UserRepository;
import com.example.rgt.CaseLine.Service.CaseService;
import com.example.rgt.CaseLine.Service.UserService;
import com.example.rgt.CaseLine.entity.Case_Group;
import com.example.rgt.CaseLine.entity.User;
import com.example.rgt.CaseLine.entity.post;
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
                caseService.createPost(Case_id, postContent);
            }
            else {
                return ResponseEntity.status(403).body("You are not part of this case");
            }
            return ResponseEntity.ok("Post updated successfully");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error updating post: " + e.getMessage());
        }
    }

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

    //                         check authority to edit the post
    @GetMapping("{caseId}/{postId}")
    public ResponseEntity<?> checkeditability(@PathVariable int caseId, @PathVariable int postId) {
        // If the user ownes the Post or is an editor in the case
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            User user = userRepository.findByName(authentication.getName());
            if(authentication == null || !authentication.isAuthenticated()) {
                return ResponseEntity.status(401).body("Unauthorized access");
            }
            if(caseService.IsPostOwner(user.getUser_id(),postId) && caseService.userRole_IN_case(user.getUser_id(), user.getOrg_id(), caseId) == Case_Group.role.editor) {
                return ResponseEntity.ok("User can edit the post");

            } else {
                return ResponseEntity.status(403).body("You are not eligible to edit this post");
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error checking post editability: " + e.getMessage());
        }
    }

    //    These two methods check Editability and Check deleatibility is used for check authority so not nned to check authority in editpost and delete post mathods
    //     call these function first befor calling editpost and delete post.


}
