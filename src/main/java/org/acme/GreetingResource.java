package org.acme;

import io.quarkus.hibernate.reactive.panache.common.WithTransaction;
import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/hello")
public class GreetingResource {

    @Inject
    GreetingRepository repository;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @WithTransaction
    public Uni<String> hello() {
        var greeting = new Greeting();
        greeting.setName("John");
        return repository.persist(greeting)
                .map(Greeting::getName);
    }
}
