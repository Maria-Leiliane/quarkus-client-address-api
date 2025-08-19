package com.client.address.application.service.mapper;

import com.client.address.application.dto.AddressResponse;
import com.client.address.application.dto.ClientRequest;
import com.client.address.application.dto.ClientResponse;
import com.client.address.infrastructure.entity.ClientEntity;
import com.client.address.infrastructure.entity.DocumentType;

import java.util.List;

public final class ClientMapper {

    private ClientMapper() {}

    public static ClientEntity toEntity(ClientRequest request) {
        ClientEntity entity = new ClientEntity();
        entity.setName(request.name());
        entity.setPhone(request.phone());
        entity.setDocument(request.document());
        entity.setEmail(request.email());

        String cleanDocument = request.document().replaceAll("\\D", "");
        entity.setDocumentType(cleanDocument.length() == 11 ? DocumentType.CPF : DocumentType.CNPJ);

        return entity;
    }

    public static ClientResponse toResponse(ClientEntity entity, List<AddressResponse> addresses) {
        return new ClientResponse(
                entity.getId(),
                entity.getName(),
                entity.getPhone(),
                entity.getDocument(),
                entity.getDocumentType(),
                entity.getEmail(),
                entity.getCreatedAt(),
                addresses
        );
    }
}