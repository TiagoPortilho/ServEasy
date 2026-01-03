package com.tiagoportilho.ServEasy.service;

import com.tiagoportilho.ServEasy.model.Feedback;
import com.tiagoportilho.ServEasy.model.Order;
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
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Testes unitários para FeedbackService.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("FeedbackService Tests")
@SuppressWarnings("null")
class FeedbackServiceTest {

    @Mock
    private FeedbackRepository feedbackRepository;

    @InjectMocks
    private FeedbackService feedbackService;

    private Feedback sampleFeedback;
    private Order sampleOrder;

    @BeforeEach
    void setUp() {
        sampleOrder = new Order();
        sampleOrder.setId(1L);
        sampleOrder.setTableNumber(5);
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
    class GetAllFeedbacksTests {

        @Test
        @DisplayName("deve retornar todos os feedbacks ordenados por data")
        void shouldReturnAllFeedbacksOrderedByDate() {
            Feedback feedback2 = new Feedback();
            feedback2.setId(2L);
            feedback2.setRating(4);
            
            when(feedbackRepository.findByOrderByCreatedAtDesc())
                    .thenReturn(Arrays.asList(sampleFeedback, feedback2));

            List<Feedback> result = feedbackService.getAllFeedbacks();

            assertThat(result).hasSize(2);
            verify(feedbackRepository, times(1)).findByOrderByCreatedAtDesc();
        }

        @Test
        @DisplayName("deve retornar lista vazia quando não há feedbacks")
        void shouldReturnEmptyListWhenNoFeedbacks() {
            when(feedbackRepository.findByOrderByCreatedAtDesc())
                    .thenReturn(Collections.emptyList());

            List<Feedback> result = feedbackService.getAllFeedbacks();

            assertThat(result).isEmpty();
        }
    }

    @Nested
    @DisplayName("getFeedbackById")
    class GetFeedbackByIdTests {

        @Test
        @DisplayName("deve retornar feedback quando existe")
        void shouldReturnFeedbackWhenExists() {
            when(feedbackRepository.findById(1L)).thenReturn(Optional.of(sampleFeedback));

            Optional<Feedback> result = feedbackService.getFeedbackById(1L);

            assertThat(result).isPresent();
            assertThat(result.get().getRating()).isEqualTo(5);
        }

        @Test
        @DisplayName("deve retornar vazio quando feedback não existe")
        void shouldReturnEmptyWhenFeedbackNotExists() {
            when(feedbackRepository.findById(999L)).thenReturn(Optional.empty());

            Optional<Feedback> result = feedbackService.getFeedbackById(999L);

            assertThat(result).isEmpty();
        }
    }

    @Nested
    @DisplayName("saveFeedback")
    class SaveFeedbackTests {

        @Test
        @DisplayName("deve salvar feedback com sucesso")
        void shouldSaveFeedbackSuccessfully() {
            when(feedbackRepository.save(any(Feedback.class))).thenReturn(sampleFeedback);

            Feedback result = feedbackService.saveFeedback(sampleFeedback);

            assertThat(result).isNotNull();
            assertThat(result.getId()).isEqualTo(1L);
            verify(feedbackRepository, times(1)).save(any(Feedback.class));
        }
    }

    @Nested
    @DisplayName("deleteFeedback")
    class DeleteFeedbackTests {

        @Test
        @DisplayName("deve deletar feedback com sucesso")
        void shouldDeleteFeedbackSuccessfully() {
            doNothing().when(feedbackRepository).deleteById(1L);

            feedbackService.deleteFeedback(1L);

            verify(feedbackRepository, times(1)).deleteById(1L);
        }
    }

    @Nested
    @DisplayName("getFeedbacksByRating")
    class GetFeedbacksByRatingTests {

        @Test
        @DisplayName("deve retornar feedbacks com rating específico")
        void shouldReturnFeedbacksWithSpecificRating() {
            when(feedbackRepository.findByRating(5))
                    .thenReturn(Arrays.asList(sampleFeedback));

            List<Feedback> result = feedbackService.getFeedbacksByRating(5);

            assertThat(result).hasSize(1);
            assertThat(result.get(0).getRating()).isEqualTo(5);
        }

        @Test
        @DisplayName("deve retornar lista vazia quando não há feedbacks com o rating")
        void shouldReturnEmptyListWhenNoFeedbacksWithRating() {
            when(feedbackRepository.findByRating(1))
                    .thenReturn(Collections.emptyList());

            List<Feedback> result = feedbackService.getFeedbacksByRating(1);

            assertThat(result).isEmpty();
        }
    }

    @Nested
    @DisplayName("getAverageRating")
    class GetAverageRatingTests {

        @Test
        @DisplayName("deve calcular média corretamente")
        void shouldCalculateAverageCorrectly() {
            Feedback feedback2 = new Feedback();
            feedback2.setRating(4);
            
            Feedback feedback3 = new Feedback();
            feedback3.setRating(3);
            
            when(feedbackRepository.findAll())
                    .thenReturn(Arrays.asList(sampleFeedback, feedback2, feedback3));

            double result = feedbackService.getAverageRating();

            assertThat(result).isEqualTo(4.0); // (5 + 4 + 3) / 3 = 4.0
        }

        @Test
        @DisplayName("deve retornar 0 quando não há feedbacks")
        void shouldReturnZeroWhenNoFeedbacks() {
            when(feedbackRepository.findAll()).thenReturn(Collections.emptyList());

            double result = feedbackService.getAverageRating();

            assertThat(result).isEqualTo(0.0);
        }

        @Test
        @DisplayName("deve calcular média com um único feedback")
        void shouldCalculateAverageWithSingleFeedback() {
            when(feedbackRepository.findAll()).thenReturn(Arrays.asList(sampleFeedback));

            double result = feedbackService.getAverageRating();

            assertThat(result).isEqualTo(5.0);
        }
    }
}
