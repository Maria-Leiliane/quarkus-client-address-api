package com.client.address;

import com.client.address.application.dto.ClientResponse;
import com.client.address.util.DocumentGenerator;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.util.UUID; // Used to generate unique emails

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.*;

@QuarkusTest
class ClientControllerTest {

    @Test
    @DisplayName("Should create a new client successfully")
    void shouldCreateClientSuccessfully() {
        // Use the generator to create a valid, random document
        String validCnpj = DocumentGenerator.generateCNPJ();
        String uniqueEmail = "client-" + validCnpj + "@example.com";

        String requestBody = String.format("""
        {
            "name": "New Client", "phone": "+5511987654321", "document": "%s",
            "email": "%s", "password": "test-pass123",
            "addresses": [{ "name": "Test Place", "street": "Test Avenue", "number": "2025", "district": "Test Zone",
                           "city": "São Paulo", "state": "SP", "zipCode": "01000-000", "mainAddress": true }]
        }
        """, validCnpj, uniqueEmail); // Inject the valid document and unique email

        given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .post("/clients")
                .then()
                .statusCode(201)
                .body("document", is(validCnpj)); // Verify with the generated document
    }
    @Test
    @DisplayName("Should find a client by ID when client exists")
    void shouldGetClientById_whenClientExists() {
        // ARRANGE: Create a client first to ensure it exists
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
    @DisplayName("Should delete a client and return 204 No Content")
    void shouldDeleteClient() {
        // ARRANGE: Create a client to be deleted
        Long idToDelete = createTestClientAndGetResponse().id();

        // ACT: Delete the client
        given()
                .contentType(ContentType.JSON)
                .when()
                .delete("/clients/{id}", idToDelete)
                .then()
                .statusCode(204);

        // ASSERT: Verify the client was actually deleted
        given()
                .contentType(ContentType.JSON)
                .when()
                .get("/clients/{id}", idToDelete)
                .then()
                .statusCode(404);
    }

    /**
     * Helper method to create a standard client with unique data for each test run.
     * This makes tests self-contained and avoids data conflicts.
     * @return The full ClientResponse of the created client.
     */
    private ClientResponse createTestClientAndGetResponse() {
        // Using UUID ensures the email and document are unique for every single test execution
        String uniqueId = UUID.randomUUID().toString().replaceAll("-", "");
        String uniqueEmail = "helper." + uniqueId.substring(0, 10) + "@example.com";
        String uniqueDocument = "1" + uniqueId.substring(0, 10); // Generates a unique 11-digit CPF-like string

        String clientRequestBody = String.format("""
        {
            "name": "Helper Client", "phone": "+5511911112222", "document": "%s",
            "email": "%s", "password": "password*test",
            "addresses": [{ "name": "House", "street": "Test Avenue II", "number": "1", "district": "Base District",
                           "city": "Cit Test", "state": "SP", "zipCode": "12345000", "mainAddress": true }]
        }
        """, uniqueDocument, uniqueEmail);

        return given()
                .contentType(ContentType.JSON)
                .body(clientRequestBody)
                .when().post("/clients")
                .then().statusCode(201)
                .extract().as(ClientResponse.class);
    }
}
// ./gradlew test --tests com.client.address.ClientControllerTest