package com.moviereco.movie_recommender.model;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;

@Entity
@Table(name = "Movie List")
public class Movie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String imdbId;


    private String title;

    @Column(name = "release_year")
    private int year;

    @Column(length = 500)
    private String genre;

    private String director;

    @Column(length = 1000)
    private String actors;

    private double imdbRating;

    @OneToMany(mappedBy = "movie", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Rating> ratings = new ArrayList<>();

    private String posterUrl;

    public Movie() {
        // required by JPA
    }

    public Movie(Long id, String imdbId, String title, int year, String genre, String director, String actors, double imdbRating, String posterUrl) {
        this.id = id;
        this.imdbId = imdbId;
        this.title = title;
        this.year = year;
        this.genre = genre;
        this.director = director;
        this.actors = actors;
        this.imdbRating = imdbRating;
        this.ratings = new ArrayList<>();
        this.posterUrl = posterUrl;
    }

    public Long getId() {
        return id;
    }
    public String getImdbId() {
        return imdbId;
    }
    public String getTitle() {
        return title;
    }
    public void setImdbId(String imdbId) {
        this.imdbId = imdbId;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public String getActors() {
        return actors;
    }

    public void setActors(String actors) {
        this.actors = actors;
    }

    public double getImdbRating() {
        return imdbRating;
    }

    public void setImdbRating(double imdbRating) {
        this.imdbRating = imdbRating;
    }

    public List<Rating> getRatings() {
        return ratings;
    }

    public String getPosterUrl() {
        return posterUrl;
    }

    public void setPosterUrl(String posterUrl) {
        this.posterUrl = posterUrl;
    }
}
