package com.splitbuddy.group_service.controller;

import com.splitbuddy.group_service.entity.AppUser;
import com.splitbuddy.group_service.service.UserService;
import com.splitbuddy.group_service.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

   @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @PostMapping
    public String createUser(@RequestBody AppUser user) {
        AppUser saved = userService.saveUser(user);
        return "✅ User saved with ID: " + saved.getId();
    }

    
    @GetMapping
    public List<AppUser> getAllUsers() {
        return userRepository.findAll();
    }

    @GetMapping("/{id}")
public AppUser getUserById(@PathVariable Long id) {
    return userRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("User not found"));
}

}