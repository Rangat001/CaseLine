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

    public void SaveOwner(int org_id,String name,String Mail,String password){
        //      Need to assign Role as Owner
        User temp = new User();
        temp.setOrg_id(org_id);
        temp.setName(name);
        temp.setEmail(Mail);
        temp.setPassword(password);
        temp.setRole(User.Role.owner);
        SaveNewUser(temp);
    }

    public void saveUser(User user){
        try{
            userRepository.save(user);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
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

    //                   Find User By email
    public User findByMail(String mail){
        try{
            User user =  userRepository.findByEmail(mail);
            if(user != null){
                return user;
            }else{
                throw new IllegalArgumentException("User not found");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
