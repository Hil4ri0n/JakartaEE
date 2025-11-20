package com.hil4ri0n.carrental.view;

import com.hil4ri0n.carrental.model.Rental;
import com.hil4ri0n.carrental.service.RentalService;
import jakarta.annotation.PostConstruct;
import jakarta.ejb.EJB;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import java.io.Serializable;
import java.util.Optional;
import java.util.UUID;

@Named
@ViewScoped
public class RentalDetailView implements Serializable {

    @EJB
    private RentalService rentalService;

    private String id;
    private Rental rental;

    @PostConstruct
    public void init() {
        id = FacesContext.getCurrentInstance()
                .getExternalContext().getRequestParameterMap().get("id");
        if (id != null) {
            try {
                UUID uuid = UUID.fromString(id);
                Optional<Rental> opt = rentalService.getById(uuid);
                opt.ifPresent(r -> rental = r);
            } catch (IllegalArgumentException ignored) {
            }
        }
    }

    public Rental getRental() {
        return rental;
    }
}
