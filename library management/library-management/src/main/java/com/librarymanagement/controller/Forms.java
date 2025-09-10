package com.librarymanagement.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/pages/forms")
public class Forms {
   


    @GetMapping("/basic_elements")
    public String getForms() {
        return "pages/forms/basic_elements";
    }
    
 
}
