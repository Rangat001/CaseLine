package com.example.rgt.CaseLine.Repository;

import com.example.rgt.CaseLine.entity.post;
import org.springframework.data.jpa.repository.JpaRepository;
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

}
