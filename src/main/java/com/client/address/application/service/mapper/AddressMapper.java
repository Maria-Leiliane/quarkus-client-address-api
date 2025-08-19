package com.client.address.application.service.mapper;

import com.client.address.application.dto.AddressRequest;
import com.client.address.application.dto.AddressResponse;
import com.client.address.infrastructure.entity.AddressEntity;

public final class AddressMapper {

    private AddressMapper() {}

    public static AddressEntity toEntity(AddressRequest request) {
        AddressEntity entity = new AddressEntity();
        entity.setName(request.name());
        entity.setStreet(request.street());
        entity.setNumber(request.number());
        entity.setComplement(request.complement());
        entity.setDistrict(request.district());
        entity.setCity(request.city());
        entity.setState(request.state());
        entity.setZipCode(request.zipCode());
        entity.setMainAddress(request.mainAddress());
        return entity;
    }

    public static AddressResponse toResponse(AddressEntity entity) {
        return new AddressResponse(
                entity.getId(),
                entity.getName(),
                entity.getStreet(),
                entity.getNumber(),
                entity.getComplement(),
                entity.getDistrict(),
                entity.getCity(),
                entity.getState(),
                entity.getZipCode(),
                entity.getMainAddress(),
                entity.getCreatedAt()
        );
    }
}