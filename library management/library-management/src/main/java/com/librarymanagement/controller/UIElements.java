package com.librarymanagement.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/pages/ui-features")
public class UIElements {
   


    @GetMapping("/buttons")
    public String getButtons() {
        return "pages/ui-features/buttons";
    }
    
     @GetMapping("/dropdowns")
     public String getDropdowns() {
         return "pages/ui-features/dropdowns";
     }
    
      @GetMapping("/typography")
    public String getTypography(){
        return "pages/ui-features/typography";
    }
}
