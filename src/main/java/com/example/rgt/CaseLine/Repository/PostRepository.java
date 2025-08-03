package com.example.rgt.CaseLine.Repository;

import com.example.rgt.CaseLine.entity.post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<post, Integer> {

    @Query("SELECT COUNT(p) " +
            "FROM post p " +
            "JOIN Case c ON p.case_id = c.caseId " +
            "WHERE c.createdBy = :createdBy")
    Integer countTotalPostsByCreatedBy(@Param("createdBy") Integer createdBy);

    @Modifying
    @Query("UPDATE post p SET p.content = :content, p.source_url = :source_url, p.media_url = :media_url, p.status = :status WHERE p.post_id = :post_id")
    void updatepostById(@Param("post_id") Integer post_id,
                        @Param("content") String content,
                        @Param("source_url") String source_url,
                        @Param("media_url") String media_url,
                        @Param("status") post.Status status);


}
