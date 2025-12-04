package com.hil4ri0n.carrental.view;

import com.hil4ri0n.carrental.model.UserRoles;
import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.security.enterprise.SecurityContext;

@Named("securityView")
@RequestScoped
public class SecurityView {

    @Inject
    private SecurityContext securityContext;

    @Inject
    private FacesContext facesContext;

    public boolean isLoggedIn() {
        return securityContext.getCallerPrincipal() != null;
    }

    public String getUsername() {
        return isLoggedIn() ? securityContext.getCallerPrincipal().getName() : null;
    }

    public boolean isAdmin() {
        return securityContext.isCallerInRole(UserRoles.ADMIN);
    }

    public boolean isUser() {
        return securityContext.isCallerInRole(UserRoles.USER);
    }

    public String getHeaderImage() {
        String lang = facesContext.getViewRoot().getLocale().getLanguage();
        if ("en".equalsIgnoreCase(lang)) {
            return "images/header_en.jpg";
        } else {
            return "images/header_pl.jpg";
        }
    }
}
