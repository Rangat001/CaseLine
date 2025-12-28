package com.example.rgt.CaseLine.Repository;

import com.example.rgt.CaseLine.entity.post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<post, Integer> {

    //     Count total post for specific case
    @Query("SELECT COUNT(p) " +
            "FROM post p " +
            "JOIN Case c ON p.case_id = c.caseId " +
            "WHERE c.createdBy = :createdBy")
    Integer countTotalPostsByCreatedBy(@Param("createdBy") Integer createdBy);

    // Count total post made by specific user
    @Query("SELECT COUNT(p) " +
            "FROM post p " +
            "WHERE p.posted_by = :posted_by")
    Integer countTotalPosts(@Param("posted_by") Integer posted_by);


    @Modifying
    @Query("UPDATE post p SET p.content = :content, p.source_url = :source_url, p.media_url = :media_url, p.status = :status,p.updated_at = :updatedtime WHERE p.post_id = :post_id")
    void updatepostById(@Param("post_id") Integer post_id,
                        @Param("content") String content,
                        @Param("source_url") String source_url,
                        @Param("media_url") String media_url,
                        @Param("status") post.Status status,
                        @Param("updatedtime") LocalDateTime updatedtime);


    @Query("SELECT p FROM post p WHERE p.case_id = :caseId")
    List<post> findByCaseId(int caseId);

    @Query("SELECT p.posted_by FROM post p WHERE p.post_id = :postid")
    int checkOwnerByid(int postid);
}
