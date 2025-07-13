package com.example.rgt.CaseLine.Repository;

import com.example.rgt.CaseLine.entity.Case_Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface Case_GrupRepository extends JpaRepository<Case_Group,Integer> {
    @Query("SELECT COUNT(DISTINCT cg.user_id) " +
            "FROM Case_Group cg " +
            "JOIN Case c ON cg.case_id = c.caseId " +
            "WHERE c.createdBy = :adminId")
    Integer countUniqueMembersByAdmin(@Param("adminId") Integer adminId);
}
