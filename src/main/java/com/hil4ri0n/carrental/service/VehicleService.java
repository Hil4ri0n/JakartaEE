package com.hil4ri0n.carrental.service;

import com.hil4ri0n.carrental.model.Vehicle;
import com.hil4ri0n.carrental.repository.VehicleRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.NoArgsConstructor;

import java.util.List;

@ApplicationScoped
@NoArgsConstructor(force = true)
public class VehicleService {

    private final VehicleRepository vehicleRepository;

    @Inject
    public VehicleService(VehicleRepository vehicleRepository) {
        this.vehicleRepository = vehicleRepository;
    }

    public List<Vehicle> getAll() {
        return vehicleRepository.findAll();
    }

    public void add(Vehicle vehicle) {
        vehicleRepository.save(vehicle);
    }
}