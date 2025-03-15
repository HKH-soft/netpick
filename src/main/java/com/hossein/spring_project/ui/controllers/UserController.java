package com.hossein.spring_project.ui.controllers;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/users")
class UserController {
    
    private final UserService userService;
    
	public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
	public List<User> getResponse(){
		return userService.getAllUsers();
	}
	
	@GetMapping("{id}")
	public User getUser(@PathVariable("id") Integer id){
		return userService.getUserById(id);
	}

}
