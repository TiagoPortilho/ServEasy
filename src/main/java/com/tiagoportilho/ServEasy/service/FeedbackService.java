package com.tiagoportilho.ServEasy.service;

import com.tiagoportilho.ServEasy.model.Feedback;
import com.tiagoportilho.ServEasy.repository.FeedbackRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FeedbackService {

    private final FeedbackRepository feedbackRepository;

    @Transactional(readOnly = true)
    public List<Feedback> getAllFeedbacks() {
        return feedbackRepository.findByOrderByCreatedAtDesc();
    }

    @Transactional(readOnly = true)
    public Optional<Feedback> getFeedbackById(Long id) {
        return feedbackRepository.findById(id);
    }

    @Transactional
    public Feedback saveFeedback(Feedback feedback) {
        return feedbackRepository.save(feedback);
    }

    @Transactional
    public void deleteFeedback(Long id) {
        feedbackRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<Feedback> getFeedbacksByRating(Integer rating) {
        return feedbackRepository.findByRating(rating);
    }

    @Transactional(readOnly = true)
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
