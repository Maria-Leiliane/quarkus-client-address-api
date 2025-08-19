package com.client.address.application.service;

import com.client.address.application.dto.AddressRequest;
import com.client.address.application.dto.AddressResponse;
import com.client.address.presentation.exception.BusinessException;
import com.client.address.presentation.exception.ResourceNotFoundException;
import com.client.address.application.service.mapper.AddressMapper;
import com.client.address.infrastructure.entity.AddressEntity;
import com.client.address.infrastructure.repository.AddressRepository;
import com.client.address.infrastructure.repository.ClientRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@ApplicationScoped
public class AddressService {

    @Inject
    ClientRepository clientRepository;
    @Inject
    AddressRepository addressRepository;

    @Transactional
    public AddressResponse create(AddressRequest request, Long clientId) {
        clientRepository.findById(clientId)
                .orElseThrow(() -> new ResourceNotFoundException("Cannot create address: Client not found with id: " + clientId));

        if (request.mainAddress()) {
            addressRepository.findByClientId(clientId).forEach(addr -> {
                if (addr.getMainAddress()) {
                    addr.setMainAddress(false);
                    addressRepository.update(addr);
                }
            });
        }

        AddressEntity addressEntity = AddressMapper.toEntity(request);
        addressEntity.setClientId(clientId);
        addressEntity.setCreatedAt(LocalDateTime.now());

        if (addressRepository.findByClientId(clientId).isEmpty()) {
            addressEntity.setMainAddress(true);
        }

        Long addressId = addressRepository.save(addressEntity);
        addressEntity.setId(addressId);

        return AddressMapper.toResponse(addressEntity);
    }

    @Transactional
    public void delete(Long clientId, Long addressId) {
        AddressEntity address = addressRepository.findById(addressId)
                .orElseThrow(() -> new ResourceNotFoundException("Address not found with id: " + addressId));

        if (!address.getClientId().equals(clientId)) {
            throw new BusinessException("Address does not belong to the specified client.");
        }

        addressRepository.delete(addressId);
    }

    @Transactional(Transactional.TxType.SUPPORTS)
    public List<AddressResponse> findByClientId(Long clientId) {
        clientRepository.findById(clientId)
                .orElseThrow(() -> new ResourceNotFoundException("Client not found with id: " + clientId));

        return addressRepository.findByClientId(clientId).stream()
                .map(AddressMapper::toResponse)
                .toList();
    }
}