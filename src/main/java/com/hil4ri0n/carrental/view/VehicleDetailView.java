package com.hil4ri0n.carrental.view;

import com.hil4ri0n.carrental.model.Rental;
import com.hil4ri0n.carrental.model.Vehicle;
import com.hil4ri0n.carrental.service.RentalService;
import com.hil4ri0n.carrental.service.VehicleService;
import jakarta.annotation.PostConstruct;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Named
@ViewScoped
public class VehicleDetailView implements Serializable {

    @Inject
    private VehicleService vehicleService;

    @Inject
    private RentalService rentalService;

    private String vin;
    private Vehicle vehicle;
    private List<Rental> rentals;

    @PostConstruct
    public void init() {
        vin = FacesContext.getCurrentInstance()
                .getExternalContext().getRequestParameterMap().get("vin");
        if (vin != null) {
            Optional<Vehicle> opt = vehicleService.getByVin(vin);
            opt.ifPresent(v -> {
                vehicle = v;
                refreshRentals();
            });
        }
    }

    private void refreshRentals() {
        rentals = vehicle.getRentals();
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public List<Rental> getRentals() {
        return rentals;
    }

    public String deleteRental(UUID id) {
        rentalService.deleteById(id);
        refreshRentals();
        return null;
    }
}
