package com.example.rgt.CaseLine.Repository;

import com.example.rgt.CaseLine.entity.CaseGroupId;
import com.example.rgt.CaseLine.entity.Case_Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface Case_GrupRepository extends JpaRepository<Case_Group, CaseGroupId> {

    @Query("SELECT COUNT(DISTINCT cg.id.userId) " +
            "FROM Case_Group cg " +
            "JOIN Case c ON cg.id.caseId = c.caseId " +
            "WHERE c.createdBy = :adminId")
    Integer countUniqueMembersByAdmin(@Param("adminId") Integer adminId);



    @Query("SELECT cg FROM Case_Group cg WHERE cg.id.caseId = :caseId")
    List<Case_Group> findAllByCase_id(@Param("caseId") Integer caseId);

    @Modifying
    @Query("UPDATE Case_Group cg SET cg.role = :role WHERE cg.id.caseId = :caseId AND cg.id.userId = :userId")
    int updateRoleByCaseIdAndUserId(@Param("caseId") Integer caseId,
                                    @Param("userId") Integer userId,
                                    @Param("role") Case_Group.role role);

    @Modifying
    @Query("DELETE FROM Case_Group cg WHERE cg.id.caseId = :caseId AND cg.id.userId = :userId")
    int deleteById(@Param("caseId") Integer caseId,
                   @Param("userId") Integer userId);

   @Query("SELECT cg.role FROM Case_Group cg WHERE cg.id.userId = :userId AND cg.groupId = :groupId")
   Case_Group.role findRoleByUserIdAndGroupId(Integer userId, int groupId);
}
