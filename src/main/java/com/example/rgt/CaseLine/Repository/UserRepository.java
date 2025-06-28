package com.example.rgt.CaseLine.Repository;

import com.example.rgt.CaseLine.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,Integer> {
    User findByEmail(String email);

}
