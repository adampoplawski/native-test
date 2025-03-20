package org.acme;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.hibernate.cfg.Configuration;
import org.hibernate.reactive.mutiny.Mutiny;
import org.hibernate.reactive.provider.ReactiveServiceRegistryBuilder;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.hibernate.cfg.JdbcSettings.*;

@QuarkusTest
class GreetingResourceTest {

    @Test
    void testHelloEndpoint() {
        given()
          .when().get("/hello")
          .then()
             .statusCode(200)
             .body(is("John"));

        var configuration = new Configuration()
                .addAnnotatedClass(Greeting.class)
                .setProperty(URL, "jdbc:postgresql://localhost:57936/postgres")
                .setProperty(USER, "quarkus")
                .setProperty(PASS, "quarkus")
                .setProperty(DIALECT, "org.hibernate.dialect.PostgreSQLDialect")
                .setProperty("hibernate.agroal.maxSize", 20)
                .setProperty(SHOW_SQL, true)
                .setProperty(FORMAT_SQL, true)
                .setProperty(HIGHLIGHT_SQL, true);

        Mutiny.SessionFactory unwrappedSession = configuration.buildSessionFactory(
                new ReactiveServiceRegistryBuilder()
                        .applySettings(configuration.getProperties())
                        .build()
        ).unwrap(Mutiny.SessionFactory.class);

        Greeting greetingFromDb = unwrappedSession.withStatelessSession(session ->
                session.get(Greeting.class, 1L)
        ).await().indefinitely();

        assert greetingFromDb.name.equals("John");
    }
}