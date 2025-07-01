package com.example.rgt.CaseLine.Service;

import com.example.rgt.CaseLine.Repository.OrgRepository;
import com.example.rgt.CaseLine.entity.Organization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class OrganizationUserDetailsService implements UserDetailsService {

    @Autowired
    private OrgRepository organizationRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        long start = System.currentTimeMillis();
        System.out.println("Start DB Fetch");
        Organization org = organizationRepository.findByEmail(email);
        System.out.println("DB Fetch Done" + (System.currentTimeMillis() - start));
        if (org == null) {
            throw new UsernameNotFoundException("Organization not found");
        }
        return new CustomeOrgDetails(org);
    }
}
