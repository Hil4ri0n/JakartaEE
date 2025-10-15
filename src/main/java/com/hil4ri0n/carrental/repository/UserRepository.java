package com.hil4ri0n.carrental.repository;

import com.hil4ri0n.carrental.model.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class UserRepository {
    private final Map<UUID, User> users = new HashMap<>();

    public UserRepository() {
        addTestUsers();
    }

    private void addTestUsers() {
        save(User.builder().login("Alice").email("alice@example.com").build());
        save(User.builder().login("Bob").email("bob@example.com").build());
        save(User.builder().login("Charlie").email("charlie@example.com").build());
        save(User.builder().login("Diana").email("diana@example.com").build());
    }

    public List<User> findAll() {
        return new ArrayList<>(users.values());
    }

    public Optional<User> findById(UUID id) {
        return Optional.ofNullable(users.get(id));
    }

    public void save(User user) {
        user.setId(UUID.randomUUID());
        user.setJoinedAt(LocalDate.now());
        users.put(user.getId(), user);
    }
}
