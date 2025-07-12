package com.example.rgt.CaseLine.Repository;

import com.example.rgt.CaseLine.entity.Case_Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Case_GrupRepository extends JpaRepository<Case_Group,Integer> {
    
}
