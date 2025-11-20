package com.hil4ri0n.carrental.rest;

import com.hil4ri0n.carrental.model.Rental;
import com.hil4ri0n.carrental.model.User;
import com.hil4ri0n.carrental.model.UserRoles;
import com.hil4ri0n.carrental.model.Vehicle;
import com.hil4ri0n.carrental.service.RentalService;
import com.hil4ri0n.carrental.service.UserService;
import com.hil4ri0n.carrental.service.VehicleService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.ejb.EJB;
import jakarta.enterprise.context.RequestScoped;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Path("/vehicles/{vin}/rentals")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@RequestScoped
public class RentalController {

    @EJB
    RentalService rentalService;

    @EJB
    VehicleService vehicleService;

    @EJB
    UserService userService;

    @Context
    UriInfo uriInfo;

    @Context
    SecurityContext securityContext;

    @GET
    @RolesAllowed({UserRoles.ADMIN, UserRoles.USER})
    public Response getRentalsByVin(@PathParam("vin") String vin) {
        Optional<Vehicle> vehicle = vehicleService.getByVin(vin);
        if (vehicle.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        List<Rental> list;
        if (securityContext.isUserInRole(UserRoles.ADMIN)) {
            list = rentalService.getByVehicleVin(vin);
        } else {
            String login = securityContext.getUserPrincipal().getName();
            list = rentalService.getByVehicleVinAndUserLogin(vin, login);
        }

        return Response.ok(list).build();
    }

    @GET
    @Path("{id}")
    @RolesAllowed({UserRoles.ADMIN, UserRoles.USER})
    public Response getRentalByVinAndId(@PathParam("vin") String vin,
                                        @PathParam("id") String idStr) {
        Optional<Vehicle> vehicle = vehicleService.getByVin(vin);
        if (vehicle.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        UUID id;
        try {
            id = UUID.fromString(idStr);
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Invalid rental id").build();
        }

        Optional<Rental> rentalOpt;
        if (securityContext.isUserInRole(UserRoles.ADMIN)) {
            rentalOpt = rentalService.getByIdAndVehicleVin(id, vin);
        } else {
            String login = securityContext.getUserPrincipal().getName();
            rentalOpt = rentalService.getByIdVehicleVinAndUserLogin(id, vin, login);
        }

        return rentalOpt
                .map(r -> Response.ok(r).build())
                .orElseGet(() -> Response.status(Response.Status.NOT_FOUND).build());
    }

    @POST
    @RolesAllowed({UserRoles.ADMIN, UserRoles.USER})
    public Response createRental(@PathParam("vin") String vin,
                                 Rental rental) {

        Optional<Vehicle> vehicle = vehicleService.getByVin(vin);
        if (vehicle.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Vehicle not found")
                    .build();
        }

        String login = securityContext.getUserPrincipal().getName();
        User user = userService.getByLogin(login)
                .orElseThrow(() -> new NotFoundException("User not found: " + login));

        rental.setId(null);
        rental.setVehicle(vehicle.get());
        rental.setUser(user);

        rentalService.saveOrUpdate(rental);
        if (rental.getId() == null) {
            return Response.serverError()
                    .entity("Failed to create rental")
                    .build();
        }

        URI location = uriInfo.getAbsolutePathBuilder()
                .path(rental.getId().toString())
                .build();
        return Response.created(location).entity(rental).build();
    }

    @PUT
    @Path("{id}")
    @RolesAllowed({UserRoles.ADMIN, UserRoles.USER})
    public Response updateRental(@PathParam("vin") String vin,
                                 @PathParam("id") String idStr,
                                 Rental rental) {

        Optional<Vehicle> vehicle = vehicleService.getByVin(vin);
        if (vehicle.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Vehicle not found")
                    .build();
        }

        UUID id;
        try {
            id = UUID.fromString(idStr);
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Invalid rental id")
                    .build();
        }

        Optional<Rental> existingOpt;
        if (securityContext.isUserInRole(UserRoles.ADMIN)) {
            existingOpt = rentalService.getByIdAndVehicleVin(id, vin);
        } else {
            String login = securityContext.getUserPrincipal().getName();
            existingOpt = rentalService.getByIdVehicleVinAndUserLogin(id, vin, login);
        }

        if (existingOpt.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        Rental existing = existingOpt.get();

        existing.setStartAt(rental.getStartAt());
        existing.setEndAt(rental.getEndAt());
        existing.setStatus(rental.getStatus());
        existing.setPrice(rental.getPrice());
        existing.setVehicle(vehicle.get());

        rentalService.update(existing);
        return Response.ok(existing).build();
    }

    @DELETE
    @Path("{id}")
    @RolesAllowed({UserRoles.ADMIN, UserRoles.USER})
    public Response deleteRental(@PathParam("vin") String vin,
                                 @PathParam("id") String idStr) {
        Optional<Vehicle> vehicle = vehicleService.getByVin(vin);
        if (vehicle.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        UUID id;
        try {
            id = UUID.fromString(idStr);
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Invalid rental id").build();
        }

        Optional<Rental> existing;
        if (securityContext.isUserInRole(UserRoles.ADMIN)) {
            existing = rentalService.getByIdAndVehicleVin(id, vin);
        } else {
            String login = securityContext.getUserPrincipal().getName();
            existing = rentalService.getByIdVehicleVinAndUserLogin(id, vin, login);
        }

        if (existing.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        rentalService.deleteById(id);
        return Response.noContent().build();
    }

    @DELETE
    @RolesAllowed(UserRoles.ADMIN)
    public Response deleteAllRentalsByVin(@PathParam("vin") String vin) {
        Optional<Vehicle> vehicle = vehicleService.getByVin(vin);
        if (vehicle.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        rentalService.deleteByVehicleVin(vin);
        return Response.noContent().build();
    }
}
