package com.client.address.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record AddressRequest(
        @NotBlank(message = "Name is required")
        String name,

        @NotBlank(message = "Street is required")
        String street,

        @NotBlank(message = "Number is required")
        String number,

        String complement,

        @NotBlank(message = "District is required")
        String district,

        @NotBlank(message = "City is required")
        String city,

        @NotBlank(message = "State is required")
        @Size(min = 2, max = 2, message = "State must be 2 characters long")
        String state,

        @NotBlank(message = "Zip code is required")
        String zipCode,

        @NotNull(message = "Main address flag is required")
        Boolean mainAddress
) {}