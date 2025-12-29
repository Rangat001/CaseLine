package com.example.rgt.CaseLine.config;

import com.example.rgt.CaseLine.Repository.UserRepository;
//import com.example.rgt.CaseLine.Service.OrganizationUserDetailsService;
import com.example.rgt.CaseLine.Service.UserDetailServiceImpl;
import com.example.rgt.CaseLine.filter.JWTfilter;
import jakarta.servlet.Filter;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.List;

@Configuration
@EnableWebSecurity
public class SpringSecurity {
    @Autowired
    private UserDetailServiceImpl userDetailService;
//    @Autowired
//    private OrganizationUserDetailsService organizationUserDetailsService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JWTfilter jwtFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        return http.authorizeHttpRequests(request -> request
                .requestMatchers("/CaseLine/**","/auth/login","/auth/org_signup","/auth/org_login").permitAll()
                                .requestMatchers("/auth/signup", "/Admin/**","/Case/**","/User/**","/emp/**").authenticated()
                ).sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .csrf(AbstractHttpConfigurer::disable)
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(8);    // Default 10 , to make process faster used 8
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

}


