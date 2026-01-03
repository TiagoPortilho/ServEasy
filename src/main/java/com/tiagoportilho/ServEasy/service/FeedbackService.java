package com.tiagoportilho.ServEasy.service;

import com.tiagoportilho.ServEasy.model.Feedback;
import com.tiagoportilho.ServEasy.repository.FeedbackRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@SuppressWarnings("null")
public class FeedbackService {
    
    private final FeedbackRepository feedbackRepository;

    public List<Feedback> getAllFeedbacks() {
        return feedbackRepository.findByOrderByCreatedAtDesc();
    }

    public Optional<Feedback> getFeedbackById(Long id) {
        return feedbackRepository.findById(id);
    }

    public Feedback saveFeedback(Feedback feedback) {
        return feedbackRepository.save(feedback);
    }

    public void deleteFeedback(Long id) {
        feedbackRepository.deleteById(id);
    }

    public List<Feedback> getFeedbacksByRating(Integer rating) {
        return feedbackRepository.findByRating(rating);
    }

    public double getAverageRating() {
        List<Feedback> feedbacks = feedbackRepository.findAll();
        if (feedbacks.isEmpty()) {
            return 0.0;
        }
        return feedbacks.stream()
                .mapToInt(Feedback::getRating)
                .average()
                .orElse(0.0);
    }
}