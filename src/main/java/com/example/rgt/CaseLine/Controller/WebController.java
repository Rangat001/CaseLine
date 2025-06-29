package com.example.rgt.CaseLine.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("CaseLine")
public class WebController {

    @GetMapping("/login")
    public String login(){
        return "user_login";
    }

    @GetMapping("/signup")
    public String signup(){
        return "user_signup";
    }

    @GetMapping("/org-login")
    public String org_login(){
        return "org_login";
    }

    @GetMapping("/org-signup")
    public String org_signup(){
        return "org_signup";
    }

    @GetMapping("/Admin_dashboard")
    public String org_dashboard(){
        return "admin_dashboard";
    }


}
