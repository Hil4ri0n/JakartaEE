package com.hil4ri0n.carrental.service;

import com.hil4ri0n.carrental.model.Vehicle;
import com.hil4ri0n.carrental.repository.VehicleRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
@NoArgsConstructor(force = true)
public class VehicleService {

    private final VehicleRepository vehicleRepository;
    private final RentalService rentalService;

    @Inject
    public VehicleService(VehicleRepository vehicleRepository, RentalService rentalService) {
        this.vehicleRepository = vehicleRepository;
        this.rentalService = rentalService;
    }

    public void create(Vehicle vehicle) {
        vehicleRepository.save(vehicle);
    }

    public List<Vehicle> getAll() {
        return vehicleRepository.findAll();
    }

    public Optional<Vehicle> getByVin(String vin) {
        return vehicleRepository.findByVin(vin);
    }

    public void deleteVehicle(String vin) {
        rentalService.deleteByVehicleVin(vin);
        vehicleRepository.deleteByVin(vin);
    }
}
