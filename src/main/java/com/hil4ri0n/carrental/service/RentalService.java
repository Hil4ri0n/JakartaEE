package com.hil4ri0n.carrental.service;

import com.hil4ri0n.carrental.model.Rental;
import com.hil4ri0n.carrental.repository.RentalRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;

@ApplicationScoped
public class RentalService {

    @Inject
    private RentalRepository rentalRepository;

    public List<Rental> getAll() {
        return rentalRepository.findAll();
    }

    public void add(Rental rental) {
        rentalRepository.save(rental);
    }
}