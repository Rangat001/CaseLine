package com.example.rgt.CaseLine.Repository;

import com.example.rgt.CaseLine.entity.Case;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CaseRepository extends JpaRepository<Case, Integer> {
    Case findById(int Case_id);
}
