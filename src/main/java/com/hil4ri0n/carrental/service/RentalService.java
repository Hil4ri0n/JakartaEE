package com.hil4ri0n.carrental.service;

import com.hil4ri0n.carrental.model.Rental;
import com.hil4ri0n.carrental.repository.RentalRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.NoArgsConstructor;

import java.util.List;

@ApplicationScoped
@NoArgsConstructor(force = true)
public class RentalService {

    private final RentalRepository rentalRepository;

    @Inject
    public RentalService(RentalRepository rentalRepository) {
        this.rentalRepository = rentalRepository;
    }

    public List<Rental> getAll() {
        return rentalRepository.findAll();
    }

    public void add(Rental rental) {
        rentalRepository.save(rental);
    }
}