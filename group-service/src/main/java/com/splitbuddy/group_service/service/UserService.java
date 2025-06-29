package com.splitbuddy.group_service.service;

import com.splitbuddy.group_service.entity.AppUser;
import com.splitbuddy.group_service.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public AppUser saveUser(AppUser user) {
        return userRepository.save(user);
    }
}
