package com.client.address;

import com.client.address.application.dto.AddressRequest;
import com.client.address.application.service.AddressService;
import com.client.address.infrastructure.entity.AddressEntity;
import com.client.address.infrastructure.entity.ClientEntity;
import com.client.address.infrastructure.repository.AddressRepository;
import com.client.address.infrastructure.repository.ClientRepository;
import com.client.address.presentation.exception.BusinessException;
import com.client.address.presentation.exception.ResourceNotFoundException;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@QuarkusTest
class AddressServiceTest {

    @Inject
    AddressService addressService;

    @InjectMock
    ClientRepository clientRepository;

    @InjectMock
    AddressRepository addressRepository;

    @Test
    @DisplayName("Deve Successfully create an address for an existing customer")
    void shouldCreateAddressSuccessfully() {
        var request = new AddressRequest("Casa", "Test Avenue 6", "123", "", "B12", "SBC", "SP", "12345000", true);
        Long clientId = 1L;
        Long generatedAddressId = 10L;

        when(clientRepository.findById(clientId)).thenReturn(Optional.of(new ClientEntity()));
        when(addressRepository.save(any(AddressEntity.class))).thenReturn(generatedAddressId);

        var response = addressService.create(request, clientId);

        assertNotNull(response);
        assertEquals(generatedAddressId, response.id());
        assertEquals("Test Avenue 6", response.street());
    }

    @Test
    @DisplayName("ResourceNotFoundException when trying to create address for non-existent customer")
    void shouldThrowExceptionWhenCreatingAddressForNonExistingClient() {
        var request = new AddressRequest("Casa", "Test Avenue 6", "123", "", "B12", "SBC", "SP", "12345000", true);
        Long nonExistingClientId = 99L;

        when(clientRepository.findById(nonExistingClientId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> addressService.create(request, nonExistingClientId));
    }

    @Test
    @DisplayName("BusinessException when trying to delete address that does not belong to the customer")
    void shouldThrowExceptionWhenDeletingAddressFromWrongClient() {
        Long clientId = 1L;
        Long otherClientId = 2L;
        Long addressId = 10L;

        var address = new AddressEntity();
        address.setId(addressId);
        address.setClientId(otherClientId);

        when(addressRepository.findById(addressId)).thenReturn(Optional.of(address));

        assertThrows(BusinessException.class,
                () -> addressService.delete(clientId, addressId));
    }
}