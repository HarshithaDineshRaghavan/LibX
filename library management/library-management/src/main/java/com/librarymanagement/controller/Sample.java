package com.librarymanagement.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/pages/samples")
public class Sample {

    @GetMapping("/blank-page")
    public String getBlankPage() {
        return "pages/samples/blank-page";
    }

    @GetMapping("/error-404")
    public String getError404() {
        return "pages/samples/error-404";
    }

    @GetMapping("/error-500")
    public String getError500() {
        return "pages/samples/error-500";
    }

    @GetMapping("/login")
    public String getLogin() {
        return "pages/samples/login";
    }

     @GetMapping("/register")
    public String getRegister() {
        return "pages/samples/register";
    }
    
 
}
