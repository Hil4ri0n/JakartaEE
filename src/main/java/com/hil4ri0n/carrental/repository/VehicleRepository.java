package com.hil4ri0n.carrental.repository;

import com.hil4ri0n.carrental.model.Vehicle;
import com.hil4ri0n.carrental.model.enums.FuelType;
import com.hil4ri0n.carrental.model.enums.Transmission;
import com.hil4ri0n.carrental.model.enums.VehicleStatus;
import jakarta.enterprise.context.ApplicationScoped;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@ApplicationScoped
public class VehicleRepository {

    private final Map<UUID, Vehicle> vehicles = new HashMap<>();

    public VehicleRepository() {
        addSampleVehicles();
    }

    private void addSampleVehicles() {
        UUID id1 = UUID.randomUUID();
        UUID id2 = UUID.randomUUID();
        vehicles.put(id1, new Vehicle(
                id1, "VIN001", "Tesla", "Model 3", LocalDate.of(2022, 5, 10),
                LocalDateTime.now(), FuelType.ELECTRIC, Transmission.AUTOMATIC,
                VehicleStatus.AVAILABLE, new BigDecimal("300.00"), new ArrayList<>()));

        vehicles.put(id2, new Vehicle(
                id2, "VIN002", "Toyota", "Corolla", LocalDate.of(2020, 2, 20),
                LocalDateTime.now(), FuelType.PETROL, Transmission.MANUAL,
                VehicleStatus.AVAILABLE, new BigDecimal("150.00"), new ArrayList<>()));
    }

    public void save(Vehicle vehicle) {
        if (vehicle.getId() == null) {
            vehicle.setId(UUID.randomUUID());
        }
        vehicles.put(vehicle.getId(), vehicle);
    }

    public List<Vehicle> findAll() {
        return new ArrayList<>(vehicles.values());
    }

    public Optional<Vehicle> findById(UUID id) {
        return Optional.ofNullable(vehicles.get(id));
    }

    public Optional<Vehicle> findByVin(String vin) {
        if (vin == null) return Optional.empty();
        return vehicles.values().stream()
                .filter(v -> vin.equalsIgnoreCase(v.getVin()))
                .findFirst();
    }

    public void deleteById(UUID id) {
        vehicles.remove(id);
    }

    public void deleteByVin(String vin) {
        List<UUID> toRemove = vehicles.values().stream()
                .filter(v -> vin != null && vin.equalsIgnoreCase(v.getVin()))
                .map(Vehicle::getId)
                .collect(Collectors.toList());

        toRemove.forEach(vehicles::remove);
    }
}
