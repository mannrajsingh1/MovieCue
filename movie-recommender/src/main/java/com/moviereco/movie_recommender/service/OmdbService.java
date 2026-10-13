package com.moviereco.movie_recommender.service;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import com.moviereco.movie_recommender.dto.OmdbMovieResponse;
import com.moviereco.movie_recommender.dto.OmdbSearchResponse;
import com.moviereco.movie_recommender.model.Movie;

@Service
public class OmdbService {
    private final RestClient restClient;
    private final String apiKey;

    public OmdbService(@Value("${omdb.api.key}") String apiKey) {
        this.restClient = RestClient.builder()
                .baseUrl("http://www.omdbapi.com/")
                .build();
        this.apiKey = apiKey;
    }

    public Movie fetchMovieByTitle(String title) {
        OmdbMovieResponse response = restClient.get().uri(uriBuilder -> uriBuilder
                .queryParam("apikey", apiKey)
                .queryParam("t", title)
                .build())
            .retrieve()
            .body(OmdbMovieResponse.class);

            if (response == null || response.getTitle() == null) {
                throw new NoSuchElementException("Movie not found on OMDB: " + title);
            }
        return convertToMovie(response);
    }

    private Movie convertToMovie(OmdbMovieResponse dto) {

        Movie movie = new Movie();
        movie.setTitle(dto.getTitle());
        movie.setGenre(dto.getGenre());
        movie.setDirector(dto.getDirector());
        movie.setImdbId(dto.getImdbId());
        movie.setActors(dto.getActors());;
        movie.setPosterUrl(dto.getPosterUrl());

        movie.setYear(parseIntSafely(dto.getYear()));
        movie.setImdbRating(parseDoubleSafely(dto.getImdbRating()));
        return movie;
    }

    private int parseIntSafely(String year) {
        try {
            return Integer.parseInt(year);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    private double parseDoubleSafely(String imdbId) {
        try {
            return Double.parseDouble(imdbId);
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }

    public List<OmdbSearchResponse.OmdbSearchResult> searchMovies(String keyword) {
        OmdbSearchResponse response = restClient.get().uri(uriBuilder -> uriBuilder
                .queryParam("apikey", apiKey)
                .queryParam("s", keyword)
                .build())
            .retrieve()
            .body(OmdbSearchResponse.class);

        if (response == null || "False".equals(response.getResponse()) || response.getSearch() == null) {
            return List.of();
        }

        return response.getSearch();
    }
    
    public Movie fetchMovieByImdbId(String imdbId) {
        OmdbMovieResponse response = restClient.get().uri(uriBuilder -> uriBuilder
                .queryParam("apikey", apiKey)
                .queryParam("i", imdbId)
                .build())
            .retrieve()
            .body(OmdbMovieResponse.class);

        if (response == null || "False".equals(response.getResponse())) {
            throw new RuntimeException("No search results found on OMDB for IMDb ID: " + imdbId);
        }
        return convertToMovie(response);
    }
}
