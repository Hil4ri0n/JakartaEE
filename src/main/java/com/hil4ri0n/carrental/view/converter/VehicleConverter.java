package com.hil4ri0n.carrental.view.converter;

import com.hil4ri0n.carrental.model.Vehicle;
import com.hil4ri0n.carrental.service.VehicleService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.convert.Converter;
import jakarta.faces.convert.FacesConverter;
import jakarta.inject.Inject;

import java.util.Optional;

@FacesConverter(value = "vehicleConverter", managed = true)
@ApplicationScoped
public class VehicleConverter implements Converter<Vehicle> {

    @Inject
    private VehicleService vehicleService;

    @Override
    public Vehicle getAsObject(FacesContext context, UIComponent component, String value) {
        if (value == null || value.isEmpty()) {
            return null;
        }
        Optional<Vehicle> opt = vehicleService.getByVin(value);
        return opt.orElse(null);
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Vehicle value) {
        if (value == null) {
            return "";
        }
        return value.getVin();
    }
}
