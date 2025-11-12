package com.hil4ri0n.carrental.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.json.bind.annotation.JsonbTransient;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class User {
    private UUID id;
    private String email;
    private String login;
    private LocalDate joinedAt;

    @JsonbTransient
    @JsonIgnore
    private List<Rental> rentals = new ArrayList<>();
}
