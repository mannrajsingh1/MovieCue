package com.moviereco.movie_recommender.service;

import com.moviereco.movie_recommender.model.Visitor;
import com.moviereco.movie_recommender.repository.VisitorRepository;
import org.springframework.stereotype.Service;

import java.util.regex.Pattern;

@Service
public class VisitorService {
    private static final Pattern KEY_PATTERN = Pattern.compile("^[A-Za-z0-9-]{16,64}$");
    
    private final VisitorRepository visitorRepository;

    public VisitorService(VisitorRepository visitorRepository) {
        this.visitorRepository = visitorRepository;
    }

    public static void validateKey(String visitorKey) {
        if (visitorKey == null || !KEY_PATTERN.matcher(visitorKey).matches()) {
            throw new IllegalArgumentException("MIssing or invalid visiotr id.");
        }
    }

    public Visitor getOrCreate(String visitorKey) {
        validateKey(visitorKey);
        return visitorRepository.findByVisitorKey(visitorKey).orElseGet(() -> visitorRepository.save(new Visitor(visitorKey)));
    }
}
