package com.hil4ri0n.carrental.repository;

import com.hil4ri0n.carrental.model.Vehicle;
import com.hil4ri0n.carrental.model.enums.FuelType;
import com.hil4ri0n.carrental.model.enums.Transmission;
import com.hil4ri0n.carrental.model.enums.VehicleStatus;
import jakarta.enterprise.context.ApplicationScoped;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class VehicleRepository {
    private final List<Vehicle> vehicles = new ArrayList<>();

    public VehicleRepository() {
        addSampleVehicles();
    }

    private void addSampleVehicles() {
        vehicles.add(new Vehicle("VIN001", "Tesla", "Model 3", LocalDate.of(2022, 5, 10),
                LocalDateTime.now(), FuelType.ELECTRIC, Transmission.AUTOMATIC,
                VehicleStatus.AVAILABLE, new BigDecimal("300.00"), new ArrayList<>()));
        vehicles.add(new Vehicle("VIN002", "Toyota", "Corolla", LocalDate.of(2020, 2, 20),
                LocalDateTime.now(), FuelType.PETROL, Transmission.MANUAL,
                VehicleStatus.AVAILABLE, new BigDecimal("150.00"), new ArrayList<>()));
    }

    public List<Vehicle> findAll() {
        return vehicles;
    }

    public void save(Vehicle vehicle) {
        vehicles.add(vehicle);
    }
}
