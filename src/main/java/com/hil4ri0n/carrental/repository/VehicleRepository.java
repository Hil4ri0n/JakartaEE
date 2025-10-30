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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@ApplicationScoped
public class VehicleRepository {
    private final Map<UUID, Vehicle> vehicles = new HashMap<>();

    public VehicleRepository() {
        addSampleVehicles();
    }

    private void addSampleVehicles() {
        UUID id1 = UUID.randomUUID();
        UUID id2 = UUID.randomUUID();
        vehicles.put(id1 ,new Vehicle(id1, "VIN001", "Tesla", "Model 3", LocalDate.of(2022, 5, 10),
                LocalDateTime.now(), FuelType.ELECTRIC, Transmission.AUTOMATIC,
                VehicleStatus.AVAILABLE, new BigDecimal("300.00"), new ArrayList<>()));
        vehicles.put(id2 ,new Vehicle(id2,"VIN002", "Toyota", "Corolla", LocalDate.of(2020, 2, 20),
                LocalDateTime.now(), FuelType.PETROL, Transmission.MANUAL,
                VehicleStatus.AVAILABLE, new BigDecimal("150.00"), new ArrayList<>()));
    }

    public List<Vehicle> findAll() {
        return new ArrayList<>(vehicles.values());
    }

    public void save(Vehicle vehicle) {
        UUID id = UUID.randomUUID();
        vehicle.setId(id);
        vehicles.put(id, vehicle);
    }
}
