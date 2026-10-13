package com.moviereco.movie_recommender.service;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.stereotype.Service;

import com.moviereco.movie_recommender.model.Movie;
import com.moviereco.movie_recommender.repository.MovieRepository;

import java.util.Optional;

@Service
public class MovieService  {
    private final MovieRepository movieRepository;
    public MovieService(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    public List<Movie> getMovies() {
        return  movieRepository.findAll();
    }

    public Movie getMovieById(Long id) {
        return movieRepository.findById(id).orElseThrow(() -> new NoSuchElementException("Movie not found with id: " + id));
    }

    public Movie saveMovie(Movie movie) {
        return movieRepository.save(movie);
    }

    public void deleteMovie(Long id) {
        if (!movieRepository.existsById(id)) {
            throw new NoSuchElementException("Movie not found with id: " + id);
        }
        movieRepository.deleteById(id);
    }
    public Movie findOrCreate(Movie movie) {
        if (movie.getImdbId() != null) {
            Optional<Movie> existing = movieRepository.findByImdbId(movie.getImdbId());
            if (existing.isPresent()) {
                return existing.get();
            }
        }
        return movieRepository.save(movie);
    }
}
