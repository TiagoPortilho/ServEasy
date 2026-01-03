package com.tiagoportilho.ServEasy.service;

import com.tiagoportilho.ServEasy.dto.OrderRequest;
import com.tiagoportilho.ServEasy.model.MenuItem;
import com.tiagoportilho.ServEasy.model.Order;
import com.tiagoportilho.ServEasy.model.RestaurantTable;
import com.tiagoportilho.ServEasy.repository.MenuItemRepository;
import com.tiagoportilho.ServEasy.repository.OrderRepository;
import com.tiagoportilho.ServEasy.repository.RestaurantTableRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Testes unitários para OrderService.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("OrderService Tests")
@SuppressWarnings("null")
class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private MenuItemRepository menuItemRepository;

    @Mock
    private RestaurantTableRepository restaurantTableRepository;

    @InjectMocks
    private OrderService orderService;

    private Order sampleOrder;
    private MenuItem sampleMenuItem;
    private RestaurantTable sampleTable;

    @BeforeEach
    void setUp() {
        sampleMenuItem = new MenuItem();
        sampleMenuItem.setId(1L);
        sampleMenuItem.setName("Pizza Margherita");
        sampleMenuItem.setPrice(new BigDecimal("35.00"));
        sampleMenuItem.setIsAvailable(true);

        sampleTable = new RestaurantTable();
        sampleTable.setId(1L);
        sampleTable.setTableNumber(5);
        sampleTable.setCapacity(4);
        sampleTable.setStatus(RestaurantTable.TableStatus.DISPONIVEL);

        sampleOrder = new Order();
        sampleOrder.setId(1L);
        sampleOrder.setTable(sampleTable);
        sampleOrder.setCustomerName("João");
        sampleOrder.setStatus(Order.OrderStatus.NOVO);
        sampleOrder.setTotal(new BigDecimal("70.00"));
        sampleOrder.setCreatedAt(LocalDateTime.now());
    }

    @Nested
    @DisplayName("getAllOrders")
    class GetAllOrdersTests {

        @Test
        @DisplayName("deve retornar lista vazia quando não há pedidos")
        void shouldReturnEmptyListWhenNoOrders() {
            when(orderRepository.findAll()).thenReturn(new ArrayList<>());

            List<Order> result = orderService.getAllOrders();

            assertThat(result).isEmpty();
            verify(orderRepository, times(1)).findAll();
        }

        @Test
        @DisplayName("deve retornar todos os pedidos")
        void shouldReturnAllOrders() {
            List<Order> orders = Arrays.asList(sampleOrder, new Order());
            when(orderRepository.findAll()).thenReturn(orders);

            List<Order> result = orderService.getAllOrders();

            assertThat(result).hasSize(2);
            verify(orderRepository, times(1)).findAll();
        }
    }

    @Nested
    @DisplayName("getOrdersByStatus")
    class GetOrdersByStatusTests {

        @Test
        @DisplayName("deve retornar pedidos com status específico")
        void shouldReturnOrdersWithSpecificStatus() {
            when(orderRepository.findByStatusOrderByCreatedAtAsc(Order.OrderStatus.NOVO))
                    .thenReturn(Arrays.asList(sampleOrder));

            List<Order> result = orderService.getOrdersByStatus(Order.OrderStatus.NOVO);

            assertThat(result).hasSize(1);
            assertThat(result.get(0).getStatus()).isEqualTo(Order.OrderStatus.NOVO);
        }
    }

    @Nested
    @DisplayName("getOrderById")
    class GetOrderByIdTests {

        @Test
        @DisplayName("deve retornar pedido quando existe")
        void shouldReturnOrderWhenExists() {
            when(orderRepository.findById(1L)).thenReturn(Optional.of(sampleOrder));

            Optional<Order> result = orderService.getOrderById(1L);

            assertThat(result).isPresent();
            assertThat(result.get().getId()).isEqualTo(1L);
        }

        @Test
        @DisplayName("deve retornar vazio quando pedido não existe")
        void shouldReturnEmptyWhenOrderNotExists() {
            when(orderRepository.findById(999L)).thenReturn(Optional.empty());

            Optional<Order> result = orderService.getOrderById(999L);

            assertThat(result).isEmpty();
        }
    }

    @Nested
    @DisplayName("createOrder")
    class CreateOrderTests {

        @Test
        @DisplayName("deve criar pedido com dados válidos")
        void shouldCreateOrderWithValidData() {
            OrderRequest request = createValidOrderRequest();
            
            when(restaurantTableRepository.findByTableNumber(5)).thenReturn(Optional.of(sampleTable));
            when(menuItemRepository.findById(1L)).thenReturn(Optional.of(sampleMenuItem));
            when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> {
                Order order = invocation.getArgument(0);
                order.setId(1L);
                return order;
            });

            Order result = orderService.createOrder(request);

            assertThat(result).isNotNull();
            assertThat(result.getTable()).isEqualTo(sampleTable);
            assertThat(result.getCustomerName()).isEqualTo("João");
            assertThat(result.getStatus()).isEqualTo(Order.OrderStatus.NOVO);
            verify(orderRepository, times(1)).save(any(Order.class));
        }

        @Test
        @DisplayName("deve lançar exceção quando número da mesa é nulo")
        void shouldThrowExceptionWhenTableNumberIsNull() {
            OrderRequest request = createValidOrderRequest();
            request.setTableNumber(null);

            assertThatThrownBy(() -> orderService.createOrder(request))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("Número da mesa");
        }

        @Test
        @DisplayName("deve lançar exceção quando mesa não é encontrada")
        void shouldThrowExceptionWhenTableNotFound() {
            OrderRequest request = createValidOrderRequest();
            
            when(restaurantTableRepository.findByTableNumber(5)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> orderService.createOrder(request))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("Mesa número 5 não encontrada");
        }

        @Test
        @DisplayName("deve lançar exceção quando nome do cliente é vazio")
        void shouldThrowExceptionWhenCustomerNameIsEmpty() {
            OrderRequest request = createValidOrderRequest();
            request.setCustomerName("");
            
            when(restaurantTableRepository.findByTableNumber(5)).thenReturn(Optional.of(sampleTable));

            assertThatThrownBy(() -> orderService.createOrder(request))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("Nome do cliente");
        }

        @Test
        @DisplayName("deve lançar exceção quando lista de itens é vazia")
        void shouldThrowExceptionWhenItemsIsEmpty() {
            OrderRequest request = createValidOrderRequest();
            request.setItems(new ArrayList<>());
            
            when(restaurantTableRepository.findByTableNumber(5)).thenReturn(Optional.of(sampleTable));

            assertThatThrownBy(() -> orderService.createOrder(request))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("item");
        }

        @Test
        @DisplayName("deve lançar exceção quando item do menu não existe")
        void shouldThrowExceptionWhenMenuItemNotFound() {
            OrderRequest request = createValidOrderRequest();
            when(restaurantTableRepository.findByTableNumber(5)).thenReturn(Optional.of(sampleTable));
            when(menuItemRepository.findById(1L)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> orderService.createOrder(request))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("Item não encontrado");
        }

        @Test
        @DisplayName("deve lançar exceção quando item não está disponível")
        void shouldThrowExceptionWhenMenuItemNotAvailable() {
            OrderRequest request = createValidOrderRequest();
            sampleMenuItem.setIsAvailable(false);
            when(restaurantTableRepository.findByTableNumber(5)).thenReturn(Optional.of(sampleTable));
            when(menuItemRepository.findById(1L)).thenReturn(Optional.of(sampleMenuItem));

            assertThatThrownBy(() -> orderService.createOrder(request))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("não disponível");
        }
    }

    @Nested
    @DisplayName("updateOrderStatus")
    class UpdateOrderStatusTests {

        @Test
        @DisplayName("deve atualizar status NOVO para EM_ANDAMENTO")
        void shouldUpdateStatusFromNovoToEmAndamento() {
            when(orderRepository.findById(1L)).thenReturn(Optional.of(sampleOrder));
            when(orderRepository.save(any(Order.class))).thenReturn(sampleOrder);

            Order result = orderService.updateOrderStatus(1L, Order.OrderStatus.EM_ANDAMENTO);

            assertThat(result.getStatus()).isEqualTo(Order.OrderStatus.EM_ANDAMENTO);
            verify(orderRepository, times(1)).save(any(Order.class));
        }

        @Test
        @DisplayName("deve lançar exceção quando transição de status é inválida")
        void shouldThrowExceptionWhenStatusTransitionIsInvalid() {
            sampleOrder.setStatus(Order.OrderStatus.ENTREGUE);
            when(orderRepository.findById(1L)).thenReturn(Optional.of(sampleOrder));

            assertThatThrownBy(() -> orderService.updateOrderStatus(1L, Order.OrderStatus.NOVO))
                    .isInstanceOf(IllegalStateException.class)
                    .hasMessageContaining("Transição inválida");
        }

        @Test
        @DisplayName("deve lançar exceção quando pedido não existe")
        void shouldThrowExceptionWhenOrderNotFound() {
            when(orderRepository.findById(999L)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> orderService.updateOrderStatus(999L, Order.OrderStatus.EM_ANDAMENTO))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessageContaining("não encontrado");
        }
    }

    @Nested
    @DisplayName("cancelOrder")
    class CancelOrderTests {

        @Test
        @DisplayName("deve cancelar pedido NOVO")
        void shouldCancelNewOrder() {
            when(orderRepository.findById(1L)).thenReturn(Optional.of(sampleOrder));
            when(orderRepository.save(any(Order.class))).thenReturn(sampleOrder);

            orderService.cancelOrder(1L);

            verify(orderRepository, times(2)).findById(1L);
            verify(orderRepository, times(1)).save(any(Order.class));
        }

        @Test
        @DisplayName("deve lançar exceção quando pedido já foi entregue")
        void shouldThrowExceptionWhenOrderAlreadyDelivered() {
            sampleOrder.setStatus(Order.OrderStatus.ENTREGUE);
            when(orderRepository.findById(1L)).thenReturn(Optional.of(sampleOrder));

            assertThatThrownBy(() -> orderService.cancelOrder(1L))
                    .isInstanceOf(IllegalStateException.class)
                    .hasMessageContaining("já entregue");
        }

        @Test
        @DisplayName("deve lançar exceção quando pedido já está cancelado")
        void shouldThrowExceptionWhenOrderAlreadyCancelled() {
            sampleOrder.setStatus(Order.OrderStatus.CANCELADO);
            when(orderRepository.findById(1L)).thenReturn(Optional.of(sampleOrder));

            assertThatThrownBy(() -> orderService.cancelOrder(1L))
                    .isInstanceOf(IllegalStateException.class)
                    .hasMessageContaining("já está cancelado");
        }
    }

    // Helper methods
    private OrderRequest createValidOrderRequest() {
        OrderRequest request = new OrderRequest();
        request.setTableNumber(5);
        request.setCustomerName("João");
        
        OrderRequest.OrderItemRequest itemRequest = new OrderRequest.OrderItemRequest();
        itemRequest.setMenuItemId(1L);
        itemRequest.setQuantity(2);
        
        request.setItems(Arrays.asList(itemRequest));
        return request;
    }
}
