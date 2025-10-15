package com.hil4ri0n.carrental.service;

import com.hil4ri0n.carrental.model.User;
import com.hil4ri0n.carrental.repository.UserRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class UserService {
    private final UserRepository userRepository = new UserRepository();

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> getUserById(UUID id) {
        return userRepository.findById(id);
    }
}
