package com.example.rgt.CaseLine.Repository;

import com.example.rgt.CaseLine.entity.Case;
import com.example.rgt.CaseLine.entity.Case_Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CaseRepository extends JpaRepository<Case, Integer> {
    Case findById(int Case_id);

    // this for Owner to get all cases in thier Organization
    Case findAllByOrgId(int org_id);

    // for admin to get their all Created Case
    List<Case> findAllByCreatedBy(int Admin_id);

    //Give the ratio of active/close
    @Query("SELECT COUNT(c) FROM Case c WHERE c.createdBy = :adminId AND c.status = com.example.rgt.CaseLine.entity.Case.Status.ongoing")
    Integer countOngoingCases(@Param("adminId") Integer adminId);

    @Query("SELECT COUNT(c) FROM Case c WHERE c.createdBy = :adminId AND c.status = com.example.rgt.CaseLine.entity.Case.Status.closed")
    Integer countClosedCases(@Param("adminId") Integer adminId);


    //cases ongoing for emp
    @Query("""
        SELECT COUNT(DISTINCT c.caseId)
        FROM Case c
        JOIN Case_Group cg
        WHERE c.status = 'ongoing'
          AND cg.id.userId = :userId
    """)
    Integer countOngoingCasesByUser(@Param("userId") Integer userId);

    // Closed cases for emp
    @Query("""
        SELECT COUNT(DISTINCT c.caseId)
        FROM Case c
        JOIN Case_Group cg
        WHERE c.status = 'closed'
          AND cg.id.userId = :userId
    """)
    Integer countClosedCasesByUser(@Param("userId") Integer userId);

    // Get all the cases by Given List of Case Ids
    List<Case> findBycaseIdIn(List<Integer> ids);



}
