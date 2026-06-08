package com.tiagoportilho.ServEasy.service;

import com.tiagoportilho.ServEasy.dto.OrderRequest;
import com.tiagoportilho.ServEasy.dto.request.OrderItemRequest;
import com.tiagoportilho.ServEasy.exception.BusinessException;
import com.tiagoportilho.ServEasy.exception.ResourceNotFoundException;
import com.tiagoportilho.ServEasy.model.MenuItem;
import com.tiagoportilho.ServEasy.model.Order;
import com.tiagoportilho.ServEasy.model.Order.OrderStatus;
import com.tiagoportilho.ServEasy.model.RestaurantTable;
import com.tiagoportilho.ServEasy.model.RestaurantTable.TableStatus;
import com.tiagoportilho.ServEasy.repository.MenuItemRepository;
import com.tiagoportilho.ServEasy.repository.OrderRepository;
import com.tiagoportilho.ServEasy.repository.RestaurantTableRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("OrderService")
class OrderServiceTest {

    @Mock private OrderRepository orderRepository;
    @Mock private MenuItemRepository menuItemRepository;
    @Mock private RestaurantTableRepository restaurantTableRepository;
    @InjectMocks private OrderService orderService;

    private RestaurantTable occupiedTable;
    private MenuItem availableItem;
    private Order novoOrder;

    @BeforeEach
    void setUp() {
        occupiedTable = new RestaurantTable();
        occupiedTable.setId(1L);
        occupiedTable.setTableNumber(5);
        occupiedTable.setStatus(TableStatus.OCUPADA);

        availableItem = new MenuItem();
        availableItem.setId(1L);
        availableItem.setName("Hambúrguer");
        availableItem.setPrice(new BigDecimal("25.00"));
        availableItem.setIsAvailable(true);

        novoOrder = new Order();
        novoOrder.setId(1L);
        novoOrder.setTable(occupiedTable);
        novoOrder.setStatus(OrderStatus.NOVO);
        novoOrder.setTotal(new BigDecimal("25.00"));
        novoOrder.setCreatedAt(LocalDateTime.now());
    }

    // ─────────────────────────────── createOrder ─────────────────────────────────

    @Nested @DisplayName("createOrder")
    class CreateOrder {

        @Test
        @DisplayName("cria pedido válido em mesa ocupada")
        void creates_order_for_occupied_table() {
            OrderRequest req = validRequest();
            when(restaurantTableRepository.findByTableNumber(5)).thenReturn(Optional.of(occupiedTable));
            when(menuItemRepository.findById(1L)).thenReturn(Optional.of(availableItem));
            when(orderRepository.save(any())).thenAnswer(inv -> {
                Order o = inv.getArgument(0);
                o.setId(1L);
                return o;
            });

            Order result = orderService.createOrder(req);

            assertThat(result.getStatus()).isEqualTo(OrderStatus.NOVO);
            assertThat(result.getTotal()).isEqualByComparingTo("50.00");
            verify(orderRepository).save(any());
        }

        @Test
        @DisplayName("lança ResourceNotFoundException quando mesa não encontrada")
        void throws_when_table_not_found() {
            when(restaurantTableRepository.findByTableNumber(5)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> orderService.createOrder(validRequest()))
                    .isInstanceOf(ResourceNotFoundException.class);
        }

        @Test
        @DisplayName("lança BusinessException quando mesa está DISPONIVEL")
        void throws_when_table_is_available() {
            occupiedTable.setStatus(TableStatus.DISPONIVEL);
            when(restaurantTableRepository.findByTableNumber(5)).thenReturn(Optional.of(occupiedTable));

            assertThatThrownBy(() -> orderService.createOrder(validRequest()))
                    .isInstanceOf(BusinessException.class)
                    .hasMessageContaining("disponível");
        }

        @Test
        @DisplayName("lança BusinessException quando mesa está em MANUTENCAO")
        void throws_when_table_in_maintenance() {
            occupiedTable.setStatus(TableStatus.MANUTENCAO);
            when(restaurantTableRepository.findByTableNumber(5)).thenReturn(Optional.of(occupiedTable));

            assertThatThrownBy(() -> orderService.createOrder(validRequest()))
                    .isInstanceOf(BusinessException.class)
                    .hasMessageContaining("manutenção");
        }

        @Test
        @DisplayName("lança ResourceNotFoundException quando item não encontrado")
        void throws_when_menu_item_not_found() {
            when(restaurantTableRepository.findByTableNumber(5)).thenReturn(Optional.of(occupiedTable));
            when(menuItemRepository.findById(1L)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> orderService.createOrder(validRequest()))
                    .isInstanceOf(ResourceNotFoundException.class);
        }

        @Test
        @DisplayName("lança BusinessException quando item está indisponível")
        void throws_when_menu_item_unavailable() {
            availableItem.setIsAvailable(false);
            when(restaurantTableRepository.findByTableNumber(5)).thenReturn(Optional.of(occupiedTable));
            when(menuItemRepository.findById(1L)).thenReturn(Optional.of(availableItem));

            assertThatThrownBy(() -> orderService.createOrder(validRequest()))
                    .isInstanceOf(BusinessException.class)
                    .hasMessageContaining("não disponível");
        }
    }

    // ────────────────────────── status transitions ────────────────────────────

    @Nested @DisplayName("status transitions")
    class StatusTransitions {

        static Stream<Arguments> validTransitions() {
            return Stream.of(
                    Arguments.of(OrderStatus.NOVO, OrderStatus.EM_ANDAMENTO),
                    Arguments.of(OrderStatus.NOVO, OrderStatus.CANCELADO),
                    Arguments.of(OrderStatus.EM_ANDAMENTO, OrderStatus.PRONTO),
                    Arguments.of(OrderStatus.EM_ANDAMENTO, OrderStatus.CANCELADO),
                    Arguments.of(OrderStatus.PRONTO, OrderStatus.ENTREGUE),
                    Arguments.of(OrderStatus.PRONTO, OrderStatus.CANCELADO)
            );
        }

        static Stream<Arguments> invalidTransitions() {
            return Stream.of(
                    Arguments.of(OrderStatus.NOVO, OrderStatus.PRONTO),
                    Arguments.of(OrderStatus.NOVO, OrderStatus.ENTREGUE),
                    Arguments.of(OrderStatus.EM_ANDAMENTO, OrderStatus.NOVO),
                    Arguments.of(OrderStatus.PRONTO, OrderStatus.NOVO),
                    Arguments.of(OrderStatus.ENTREGUE, OrderStatus.NOVO),
                    Arguments.of(OrderStatus.CANCELADO, OrderStatus.NOVO)
            );
        }

        @ParameterizedTest(name = "{0} → {1}")
        @MethodSource("validTransitions")
        @DisplayName("permite transição válida")
        void allows_valid_transition(OrderStatus from, OrderStatus to) {
            novoOrder.setStatus(from);
            when(orderRepository.findById(1L)).thenReturn(Optional.of(novoOrder));
            when(orderRepository.save(any())).thenReturn(novoOrder);

            Order result = orderService.updateOrderStatus(1L, to);
            assertThat(result.getStatus()).isEqualTo(to);
        }

        @ParameterizedTest(name = "{0} → {1}")
        @MethodSource("invalidTransitions")
        @DisplayName("rejeita transição inválida com BusinessException")
        void rejects_invalid_transition(OrderStatus from, OrderStatus to) {
            novoOrder.setStatus(from);
            when(orderRepository.findById(1L)).thenReturn(Optional.of(novoOrder));

            assertThatThrownBy(() -> orderService.updateOrderStatus(1L, to))
                    .isInstanceOf(BusinessException.class)
                    .hasMessageContaining("Transição inválida");
        }
    }

    // ──────────────────────────── cancelOrder ────────────────────────────────

    @Nested @DisplayName("cancelOrder")
    class CancelOrder {

        @Test
        @DisplayName("cancela pedido NOVO com único lookup no banco")
        void cancels_novo_order() {
            when(orderRepository.findById(1L)).thenReturn(Optional.of(novoOrder));
            when(orderRepository.save(any())).thenReturn(novoOrder);

            orderService.cancelOrder(1L);

            verify(orderRepository, times(1)).findById(1L);
            verify(orderRepository).save(any());
        }

        @Test
        @DisplayName("lança BusinessException ao cancelar pedido ENTREGUE")
        void throws_when_order_already_delivered() {
            novoOrder.setStatus(OrderStatus.ENTREGUE);
            when(orderRepository.findById(1L)).thenReturn(Optional.of(novoOrder));

            assertThatThrownBy(() -> orderService.cancelOrder(1L))
                    .isInstanceOf(BusinessException.class)
                    .hasMessageContaining("já entregue");
        }

        @Test
        @DisplayName("lança BusinessException ao cancelar pedido CANCELADO")
        void throws_when_order_already_cancelled() {
            novoOrder.setStatus(OrderStatus.CANCELADO);
            when(orderRepository.findById(1L)).thenReturn(Optional.of(novoOrder));

            assertThatThrownBy(() -> orderService.cancelOrder(1L))
                    .isInstanceOf(BusinessException.class)
                    .hasMessageContaining("já está cancelado");
        }

        @Test
        @DisplayName("lança ResourceNotFoundException quando pedido não existe")
        void throws_when_order_not_found() {
            when(orderRepository.findById(99L)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> orderService.cancelOrder(99L))
                    .isInstanceOf(ResourceNotFoundException.class);
        }
    }

    // ─────────────────────────── getOrdersByTable ─────────────────────────────

    @Nested @DisplayName("getOrdersByTable")
    class GetOrdersByTable {

        @Test
        @DisplayName("retorna lista vazia quando mesa não encontrada")
        void returns_empty_when_table_not_found() {
            when(restaurantTableRepository.findByTableNumber(99)).thenReturn(Optional.empty());
            List<Order> result = orderService.getOrdersByTable(99);
            assertThat(result).isEmpty();
        }
    }

    // ─────────────────────────────── helpers ─────────────────────────────────

    private OrderRequest validRequest() {
        OrderItemRequest item = new OrderItemRequest(1L, 2, null);
        return new OrderRequest(5, "João", null, List.of(item));
    }
}
