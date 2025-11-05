package com.hil4ri0n.carrental.view;

import com.hil4ri0n.carrental.model.Vehicle;
import com.hil4ri0n.carrental.service.VehicleService;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import java.io.Serializable;
import java.util.List;

@Named
@ViewScoped
public class VehicleListView implements Serializable {

    @Inject
    private VehicleService vehicleService;

    public List<Vehicle> getVehicles() {
        return vehicleService.getAll();
    }

    public String delete(String vin) {
        vehicleService.deleteVehicle(vin);
        return "vehicles.xhtml?faces-redirect=true";
    }
}
