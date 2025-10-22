package com.hil4ri0n.carrental.startup;

import com.hil4ri0n.carrental.model.Rental;
import com.hil4ri0n.carrental.model.User;
import com.hil4ri0n.carrental.model.Vehicle;
import com.hil4ri0n.carrental.model.enums.RentalStatus;
import com.hil4ri0n.carrental.service.RentalService;
import com.hil4ri0n.carrental.service.UserService;
import com.hil4ri0n.carrental.service.VehicleService;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.Initialized;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@ApplicationScoped
@NoArgsConstructor(force = true)
public class StartupRunner {

    private final VehicleService vehicleService;
    private final RentalService rentalService;
    private final UserService userService;

    @Inject
    public StartupRunner(VehicleService vehicleService, RentalService rentalService, UserService userService) {
        this.vehicleService = vehicleService;
        this.rentalService = rentalService;
        this.userService = userService;
    }

    public void onStart(@Observes @Initialized(ApplicationScoped.class) Object event) {
        System.out.println("=========================");

        User user = userService.getAllUsers().getFirst();
        Vehicle vehicle = vehicleService.getAll().getFirst();

        Rental rental = new Rental();
        rental.setUser(user);
        rental.setVehicle(vehicle);
        rental.setStartAt(LocalDateTime.now());
        rental.setEndAt(LocalDateTime.now().plusDays(3));
        rental.setStatus(RentalStatus.CREATED);
        rental.setPrice(new BigDecimal("900.00"));

        rentalService.add(rental);

        System.out.println("Vehicles:");
        vehicleService.getAll()
                .forEach(v -> System.out.println(" - " + v.getBrand() + " " + v.getModel()));

        System.out.println("Rentals:");
        rentalService.getAll()
                .forEach(r -> System.out.println(" - " + r.getUser().getLogin()
                        + " rented " + r.getVehicle().getModel()));
    }
}