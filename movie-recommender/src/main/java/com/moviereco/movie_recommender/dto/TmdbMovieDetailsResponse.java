package com.moviereco.movie_recommender.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TmdbMovieDetailsResponse {
    @JsonProperty("title")
    private String title;

    @JsonProperty("release_date")
    private String releaseDate;

    @JsonProperty("poster_path")
    private String poster_path;

    @JsonProperty("vote_average")
    private Double voteAverage;

    @JsonProperty("genres")
    private List<Genre> genres;

    @JsonProperty("credits")
    private Credits credits;

    @JsonProperty("external_ids")
    private ExternalIds externalIds;

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title; 
    }
    public String getReleaseDate() {
        return releaseDate;
    }
    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }
    public String getPoster_path() {
        return poster_path;
    }
    public void setPoster_path(String poster_path) {
        this.poster_path = poster_path;
    }
    public Double getVoteAverage() {
        return voteAverage;
    }
    public void setVoteAverage(Double voteAverage) {
        this.voteAverage = voteAverage;
    }

    public List<Genre> getGenres() {
        return genres;
    }
    public void setGenres(List<Genre> genres) {
        this.genres = genres;
    }
    
    public Credits getCredits() {
        return credits;
    }
    public void setCredits(Credits credits) {
        this.credits = credits;
    }

    public ExternalIds getExternalIds() {
        return externalIds;
    }
    public void setExternalIds(ExternalIds externalIds) {
        this.externalIds = externalIds;
    }


@JsonIgnoreProperties(ignoreUnknown = true)
public static class Genre {
        @JsonProperty("name")
        private String name;

        public String getName() {
            return name;
        }
        public void setName(String name) {
            this.name = name;
        }

}

@JsonIgnoreProperties(ignoreUnknown = true)
public static class Credits {
    @JsonProperty("cast")
    private List<CastMember> cast;

    @JsonProperty("crew")
    private List<CrewMember> crew;

    public List<CastMember> getCast() {
        return cast;
    }
    public void setCast(List<CastMember> cast) {
        this.cast = cast;
    }
    public List<CrewMember> getCrew() {
        return crew;
    }
    public void setCrew(List<CrewMember> crew) {
        this.crew = crew;
    }
}

@JsonIgnoreProperties(ignoreUnknown = true)
public static class CastMember {
    @JsonProperty("name")
    private String name;

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;

    }
}

@JsonIgnoreProperties(ignoreUnknown = true)
public static class CrewMember {
    @JsonProperty("name")
    private String name;

    @JsonProperty("job")
    private String job;

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getJob() {
        return job;
    }
    public void setJob(String job) {
        this.job = job;
    }
}
@JsonIgnoreProperties(ignoreUnknown = true)
public static class ExternalIds {
    @JsonProperty("imdb_id")
    private String imdbId;

    public String getImdbId() {
        return imdbId;
    }
    public void setImdbId(String imdbId) {
        this.imdbId = imdbId;
    }
}
}
