package com.hil4ri0n.carrental.repository;

import com.hil4ri0n.carrental.model.Rental;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class RentalRepository {
    private final List<Rental> rentals = new ArrayList<>();

    public void save(Rental rental) {
        rentals.add(rental);
    }

    public List<Rental> findAll() {
        return rentals;
    }
}
