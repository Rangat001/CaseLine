package com.example.rgt.CaseLine.Service;

import com.example.rgt.CaseLine.Repository.OrgRepository;
import com.example.rgt.CaseLine.entity.Organization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service

public class OrgService {
    @Autowired
    private OrgRepository orgRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public void createOrg(Organization org){
        try{
            if (orgRepository.findByEmail(org.getEmail()) != null) {
                throw new RuntimeException("Email already registered");
            }
            org.setPassword(passwordEncoder.encode(org.getPassword()));
            orgRepository.save(org);
        } catch (Exception e) {
            System.out.println("Exception while creating Organization");
        }
    }

    public Optional<Organization> findByid(int id){
        try {
            return orgRepository.findById(id);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
