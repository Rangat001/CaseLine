package com.example.rgt.CaseLine.Service;


import com.example.rgt.CaseLine.Repository.UserRepository;
import com.example.rgt.CaseLine.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service

public class UserSevice {
    @Autowired UserRepository userRepository;

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }
}
