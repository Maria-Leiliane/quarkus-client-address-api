package com.client.address.application.service.mapper;

import com.client.address.application.dto.*;
import com.client.address.infrastructure.entity.*;
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
        DocumentType docTypeEnum = cleanDocument.length() == 11 ? DocumentType.CPF : DocumentType.CNPJ;

        // Convert the enum to its String name before setting it in the entity
        entity.setDocumentType(docTypeEnum.name());
        return entity;
    }

    public static ClientResponse toResponse(ClientEntity entity, List<AddressResponse> addresses) {
        return new ClientResponse(
                entity.getId(), entity.getName(), entity.getPhone(), entity.getDocument(),
                DocumentType.valueOf(entity.getDocumentType()), // Convert the String from the entity back to an Enum
                entity.getEmail(), entity.getCreatedAt(), addresses
        );
    }
}