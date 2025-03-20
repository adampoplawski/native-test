package org.acme;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.hibernate.reactive.mutiny.Mutiny;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

@QuarkusTest
class GreetingResourceTest {

    @Inject
    Mutiny.SessionFactory sessionFactory;

    @Test
    void testHelloEndpoint() {
        given()
          .when().get("/hello")
          .then()
             .statusCode(200)
             .body(is("John"));

        Greeting greetingFromDb = sessionFactory.withStatelessSession(session ->
                session.get(Greeting.class, 1L)
        ).await().indefinitely();

        assert greetingFromDb.name.equals("John");
    }
}