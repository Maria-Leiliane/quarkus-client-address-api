package com.client.address;

import com.client.address.application.dto.ClientResponse;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.*;
import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@QuarkusTest
class ClientControllerTest {

    // Create ID client first to use in tests
    private static Long testClientId;

    // Client base for all tests.
    @BeforeAll
    @Transactional
    static void setup() {
        String clientRequestBody = """
        {
            "name": "Test Base Client",
            "phone": "+5511911112222",
            "document": "11122233344",
            "email": "test-base.get@example.com",
            "password": "password*test",
            "addresses": [
                { "name": "House", "street": "Test Avenue II", "number": "1", "district": "Base District",
                  "city": "Cit Test", "state": "SP", "zipCode": "12345000", "mainAddress": true }
            ]
        }
        """;

        // Extract ID for use in other tests
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
    @DisplayName("Create a new client")
    void shouldCreateClientSuccessfully() {
        String requestBody = """
        {
            "name": "Test", "phone": "+5511987654321", "document": "13148373000185",
            "email": "test@example.com", "password": "test-pass123",
            "addresses": [{ "name": "Test Place", "street": "Test Avenue", "number": "2025", "district": "Test Zone",
                           "city": "São Paulo", "state": "SP", "zipCode": "01000-000", "mainAddress": true }]
        }
        """;

        given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .post("/clients")
                .then()
                .statusCode(201)
                .header("Location", notNullValue())
                .body("name", is("Test"))
                .body("documentType", is("CNPJ"));
    }

    @Test
    @Order(2)
    @Transactional
    @DisplayName("Find client by ID")
    void shouldGetClientById_whenClientExists() {
        String testClientEmail = "test.get@example.com";
        given()
                .contentType(ContentType.JSON)
                .when()
                .get("/clients/{id}", testClientId)
                .then()
                .statusCode(200)
                .body("id", equalTo(testClientId.intValue()))
                .body("email", is(testClientEmail));
    }

    @Test
    @Order(3)
    @Transactional
    @DisplayName("Not Found for dont's a client")
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
    @Order(4)
    @Transactional
    @DisplayName("List client pagination")
    void shouldGetAllClients() {
        given()
                .contentType(ContentType.JSON)
                .queryParam("page", 0)
                .queryParam("size", 5)
                .when()
                .get("/clients")
                .then()
                .statusCode(200)
                .body("content", notNullValue())
                .body("currentPage", is(0))
                .body("pageSize", is(5));
    }

    @Test
    @Order(5)
    @Transactional
    @DisplayName("Delete a client and return 204 No Content")
    void shouldDeleteClient() {
        given()
                .contentType(ContentType.JSON)
                .when()
                .delete("/clients/{id}", testClientId)
                .then()
                .statusCode(204);

        // Verify if a client was deleted
        given()
                .contentType(ContentType.JSON)
                .when()
                .get("/clients/{id}", testClientId)
                .then()
                .statusCode(404);
    }
}
//./gradlew test --tests com.client.address.ClientControllerTest