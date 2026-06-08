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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/feedbacks")
@RequiredArgsConstructor
@Tag(name = "Feedbacks", description = "Avaliações dos clientes: submissão pública e consulta administrativa")
public class FeedbackController {

    private final FeedbackService feedbackService;
    private final OrderService orderService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Listar feedbacks", description = "Retorna todos os feedbacks ordenados por data. Somente ADMIN.")
    public ResponseEntity<ApiResponse<List<FeedbackResponseDTO>>> getAllFeedbacks() {
        List<FeedbackResponseDTO> dtos = feedbackService.getAllFeedbacks().stream()
                .map(FeedbackResponseDTO::fromEntity).toList();
        return ResponseEntity.ok(ApiResponse.success(dtos));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Buscar feedback por ID", description = "Retorna um feedback específico. Somente ADMIN. Retorna 404 se não encontrado.")
    public ResponseEntity<ApiResponse<FeedbackResponseDTO>> getFeedbackById(
            @Parameter(description = "ID do feedback", example = "1") @PathVariable Long id) {
        Feedback feedback = feedbackService.getFeedbackById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Feedback", id));
        return ResponseEntity.ok(ApiResponse.success(FeedbackResponseDTO.fromEntity(feedback)));
    }

    @GetMapping("/rating/{rating}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Feedbacks por avaliação", description = "Retorna feedbacks com uma nota específica (1-5). Somente ADMIN.")
    public ResponseEntity<ApiResponse<List<FeedbackResponseDTO>>> getFeedbacksByRating(
            @Parameter(description = "Nota (1-5)", example = "5") @PathVariable Integer rating) {
        List<FeedbackResponseDTO> dtos = feedbackService.getFeedbacksByRating(rating).stream()
                .map(FeedbackResponseDTO::fromEntity).toList();
        return ResponseEntity.ok(ApiResponse.success(dtos));
    }

    @GetMapping("/average-rating")
    @Operation(summary = "Média de avaliações", description = "Retorna a média de todas as avaliações. Acesso público.")
    public ResponseEntity<ApiResponse<Double>> getAverageRating() {
        return ResponseEntity.ok(ApiResponse.success(feedbackService.getAverageRating()));
    }

    @PostMapping
    @Operation(summary = "Enviar feedback", description = "Submete uma avaliação do cliente. Acesso público — não requer autenticação.")
    public ResponseEntity<ApiResponse<FeedbackResponseDTO>> createFeedback(@Valid @RequestBody FeedbackDto feedbackDto) {
        Feedback feedback = new Feedback();
        feedback.setCustomerName(feedbackDto.getCustomerName());
        feedback.setRating(feedbackDto.getRating());
        feedback.setComment(feedbackDto.getComment());

        if (feedbackDto.getOrderId() != null) {
            Optional<Order> order = orderService.getOrderById(feedbackDto.getOrderId());
            order.ifPresent(feedback::setOrder);
        }

        Feedback saved = feedbackService.saveFeedback(feedback);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Feedback enviado com sucesso", FeedbackResponseDTO.fromEntity(saved)));
    }

    @PostMapping("/simple")
    @Operation(summary = "Enviar feedback simplificado", description = "Submete avaliação sem associação com pedido. Acesso público.")
    public ResponseEntity<ApiResponse<FeedbackResponseDTO>> createSimpleFeedback(@Valid @RequestBody FeedbackDto feedbackDto) {
        Feedback feedback = new Feedback();
        feedback.setCustomerName(feedbackDto.getCustomerName());
        feedback.setRating(feedbackDto.getRating());
        feedback.setComment(feedbackDto.getComment());

        Feedback saved = feedbackService.saveFeedback(feedback);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Feedback enviado com sucesso", FeedbackResponseDTO.fromEntity(saved)));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Deletar feedback", description = "Remove um feedback. Somente ADMIN. Retorna 404 se não encontrado.")
    public ResponseEntity<Void> deleteFeedback(
            @Parameter(description = "ID do feedback", example = "1") @PathVariable Long id) {
        feedbackService.getFeedbackById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Feedback", id));
        feedbackService.deleteFeedback(id);
        return ResponseEntity.noContent().build();
    }
}
