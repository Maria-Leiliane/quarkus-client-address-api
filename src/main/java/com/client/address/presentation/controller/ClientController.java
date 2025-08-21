package com.client.address.presentation.controller;

import com.client.address.application.dto.ClientRequest;
import com.client.address.application.dto.ClientResponse;
import com.client.address.application.dto.PageResponse;
import com.client.address.application.service.ClientService;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;

import java.net.URI;
import java.time.LocalDate;

@Path("/clients")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ClientController {

    @Inject
    ClientService clientService;

    @GET
    public PageResponse<ClientResponse> getAllClients(
            @QueryParam("name") String name,
            @QueryParam("creationDate") LocalDate creationDate,
            @QueryParam("page") @DefaultValue("0") int page,
            @QueryParam("size") @DefaultValue("10") int size) {
        return clientService.findAllPaginated(name, creationDate, page, size);
    }

    @GET
    @Path("/{id}")
    public Response getClientById(@PathParam("id") Long id) {
        return clientService.findById(id)
                .map(client -> Response.ok(client).build())
                .orElse(Response.status(Response.Status.NOT_FOUND).build());
    }

    @POST
    public Response createClient(@Valid ClientRequest clientRequest, @Context UriInfo uriInfo) {
        ClientResponse createdClient = clientService.create(clientRequest);
        URI location = uriInfo.getAbsolutePathBuilder().path(createdClient.id().toString()).build();
        return Response.created(location).entity(createdClient).build();
    }

    @PATCH
    @Path("/{id}")
    public Response partiallyUpdateClient(@PathParam("id") Long id, ClientRequest clientRequest) {
        ClientResponse updatedClient = clientService.partialUpdate(id, clientRequest);
        return Response.ok(updatedClient).build();
    }

    @DELETE
    @Path("/{id}")
    public Response deleteClient(@PathParam("id") Long id) {
        clientService.delete(id);
        return Response.noContent().build();
    }
}