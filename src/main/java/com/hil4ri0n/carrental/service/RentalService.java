package com.hil4ri0n.carrental.service;

import com.hil4ri0n.carrental.model.Rental;
import com.hil4ri0n.carrental.model.User;
import com.hil4ri0n.carrental.model.UserRoles;
import com.hil4ri0n.carrental.model.Vehicle;
import com.hil4ri0n.carrental.model.enums.RentalStatus;
import com.hil4ri0n.carrental.repository.RentalRepository;
import com.hil4ri0n.carrental.repository.VehicleRepository;
import jakarta.annotation.Resource;
import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.ejb.LocalBean;
import jakarta.ejb.SessionContext;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@LocalBean
@Stateless
public class RentalService {

    @Inject
    private RentalRepository rentalRepository;

    @Inject
    private VehicleRepository vehicleRepository;

    @Inject
    private UserService userService;

    @Resource
    private SessionContext ctx;

    private String currentLogin() {
        return ctx.getCallerPrincipal() != null
                ? ctx.getCallerPrincipal().getName()
                : null;
    }

    @PermitAll
    public void createRental(Rental rental) {
        if (rental.getStatus() == null) {
            rental.setStatus(RentalStatus.CREATED);
        }
        rentalRepository.save(rental);

        Vehicle vehicle = rental.getVehicle();
        if (vehicle != null) {
            boolean missing = vehicle.getRentals().stream()
                    .noneMatch(r -> r.getId() != null && r.getId().equals(rental.getId()));
            if (missing) {
                vehicle.getRentals().add(rental);
                vehicleRepository.save(vehicle);
            }
        }
    }

    @PermitAll
    public Optional<Rental> getById(UUID id) {
        return rentalRepository.findById(id);
    }

    @PermitAll
    public List<Rental> getAll() {
        return rentalRepository.findAll();
    }

    @RolesAllowed(UserRoles.USER)
    public void add(Rental rental) {
        String login = currentLogin();
        if (login == null) {
            throw new SecurityException("No authenticated user");
        }

        User owner = userService.getByLogin(login)
                .orElseThrow(() -> new IllegalStateException("Current user not found in DB"));

        rental.setUser(owner);
        createRental(rental);
    }

    @RolesAllowed({UserRoles.ADMIN, UserRoles.USER})
    public void deleteById(UUID id) {
        if (ctx.isCallerInRole(UserRoles.ADMIN)) {
            rentalRepository.findById(id).ifPresent(rental -> {
                Vehicle vehicle = rental.getVehicle();
                if (vehicle != null && vehicle.getRentals() != null) {
                    vehicle.getRentals().removeIf(r -> id.equals(r.getId()));
                    vehicleRepository.save(vehicle);
                }
                rentalRepository.deleteById(id);
            });
        } else {
            String login = currentLogin();
            if (login == null) {
                throw new SecurityException("No authenticated user");
            }

            rentalRepository.findByUserLogin(login).stream()
                    .filter(r -> id.equals(r.getId()))
                    .findFirst()
                    .ifPresent(rental -> {
                        Vehicle vehicle = rental.getVehicle();
                        if (vehicle != null && vehicle.getRentals() != null) {
                            vehicle.getRentals().removeIf(r -> id.equals(r.getId()));
                            vehicleRepository.save(vehicle);
                        }
                        rentalRepository.deleteById(id);
                    });
        }
    }

    @RolesAllowed({UserRoles.ADMIN, UserRoles.USER})
    public void saveOrUpdate(Rental rental) {
        if (rental.getStatus() == null) {
            rental.setStatus(RentalStatus.CREATED);
        }

        if (ctx.isCallerInRole(UserRoles.ADMIN)) {
            if (rental.getId() != null && rentalRepository.findById(rental.getId()).isPresent()) {
                rentalRepository.update(rental);
            } else {
                createRental(rental);
            }
        } else {
            String login = currentLogin();
            if (login == null) {
                throw new SecurityException("No authenticated user");
            }

            if (rental.getId() == null) {
                User owner = userService.getByLogin(login)
                        .orElseThrow(() -> new IllegalStateException("Current user not found in DB"));
                rental.setUser(owner);
                createRental(rental);
            } else {
                Optional<Rental> existingOpt = rentalRepository.findByUserLogin(login).stream()
                        .filter(r -> rental.getId().equals(r.getId()))
                        .findFirst();

                if (existingOpt.isEmpty()) {
                    throw new SecurityException("Cannot modify rental of another user");
                }

                rental.setUser(existingOpt.get().getUser());
                rentalRepository.update(rental);
            }
        }
    }

    @RolesAllowed({UserRoles.ADMIN, UserRoles.USER})
    public void update(Rental rental) {
        saveOrUpdate(rental);
    }

    @RolesAllowed(UserRoles.ADMIN)
    public void deleteByVehicleVin(String vin) {
        rentalRepository.findByVin(vin).forEach(r -> {
            UUID id = r.getId();
            Vehicle v = r.getVehicle();
            if (v != null && v.getRentals() != null) {
                v.getRentals().removeIf(rr -> id.equals(rr.getId()));
                vehicleRepository.save(v);
            }
            rentalRepository.deleteById(id);
        });
    }

    @RolesAllowed({UserRoles.ADMIN, UserRoles.USER})
    public Optional<Rental> getByIdAndVehicleVin(UUID id, String vin) {
        if (ctx.isCallerInRole(UserRoles.ADMIN)) {
            return rentalRepository.findByIdAndVin(id, vin);
        } else {
            String login = currentLogin();
            if (login == null) {
                return Optional.empty();
            }
            return rentalRepository.findByIdVinAndUserLogin(id, vin, login);
        }
    }

    @RolesAllowed({UserRoles.ADMIN, UserRoles.USER})
    public List<Rental> getByVehicleVin(String vin) {
        if (ctx.isCallerInRole(UserRoles.ADMIN)) {
            return rentalRepository.findByVin(vin);
        } else {
            String login = currentLogin();
            if (login == null) {
                return List.of();
            }
            return rentalRepository.findByVinAndUserLogin(vin, login);
        }
    }

    @PermitAll
    public List<Rental> getByVehicleVinAndUserLogin(String vin, String login) {
        return rentalRepository.findByVinAndUserLogin(vin, login);
    }

    @PermitAll
    public Optional<Rental> getByIdVehicleVinAndUserLogin(UUID id, String vin, String login) {
        return rentalRepository.findByIdVinAndUserLogin(id, vin, login);
    }

    @RolesAllowed(UserRoles.ADMIN)
    public List<Rental> getByUserLogin(String login) {
        return rentalRepository.findByUserLogin(login);
    }
}
