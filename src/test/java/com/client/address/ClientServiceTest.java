package com.client.address;


import com.client.address.application.dto.AddressRequest;
import com.client.address.application.dto.ClientRequest;
import com.client.address.application.service.ClientService;
import com.client.address.infrastructure.entity.ClientEntity;
import com.client.address.infrastructure.repository.ClientRepository;
import com.client.address.presentation.exception.BusinessException;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@QuarkusTest
class ClientServiceTest {

    @Inject
    ClientService clientService;

    @InjectMock
    ClientRepository clientRepository;

    @Test
    @DisplayName("Should throw BusinessException when creating a client with an existing email")
    void shouldThrowExceptionForDuplicateEmail() {
        var request = new ClientRequest(
                "Nome Teste", "email.dooble@example.com", "11999990000", "11122233344", "client_password",
                List.of(new AddressRequest("Home", "Test Avenue 5", "1", "", "B13", "SBC", "SP", "12345000", true))
        );

        when(clientRepository.findByEmail("email.dooble@example.com"))
                .thenReturn(Optional.of(new ClientEntity()));

        BusinessException exception = assertThrows(BusinessException.class, () -> clientService.create(request));

        assertEquals("Email already registered.", exception.getMessage());
    }

}