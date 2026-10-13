package com.moviereco.movie_recommender.repository;

import com.moviereco.movie_recommender.model.Visitor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VisitorRepository extends JpaRepository<Visitor, Long> {
    Optional<Visitor> findByVisitorKey(String visitorKey);

}

