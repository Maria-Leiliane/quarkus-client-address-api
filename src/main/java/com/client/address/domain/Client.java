package com.client.address.domain;

import java.util.List;

public record Client(
        Long id,
        String name,
        String email,
        List<Address> addresses
) {
}
