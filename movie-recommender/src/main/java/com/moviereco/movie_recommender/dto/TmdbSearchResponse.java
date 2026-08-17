package com.moviereco.movie_recommender.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TmdbSearchResponse {
    @JsonProperty("results")
    private List<TmdbPerson> results;

    public List<TmdbPerson> getResults() {
        return results;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class TmdbPerson {
        @JsonProperty("id")
        private Long id;

        @JsonProperty("name")
        private String name;

        public Long getId() {
            return id;
        }
        public void setId(Long id) {
            this.id = id;
        }
        public String getName() {
            return name;
        }
        public void setName(String name) {
            this.name = name;
        }

}
}
