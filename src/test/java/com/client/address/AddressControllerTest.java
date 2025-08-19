package com.client.address;

import com.client.address.application.dto.ClientResponse;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.*;
import java.util.List;
import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class) 
class AddressControllerTest {

    private static Long testClientId; 
    
    @BeforeAll
    @Transactional
    static void setupClient() {
        String clientRequestBody = """
        {
            "name": "AddressTest",
            "phone": "+5521999998888",
            "document": "99988877766",
            "email": "mail.test@example.com",
            "password": "password_test",
            "addresses": [
                { "name": "House 1", "street": "5 avenue", "number": "0", "district": "SBC",
                  "city": "SBC", "state": "SP", "zipCode": "20000000", "mainAddress": true }
            ]
        }
        """;

        ClientResponse response = given()
                .contentType(ContentType.JSON)
                .body(clientRequestBody)
                .when().post("/clients")
                .then().statusCode(201)
                .extract().as(ClientResponse.class);

        testClientId = response.id();
    }

    @Test
    @Order(1)
    @Transactional
    @DisplayName("Add a new address to a customer")
    void shouldCreateAddress() {
        String addressRequestBody = """
        {
            "name": "House 2", "street": "Tests Avenue", "number": "1000", "district": "B13",
            "city": "SBC", "state": "SP", "zipCode": "21000000", "mainAddress": false
        }
        """;

        given()
                .contentType(ContentType.JSON)
                .body(addressRequestBody)
                .when()
                .post("/clients/{clientId}/addresses", testClientId)
                .then()
                .statusCode(201);
    }

    @Test
    @Order(2)
    @Transactional
    @DisplayName("Client Address List")
    void shouldListAddresses() {
        given()
                .contentType(ContentType.JSON)
                .when()
                .get("/clients/{clientId}/addresses", testClientId)
                .then()
                .statusCode(200)
                .body("size()", is(2)); // Must be 2 Addresses
    }

    @Test
    @Order(3)
    @Transactional
    @DisplayName("Delete Address Client")
    void shouldDeleteAddress() {
        List<Integer> addressIds = given().when().get("/clients/{clientId}/addresses", testClientId).path("id");
        Integer addressIdToDelete = addressIds.getFirst();

        given()
                .contentType(ContentType.JSON)
                .when()
                .delete("/clients/{clientId}/addresses/{addressId}", testClientId, addressIdToDelete)
                .then()
                .statusCode(204);

        // List Address Verify
        given().when().get("/clients/{clientId}/addresses", testClientId).then().body("size()", is(1));
    }
}
