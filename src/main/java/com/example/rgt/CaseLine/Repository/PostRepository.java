package com.example.rgt.CaseLine.Repository;

import com.example.rgt.CaseLine.DTO.CasePostCountDTO;
import com.example.rgt.CaseLine.entity.post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PostRepository extends JpaRepository<post, Integer> {
    List<post> findByCaseId(Integer caseId);

    @Query("SELECT COUNT(p) " +
            "FROM post p " +
            "JOIN Case c ON p.case_id = c.caseId " +
            "WHERE c.createdBy = :createdBy")
    Integer countTotalPostsByCreatedBy(@Param("createdBy") Integer createdBy);

}
