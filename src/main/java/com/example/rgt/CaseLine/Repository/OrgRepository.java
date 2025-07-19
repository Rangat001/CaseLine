package com.example.rgt.CaseLine.Repository;

import com.example.rgt.CaseLine.entity.Organization;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface OrgRepository extends JpaRepository<Organization,Integer> {
     @Query("SELECT o FROM Organization o WHERE o.email = :email")
     Organization findByEmail(@Param("email") String email);

     @Query("SELECT org_name FROM Organization o where o.org_id = :id")
     String findorg_nameById(@Param("id") Integer id);

}
