package com.client.address.application.service;

import com.client.address.application.dto.*;
import com.client.address.presentation.exception.BusinessException;
import com.client.address.presentation.exception.ResourceNotFoundException;
import com.client.address.application.service.mapper.ClientMapper;
import com.client.address.infrastructure.entity.ClientEntity;
import com.client.address.infrastructure.repository.AddressRepository;
import com.client.address.infrastructure.repository.ClientRepository;
import io.quarkus.elytron.security.common.BcryptUtil;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@ApplicationScoped
public class ClientService {

    @Inject
    ClientRepository clientRepository;
    @Inject
    AddressRepository addressRepository;
    @Inject
    AddressService addressService;

    // --- MÉTODOS DE CONSULTA (LEITURA) ---
    @Transactional(Transactional.TxType.SUPPORTS)
    public Optional<ClientResponse> findById(Long id) {
        return clientRepository.findById(id).map(this::mapToClientResponseWithAddresses);
    }

    @Transactional(Transactional.TxType.SUPPORTS)
    public PageResponse<ClientResponse> findAllPaginated(
            String name,
            LocalDate creationDate,
            int page,
            int size
    ) {
        int offset = page * size;
        String nameFilter = (name != null && !name.isBlank()) ? "%" + name + "%" : null;

        List<ClientEntity> clientEntities = clientRepository.findAllPaginated(
                nameFilter,
                creationDate,
                size,
                offset
        );

        long totalElements = clientRepository.countWithFilters(nameFilter, creationDate);

        List<ClientResponse> responses = clientEntities.stream()
                .map(this::mapToClientResponseWithAddresses)
                .collect(Collectors.toList());

        long totalPages = totalElements == 0 ? 0 : (long) Math.ceil((double) totalElements / size);

        return new PageResponse<>(responses, page, size, totalElements, totalPages);
    }

    @Transactional
    public ClientResponse create(ClientRequest request) {
        clientRepository.findByEmail(request.email()).ifPresent(c -> {
            throw new BusinessException("Email already registered.");
        });

        ClientEntity clientEntity = ClientMapper.toEntity(request);
        clientEntity.setPassword(BcryptUtil.bcryptHash(request.password()));
        clientEntity.setCreatedAt(LocalDateTime.now());

        Long clientId = clientRepository.save(clientEntity);
        clientEntity.setId(clientId);

        List<AddressResponse> createdAddresses = createOrUpdateAddresses(request.addresses(), clientId);
        return ClientMapper.toResponse(clientEntity, createdAddresses);
    }

    @Transactional
    public ClientResponse partialUpdate(Long id, ClientRequest request) {
        ClientEntity client = clientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Client not found with id: " + id));

        updateClientFields(client, request);
        clientRepository.update(client);

        List<AddressResponse> finalAddresses;
        if (request.addresses() != null) {
            addressRepository.deleteByClientId(id);
            finalAddresses = createOrUpdateAddresses(request.addresses(), id);
        } else {
            finalAddresses = addressService.findByClientId(id);
        }
        return ClientMapper.toResponse(client, finalAddresses);
    }

    @Transactional
    public void delete(Long id) {
        if (clientRepository.delete(id) == 0) {
            throw new ResourceNotFoundException("Client not found with id: " + id);
        }
    }

    private ClientResponse mapToClientResponseWithAddresses(ClientEntity client) {
        List<AddressResponse> addresses = addressService.findByClientId(client.getId());
        return ClientMapper.toResponse(client, addresses);
    }

    private void updateClientFields(ClientEntity client, ClientRequest request) {
        if (request.name() != null) client.setName(request.name());
        if (request.phone() != null) client.setPhone(request.phone());
        if (request.document() != null) client.setDocument(request.document());

        if (request.email() != null && !request.email().equals(client.getEmail())) {
            clientRepository.findByEmail(request.email()).ifPresent(c -> {
                throw new BusinessException("Email already registered.");
            });
            client.setEmail(request.email());
        }
        if (request.password() != null && !request.password().isBlank()) {
            client.setPassword(BcryptUtil.bcryptHash(request.password()));
        }
    }

    private List<AddressResponse> createOrUpdateAddresses(List<AddressRequest> requests, Long clientId) {
        if (requests == null || requests.isEmpty()) return new ArrayList<>();
        return requests.stream()
                .map(req -> addressService.create(req, clientId))
                .collect(Collectors.toList());
    }


}