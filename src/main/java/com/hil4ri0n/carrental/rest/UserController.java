package com.hil4ri0n.carrental.rest;

import com.hil4ri0n.carrental.model.User;
import com.hil4ri0n.carrental.model.UserRoles;
import com.hil4ri0n.carrental.service.UserService;
import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.ejb.EJB;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Path("/users")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@RequestScoped
public class UserController {

    @Inject
    UserService userService;

    @Context
    UriInfo uriInfo;

    @GET
    @RolesAllowed(UserRoles.ADMIN)
    public Response getAll() {
        List<User> users = userService.getAllUsers();
        return Response.ok(users).build();
    }

    @GET
    @Path("{id}")
    @RolesAllowed(UserRoles.ADMIN)
    public Response getOne(@PathParam("id") String idStr) {
        try {
            UUID id = UUID.fromString(idStr);
            Optional<User> user = userService.getUserById(id);
            return user.map(value -> Response.ok(value).build())
                    .orElseGet(() -> Response.status(Response.Status.NOT_FOUND).build());
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Invalid user id").build();
        }
    }

    @POST
    @Path("/register")
    @PermitAll
    public Response register(User user) {
        if (user == null ||
                user.getLogin() == null || user.getLogin().isBlank() ||
                user.getEmail() == null || user.getEmail().isBlank()) {

            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Login and email are required").build();
        }

        if (userService.existsByLoginOrEmail(user.getLogin(), user.getEmail())) {
            return Response.status(Response.Status.CONFLICT)
                    .entity("User with this login or email already exists").build();
        }

        User created = userService.registerUser(user);

        URI location = uriInfo.getAbsolutePathBuilder()
                .path(created.getId().toString())
                .build();

        return Response.created(location).entity(created).build();
    }
}
