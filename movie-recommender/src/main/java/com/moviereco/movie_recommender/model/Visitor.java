package com.moviereco.movie_recommender.model;

import jakarta.persistence.*;

@Entity
@Table(name = "Visitor")
public class Visitor {
    
    @Id
    @GeneratedValue
    private Long id;

    @Column(unique = true, nullable = false)
    private String visitorKey;

    public Visitor() {
        // required by JPA
    }

    public Visitor(String visitorKey) {
        this.visitorKey = visitorKey;
    }

    public Long getId() {
        return id;
    }

    public String getVisitorKey() {
        return visitorKey;
    }
}
