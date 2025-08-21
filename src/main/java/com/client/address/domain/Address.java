package com.client.address.domain;

public record Address(
        Long id,
        String street,
        String city,
        String zipCode,
        Long clientId
) {
}