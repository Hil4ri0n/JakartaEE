package com.hil4ri0n.carrental.rest;

import com.hil4ri0n.carrental.model.Vehicle;
import com.hil4ri0n.carrental.service.VehicleService;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;

import java.net.URI;
import java.util.List;

@Path("/vehicles")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@RequestScoped
public class VehicleController {

    @Inject
    VehicleService vehicleService;

    @Context
    UriInfo uriInfo;

    @GET
    public Response getAll() {
        List<Vehicle> all = vehicleService.getAll();
        return Response.ok(all).build();
    }

    @GET
    @Path("{vin}")
    public Response getOne(@PathParam("vin") String vin) {
        return vehicleService.getByVin(vin)
                .map(v -> Response.ok(v).build())
                .orElseGet(() -> Response.status(Response.Status.NOT_FOUND).build());
    }

    @POST
    public Response create(Vehicle vehicle) {
        if (vehicle == null || vehicle.getVin() == null || vehicle.getVin().isBlank()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("VIN is required").build();
        }

        if (vehicleService.getByVin(vehicle.getVin()).isPresent()) {
            return Response.status(Response.Status.CONFLICT)
                    .entity("Vehicle with this VIN already exists").build();
        }

        vehicleService.create(vehicle);
        URI location = uriInfo.getAbsolutePathBuilder()
                .path(vehicle.getVin())
                .build();
        return Response.created(location).entity(vehicle).build();
    }

    @PUT
    @Path("{vin}")
    public Response update(@PathParam("vin") String vin, Vehicle vehicle) {
        var existing = vehicleService.getByVin(vin);
        if (existing.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        Vehicle toUpdate = vehicle;
        toUpdate.setVin(vin);

        toUpdate.setId(existing.get().getId());

        vehicleService.create(toUpdate);
        return Response.ok(toUpdate).build();
    }

    @DELETE
    @Path("{vin}")
    public Response delete(@PathParam("vin") String vin) {
        if (vehicleService.getByVin(vin).isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        vehicleService.deleteVehicle(vin);
        return Response.noContent().build();
    }
}
