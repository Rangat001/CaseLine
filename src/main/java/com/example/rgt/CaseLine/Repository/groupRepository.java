package com.example.rgt.CaseLine.Repository;

import com.example.rgt.CaseLine.entity.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface groupRepository extends JpaRepository<Group,Integer> {
    @Query("select group_id from Group where case_id = :caseId")
    Integer findGroupIdByCaseId(@Param("caseId") Integer caseId);
}
