package com.moviereco.movie_recommender.controller;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.moviereco.movie_recommender.model.Movie;
import com.moviereco.movie_recommender.service.MovieService;

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

import com.moviereco.movie_recommender.service.OmdbService;
import com.moviereco.movie_recommender.service.VisitorService;


@CrossOrigin(origins = "http://localhost:5174")
@RestController
@RequestMapping("/api/movies")
public class MovieController {
    private final MovieService movieService;
    private final OmdbService omdbService;

    public MovieController(MovieService movieService, OmdbService omdbService) {
        this.movieService = movieService;
        this.omdbService = omdbService;
    }

    @GetMapping("/{id}")
    public Movie getMovie(@PathVariable Long id) {
        return movieService.getMovieById(id);
    }

    @GetMapping("/search")
    public Movie searchMovie(@RequestHeader("X-Visitor-Id") String visitorKey, @RequestParam String title) {
        VisitorService.validateKey(visitorKey);
        if (title.isBlank() || title.length() > 100) {
            throw new IllegalArgumentException("Title must be between 1 and 100 characters");
        }
        Movie movie = omdbService.fetchMovieByTitle(title);
        return movieService.findOrCreate(movie);
    }
    

}
