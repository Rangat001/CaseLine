package com.example.rgt.CaseLine.Controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/User")
public class UserController {

    @GetMapping("/checkrole")
    public ResponseEntity<String> checkRole() {
        

    }
}
