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

    public List<Rental> findByVehicleBrand(String brand) {
        List<Rental> out = new ArrayList<>();
        for (Rental r : rentals.values()) {
            if (r.getVehicle() != null && brand.equals(r.getVehicle().getBrand())) {
                out.add(r);
            }
        }
        return out;
    }

    public void deleteByVehicleBrand(String brand) {
        rentals.values().removeIf(r ->
                r.getVehicle() != null && r.getVehicle().getBrand().equals(brand));
    }

    public void update(Rental rental) {
        rentals.put(rental.getId(), rental);
    }
}
