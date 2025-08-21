package com.client.address.presentation.controller;

import com.client.address.application.dto.AddressRequest;
import com.client.address.application.dto.AddressResponse;
import com.client.address.application.service.AddressService;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;

import java.net.URI;
import java.util.List;

@Path("/clients/{clientId}/addresses")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AddressController {

    @Inject
    AddressService addressService;

    @GET
    public List<AddressResponse> getAddressesByClientId(@PathParam("clientId") Long clientId) {
        return addressService.findByClientId(clientId);
    }

    @POST
    public Response createAddress(@PathParam("clientId") Long clientId, @Valid AddressRequest request, @Context UriInfo uriInfo) {
        AddressResponse createdAddress = addressService.create(request, clientId);
        URI location = uriInfo.getAbsolutePathBuilder().path(createdAddress.id().toString()).build();
        return Response.created(location).entity(createdAddress).build();
    }

    @DELETE
    @Path("/{addressId}")
    public Response deleteAddress(@PathParam("clientId") Long clientId, @PathParam("addressId") Long addressId) {
        addressService.delete(clientId, addressId);
        return Response.noContent().build();
    }
}