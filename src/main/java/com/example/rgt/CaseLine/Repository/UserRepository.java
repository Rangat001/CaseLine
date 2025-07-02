package com.example.rgt.CaseLine.Repository;

import com.example.rgt.CaseLine.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<User,Integer> {
    User findByEmail(String email);

    //                     Find Org_id for Employee creation
    @Query("SELECT org_id FROM User o WHERE o.name = :name")
    int findOrgId_ByName(@Param("name") String name);

}
