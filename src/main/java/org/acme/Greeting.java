package org.acme;

import jakarta.persistence.*;

@Entity
public class Greeting {

    @Id
    @SequenceGenerator(name = "greeting_seq", sequenceName = "greeting_seq", allocationSize = 1)
    @GeneratedValue(generator = "greeting_seq", strategy = GenerationType.SEQUENCE)
    Long id;

    String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
