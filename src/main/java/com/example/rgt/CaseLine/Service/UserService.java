package com.example.rgt.CaseLine.Service;


import com.example.rgt.CaseLine.Repository.UserRepository;
import com.example.rgt.CaseLine.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service

public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public void saveUser(User user){
        userRepository.save(user);
    }

    //                                save New User
    public void SaveNewUser(User userEntity){
        try {
            if (userEntity.getName() == null || userEntity.getName().isEmpty() || userEntity.getPassword() == null || userEntity.getPassword().isEmpty()) {
                throw new IllegalArgumentException("Username cannot be null or empty");
            }
            userEntity.setPassword(passwordEncoder.encode(userEntity.getPassword()));
            userRepository.save(userEntity);
        }catch (Exception e){
            System.out.println("Exception while Create new User");
        }
    }

}
