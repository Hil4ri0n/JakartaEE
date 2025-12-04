package com.hil4ri0n.carrental.view;

import com.hil4ri0n.carrental.model.Rental;
import com.hil4ri0n.carrental.model.User;
import com.hil4ri0n.carrental.model.Vehicle;
import com.hil4ri0n.carrental.model.enums.RentalStatus;
import com.hil4ri0n.carrental.service.RentalService;
import com.hil4ri0n.carrental.service.UserService;
import com.hil4ri0n.carrental.service.VehicleService;
import jakarta.annotation.PostConstruct;
import jakarta.ejb.EJBException;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.persistence.OptimisticLockException;

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

    @Inject
    private FacesContext facesContext;

    private Rental rental;
    private List<Vehicle> allVehicles;
    private List<User> allUsers;
    private boolean editMode;
    private Rental dbRental;
    private boolean optimisticLockFailed;

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

    public boolean isOptimisticLockFailed() {
        return optimisticLockFailed;
    }

    public Rental getDbRental() {
        return dbRental;
    }

    public String save() {
        try {
            rentalService.saveOrUpdate(rental);
            optimisticLockFailed = false;
            dbRental = null;

            if (rental.getVehicle() != null) {
                return "vehicle.xhtml?faces-redirect=true&vin=" + rental.getVehicle().getVin();
            }
            return "vehicles.xhtml?faces-redirect=true";

        } catch (EJBException e) {
            if (isOptimisticLockException(e)) {
                handleOptimisticLockConflict();
                return null;
            } else {
                throw e;
            }
        } catch (OptimisticLockException e) {
            handleOptimisticLockConflict();
            return null;
        }
    }

    private boolean isOptimisticLockException(EJBException e) {
        Throwable cause = e.getCause();
        if (cause instanceof OptimisticLockException) {
            return true;
        }
        return cause != null && cause.getCause() instanceof OptimisticLockException;
    }

    private void handleOptimisticLockConflict() {
        optimisticLockFailed = true;
        if (rental != null && rental.getId() != null) {
            rentalService.getById(rental.getId()).ifPresent(r -> dbRental = r);
        }
        facesContext.addMessage(null,
                new FacesMessage(
                        FacesMessage.SEVERITY_ERROR,
                        "Wypożyczenie zostało zmienione przez innego użytkownika. Twoich zmian nie zapisano. Poniżej widzisz stan w bazie oraz swoje dane.",
                        null
                )
        );
    }
}
