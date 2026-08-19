package com.moviereco.movie_recommender.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.moviereco.movie_recommender.service.RecommendationService;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/api/recommendations")
public class RecommendationController {
    
    private final RecommendationService recommendationService;

    public RecommendationController(RecommendationService recommendationService) {
        this.recommendationService = recommendationService;
    }

    @GetMapping
    public List<RecommendationService.ScoredMovie> getRecommendations(@RequestParam String keyword, @RequestParam(defaultValue = "7") int limit) {
        return recommendationService.recommend(keyword, limit);
    }

    @GetMapping("/for-me")
    public List<RecommendationService.ScoredMovie> getPersonalizedRecommendations(@RequestParam(defaultValue = "5") int limit) {
        return recommendationService.recommend(limit);
    }
    
}
