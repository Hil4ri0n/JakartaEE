package com.hil4ri0n.carrental.service;

import com.hil4ri0n.carrental.model.User;
import com.hil4ri0n.carrental.model.UserRoles;
import com.hil4ri0n.carrental.repository.UserRepository;
import jakarta.ejb.LocalBean;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.security.enterprise.identitystore.Pbkdf2PasswordHash;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@LocalBean
@Stateless
public class UserService {

    @Inject
    private UserRepository userRepository;

    @Inject
    private Pbkdf2PasswordHash passwordHash;

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> getUserById(UUID id) {
        return userRepository.findById(id);
    }

    public Optional<User> getByLogin(String login) {
        return userRepository.findByLogin(login);
    }

    public Optional<User> getByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public boolean existsByLoginOrEmail(String login, String email) {
        return getByLogin(login).isPresent() || getByEmail(email).isPresent();
    }

    public User registerUser(User user) {
        if (user.getJoinedAt() == null) {
            user.setJoinedAt(LocalDate.now());
        }
        if (user.getRole() == null) {
            user.setRole(UserRoles.USER);
        }

        if (user.getPassword() != null && !user.getPassword().isBlank()) {
            String hashed = passwordHash.generate(user.getPassword().toCharArray());
            user.setPassword(hashed);
        }

        userRepository.save(user);
        return user;
    }
}
