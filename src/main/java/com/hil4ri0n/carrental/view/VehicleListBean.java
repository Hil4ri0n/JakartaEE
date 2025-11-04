package com.hil4ri0n.carrental.view;

import com.hil4ri0n.carrental.model.Vehicle;
import com.hil4ri0n.carrental.service.VehicleService;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import java.util.List;

@Named("vehicleList")
@RequestScoped
public class VehicleListBean {

    @Inject
    private VehicleService vehicleService;

    public List<Vehicle> getAll() {
        return vehicleService.getAll();
    }
}
