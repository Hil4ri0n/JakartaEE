package com.hil4ri0n.carrental.repository;

import com.hil4ri0n.carrental.model.User;

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
        save(new User("Alice", "alice@example.com"));
        save(new User("Bob", "bob@example.com"));
        save(new User("Charlie", "charlie@example.com"));
        save(new User("Diana", "diana@example.com"));
    }

    public List<User> findAll() {
        return new ArrayList<>(users.values());
    }

    public Optional<User> findById(UUID id) {
        return Optional.ofNullable(users.get(id));
    }

    public void save(User user) {
        users.put(user.getId(), user);
    }
}
