package com.example.rgt.CaseLine.Repository;

import com.example.rgt.CaseLine.entity.Case;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CaseRepository extends JpaRepository<Case, Integer> {
    Case findById(int Case_id);

    // this for Owner to get all cases in thier Organization
    Case findAllByOrgId(int org_id);

    // for admin to get their all Created Case
    List<Case> findAllByCreatedBy(int Admin_id);


}
