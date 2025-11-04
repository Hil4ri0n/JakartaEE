package com.hil4ri0n.carrental.service;

import com.hil4ri0n.carrental.model.Rental;
import com.hil4ri0n.carrental.model.enums.RentalStatus;
import com.hil4ri0n.carrental.repository.RentalRepository;
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

    @Inject
    public RentalService(RentalRepository rentalRepository) {
        this.rentalRepository = rentalRepository;
    }

    public void add(Rental rental) {
        rentalRepository.save(rental);
    }

    public List<Rental> getAll() {
        return rentalRepository.findAll();
    }

    public void deleteById(UUID id) {
        rentalRepository.deleteById(id);
    }

    public List<Rental> findByVehicleBrand(String brand) {
        return rentalRepository.findByVehicleBrand(brand);
    }

    public void deleteByVehicleBrand(String brand) {
        rentalRepository.deleteByVehicleBrand(brand);
    }

    public Optional<Rental> getById(UUID id) {
        return rentalRepository.findById(id);
    }

    public void upsert(Rental rental) {
        if (rental.getId() != null && rentalRepository.findById(rental.getId()).isPresent()) {
            rentalRepository.update(rental);
        } else {
            rentalRepository.save(rental);
        }
    }

    public void saveOrUpdate(Rental rental) {
        if(rental.getStatus() == null) {
            rental.setStatus(RentalStatus.CREATED);
        }
        upsert(rental);
    }

    public void update(Rental rental) {
        rentalRepository.update(rental);
    }

    public void deleteByVehicleVin(String vin) {
        rentalRepository.findAll().stream()
                .filter(r -> r.getVehicle() != null && vin != null && vin.equalsIgnoreCase(r.getVehicle().getVin()))
                .map(Rental::getId)
                .forEach(rentalRepository::deleteById);
    }
}
