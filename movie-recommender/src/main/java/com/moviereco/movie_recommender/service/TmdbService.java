package com.moviereco.movie_recommender.service;

import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import com.moviereco.movie_recommender.dto.TmdbMovieCreditsResponse;
import com.moviereco.movie_recommender.dto.TmdbMovieDetailsResponse;
import com.moviereco.movie_recommender.dto.TmdbSearchResponse;
import com.moviereco.movie_recommender.model.Movie;

@Service
public class TmdbService {
    private final RestClient restClient;
    private final String apiKey;

    private static final String POSTER_BASE_URL = "https://image.tmdb.org/t/p/w500";

    public TmdbService(@Value("${tmdb.api.key}") String apiKey) {
        this.apiKey = apiKey;
        this.restClient = RestClient.builder()
                .baseUrl("https://api.themoviedb.org/3/")
                .build();
    }

    public Optional<Long> searchPersonIdByName(String name) {
        TmdbSearchResponse response = restClient.get().uri(uriBuilder -> uriBuilder
                .path("/search/person")
                .queryParam("api_key", apiKey)
                .queryParam("query", name)
                .build())
            .retrieve()
            .body(TmdbSearchResponse.class);

        if (response != null && response.getResults() != null && !response.getResults().isEmpty()) {
            return Optional.of(response.getResults().get(0).getId());
        }
        return Optional.empty();
    }

    public Set<Long> getFilmographyMovieIds(Long personId) {
        TmdbMovieCreditsResponse response = restClient.get().uri(uriBuilder -> uriBuilder
                .path("/person/{person_id}/movie_credits")
                .queryParam("api_key", apiKey)
                .build(personId))
            .retrieve()
            .body(TmdbMovieCreditsResponse.class);

            Set<Long> ids = new LinkedHashSet<>();

            if(response == null){
                return ids;
            }
            int maxPerPerson = 8;
            if (response.getCast() != null) {
                response.getCast().stream()
                .limit(maxPerPerson)
                .map(TmdbMovieCreditsResponse.TmdbCredit::getId)
                .filter(id -> id != null)
                .forEach(ids::add);
            }

            if (response.getCrew() != null) {
                response.getCrew().stream()
                .filter(credit -> "Director".equals(credit.getJob()))
                .limit(maxPerPerson)
                .map(TmdbMovieCreditsResponse.TmdbCredit::getId)
                .filter(id -> id != null)
                .forEach(ids::add);
            }
            return ids;
    }

    public Movie getMovieDetails(Long tmdbMovieId) {
        TmdbMovieDetailsResponse response = restClient.get()
        .uri(uriBuilder -> uriBuilder
            .path("/movie/{movieId}")
            .queryParam("api_key", apiKey)
            .queryParam("append_to_response", "credits,external_ids")
            .build(tmdbMovieId))
        .retrieve()
        .body(TmdbMovieDetailsResponse.class);

        if (response == null) {
            return null;
        }

        return convertToMovie(response);
    }

    private Movie convertToMovie(TmdbMovieDetailsResponse response) {
        Movie movie = new Movie();
        movie.setTitle(response.getTitle());
        if (response.getPoster_path() != null) {
            movie.setPosterUrl(POSTER_BASE_URL + response.getPoster_path());
        } else {
            movie.setPosterUrl(null);
        }
        
        if (response.getVoteAverage() != null) {
            movie.setImdbRating(response.getVoteAverage());
        } else {
            movie.setImdbRating(0.0);
        }

        movie.setYear(parseYear(response.getReleaseDate()));

        if (response.getExternalIds() != null) {
            movie.setImdbId(response.getExternalIds().getImdbId());
        }

        if (response.getGenres() != null) {
                String genres = response.getGenres().stream()
                .map(TmdbMovieDetailsResponse.Genre::getName)
                .collect(Collectors.joining(", "));
            movie.setGenre(genres);
        }

        if (response.getCredits() != null) {
            if (response.getCredits().getCast() != null) {
                String actors= response.getCredits().getCast().stream()
                .limit(5)
                .map(TmdbMovieDetailsResponse.CastMember::getName)
                .collect(Collectors.joining(", "));
            movie.setActors(actors);
            }

            if (response.getCredits().getCrew() != null) {
                String director = response.getCredits().getCrew().stream()
                .filter(c -> "Director".equals(c.getJob()))
                .map(TmdbMovieDetailsResponse.CrewMember::getName)
                .findFirst()
                .orElse("N/A");
                movie.setDirector(director);
            }
        }
        return movie;
    }

    private int parseYear(String releaseDate) {
        if (releaseDate == null || releaseDate.length() < 4) {
            return 0;
        }

        try {
            return Integer.parseInt(releaseDate.substring(0,4));
        } catch (NumberFormatException n) {
            return 0;
        }

    }


}
