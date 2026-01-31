package com.example.rgt.CaseLine.filter;

import com.example.rgt.CaseLine.Service.UserDetailServiceImpl;
import com.example.rgt.CaseLine.util.JWTutil;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JWTfilter extends OncePerRequestFilter {
    @Autowired
    private UserDetailServiceImpl userDetailsService;

    @Autowired
    private JWTutil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        String path = request.getRequestURI();

        if (path.startsWith("/v3/api-docs") ||
                path.startsWith("/swagger-ui")) {
            chain.doFilter(request, response);
            return;
        }

        String authorizationHeader = request.getHeader("Authorization");

        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            chain.doFilter(request, response);
            return;
        }

        String jwt = authorizationHeader.substring(7);
        String username = null;

        try {
            username = jwtUtil.extractUsername(jwt);
        } catch (ExpiredJwtException e) {
            System.out.println("Expired JWT: " + jwt); // Log invalid token instead of throwing an error
            chain.doFilter(request, response);
            return;
        } catch (Exception e) {
            System.out.println("Invalid JWT: " + jwt);
            chain.doFilter(request, response);
            return;
        }

        if (username == null || !jwtUtil.validateToken(jwt)) {
            System.out.println("Invalid or expired JWT token: " + jwt);
            chain.doFilter(request, response);
            return;
        }

        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities()
        );
        auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(auth);

        // Extend token validity if it's expiring soon
        if (jwtUtil.isTokenExpiringSoon(jwt)) {
            String newJwt = jwtUtil.generateToken(username);
            System.out.println("New token Generated");
            response.setHeader("Authorization", "Bearer " + newJwt);
        }

        chain.doFilter(request, response);
    }
}
