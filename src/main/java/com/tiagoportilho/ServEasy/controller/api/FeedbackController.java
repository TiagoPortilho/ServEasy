package com.tiagoportilho.ServEasy.controller.api;

import com.tiagoportilho.ServEasy.dto.ApiResponse;
import com.tiagoportilho.ServEasy.dto.FeedbackDto;
import com.tiagoportilho.ServEasy.dto.FeedbackResponseDTO;
import com.tiagoportilho.ServEasy.exception.ResourceNotFoundException;
import com.tiagoportilho.ServEasy.model.Feedback;
import com.tiagoportilho.ServEasy.model.Order;
import com.tiagoportilho.ServEasy.service.FeedbackService;
import com.tiagoportilho.ServEasy.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * Controller para gerenciamento de feedbacks dos clientes.
 */
@RestController
@RequestMapping("/api/feedbacks")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Feedbacks", description = "Gerenciamento de avaliações dos clientes")
public class FeedbackController {

    private final FeedbackService feedbackService;
    private final OrderService orderService;

    @GetMapping
    @Operation(summary = "Listar todos os feedbacks", description = "Retorna todos os feedbacks ordenados por data de criação")
    public ResponseEntity<ApiResponse<List<FeedbackResponseDTO>>> getAllFeedbacks() {
        List<Feedback> feedbacks = feedbackService.getAllFeedbacks();
        List<FeedbackResponseDTO> feedbackDTOs = feedbacks.stream()
                .map(FeedbackResponseDTO::fromEntity)
                .toList();
        return ResponseEntity.ok(ApiResponse.success(feedbackDTOs));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar feedback por ID", description = "Retorna um feedback específico pelo seu ID")
    public ResponseEntity<ApiResponse<Feedback>> getFeedbackById(
            @Parameter(description = "ID do feedback") @PathVariable Long id) {
        Feedback feedback = feedbackService.getFeedbackById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Feedback", id));
        return ResponseEntity.ok(ApiResponse.success(feedback));
    }

    @GetMapping("/rating/{rating}")
    @Operation(summary = "Buscar feedbacks por avaliação", description = "Retorna todos os feedbacks com uma avaliação específica (1-5)")
    public ResponseEntity<ApiResponse<List<FeedbackResponseDTO>>> getFeedbacksByRating(
            @Parameter(description = "Valor da avaliação (1-5)") @PathVariable Integer rating) {
        List<Feedback> feedbacks = feedbackService.getFeedbacksByRating(rating);
        List<FeedbackResponseDTO> feedbackDTOs = feedbacks.stream()
                .map(FeedbackResponseDTO::fromEntity)
                .toList();
        return ResponseEntity.ok(ApiResponse.success(feedbackDTOs));
    }

    @GetMapping("/average-rating")
    @Operation(summary = "Obter média de avaliações", description = "Retorna a média de todas as avaliações")
    public ResponseEntity<ApiResponse<Double>> getAverageRating() {
        double averageRating = feedbackService.getAverageRating();
        return ResponseEntity.ok(ApiResponse.success(averageRating));
    }

    @PostMapping
    @Operation(summary = "Criar feedback", description = "Cria um novo feedback com validação de dados")
    public ResponseEntity<ApiResponse<Feedback>> createFeedback(
            @Valid @RequestBody FeedbackDto feedbackDto) {
        Feedback feedback = new Feedback();
        feedback.setCustomerName(feedbackDto.getCustomerName());
        feedback.setRating(feedbackDto.getRating());
        feedback.setComment(feedbackDto.getComment());
        
        if (feedbackDto.getOrderId() != null) {
            Optional<Order> order = orderService.getOrderById(feedbackDto.getOrderId());
            order.ifPresent(feedback::setOrder);
        }
        
        Feedback savedFeedback = feedbackService.saveFeedback(feedback);
        log.info("Feedback criado com ID: {}", savedFeedback.getId());
        return ResponseEntity.ok(ApiResponse.success("Feedback criado com sucesso", savedFeedback));
    }

    @PostMapping("/simple")
    @Operation(summary = "Criar feedback simplificado", description = "Cria um novo feedback sem associação com pedido")
    public ResponseEntity<ApiResponse<Feedback>> createSimpleFeedback(
            @RequestBody FeedbackDto feedbackData) {
        log.debug("Recebendo feedback simplificado: {}", feedbackData);
        
        Feedback feedback = new Feedback();
        feedback.setCustomerName(feedbackData.getCustomerName());
        feedback.setRating(feedbackData.getRating());
        feedback.setComment(feedbackData.getComment());
        
        Feedback savedFeedback = feedbackService.saveFeedback(feedback);
        log.info("Feedback simplificado criado com ID: {}", savedFeedback.getId());
        
        return ResponseEntity.ok(ApiResponse.success("Feedback criado com sucesso", savedFeedback));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar feedback", description = "Remove um feedback pelo ID")
    public ResponseEntity<ApiResponse<Void>> deleteFeedback(
            @Parameter(description = "ID do feedback") @PathVariable Long id) {
        feedbackService.getFeedbackById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Feedback", id));
        
        feedbackService.deleteFeedback(id);
        log.info("Feedback deletado com ID: {}", id);
        return ResponseEntity.ok(ApiResponse.success("Feedback deletado com sucesso", null));
    }
}