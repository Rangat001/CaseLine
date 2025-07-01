package com.example.rgt.CaseLine.Service;

import com.example.rgt.CaseLine.entity.Organization;
import com.example.rgt.CaseLine.entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public class CustomeOrgDetails implements UserDetails {

    private final Organization org;

    public CustomeOrgDetails(Organization org) {
        this.org = org;
    }

    public int getId() {
        return org.getOrg_id();
    }
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public String getPassword() {
        return org.getPassword();
    }

    @Override
    public String getUsername() {
        return org.getEmail();
    }
}
