package com.moviereco.movie_recommender.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.moviereco.movie_recommender.dto.OmdbSearchResponse;
import com.moviereco.movie_recommender.model.Movie;
import com.moviereco.movie_recommender.model.Rating;
import com.moviereco.movie_recommender.repository.RatingRepository;

@Service
public class RecommendationService {
    private final RatingRepository ratingRepository;
    private final OmdbService omdbService;
    private final TmdbService tmdbService;

    private final double genreWeight;
    private final double actorWeight;
    private final double directorWeight;

    public RecommendationService(RatingRepository ratingRepository,
                                 OmdbService omdbService,
                                 TmdbService tmdbService,
                                 @Value("${recommendation.weight.genre}") double genreWeight,
                                 @Value("${recommendation.weight.actor}") double actorWeight,
                                 @Value("${recommendation.weight.director}") double directorWeight) {
        this.ratingRepository = ratingRepository;
        this.omdbService = omdbService;
        this.tmdbService = tmdbService;
        this.genreWeight = genreWeight;
        this.actorWeight = actorWeight;
        this.directorWeight = directorWeight;
    }

    public List<ScoredMovie> recommend(String keyword, int limit) {

        List<Rating> userRatings = ratingRepository.findAll();

        if (userRatings.isEmpty()) {
            throw new EmptyRatingsException("No ratings found for the user. Please rate some movies first.");
        }

        List<OmdbSearchResponse.OmdbSearchResult> candidates = omdbService.searchMovies(keyword);

        Set<String> alreadyRatedImdbIds = userRatings.stream()
                .map(rating -> rating.getMovie().getImdbId())
                .collect(Collectors.toSet());

        List<ScoredMovie> scoredMovies = new ArrayList<>();
        for (OmdbSearchResponse.OmdbSearchResult candidate : candidates) {
            if (!alreadyRatedImdbIds.contains(candidate.getImdbId())) {
                Movie candidateMovie = omdbService.fetchMovieByImdbId(candidate.getImdbId());
                double score = scoreCandidate(candidateMovie, userRatings);
                scoredMovies.add(new ScoredMovie(candidateMovie, score));
            }
        }
        return scoredMovies.stream().limit(limit).toList();
    }

    public List<ScoredMovie> recommend(int limit) {
        List<Rating> userRatings = requireRatings();

        Set<String> peopleNames = derivePeopleFromRatings(userRatings);

        Set<Long> candidateMovieIds = new LinkedHashSet<>();

        for (String name : peopleNames) {
            tmdbService.searchPersonIdByName(name)
                    .ifPresent(personId -> candidateMovieIds.addAll(tmdbService.getFilmographyMovieIds(personId)));
        }

        if (candidateMovieIds.isEmpty()) {
            throw new IllegalStateException("No candidates found from TMDB based on your ratings.");
        }

        Set<String> alreadyRatedImdbIds = userRatings.stream()
                .map(rating -> rating.getMovie().getImdbId())
                .collect(Collectors.toSet());

        List<ScoredMovie> scoredMovies = new ArrayList<>();

        for (Long movieId : candidateMovieIds) {
            Movie candidateMovie = tmdbService.getMovieDetails(movieId);
            if (candidateMovie == null) {
                continue;
            }

            if (candidateMovie.getImdbId() != null && alreadyRatedImdbIds.contains(candidateMovie.getImdbId())) {
                continue;
            }
                double score = scoreCandidate(candidateMovie, userRatings);
                scoredMovies.add(new ScoredMovie(candidateMovie, score));
            }

        scoredMovies.sort((a, b) -> Double.compare(b.score(), a.score()));
        return scoredMovies.stream().distinct().limit(limit).toList();
    }

    private List<Rating> requireRatings() {
        List<Rating> userRatings = ratingRepository.findAll();
        if (userRatings.isEmpty()) {
            throw new IllegalStateException("Rate atleast one movie to get recommendations.");
        }
        return userRatings;
    }

    private Set<String> derivePeopleFromRatings(List<Rating> userRatings) {
        Set<String> people = new LinkedHashSet<>();
        for (Rating rating : userRatings) {
            Movie movie = rating.getMovie();
            if (movie.getDirector() != null && !movie.getDirector().equalsIgnoreCase("N/A")) {
                people.add(movie.getDirector());
            }
            List<String> actors = splitToList(movie.getActors());
            if (!actors.isEmpty()) {
                people.add(actors.get(0));
            }
        }
        return people;
    }

    private List<String> splitToList(String commaSeparated) {
        if (commaSeparated == null || commaSeparated.isEmpty()) {
            return List.of();
        }
        return Arrays.stream(commaSeparated.split(","))
                .map(String::trim)
                .filter(s -> !s.isBlank())
                .toList();
    }

    private List<ScoredMovie> scoreAndRank(List<OmdbSearchResponse.OmdbSearchResult> candidates, List<Rating> userRatings, int limit) {
        Set<String> alreadyRatedImdbIds = userRatings.stream()
                .map(rating -> rating.getMovie().getImdbId())
                .collect(Collectors.toSet());

        List<ScoredMovie> scoredMovies = new ArrayList<>();
        for (OmdbSearchResponse.OmdbSearchResult candidate : candidates) {
            if (!alreadyRatedImdbIds.contains(candidate.getImdbId())) {
                Movie candidateMovie = omdbService.fetchMovieByImdbId(candidate.getImdbId());
                double score = scoreCandidate(candidateMovie, userRatings);
                scoredMovies.add(new ScoredMovie(candidateMovie, score));
            }
        }
        scoredMovies.sort((a, b) -> Double.compare(b.score(), a.score()));
        return scoredMovies.stream().limit(limit).toList();
    }

    private double scoreCandidate(Movie candidate, List<Rating> userRatings) {
        Set<String> candidateGenres = splitToSet(candidate.getGenre());
        Set<String> candidateActors = splitToSet(candidate.getActors());
        String candidateDirector = candidate.getDirector();

        double totalScore = 0.0;
        for (Rating rating : userRatings) {
            Movie ratedMovie = rating.getMovie();

            Set<String> ratedGenres = splitToSet(ratedMovie.getGenre());
            Set<String> ratedActors = splitToSet(ratedMovie.getActors());

            int genreOverlap = intersectionSize(candidateGenres, ratedGenres);
            int actorOverlap = intersectionSize(candidateActors, ratedActors);

            boolean directorMatch = candidateDirector != null && candidateDirector.equalsIgnoreCase(ratedMovie.getDirector());

            double similarity = (genreWeight * genreOverlap) + (actorWeight * actorOverlap) + (directorMatch ? directorWeight : 0);

            double weightByRating = rating.getRating() / 10.0;
            totalScore += similarity * weightByRating;
        }
        return totalScore;
    }

    private Set<String> splitToSet(String commaSeparated) {
        if (commaSeparated == null || commaSeparated.isEmpty()) {
            return Set.of();
        }
        return Arrays.stream(commaSeparated.split(","))
                .map(String::trim)
                .map(String::toLowerCase)
                .collect(Collectors.toSet());
    }

    private int intersectionSize(Set<String> set1, Set<String> set2) {
        Set<String> copy = new HashSet<>(set1);
        copy.retainAll(set2);
        return copy.size();
    }

    public static class EmptyRatingsException extends RuntimeException {
        public EmptyRatingsException(String message) {
            super(message);
        }
    }

    public record ScoredMovie(Movie movie, double score) {
    }
}

    
