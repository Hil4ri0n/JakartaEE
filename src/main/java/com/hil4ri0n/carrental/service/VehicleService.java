package com.hil4ri0n.carrental.service;

import com.hil4ri0n.carrental.model.UserRoles;
import com.hil4ri0n.carrental.model.Vehicle;
import com.hil4ri0n.carrental.repository.VehicleRepository;
import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
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

    @Inject
    private RentalService rentalService;

    @RolesAllowed(UserRoles.ADMIN)
    public void create(Vehicle vehicle) {
        vehicleRepository.save(vehicle);
    }

    @PermitAll
    public void createInitial(Vehicle vehicle) {
        vehicleRepository.save(vehicle);
    }

    @RolesAllowed({UserRoles.ADMIN, UserRoles.USER})
    public List<Vehicle> getAll() {
        return vehicleRepository.findAll();
    }

    @PermitAll
    public List<Vehicle> getAllInternal() {
        return vehicleRepository.findAll();
    }

    @RolesAllowed({UserRoles.ADMIN, UserRoles.USER})
    public Optional<Vehicle> getByVin(String vin) {
        return vehicleRepository.findByVin(vin);
    }

    @RolesAllowed(UserRoles.ADMIN)
    public void deleteVehicle(String vin) {
        rentalService.deleteByVehicleVin(vin);
        vehicleRepository.deleteByVin(vin);
    }
}
