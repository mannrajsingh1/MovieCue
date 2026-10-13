package com.moviereco.movie_recommender.repository;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.moviereco.movie_recommender.model.Movie;
import com.moviereco.movie_recommender.model.Rating;
import com.moviereco.movie_recommender.model.Visitor;

public interface RatingRepository extends JpaRepository<Rating, Long> {
    List<Rating> findByVisitor_VisitorKey(String visitorKey);
    Optional<Rating> findByVisitorAndMovie(Visitor visitor, Movie movie);
    
}
