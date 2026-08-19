package com.moviereco.movie_recommender.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TmdbMovieCreditsResponse {

    @JsonProperty("cast")
    private List<TmdbCredit> cast;

    @JsonProperty("crew")
    private List<TmdbCredit> crew;

    public List<TmdbCredit> getCast() {
        return cast;
    }

    public void setCast(List<TmdbCredit> cast) {
        this.cast = cast;
    }

    public List<TmdbCredit> getCrew() {
        return crew;
    }

    public void setCrew(List<TmdbCredit> crew) {
        this.crew = crew;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class TmdbCredit {

        @JsonProperty("id")
        private Long id;

        @JsonProperty("title")
        private String title;

        @JsonProperty("job")
        private String job;

        public Long getId() {
            return id;
        }
        public void setId(Long id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }
        public void setTitle(String title) {
            this.title = title;
        }
        public String getJob() {
            return job;
        }
        public void setJob(String job) {
            this.job = job;
        }
}
}
