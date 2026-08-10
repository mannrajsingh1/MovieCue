package com.moviereco.movie_recommender.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class OmdbSearchResponse {

    @JsonProperty("Search")
    private List<OmdbSearchResult> search;

    @JsonProperty("Response")
    private String response;

    public List<OmdbSearchResult> getSearch() {
        return search;
    }

    public void setSearch(List<OmdbSearchResult> search) {
        this.search = search;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class OmdbSearchResult {

        @JsonProperty("Title")
        private String title;

        @JsonProperty("imdbID")
        private String imdbId;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getImdbId() {
            return imdbId;
        }

        public void setImdbId(String imdbId) {
            this.imdbId = imdbId;
        }
    }
}
