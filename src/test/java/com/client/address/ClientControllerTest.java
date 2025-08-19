package com.client.address;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;

@QuarkusTest
class ClientControllerTest {

    @Test
    @Transactional
    @DisplayName("Should create a new client successfully when given valid data")
    void shouldCreateClientSuccessfully() {
        String requestBody = """
        {
            "name": "Cliente de Teste Final",
            "phone": "+5511912345678",
            "document": "13148373000185",
            "email": "teste.final@example.com",
            "password": "senhaSegura123",
            "addresses": [
                {
                    "name": "Escritório",
                    "street": "Rua da Vitória",
                    "number": "2025",
                    "complement": null,
                    "district": "Centro",
                    "city": "São Paulo",
                    "state": "SP",
                    "zipCode": "01000-000",
                    "mainAddress": true
                }
            ]
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
                .body("name", is("Cliente de Teste Final"))
                .body("documentType", is("CNPJ"));
    }
}
