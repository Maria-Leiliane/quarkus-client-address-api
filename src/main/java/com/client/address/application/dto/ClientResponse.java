package com.client.address.application.dto;

import com.client.address.infrastructure.entity.DocumentType;
import java.time.LocalDateTime;
import java.util.List;

public record ClientResponse(
        Long id,
        String name,
        String phone,
        String document,
        DocumentType documentType,
        String email,
        LocalDateTime createdAt,
        List<AddressResponse> addresses
) {}