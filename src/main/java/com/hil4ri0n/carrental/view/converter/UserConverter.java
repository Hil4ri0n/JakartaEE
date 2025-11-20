package com.hil4ri0n.carrental.view.converter;

import com.hil4ri0n.carrental.model.User;
import com.hil4ri0n.carrental.service.UserService;
import jakarta.ejb.EJB;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.convert.Converter;
import jakarta.faces.convert.FacesConverter;
import jakarta.inject.Inject;

import java.util.Optional;
import java.util.UUID;

@FacesConverter(value = "userConverter", managed = true)
@ApplicationScoped
public class UserConverter implements Converter<User> {

    @EJB
    private UserService userService;

    @Override
    public User getAsObject(FacesContext context, UIComponent component, String value) {
        if (value == null || value.isEmpty()) {
            return null;
        }
        try {
            UUID id = UUID.fromString(value);
            Optional<User> opt = userService.getUserById(id);
            return opt.orElse(null);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, User value) {
        if (value == null) {
            return "";
        }
        return value.getId().toString();
    }
}
