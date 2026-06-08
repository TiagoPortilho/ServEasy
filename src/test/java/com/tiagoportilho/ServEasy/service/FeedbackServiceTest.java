package com.tiagoportilho.ServEasy.service;

import com.tiagoportilho.ServEasy.model.Feedback;
import com.tiagoportilho.ServEasy.model.Order;
import com.tiagoportilho.ServEasy.model.RestaurantTable;
import com.tiagoportilho.ServEasy.repository.FeedbackRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("FeedbackService")
class FeedbackServiceTest {

    @Mock
    private FeedbackRepository feedbackRepository;

    @InjectMocks
    private FeedbackService feedbackService;

    private Feedback sampleFeedback;

    @BeforeEach
    void setUp() {
        RestaurantTable table = new RestaurantTable();
        table.setId(1L);
        table.setTableNumber(5);
        table.setCapacity(4);
        table.setStatus(RestaurantTable.TableStatus.DISPONIVEL);

        Order sampleOrder = new Order();
        sampleOrder.setId(1L);
        sampleOrder.setTable(table);
        sampleOrder.setCustomerName("João");

        sampleFeedback = new Feedback();
        sampleFeedback.setId(1L);
        sampleFeedback.setRating(5);
        sampleFeedback.setComment("Excelente comida!");
        sampleFeedback.setCustomerName("João");
        sampleFeedback.setOrder(sampleOrder);
        sampleFeedback.setCreatedAt(LocalDateTime.now());
    }

    @Nested
    @DisplayName("getAllFeedbacks")
    class GetAllFeedbacks {

        @Test
        @DisplayName("retorna todos os feedbacks ordenados por data")
        void returns_all_feedbacks_ordered_by_date() {
            Feedback feedback2 = new Feedback();
            feedback2.setId(2L);
            feedback2.setRating(4);

            when(feedbackRepository.findByOrderByCreatedAtDesc())
                    .thenReturn(List.of(sampleFeedback, feedback2));

            List<Feedback> result = feedbackService.getAllFeedbacks();

            assertThat(result).hasSize(2);
            verify(feedbackRepository).findByOrderByCreatedAtDesc();
        }

        @Test
        @DisplayName("retorna lista vazia quando não há feedbacks")
        void returns_empty_list_when_no_feedbacks() {
            when(feedbackRepository.findByOrderByCreatedAtDesc()).thenReturn(Collections.emptyList());

            assertThat(feedbackService.getAllFeedbacks()).isEmpty();
        }
    }

    @Nested
    @DisplayName("getFeedbackById")
    class GetFeedbackById {

        @Test
        @DisplayName("retorna feedback quando existe")
        void returns_feedback_when_exists() {
            when(feedbackRepository.findById(1L)).thenReturn(Optional.of(sampleFeedback));

            Optional<Feedback> result = feedbackService.getFeedbackById(1L);

            assertThat(result).isPresent();
            assertThat(result.get().getRating()).isEqualTo(5);
        }

        @Test
        @DisplayName("retorna vazio quando não existe")
        void returns_empty_when_not_found() {
            when(feedbackRepository.findById(999L)).thenReturn(Optional.empty());

            assertThat(feedbackService.getFeedbackById(999L)).isEmpty();
        }
    }

    @Nested
    @DisplayName("saveFeedback")
    class SaveFeedback {

        @Test
        @DisplayName("persiste e retorna o feedback salvo")
        void saves_and_returns_feedback() {
            when(feedbackRepository.save(any(Feedback.class))).thenReturn(sampleFeedback);

            Feedback result = feedbackService.saveFeedback(sampleFeedback);

            assertThat(result.getId()).isEqualTo(1L);
            verify(feedbackRepository).save(any(Feedback.class));
        }
    }

    @Nested
    @DisplayName("deleteFeedback")
    class DeleteFeedback {

        @Test
        @DisplayName("delega deleção ao repositório")
        void delegates_to_repository() {
            doNothing().when(feedbackRepository).deleteById(1L);

            feedbackService.deleteFeedback(1L);

            verify(feedbackRepository).deleteById(1L);
        }
    }

    @Nested
    @DisplayName("getFeedbacksByRating")
    class GetFeedbacksByRating {

        @Test
        @DisplayName("retorna feedbacks com rating específico")
        void returns_feedbacks_with_specific_rating() {
            when(feedbackRepository.findByRating(5)).thenReturn(List.of(sampleFeedback));

            List<Feedback> result = feedbackService.getFeedbacksByRating(5);

            assertThat(result).hasSize(1);
            assertThat(result.get(0).getRating()).isEqualTo(5);
        }

        @Test
        @DisplayName("retorna lista vazia quando não há feedbacks com o rating")
        void returns_empty_when_no_matching_rating() {
            when(feedbackRepository.findByRating(1)).thenReturn(Collections.emptyList());

            assertThat(feedbackService.getFeedbacksByRating(1)).isEmpty();
        }
    }

    @Nested
    @DisplayName("getAverageRating")
    class GetAverageRating {

        @Test
        @DisplayName("calcula média corretamente com múltiplos feedbacks")
        void calculates_average_correctly() {
            Feedback f2 = new Feedback();
            f2.setRating(4);
            Feedback f3 = new Feedback();
            f3.setRating(3);

            when(feedbackRepository.findAll()).thenReturn(List.of(sampleFeedback, f2, f3));

            assertThat(feedbackService.getAverageRating()).isEqualTo(4.0); // (5 + 4 + 3) / 3
        }

        @Test
        @DisplayName("retorna 0.0 quando não há feedbacks")
        void returns_zero_when_no_feedbacks() {
            when(feedbackRepository.findAll()).thenReturn(Collections.emptyList());

            assertThat(feedbackService.getAverageRating()).isEqualTo(0.0);
        }

        @Test
        @DisplayName("retorna exatamente o rating quando há um único feedback")
        void returns_exact_rating_for_single_feedback() {
            when(feedbackRepository.findAll()).thenReturn(List.of(sampleFeedback));

            assertThat(feedbackService.getAverageRating()).isEqualTo(5.0);
        }
    }
}
