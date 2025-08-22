package com.client.address.presentation;

import com.client.address.application.dto.ClientResponse;
import com.client.address.util.DocumentGenerator; // Import the generator
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.empty;

@QuarkusTest
class AddressControllerTest {

    @Test
    @Transactional
    @DisplayName("Should add a new address to a client")
    void shouldCreateAddress() {
        // Create a client to add an address to.
        ClientResponse client = createTestClient();

        String addressRequestBody = """
        {
            "name": "Work", "street": "Commercial Avenue", "number": "1000", "district": "Downtown",
            "city": "São Paulo", "state": "SP", "zipCode": "01000-100", "mainAddress": false
        }
        """;

        given()
                .contentType(ContentType.JSON)
                .body(addressRequestBody)
                .when()
                .post("/clients/{clientId}/addresses", client.id())
                .then()
                .statusCode(201);
    }

    @Test
    @Transactional
    @DisplayName("Should list all addresses for a client")
    void shouldListAddresses() {
        // Create a client who already has one address.
        ClientResponse client = createTestClient();

        given()
                .contentType(ContentType.JSON)
                .when()
                .get("/clients/{clientId}/addresses", client.id())
                .then()
                .statusCode(200)
                .body("size()", is(1))
                .body("[0].name", is("House"));
    }

    @Test
    @Transactional
    @DisplayName("Should delete an address from a client")
    void shouldDeleteAddress() {
        // Create a client and get their initial address ID.
        ClientResponse client = createTestClient();
        Long addressIdToDelete = client.addresses().getFirst().id();

        // Delete the address.
        given()
                .contentType(ContentType.JSON)
                .when()
                .delete("/clients/{clientId}/addresses/{addressId}", client.id(), addressIdToDelete)
                .then()
                .statusCode(204);

        // Verify the address list is now empty.
        given()
                .contentType(ContentType.JSON)
                .when()
                .get("/clients/{clientId}/addresses", client.id())
                .then()
                .statusCode(200)
                .body("$", is(empty()));
    }

    /**
     * Helper method to create a standard client with unique and VALID data.
     * @return The full ClientResponse of the created client.
     */
    private ClientResponse createTestClient() {
        // Using the DocumentGenerator to create a valid CPF
        String validDocument = DocumentGenerator.generateCPF();
        String clientRequestBody = getString(validDocument);

        return given()
                .contentType(ContentType.JSON)
                .body(clientRequestBody)
                .when().post("/clients")
                .then()
                .statusCode(201)
                .extract().as(ClientResponse.class);
    }

    private static String getString(String validDocument) {
        String uniqueEmail = "address-test-" + validDocument + "@example.com";

        return String.format("""
        {
            "name": "Address Test Client", "phone": "+5511988887777", "document": "%s",
            "email": "%s", "password": "password123",
            "addresses": [{ "name": "House", "street": "Main Street", "number": "123", "district": "Suburb",
                           "city": "Test City", "state": "SP", "zipCode": "12345-000", "mainAddress": true }]
        }
        """, validDocument, uniqueEmail);
    }
}
// ./gradlew test --tests com.client.address.presentation.AddressControllerTest