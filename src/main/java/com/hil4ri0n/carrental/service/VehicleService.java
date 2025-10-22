package com.hil4ri0n.carrental.service;

import com.hil4ri0n.carrental.model.Vehicle;
import com.hil4ri0n.carrental.repository.VehicleRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;

@ApplicationScoped
public class VehicleService {

    @Inject
    private VehicleRepository vehicleRepository;

    public List<Vehicle> getAll() {
        return vehicleRepository.findAll();
    }

    public void add(Vehicle vehicle) {
        vehicleRepository.save(vehicle);
    }
}