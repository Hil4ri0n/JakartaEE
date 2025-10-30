package com.hil4ri0n.carrental.repository;

import com.hil4ri0n.carrental.model.Rental;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@ApplicationScoped
public class RentalRepository {
    private final Map<UUID, Rental> rentals = new HashMap<>();

    public void save(Rental rental) {
        UUID id = UUID.randomUUID();
        rental.setId(id);
        rentals.put(id, rental);
    }

    public List<Rental> findAll() {
        return new ArrayList<>(rentals.values());
    }
}
