package com.hil4ri0n.carrental.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Getter
@Setter
public class User {
    private String login;
    private LocalDate joinedAt;
    private List<Rental> rentals = new ArrayList<>();
}
