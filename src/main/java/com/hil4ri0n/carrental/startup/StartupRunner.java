package com.hil4ri0n.carrental.startup;

import com.hil4ri0n.carrental.model.Rental;
import com.hil4ri0n.carrental.model.User;
import com.hil4ri0n.carrental.model.UserRoles;
import com.hil4ri0n.carrental.model.Vehicle;
import com.hil4ri0n.carrental.model.enums.FuelType;
import com.hil4ri0n.carrental.model.enums.RentalStatus;
import com.hil4ri0n.carrental.model.enums.Transmission;
import com.hil4ri0n.carrental.model.enums.VehicleStatus;
import com.hil4ri0n.carrental.service.RentalService;
import com.hil4ri0n.carrental.service.UserService;
import com.hil4ri0n.carrental.service.VehicleService;

import jakarta.ejb.EJB;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.Initialized;
import jakarta.enterprise.event.Observes;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@ApplicationScoped
public class StartupRunner {

    @EJB
    private VehicleService vehicleService;
    @EJB
    private RentalService rentalService;
    @EJB
    private UserService userService;

    public void onStart(@Observes @Initialized(ApplicationScoped.class) Object event) {

        if (!vehicleService.getAll().isEmpty()) {
            return;
        }

        // === Pojazdy ===
        Vehicle tesla = new Vehicle();
        tesla.setVin("VIN-101");
        tesla.setBrand("Tesla");
        tesla.setModel("Model 3");
        tesla.setFuel(FuelType.ELECTRIC);
        tesla.setTransmission(Transmission.AUTOMATIC);
        tesla.setStatus(VehicleStatus.AVAILABLE);
        tesla.setRegisteredOn(LocalDate.of(2023, 1, 10));
        tesla.setAddedAt(LocalDateTime.now());
        tesla.setDailyRate(new BigDecimal("300.00"));

        Vehicle toyota = new Vehicle();
        toyota.setVin("VIN-102");
        toyota.setBrand("Toyota");
        toyota.setModel("Corolla");
        toyota.setFuel(FuelType.PETROL);
        toyota.setTransmission(Transmission.MANUAL);
        toyota.setStatus(VehicleStatus.AVAILABLE);
        toyota.setRegisteredOn(LocalDate.of(2021, 5, 5));
        toyota.setAddedAt(LocalDateTime.now());
        toyota.setDailyRate(new BigDecimal("150.00"));

        Vehicle bmw = new Vehicle();
        bmw.setVin("VIN-103");
        bmw.setBrand("BMW");
        bmw.setModel("X5");
        bmw.setFuel(FuelType.DIESEL);
        bmw.setTransmission(Transmission.AUTOMATIC);
        bmw.setStatus(VehicleStatus.AVAILABLE);
        bmw.setRegisteredOn(LocalDate.of(2022, 9, 15));
        bmw.setAddedAt(LocalDateTime.now());
        bmw.setDailyRate(new BigDecimal("400.00"));

        vehicleService.create(tesla);
        vehicleService.create(toyota);
        vehicleService.create(bmw);

        // === użytkownicy ===
        User alice = User.builder()
                .login("alice")
                .email("alice@example.com")
                .password("alice123")
                .role(UserRoles.USER)
                .build();
        userService.registerUser(alice);

        User bob = User.builder()
                .login("bob")
                .email("bob@example.com")
                .password("bob123")
                .role(UserRoles.ADMIN)
                .build();
        userService.registerUser(bob);

        List<User> users = userService.getAllUsers();
        if (users.size() < 2) {
            System.out.println("Za mało użytkowników do utworzenia przykładowych wypożyczeń.");
        } else {
            User user1 = users.get(0);
            User user2 = users.get(1);

            // === Wypożyczenia ===
            Rental r1 = new Rental();
            r1.setVehicle(tesla);
            r1.setUser(user1);
            r1.setStartAt(LocalDateTime.of(2025, 11, 1, 10, 0));
            r1.setEndAt(LocalDateTime.of(2025, 11, 4, 10, 0));
            r1.setStatus(RentalStatus.ACTIVE);
            r1.setPrice(new BigDecimal("900.00"));
            rentalService.createRental(r1);

            Rental r2 = new Rental();
            r2.setVehicle(bmw);
            r2.setUser(user2);
            r2.setStartAt(LocalDateTime.of(2025, 12, 10, 9, 0));
            r2.setEndAt(LocalDateTime.of(2025, 12, 15, 9, 0));
            r2.setStatus(RentalStatus.CREATED);
            r2.setPrice(new BigDecimal("2000.00"));
            rentalService.createRental(r2);
        }

        // === Log ===
        System.out.println("Vehicles:");
        for (var v : vehicleService.getAll()) {
            System.out.println("VIN: " + v.getVin());
            System.out.println("Brand: " + v.getBrand());
            System.out.println("Model: " + v.getModel());
            System.out.println("Status: " + v.getStatus());
            System.out.println("Daily rate: " + v.getDailyRate());
            System.out.println();
        }

        System.out.println("Rentals:");
        for (var r : rentalService.getAll()) {
            System.out.println("ID: " + r.getId());
            System.out.println("Vehicle: " + (r.getVehicle() != null
                    ? r.getVehicle().getBrand() + " " + r.getVehicle().getModel()
                    : "(brak pojazdu)"));
            String userName = (r.getUser() != null) ? r.getUser().getLogin()
                    : "(brak / nie utrwalamy w JPA)";
            System.out.println("User: " + userName);
            System.out.println("Status: " + r.getStatus());
            System.out.println("Price: " + r.getPrice());
            System.out.println();
        }
    }
}