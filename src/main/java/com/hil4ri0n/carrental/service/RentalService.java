package com.hil4ri0n.carrental.service;

import com.hil4ri0n.carrental.model.Rental;
import com.hil4ri0n.carrental.model.Vehicle;
import com.hil4ri0n.carrental.model.enums.RentalStatus;
import com.hil4ri0n.carrental.repository.RentalRepository;
import com.hil4ri0n.carrental.repository.VehicleRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
@NoArgsConstructor(force = true)
public class RentalService {

    private final RentalRepository rentalRepository;
    private final VehicleRepository vehicleRepository;

    @Inject
    public RentalService(RentalRepository rentalRepository, VehicleRepository vehicleRepository) {
        this.rentalRepository = rentalRepository;
        this.vehicleRepository = vehicleRepository;
    }

    public void createRental(Rental rental) {
        if (rental.getStatus() == null) {
            rental.setStatus(RentalStatus.CREATED);
        }
        rentalRepository.save(rental);

        Vehicle vehicle = rental.getVehicle();
        if (vehicle != null) {
            boolean missing = vehicle.getRentals().stream()
                    .noneMatch(r -> r.getId() != null && r.getId().equals(rental.getId()));
            if (missing) {
                vehicle.getRentals().add(rental);
                vehicleRepository.save(vehicle);
            }
        }
    }

    public Optional<Rental> getById(UUID id) {
        return rentalRepository.findById(id);
    }

    public List<Rental> getAll() {
        return rentalRepository.findAll();
    }

    public void deleteById(UUID id) {
        rentalRepository.findById(id).ifPresent(rental -> {
            Vehicle vehicle = rental.getVehicle();
            if (vehicle != null && vehicle.getRentals() != null) {
                vehicle.getRentals().removeIf(r -> id.equals(r.getId())); // po id
                vehicleRepository.save(vehicle);
            }
            rentalRepository.deleteById(id);
        });
    }

    public void saveOrUpdate(Rental rental) {
        if (rental.getStatus() == null) {
            rental.setStatus(RentalStatus.CREATED);
        }
        if(rental.getId() != null && rentalRepository.findById(rental.getId()).isPresent()) {
            rentalRepository.update(rental);
        } else {
            createRental(rental);
        }
    }

    public void update(Rental rental) {
        rentalRepository.update(rental);
    }

    public void deleteByVehicleVin(String vin) {
        rentalRepository.findByVin(vin).forEach(r -> {
            UUID id = r.getId();
            Vehicle v = r.getVehicle();
            if (v != null && v.getRentals() != null) {
                v.getRentals().removeIf(rr -> id.equals(rr.getId()));
                vehicleRepository.save(v);
            }
            rentalRepository.deleteById(id);
        });
    }

    public Optional<Rental> getByIdAndVehicleVin(UUID id, String vin) {
        return rentalRepository.findByIdAndVin(id, vin);
    }
}
