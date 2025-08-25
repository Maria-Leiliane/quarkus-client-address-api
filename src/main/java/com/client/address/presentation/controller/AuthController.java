package com.client.address.presentation.controller;

import com.client.address.application.dto.LoginRequest;
import com.client.address.infrastructure.entity.ClientEntity;
import com.client.address.infrastructure.repository.ClientRepository;
import io.smallrye.jwt.build.Jwt;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import org.wildfly.security.password.Password;
import org.wildfly.security.password.PasswordFactory;
import org.wildfly.security.password.interfaces.BCryptPassword;
import org.wildfly.security.password.spec.ClearPasswordSpec;
import org.wildfly.security.password.util.ModularCrypt;

import java.security.InvalidKeyException;
import java.security.spec.InvalidKeySpecException;
import java.security.NoSuchAlgorithmException;
import java.time.Duration;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

@Path("/auth")
@ApplicationScoped
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AuthController {

    @Inject
    ClientRepository clientRepository;

    @POST
    @Path("/login")
    public Response login(@Valid LoginRequest loginRequest) {
        ClientEntity client = clientRepository.findByEmail(loginRequest.email())
                .orElseThrow(() -> new WebApplicationException("User or password invalid", 401));

        try {
            PasswordFactory passwordFactory = PasswordFactory.getInstance(BCryptPassword.ALGORITHM_BCRYPT);
            Password storedPassword = ModularCrypt.decode(client.getPassword());

            // FINAL CORRECTION: The verify method takes the raw char array directly.
            if (!passwordFactory.verify(storedPassword, loginRequest.password().toCharArray())) {
                throw new WebApplicationException("User or password invalid", 401);
            }
        } catch (NoSuchAlgorithmException | InvalidKeySpecException | InvalidKeyException e) {
            e.printStackTrace();
            throw new WebApplicationException("Error during authentication", 500);
        }

        String token = Jwt.issuer("https://client-address.api/issuer")
                .upn(client.getEmail())
                .groups(new HashSet<>(List.of("user")))
                .expiresIn(Duration.ofHours(1))
                .sign();

        return Response.ok(Map.of("token", token)).build();
    }
}