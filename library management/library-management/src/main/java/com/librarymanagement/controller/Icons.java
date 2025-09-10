package com.librarymanagement.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/pages/icons")
public class Icons {

    @GetMapping("/font-awesome")
    public String getIcons() {
        return "pages/icons/font-awesome";
    }
    
 
}
