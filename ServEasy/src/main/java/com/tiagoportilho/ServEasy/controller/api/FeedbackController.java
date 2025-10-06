package com.tiagoportilho.ServEasy.controller.api;

import com.tiagoportilho.ServEasy.dto.ApiResponse;
import com.tiagoportilho.ServEasy.model.Feedback;
import com.tiagoportilho.ServEasy.service.FeedbackService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/feedbacks")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class FeedbackController {

    private final FeedbackService feedbackService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<Feedback>>> getAllFeedbacks() {
        try {
            List<Feedback> feedbacks = feedbackService.getAllFeedbacks();
            return ResponseEntity.ok(ApiResponse.success(feedbacks));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.error("Erro ao buscar feedbacks: " + e.getMessage()));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Feedback>> getFeedbackById(@PathVariable Long id) {
        try {
            Optional<Feedback> feedback = feedbackService.getFeedbackById(id);
            if (feedback.isPresent()) {
                return ResponseEntity.ok(ApiResponse.success(feedback.get()));
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.error("Erro ao buscar feedback: " + e.getMessage()));
        }
    }

    @GetMapping("/rating/{rating}")
    public ResponseEntity<ApiResponse<List<Feedback>>> getFeedbacksByRating(@PathVariable Integer rating) {
        try {
            List<Feedback> feedbacks = feedbackService.getFeedbacksByRating(rating);
            return ResponseEntity.ok(ApiResponse.success(feedbacks));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.error("Erro ao buscar feedbacks por avaliação: " + e.getMessage()));
        }
    }

    @GetMapping("/average-rating")
    public ResponseEntity<ApiResponse<Double>> getAverageRating() {
        try {
            double averageRating = feedbackService.getAverageRating();
            return ResponseEntity.ok(ApiResponse.success(averageRating));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.error("Erro ao calcular média de avaliações: " + e.getMessage()));
        }
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Feedback>> createFeedback(@RequestBody Feedback feedback) {
        try {
            Feedback savedFeedback = feedbackService.saveFeedback(feedback);
            return ResponseEntity.ok(ApiResponse.success("Feedback criado com sucesso", savedFeedback));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Erro ao criar feedback: " + e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteFeedback(@PathVariable Long id) {
        try {
            feedbackService.deleteFeedback(id);
            return ResponseEntity.ok(ApiResponse.success("Feedback deletado com sucesso", null));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Erro ao deletar feedback: " + e.getMessage()));
        }
    }
}