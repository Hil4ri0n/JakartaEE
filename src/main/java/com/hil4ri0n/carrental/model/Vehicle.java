package com.hil4ri0n.carrental.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hil4ri0n.carrental.model.enums.FuelType;
import com.hil4ri0n.carrental.model.enums.Transmission;
import com.hil4ri0n.carrental.model.enums.VehicleStatus;
import jakarta.json.bind.annotation.JsonbTransient;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "vehicles")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Vehicle {

    @Id
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    @Column(name = "vin", nullable = false, unique = true, length = 50)
    private String vin;

    @Column(name = "brand", nullable = false, length = 100)
    private String brand;

    @Column(name = "model", nullable = false, length = 100)
    private String model;

    @Column(name = "registered_on")
    private LocalDate registeredOn;

    @Column(name = "added_at")
    private LocalDateTime addedAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "fuel", length = 20)
    private FuelType fuel;

    @Enumerated(EnumType.STRING)
    @Column(name = "transmission", length = 20)
    private Transmission transmission;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 20)
    private VehicleStatus status;

    @Column(name = "daily_rate", precision = 19, scale = 2)
    private BigDecimal dailyRate;

    @OneToMany(
            mappedBy = "vehicle",
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @JsonbTransient
    @JsonIgnore
    private List<Rental> rentals = new ArrayList<>();

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Vehicle vehicle = (Vehicle) o;
        return Objects.equals(id, vehicle.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
