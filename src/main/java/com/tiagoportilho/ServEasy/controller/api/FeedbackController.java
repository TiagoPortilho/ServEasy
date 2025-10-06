package com.tiagoportilho.ServEasy.controller.api;

import com.tiagoportilho.ServEasy.dto.ApiResponse;
import com.tiagoportilho.ServEasy.dto.FeedbackDto;
import com.tiagoportilho.ServEasy.dto.FeedbackResponseDTO;
import com.tiagoportilho.ServEasy.model.Feedback;
import com.tiagoportilho.ServEasy.model.Order;
import com.tiagoportilho.ServEasy.service.FeedbackService;
import com.tiagoportilho.ServEasy.service.OrderService;
import jakarta.validation.Valid;
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
    private final OrderService orderService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<FeedbackResponseDTO>>> getAllFeedbacks() {
        try {
            List<Feedback> feedbacks = feedbackService.getAllFeedbacks();
            List<FeedbackResponseDTO> feedbackDTOs = feedbacks.stream()
                    .map(FeedbackResponseDTO::fromEntity)
                    .toList();
            return ResponseEntity.ok(ApiResponse.success(feedbackDTOs));
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
    public ResponseEntity<ApiResponse<List<FeedbackResponseDTO>>> getFeedbacksByRating(@PathVariable Integer rating) {
        try {
            List<Feedback> feedbacks = feedbackService.getFeedbacksByRating(rating);
            List<FeedbackResponseDTO> feedbackDTOs = feedbacks.stream()
                    .map(FeedbackResponseDTO::fromEntity)
                    .toList();
            return ResponseEntity.ok(ApiResponse.success(feedbackDTOs));
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
    public ResponseEntity<ApiResponse<Feedback>> createFeedback(@Valid @RequestBody FeedbackDto feedbackDto) {
        try {
            Feedback feedback = new Feedback();
            feedback.setCustomerName(feedbackDto.getCustomerName());
            feedback.setRating(feedbackDto.getRating());
            feedback.setComment(feedbackDto.getComment());
            
            // Se foi fornecido um orderId, buscar o pedido
            if (feedbackDto.getOrderId() != null) {
                Optional<Order> order = orderService.getOrderById(feedbackDto.getOrderId());
                order.ifPresent(feedback::setOrder);
            }
            
            Feedback savedFeedback = feedbackService.saveFeedback(feedback);
            return ResponseEntity.ok(ApiResponse.success("Feedback criado com sucesso", savedFeedback));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Erro ao criar feedback: " + e.getMessage()));
        }
    }

    @PostMapping("/simple")
    @CrossOrigin(origins = "*")
    public ResponseEntity<ApiResponse<Feedback>> createSimpleFeedback(@RequestBody FeedbackDto feedbackData) {
        try {
            System.out.println("Recebendo feedback: " + feedbackData);
            
            Feedback feedback = new Feedback();
            feedback.setCustomerName(feedbackData.getCustomerName());
            feedback.setRating(feedbackData.getRating());
            feedback.setComment(feedbackData.getComment());
            
            Feedback savedFeedback = feedbackService.saveFeedback(feedback);
            System.out.println("Feedback salvo: " + savedFeedback);
            
            return ResponseEntity.ok(ApiResponse.success("Feedback criado com sucesso", savedFeedback));
        } catch (Exception e) {
            System.err.println("Erro ao criar feedback: " + e.getMessage());
            e.printStackTrace();
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