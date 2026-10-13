package com.moviereco.movie_recommender.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.moviereco.movie_recommender.service.RatingService;
import com.moviereco.movie_recommender.service.VisitorService;

import java.util.List;
import java.util.NoSuchElementException;

import com.moviereco.movie_recommender.model.Rating;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;



@RestController
@RequestMapping("/api/ratings")
public class RatingController {
    private final RatingService ratingService;

    public RatingController(RatingService ratingService) {
        this.ratingService = ratingService;
    }

    @GetMapping
    public List<Rating> getMyRatings(@RequestHeader("X-Visitor-Id") String visitorKey) {
        return ratingService.getRatingsFor(visitorKey);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Rating rateMovie(@RequestHeader("X-Visitor-Id") String visitorKey, @RequestParam Long movieId, @RequestParam double score) {
        return ratingService.rateMovie(visitorKey, movieId, score);
    }
    
    @DeleteMapping("/{ratingId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteRating(@RequestHeader("X-Visitor-Id") String visitorKey, @PathVariable Long ratingId) {
        ratingService.deleteRating(visitorKey, ratingId);
    }


    public record RateMovieRequest(Long movieId, double score) {}


}