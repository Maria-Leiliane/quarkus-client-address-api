package com.client.address.application.dto;

import java.time.LocalDateTime;

public record AddressResponse(
        Long id,
        String name,
        String street,
        String number,
        String complement,
        String district,
        String city,
        String state,
        String zipCode,
        Boolean mainAddress,
        LocalDateTime createdAt
) {}