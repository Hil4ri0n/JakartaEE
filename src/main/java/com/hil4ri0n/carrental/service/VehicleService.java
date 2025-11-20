package com.hil4ri0n.carrental.service;

import com.hil4ri0n.carrental.model.Vehicle;
import com.hil4ri0n.carrental.repository.VehicleRepository;
import jakarta.ejb.EJB;
import jakarta.ejb.LocalBean;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;

import java.util.List;
import java.util.Optional;

@LocalBean
@Stateless
public class VehicleService {

    @Inject
    private VehicleRepository vehicleRepository;

    @EJB
    private RentalService rentalService;

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
