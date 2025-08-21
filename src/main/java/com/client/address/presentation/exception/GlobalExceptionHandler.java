package com.client.address.presentation.exception;

import jakarta.ws.rs.core.Response;
import org.jboss.resteasy.reactive.server.ServerExceptionMapper;
import java.util.Map;

public class GlobalExceptionHandler {

    @ServerExceptionMapper
    public Response handleNotFound(ResourceNotFoundException exception) {
        return Response.status(Response.Status.NOT_FOUND)
                .entity(Map.of("error", exception.getMessage()))
                .build();
    }

    @ServerExceptionMapper
    public Response handleConflict(BusinessException exception) {
        return Response.status(Response.Status.CONFLICT)
                .entity(Map.of("error", exception.getMessage()))
                .build();
    }

    // Pode-se adicionar outros mappers para exceções genéricas, como um 500
    @ServerExceptionMapper
    public Response handleGenericException(Exception exception) {
        // Logar o erro em um sistema de logs real é uma boa prática
        System.err.println("An unexpected error occurred: " + exception.getMessage());
        exception.printStackTrace();

        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(Map.of("error", "An internal server error has occurred."))
                .build();
    }
}