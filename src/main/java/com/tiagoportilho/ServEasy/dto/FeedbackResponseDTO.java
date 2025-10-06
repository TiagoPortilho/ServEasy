package com.tiagoportilho.ServEasy.dto;

import com.tiagoportilho.ServEasy.model.Feedback;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FeedbackResponseDTO {
    private Long id;
    private String customerName;
    private Integer rating;
    private String comment;
    private Long orderId;
    private LocalDateTime feedbackDate;

    public static FeedbackResponseDTO fromEntity(Feedback feedback) {
        FeedbackResponseDTO dto = new FeedbackResponseDTO();
        dto.setId(feedback.getId());
        dto.setCustomerName(feedback.getCustomerName());
        dto.setRating(feedback.getRating());
        dto.setComment(feedback.getComment());
        dto.setFeedbackDate(feedback.getCreatedAt());
        
        // Evitar problemas de lazy loading
        if (feedback.getOrder() != null) {
            dto.setOrderId(feedback.getOrder().getId());
        }
        
        return dto;
    }
}