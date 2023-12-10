package org.example;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

/**
 * Unit test for simple App.
 */
@QuarkusTest
public class RestApiTest {
    /**
     * Rigorous Test :-)
     */
    @Test
    public void testControllerEndpoint() {
        given()
                .when().get("/v1")
                .then()
                .statusCode(200)
                .body(is("Welcome"));
    }
}
