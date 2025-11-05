package com.hil4ri0n.carrental.rest;

import com.hil4ri0n.carrental.model.Rental;
import com.hil4ri0n.carrental.model.Vehicle;
import com.hil4ri0n.carrental.service.RentalService;
import com.hil4ri0n.carrental.service.VehicleService;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
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

    @Inject
    RentalService rentalService;

    @Inject
    VehicleService vehicleService;

    @Context
    UriInfo uriInfo;

    @GET
    public Response getRentalsByVin(@PathParam("vin") String vin) {
        Optional<Vehicle> vehicle = vehicleService.getByVin(vin);
        if (vehicle.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        List<Rental> list = rentalService.getByVehicleVin(vin);
        return Response.ok(list).build();
    }

    @GET
    @Path("{id}")
    public Response getRentalByVinAndId(@PathParam("vin") String vin, @PathParam("id") String idStr) {
        Optional<Vehicle> vehicle = vehicleService.getByVin(vin);
        if (vehicle.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        UUID id;
        try {
            id = UUID.fromString(idStr);
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Invalid rental id").build();
        }

        return rentalService.getByIdAndVehicleVin(id, vin)
                .map(r -> Response.ok(r).build())
                .orElseGet(() -> Response.status(Response.Status.NOT_FOUND).build());
    }

    @POST
    public Response createRental(@PathParam("vin") String vin, Rental rental) {
        Optional<Vehicle> vehicle = vehicleService.getByVin(vin);
        if (vehicle.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND).entity("Vehicle not found").build();
        }
        rental.setVehicle(vehicle.get());
        rental.setId(null);

        rentalService.saveOrUpdate(rental);
        if (rental.getId() == null) {
            return Response.serverError().entity("Failed to create rental").build();
        }

        URI location = uriInfo.getAbsolutePathBuilder()
                .path(rental.getId().toString())
                .build();
        return Response.created(location).entity(rental).build();
    }

    @PUT
    @Path("{id}")
    public Response updateRental(@PathParam("vin") String vin,
                                 @PathParam("id") String idStr,
                                 Rental rental) {

        Optional<Vehicle> vehicle = vehicleService.getByVin(vin);
        if (vehicle.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND).entity("Vehicle not found").build();
        }

        UUID id;
        try {
            id = UUID.fromString(idStr);
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Invalid rental id").build();
        }

        var existing = rentalService.getByIdAndVehicleVin(id, vin);
        if (existing.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        rental.setId(id);
        rental.setVehicle(vehicle.get());

        rentalService.update(rental);
        return Response.ok(rental).build();
    }

    @DELETE
    @Path("{id}")
    public Response deleteRental(@PathParam("vin") String vin, @PathParam("id") String idStr) {
        Optional<Vehicle> vehicle = vehicleService.getByVin(vin);
        if (vehicle.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        UUID id;
        try {
            id = UUID.fromString(idStr);
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Invalid rental id").build();
        }

        var existing = rentalService.getByIdAndVehicleVin(id, vin);
        if (existing.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        rentalService.deleteById(id);
        return Response.noContent().build();
    }

    @DELETE
    public Response deleteAllRentalsByVin(@PathParam("vin") String vin) {
        Optional<Vehicle> vehicle = vehicleService.getByVin(vin);
        if (vehicle.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        rentalService.deleteByVehicleVin(vin);
        return Response.noContent().build();
    }
}
