package com.hil4ri0n.carrental.rest;

import com.hil4ri0n.carrental.model.UserRoles;
import jakarta.annotation.security.DeclareRoles;
import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;

@ApplicationPath("/api")
@DeclareRoles({UserRoles.USER, UserRoles.ADMIN})
public class ApiApplication extends Application {
}