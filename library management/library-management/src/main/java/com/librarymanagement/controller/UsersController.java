package com.librarymanagement.controller;

import com.librarymanagement.dto.UserRequest;
import com.librarymanagement.entity.Authors;
import com.librarymanagement.entity.Roles;
import com.librarymanagement.entity.Users;
import com.librarymanagement.service.RolesService;
import com.librarymanagement.service.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin/user/")
public class UsersController {

    private final UsersService usersService;
    @Autowired
    private RolesService rolesService;

    public UsersController(UsersService usersService) {
        this.usersService = usersService;
    }




    //@GetMapping("/addUser")
    //public String getAddUserPage(){
      //  System.out.println("UsersController.getAddUserPage");
        //return "add-user";
    //}

    @GetMapping("/{id}")
    public ResponseEntity<Users> getUserById(@PathVariable Integer id) {
        return usersService.getUserById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }


    @GetMapping("/list")
    public String listAllUsers(Model model){
        List<Users> users = usersService.getAllUsers();
        model.addAttribute("users", users);
        return "list-users";
    }
    @GetMapping("/delete/{id}")
    public String deleteUser(@PathVariable Integer id) {
        usersService.deleteUser(id);
        return "redirect:/admin/user/list";
    }

    @GetMapping("/edit/{id}")
    public String editUser(@PathVariable Integer id, Model model) {
        Users user = usersService.getUserById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));
        model.addAttribute("user", user);

        List<Roles> roles = usersService.getAllRoles();
        model.addAttribute("roles", roles);


        return "edit-user";
    }


    @PostMapping("/update/{id}")
    public String updateUser(@PathVariable Integer id, @ModelAttribute Users user) {
        usersService.updateUser(id, user);
        return "redirect:/admin/user/list";
    }
    @GetMapping("/addUser")
    public String getAddUserPage(Model model) {
        model.addAttribute("user", new UserRequest()); // empty DTO for form
        model.addAttribute("roles", rolesService.findAll()); // roles dropdown
        return "add-user";
    }

    @PostMapping("/save")
    public String addUser(@ModelAttribute UserRequest userRequest) {
        usersService.saveUser(userRequest);
        return "redirect:/admin/user/list";
    }


}




