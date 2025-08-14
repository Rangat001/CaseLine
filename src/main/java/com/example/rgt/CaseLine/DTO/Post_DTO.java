package com.example.rgt.CaseLine.DTO;

import com.example.rgt.CaseLine.Repository.UserRepository;
import com.example.rgt.CaseLine.Service.UserService;
import com.example.rgt.CaseLine.entity.post;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;

@Getter
@Setter
public class Post_DTO {


    private int post_id;
    private int case_id;
    private int posted_by_id;
    public String posted_by;
    private String created_at;
    private String content;
    private String updated_at;
    private String source_url;
    private String media_url;
    private post.Status status;

    public Post_DTO(post post) {
        this.post_id = post.getPost_id();
        this.case_id = post.getCase_id();

        this.posted_by_id = post.getPosted_by();
//        this.posted_by = userRepository.findNameById(post.getPosted_by());
        this.created_at = post.getCreated_at().toString();
        this.content = post.getContent();
        this.updated_at = post.getUpdated_at().toString();
        this.source_url = post.getSource_url();
        this.media_url = post.getMedia_url();
        this.status = post.getStatus();
    }
}
