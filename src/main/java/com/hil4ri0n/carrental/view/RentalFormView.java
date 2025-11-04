package com.hil4ri0n.carrental.view;

import com.hil4ri0n.carrental.model.Rental;
import com.hil4ri0n.carrental.model.User;
import com.hil4ri0n.carrental.model.Vehicle;
import com.hil4ri0n.carrental.model.enums.RentalStatus;
import com.hil4ri0n.carrental.service.RentalService;
import com.hil4ri0n.carrental.service.UserService;
import com.hil4ri0n.carrental.service.VehicleService;
import jakarta.annotation.PostConstruct;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Named
@ViewScoped
public class RentalFormView implements Serializable {

    @Inject
    private RentalService rentalService;

    @Inject
    private VehicleService vehicleService;

    @Inject
    private UserService userService;

    private Rental rental;
    private List<Vehicle> allVehicles;
    private List<User> allUsers;
    private boolean editMode;

    @PostConstruct
    public void init() {
        allVehicles = vehicleService.getAll();
        allUsers = userService.getAllUsers();

        var params = FacesContext.getCurrentInstance()
                .getExternalContext()
                .getRequestParameterMap();

        String idParam = params.get("id");
        String vinParam = params.get("vin");

        if (idParam != null && !idParam.isEmpty()) {
            try {
                UUID id = UUID.fromString(idParam);
                Optional<Rental> opt = rentalService.getById(id);
                if (opt.isPresent()) {
                    rental = opt.get();
                    editMode = true;
                } else {
                    createEmptyRental();
                    editMode = false;
                }
            } catch (IllegalArgumentException e) {
                createEmptyRental();
                editMode = false;
            }
        } else {
            createEmptyRental();
            editMode = false;
            if (vinParam != null && !vinParam.isEmpty()) {
                vehicleService.getByVin(vinParam)
                        .ifPresent(rental::setVehicle);
            }
        }
    }

    private void createEmptyRental() {
        rental = new Rental();
        rental.setStartAt(LocalDateTime.now());
        rental.setEndAt(LocalDateTime.now().plusDays(1));
        rental.setStatus(RentalStatus.CREATED);
        rental.setPrice(BigDecimal.ZERO);
    }

    public Rental getRental() {
        return rental;
    }

    public List<Vehicle> getAllVehicles() {
        return allVehicles;
    }

    public List<User> getAllUsers() {
        return allUsers;
    }

    public RentalStatus[] getAllStatuses() {
        return RentalStatus.values();
    }

    public boolean isEditMode() {
        return editMode;
    }

    public String save() {
        rentalService.saveOrUpdate(rental);
        if (rental.getVehicle() != null) {
            return "vehicle.xhtml?faces-redirect=true&vin=" + rental.getVehicle().getVin();
        }
        return "vehicles.xhtml?faces-redirect=true";
    }
}
