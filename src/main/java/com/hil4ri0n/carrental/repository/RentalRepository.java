package com.hil4ri0n.carrental.repository;

import com.hil4ri0n.carrental.model.Rental;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
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

    public void deleteById(UUID id) {
        rentals.remove(id);
    }

    public Optional<Rental> findById(UUID id) {
        return Optional.ofNullable(rentals.get(id));
    }

    public void update(Rental rental) {
        rentals.put(rental.getId(), rental);
    }

    public List<Rental> findByVin(String vin) {
        return rentals.values().stream()
                .filter(r -> r.getVehicle() != null
                        && vin != null
                        && vin.equalsIgnoreCase(r.getVehicle().getVin()))
                .toList();
    }

    public Optional<Rental> findByIdAndVin(UUID id, String vin) {
        return Optional.ofNullable(rentals.get(id))
                .filter(r -> r.getVehicle() != null
                        && vin != null
                        && vin.equalsIgnoreCase(r.getVehicle().getVin()));
    }
}
