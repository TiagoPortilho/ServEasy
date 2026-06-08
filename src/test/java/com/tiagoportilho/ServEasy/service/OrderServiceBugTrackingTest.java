package com.tiagoportilho.ServEasy.service;

import com.tiagoportilho.ServEasy.dto.request.OrderItemRequest;
import com.tiagoportilho.ServEasy.dto.request.OrderRequest;
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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("OrderService — edge cases e integridade de dados")
class OrderServiceBugTrackingTest {

    @Mock private OrderRepository orderRepository;
    @Mock private MenuItemRepository menuItemRepository;
    @Mock private RestaurantTableRepository restaurantTableRepository;
    @InjectMocks private OrderService orderService;

    private MenuItem menuItem;
    private RestaurantTable occupiedTable;

    @BeforeEach
    void setUp() {
        menuItem = new MenuItem();
        menuItem.setId(1L);
        menuItem.setName("Pizza Margherita");
        menuItem.setPrice(new BigDecimal("25.00"));
        menuItem.setIsAvailable(true);

        occupiedTable = new RestaurantTable();
        occupiedTable.setId(1L);
        occupiedTable.setTableNumber(5);
        occupiedTable.setStatus(TableStatus.OCUPADA);
    }

    @Test
    @DisplayName("lança ResourceNotFoundException para item inexistente no cardápio")
    void throws_for_nonexistent_menu_item() {
        when(restaurantTableRepository.findByTableNumber(5)).thenReturn(Optional.of(occupiedTable));
        when(menuItemRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> orderService.createOrder(validRequest()))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    @DisplayName("lança ResourceNotFoundException para mesa não cadastrada")
    void throws_for_nonexistent_table() {
        when(restaurantTableRepository.findByTableNumber(5)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> orderService.createOrder(validRequest()))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    @DisplayName("lança ResourceNotFoundException para pedido inexistente ao atualizar status")
    void throws_for_nonexistent_order_on_status_update() {
        when(orderRepository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> orderService.updateOrderStatus(999L, OrderStatus.EM_ANDAMENTO))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    @DisplayName("pedido criado tem total correto baseado no preço do cardápio")
    void created_order_total_matches_menu_price() {
        when(restaurantTableRepository.findByTableNumber(5)).thenReturn(Optional.of(occupiedTable));
        when(menuItemRepository.findById(1L)).thenReturn(Optional.of(menuItem));
        when(orderRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        Order result = orderService.createOrder(validRequest());

        assertThat(result.getTotal()).isEqualByComparingTo("50.00"); // 25.00 × 2
        assertThat(result.getStatus()).isEqualTo(OrderStatus.NOVO);
        assertThat(result.getTable()).isEqualTo(occupiedTable);
    }

    @Test
    @DisplayName("sequência completa NOVO→EM_ANDAMENTO→PRONTO→ENTREGUE é válida")
    void full_lifecycle_sequence_is_valid() {
        Order order = new Order();
        order.setId(1L);
        order.setStatus(OrderStatus.NOVO);
        order.setTotal(BigDecimal.TEN);

        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(orderRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        orderService.markAsInProgress(1L);
        assertThat(order.getStatus()).isEqualTo(OrderStatus.EM_ANDAMENTO);

        orderService.markAsReady(1L);
        assertThat(order.getStatus()).isEqualTo(OrderStatus.PRONTO);

        orderService.markAsDelivered(1L);
        assertThat(order.getStatus()).isEqualTo(OrderStatus.ENTREGUE);
    }

    @Test
    @DisplayName("não permite cancelar pedido ENTREGUE")
    void cannot_cancel_delivered_order() {
        Order order = new Order();
        order.setId(1L);
        order.setStatus(OrderStatus.ENTREGUE);

        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        assertThatThrownBy(() -> orderService.cancelOrder(1L))
                .isInstanceOf(BusinessException.class);
    }

    @Test
    @DisplayName("getAllOrders retorna corretamente lista de 1000 pedidos")
    void returns_large_list_correctly() {
        List<Order> many = java.util.stream.IntStream.range(0, 1000)
                .mapToObj(i -> {
                    Order o = new Order();
                    o.setId((long) i);
                    o.setStatus(OrderStatus.NOVO);
                    return o;
                }).toList();

        when(orderRepository.findAll()).thenReturn(many);

        assertThat(orderService.getAllOrders()).hasSize(1000);
    }

    private OrderRequest validRequest() {
        return new OrderRequest(5, "João", null, List.of(new OrderItemRequest(1L, 2, null)));
    }
}
