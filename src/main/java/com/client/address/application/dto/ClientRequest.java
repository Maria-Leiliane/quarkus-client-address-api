package com.client.address.application.dto;

import com.client.address.validation.CpfOrCnpj;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import java.util.List;

public record ClientRequest(
        @NotBlank(message = "Name cannot be blank")
        String name,

        @NotBlank(message = "Email cannot be blank")
        @Email(message = "Email should be valid")
        String email,

        @NotBlank(message = "Phone cannot be blank")
        String phone,

        @NotBlank(message = "Document cannot be blank")
        @CpfOrCnpj
        String document,

        @NotBlank(message = "Password cannot be blank")
        @Size(min = 6, message = "Password must be at least 6 characters long")
        String password,

        @NotNull(message = "Addresses list cannot be null")
        @Size(min = 1, message = "At least one address is required")
        @Valid
        List<AddressRequest> addresses
) {}