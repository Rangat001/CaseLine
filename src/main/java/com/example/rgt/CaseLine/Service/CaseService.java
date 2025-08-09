package com.example.rgt.CaseLine.Service;

import com.example.rgt.CaseLine.Repository.*;
import com.example.rgt.CaseLine.entity.Case;
import com.example.rgt.CaseLine.entity.Case_Group;
import com.example.rgt.CaseLine.entity.User;
import com.example.rgt.CaseLine.entity.post;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CaseService {
    @Autowired
    private CaseRepository caseRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private Case_GrupRepository caseGrupRepository;

    @Autowired
    private groupRepository groupRepository;

    @Autowired
    private UserRepository userRepository;

        public Case getCaseDetails(int id) {
            try{
                Case Case = caseRepository.findById(id);
                if(Case == null) {
                    throw new RuntimeException("Case not found with id: " + id);
                }
                return Case;
            }catch (Exception e){
                throw new RuntimeException("Error retrieving case details: " + e.getMessage());
            }
        }

        public void createPost(int caseId, post postContent) {
            try {
                Case existingCase = caseRepository.findById(caseId);
                if(existingCase == null) {
                    throw new RuntimeException("Case not found with id: " + caseId);
                }
                postContent.setCreated_at(LocalDateTime.now());
                postContent.setUpdated_at(LocalDateTime.now());
                postRepository.save(postContent); // This method should be defined in the Case entity

            } catch (Exception e) {
                throw new RuntimeException("Error creating post: " + e.getMessage());
            }
        }

        @Transactional
        public void updatePost(int caseId, post postContent){
            try {
                Case existingCase = caseRepository.findById(caseId);
                if(existingCase == null) {
                    throw new RuntimeException("Case not found with id: " + caseId);
                }
                Optional<post> existingPost = postRepository.findById(postContent.getPost_id());
                if(existingPost == null) {
                    throw new RuntimeException("Post not found with id: " + postContent.getPost_id());
                }
                // Update the post content

                postRepository.updatepostById(postContent.getPost_id(),
                        postContent.getContent(),
                        postContent.getSource_url(),
                        postContent.getMedia_url(),
                        postContent.getStatus());
            } catch (Exception e) {
                throw new RuntimeException("Error updating post: " + e.getMessage());
            }
        }

        public boolean partOfCase(Integer userId, int caseId) {
        try {
            Case existingCase = caseRepository.findById(caseId);
            if (existingCase == null) {
                throw new RuntimeException("Case not found with id: " + caseId);
            }
            //        Group Id for The Perticular case
            int groupId = groupRepository.findGroupIdByCaseId(caseId);
            //              Find Role in the Group
            Case_Group.role role = caseGrupRepository.findRoleByUserIdAndGroupId(userId, groupId);
            if (role != null) return true; // User is part of the case
            return false;
        } catch (Exception e) {
            throw new RuntimeException("Error checking case membership: " + e.getMessage());
        }
        }


    public List<post> getCasePosts(int caseId, String name) {
            try{
                User user = userRepository.findByName(name);
                Case existingCase = caseRepository.findById(caseId);

                if (existingCase == null) {
                    throw new RuntimeException("Case not found with id: " + caseId);
                }

                if(existingCase.getOrgId() == user.getOrg_id()){
                    // User is part of the organization that owns the case
                    List<post> posts = postRepository.findByCaseId(caseId);
                    return posts;
                }
                //      User not a part of the org who own the case
                return new ArrayList<post>();
            }catch (Exception e) {
                throw new RuntimeException("Error retrieving case posts: " + e.getMessage());
            }
    }
}
