package com.client.address.presentation;

import com.client.address.application.dto.ClientResponse;
import com.client.address.util.DocumentGenerator;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.*;

@QuarkusTest
class ClientControllerTest {

    @Test
    @Transactional
    @DisplayName("Should create a new client successfully")
    void shouldCreateClientSuccessfully() {
        String validCnpj = DocumentGenerator.generateCNPJ();
        String requestBody = getString("client-", validCnpj, """
                {
                    "name": "New Client", "phone": "+5511987654321", "document": "%s",
                    "email": "%s", "password": "test-pass123",
                    "addresses": [{ "name": "Test Place", "street": "Test Avenue", "number": "2025", "district": "Test Zone",
                                   "city": "São Paulo", "state": "SP", "zipCode": "01000-000", "mainAddress": true }]
                }
                """);

        given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .post("/clients")
                .then()
                .log().ifValidationFails() // Log response body if validation fails
                .statusCode(201)
                .body("document", is(validCnpj));
    }

    private static String getString(String x, String validCnpj, String format) {
        String uniqueEmail = x + validCnpj + "@example.com";

        return String.format(format, validCnpj, uniqueEmail);
    }

    @Test
    @Transactional
    @DisplayName("Should find a client by ID when client exists")
    void shouldGetClientById_whenClientExists() {
        ClientResponse client = createTestClientAndGetResponse();

        given()
                .contentType(ContentType.JSON)
                .when()
                .get("/clients/{id}", client.id())
                .then()
                .statusCode(200)
                .body("id", equalTo(client.id().intValue()))
                .body("email", is(client.email()));
    }

    @Test
    @DisplayName("Should return 404 Not Found when client does not exist")
    void shouldGetClientById_whenClientNotFound() {
        Long nonExistentId = 99999L;
        given()
                .contentType(ContentType.JSON)
                .when()
                .get("/clients/{id}", nonExistentId)
                .then()
                .statusCode(404);
    }

    @Test
    @Transactional
    @DisplayName("Should delete a client and return 204 No Content")
    void shouldDeleteClient() {
        Long idToDelete = createTestClientAndGetResponse().id();

        given()
                .contentType(ContentType.JSON)
                .when()
                .delete("/clients/{id}", idToDelete)
                .then()
                .statusCode(204);

        given()
                .contentType(ContentType.JSON)
                .when()
                .get("/clients/{id}", idToDelete)
                .then()
                .statusCode(404);
    }

    /**
     * Helper method to create a standard client with unique and VALID data.
     */
    private ClientResponse createTestClientAndGetResponse() {
        String validDocument = DocumentGenerator.generateCPF();
        String clientRequestBody = getString("helper.", validDocument, """
                {
                    "name": "Helper Client", "phone": "+5511911112222", "document": "%s",
                    "email": "%s", "password": "password*test",
                    "addresses": [{ "name": "House", "street": "Test Avenue II", "number": "1", "district": "Base District",
                                   "city": "Cit Test", "state": "SP", "zipCode": "12345000", "mainAddress": true }]
                }
                """);

        return given()
                .contentType(ContentType.JSON)
                .body(clientRequestBody)
                .when()
                .post("/clients")
                .then()
                .log().ifValidationFails() // Log response body if validation fails
                .statusCode(201)
                .extract().as(ClientResponse.class);
    }
}
// ./gradlew test --tests com.client.address.presentation.ClientControllerTest