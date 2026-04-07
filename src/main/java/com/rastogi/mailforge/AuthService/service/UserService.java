package com.rastogi.mailforge.AuthService.service;

import com.rastogi.mailforge.AuthService.entity.User;
import com.rastogi.mailforge.AuthService.error.errors.ResourceNotFoundException;
import com.rastogi.mailforge.AuthService.repo.UserRepo;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepo userRepo;

    public UserService(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    public Optional<User> loadUserByUsername(String username) {

        return userRepo.findByUsername(username);

    }

    public boolean getRandom() {
        return true;
    }

    public User verifyUser(String userId) {

        Optional<User> userById = userRepo.findUserById(userId);

        return userById.orElseThrow(() -> new ResourceNotFoundException(userId));
    }
}
