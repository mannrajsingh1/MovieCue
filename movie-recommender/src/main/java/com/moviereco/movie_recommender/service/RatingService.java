package com.moviereco.movie_recommender.service;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.stereotype.Service;

import com.moviereco.movie_recommender.model.Movie;
import com.moviereco.movie_recommender.model.Rating;
import com.moviereco.movie_recommender.model.Visitor;
import com.moviereco.movie_recommender.repository.MovieRepository;
import com.moviereco.movie_recommender.repository.RatingRepository;

@Service
public class RatingService {
    private final RatingRepository ratingRepository;
    private final MovieRepository movieRepository;
    private final VisitorService visitorService;
    
    public RatingService(RatingRepository ratingRepository, MovieRepository movieRepository, VisitorService visitorService) {
        this.ratingRepository = ratingRepository;
        this.movieRepository = movieRepository;
        this.visitorService = visitorService;
    }

    public List<Rating> getRatingsFor(String visitorKey) {
        VisitorService.validateKey(visitorKey);
        return ratingRepository.findByVisitor_VisitorKey(visitorKey);
    }

    public Rating rateMovie(String visitorKey, Long movieId, double score) {
        if (score < 0 || score > 10) {
            throw new IllegalArgumentException("Score must be between 0 and 10");
        }
        Visitor visitor = visitorService.getOrCreate(visitorKey);
        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new NoSuchElementException("Movie not found with id: " + movieId));

        Rating rating = ratingRepository.findByVisitorAndMovie(visitor,movie)
        .orElseGet(Rating::new);
        rating.setVisitor(visitor);
        rating.setMovie(movie);
        rating.setRating(score);
        rating.setDateRated(LocalDate.now());

        return ratingRepository.save(rating);
    }

    public void deleteRating(String visitorKey, Long ratingId) {
        VisitorService.validateKey(visitorKey);
        Rating rating = ratingRepository.findById(ratingId)
        .filter(r->r.getVisitor().getVisitorKey().equals(visitorKey))
        .orElseThrow(()-> new NoSuchElementException("Rating not found"));

        ratingRepository.delete(rating);
    }
}
