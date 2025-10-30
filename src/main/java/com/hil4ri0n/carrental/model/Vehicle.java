package com.hil4ri0n.carrental.model;

import com.hil4ri0n.carrental.model.enums.FuelType;
import com.hil4ri0n.carrental.model.enums.Transmission;
import com.hil4ri0n.carrental.model.enums.VehicleStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@Getter
@Setter
public class Vehicle {
    private UUID id;
    private String vin;
    private String brand;
    private String model;
    private LocalDate registeredOn;
    private LocalDateTime addedAt;
    private FuelType fuel;
    private Transmission transmission;
    private VehicleStatus status;
    private BigDecimal dailyRate;
    private List<Rental> rentals = new ArrayList<>();
}
