package com.hil4ri0n.carrental.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@Getter
@Setter
public class User {
    private UUID id;
    private String email;
    private String login;
    private LocalDate joinedAt;
    private List<Rental> rentals = new ArrayList<>();

    public User(String login, String email) {
        this.id = UUID.randomUUID();
        this.login = login;
        this.email = email;
    }
}
