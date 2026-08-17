package com.moviereco.movie_recommender.service;

import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import com.moviereco.movie_recommender.dto.TmdbMovieCreditsResponse;
import com.moviereco.movie_recommender.dto.TmdbSearchResponse;

@Service
public class TmdbService {
    private final RestClient restClient;
    private final String apiKey;

    public TmdbService(@Value("${tmdb.api.key}") String apiKey) {
        this.apiKey = apiKey;
        this.restClient = RestClient.builder()
                .baseUrl("https://api.themoviedb.org/3/")
                .build();
    }

    public Optional<Long> searchPersonIdByName(String name) {
        TmdbSearchResponse response = restClient.get().uri(uriBuilder -> uriBuilder
                .path("search/person")
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

    public Set<String> getFilmographyTitles(Long personId) {
        TmdbMovieCreditsResponse response = restClient.get().uri(uriBuilder -> uriBuilder
                .path("person/{person_id}/movie_credits")
                .queryParam("api_key", apiKey)
                .build(personId))
            .retrieve()
            .body(TmdbMovieCreditsResponse.class);

            Set<String> titles = new LinkedHashSet<>();

            if(response == null){
                return titles;
            }

            if (response.getCast() != null) {
                response.getCast().forEach(credit -> {
                    if (credit.getTitle() != null) {
                        titles.add(credit.getTitle());
                    }
                });
            }

            if (response.getCrew() != null) {
                response.getCrew().stream().filter(credit -> "Director".equals(credit.getJob()))
                .forEach(credit -> {
                    if (credit.getTitle() != null) {
                        titles.add(credit.getTitle());
                    }
                });
            }
            return titles;
    }


}
