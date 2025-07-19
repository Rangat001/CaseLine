package com.example.rgt.CaseLine.Controller;

import com.example.rgt.CaseLine.Service.CaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("Case")
public class CaseController {

    @Autowired
    private CaseService caseService;

    @GetMapping("{id}")
    public ResponseEntity<?> getCaseDetails(@PathVariable int id) {
        try{
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if(authentication == null || !authentication.isAuthenticated()) {
                return ResponseEntity.status(401).body("Unauthorized access");
            }
            return ResponseEntity.ok(caseService.getCaseDetails(id));
        }catch (Exception e){
            return ResponseEntity.status(500).body("Error retrieving case details: " + e.getMessage());
        }
    }
}
