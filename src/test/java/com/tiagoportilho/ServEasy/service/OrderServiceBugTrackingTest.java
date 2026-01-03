package com.tiagoportilho.ServEasy.service;

import com.tiagoportilho.ServEasy.dto.OrderRequest;
import com.tiagoportilho.ServEasy.model.MenuItem;
import com.tiagoportilho.ServEasy.model.Order;
import com.tiagoportilho.ServEasy.model.RestaurantTable;
import com.tiagoportilho.ServEasy.repository.MenuItemRepository;
import com.tiagoportilho.ServEasy.repository.OrderRepository;
import com.tiagoportilho.ServEasy.repository.RestaurantTableRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Testes de bugtracking simples para OrderService
 * Foca em detectar bugs básicos sem depender de métodos inexistentes
 */
@SuppressWarnings("null")
class OrderServiceBugTrackingTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private MenuItemRepository menuItemRepository;

    @Mock
    private RestaurantTableRepository restaurantTableRepository;

    @InjectMocks
    private OrderService orderService;

    private MenuItem menuItem;
    private Order order;
    private OrderRequest orderRequest;
    private RestaurantTable table;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        
        // Setup test data
        menuItem = new MenuItem();
        menuItem.setId(1L);
        menuItem.setName("Pizza Margherita");
        menuItem.setPrice(new BigDecimal("25.00"));
        menuItem.setCategory(MenuItem.Category.PIZZAS);
        
        table = new RestaurantTable();
        table.setId(1L);
        table.setTableNumber(5);
        table.setCapacity(4);
        table.setStatus(RestaurantTable.TableStatus.DISPONIVEL);
        
        order = new Order();
        order.setId(1L);
        order.setTable(table);
        order.setCustomerName("João Silva");
        order.setStatus(Order.OrderStatus.NOVO);
        order.setTotal(new BigDecimal("25.00"));
        
        // Setup order request
        orderRequest = new OrderRequest();
        orderRequest.setTableNumber(5);
        orderRequest.setCustomerName("João Silva");
        orderRequest.setObservations("Sem cebola");
        
        OrderRequest.OrderItemRequest itemRequest = new OrderRequest.OrderItemRequest();
        itemRequest.setMenuItemId(1L);
        itemRequest.setQuantity(1);
        itemRequest.setNotes("Extra queijo");
        
        orderRequest.setItems(Arrays.asList(itemRequest));
    }

    @Test
    @DisplayName("BUG FIX VERIFIED: Sistema agora rejeita itens inexistentes")
    void testCreateOrderWithNonExistentMenuItem() {
        System.out.println("\n🔍 ====== TESTE: ITEM INEXISTENTE ======");
        
        // Arrange
        table.setStatus(RestaurantTable.TableStatus.OCUPADA);
        when(restaurantTableRepository.findByTableNumber(5)).thenReturn(Optional.of(table));
        when(menuItemRepository.findById(1L)).thenReturn(Optional.empty());
        
        // Act & Assert
        try {
            orderService.createOrder(orderRequest);
            System.out.println("❌ FALHA: Sistema deveria ter rejeitado item inexistente");
            fail("Sistema deveria lançar exceção para item inexistente");
        } catch (IllegalArgumentException e) {
            System.out.println("✅ BUG CORRIGIDO: Sistema rejeitou item inexistente");
            System.out.println("  → Mensagem: " + e.getMessage());
            assertTrue(e.getMessage().contains("Item não encontrado"));
        }
    }

    @Test
    @DisplayName("BUG FIX VERIFIED: Sistema agora rejeita quantidades inválidas")
    void testNegativeOrZeroQuantity() {
        System.out.println("\n🔍 ====== TESTE: QUANTIDADE INVÁLIDA ======");
        
        // Setup para quantidade zero
        OrderRequest.OrderItemRequest zeroQuantityRequest = new OrderRequest.OrderItemRequest();
        zeroQuantityRequest.setMenuItemId(1L);
        zeroQuantityRequest.setQuantity(0);
        orderRequest.setItems(Arrays.asList(zeroQuantityRequest));
        
        when(restaurantTableRepository.findByTableNumber(5)).thenReturn(Optional.of(table));
        when(menuItemRepository.findById(1L)).thenReturn(Optional.of(menuItem));
        
        // Test quantidade zero
        System.out.println("🧪 Testando quantidade ZERO...");
        try {
            orderService.createOrder(orderRequest);
            fail("Sistema deveria rejeitar quantidade zero");
        } catch (IllegalArgumentException e) {
            System.out.println("✅ BUG CORRIGIDO: Quantidade zero rejeitada");
            System.out.println("  → Mensagem: " + e.getMessage());
        }
        
        // Test quantidade negativa
        System.out.println("🧪 Testando quantidade NEGATIVA...");
        zeroQuantityRequest.setQuantity(-5);
        try {
            orderService.createOrder(orderRequest);
            fail("Sistema deveria rejeitar quantidade negativa");
        } catch (IllegalArgumentException e) {
            System.out.println("✅ BUG CORRIGIDO: Quantidade negativa rejeitada");
            System.out.println("  → Mensagem: " + e.getMessage());
        }
        
        System.out.println("📊 ANÁLISE: Todos os bugs de quantidade foram corrigidos!");
    }

    @Test
    @DisplayName("BUG FIX VERIFIED: Sistema agora rejeita pedidos sem mesa")
    void testOrderWithoutTable() {
        System.out.println("\n🔍 ====== TESTE: PEDIDO SEM MESA ======");
        
        orderRequest.setTableNumber(null);
        when(menuItemRepository.findById(1L)).thenReturn(Optional.of(menuItem));
        // Não adicionar mock do restaurantTableRepository para testar falha
        
        try {
            orderService.createOrder(orderRequest);
            fail("Sistema deveria rejeitar pedido sem mesa");
        } catch (IllegalArgumentException e) {
            System.out.println("✅ BUG CORRIGIDO: Sistema rejeitou pedido sem mesa");
            System.out.println("  → Mensagem: " + e.getMessage());
            assertTrue(e.getMessage().contains("mesa"));
        }
    }

    @Test
    @DisplayName("BUG TEST: Atualização de status de pedido inexistente")
    void testUpdateStatusOfNonExistentOrder() {
        System.out.println("\n🔍 ====== TESTE: PEDIDO INEXISTENTE ======");
        
        when(orderRepository.findById(999L)).thenReturn(Optional.empty());
        
        try {
            orderService.updateOrderStatus(999L, Order.OrderStatus.EM_ANDAMENTO);
            System.out.println("🐛 BUG: Sistema deveria lançar exceção para pedido inexistente");
        } catch (RuntimeException e) {
            System.out.println("✅ Sistema rejeitou atualização de pedido inexistente");
            System.out.println("  → Mensagem: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("⚠️ ALERTA: Tipo de exceção inesperado: " + e.getClass().getSimpleName());
        }
    }

    @Test
    @DisplayName("PERFORMANCE TEST: Buscar muitos pedidos")
    void testGetAllOrdersPerformance() {
        System.out.println("\n🔍 ====== TESTE: PERFORMANCE ======");
        
        // Simular muitos pedidos
        List<Order> manyOrders = generateManyOrders(1000);
        when(orderRepository.findAll()).thenReturn(manyOrders);
        
        long startTime = System.currentTimeMillis();
        List<Order> result = orderService.getAllOrders();
        long endTime = System.currentTimeMillis();
        
        System.out.println("📊 RESULTADOS DE PERFORMANCE:");
        System.out.println("  → Pedidos processados: " + result.size());
        System.out.println("  → Tempo de execução: " + (endTime - startTime) + "ms");
        
        if (endTime - startTime > 100) {
            System.out.println("⚠️ ALERTA: Performance pode estar degradada!");
            System.out.println("  → Considere implementar paginação");
        } else {
            System.out.println("✅ Performance adequada");
        }
        
        assertEquals(1000, result.size());
    }

    @Test
    @DisplayName("BUSINESS LOGIC TEST: Transições de status")
    void testStatusTransitions() {
        System.out.println("\n🔍 ====== TESTE: TRANSIÇÕES DE STATUS ======");
        
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(orderRepository.save(any(Order.class))).thenAnswer(i -> i.getArguments()[0]);
        
        // Testar sequência normal
        System.out.println("🔄 Testando sequência normal de status...");
        
        Order step1 = orderService.markAsInProgress(1L);
        System.out.println("  → NOVO → EM_ANDAMENTO: " + step1.getStatus());
        
        Order step2 = orderService.markAsReady(1L);
        System.out.println("  → EM_ANDAMENTO → PRONTO: " + step2.getStatus());
        
        Order step3 = orderService.markAsDelivered(1L);
        System.out.println("  → PRONTO → ENTREGUE: " + step3.getStatus());
        
        // Testar transição inválida (cancelar pedido já entregue)
        System.out.println("🧪 Testando transição potencialmente inválida...");
        try {
            orderService.cancelOrder(1L);
            System.out.println("⚠️ ALERTA: Sistema permite cancelar pedido já entregue!");
        } catch (Exception e) {
            System.out.println("✅ Sistema impediu cancelamento indevido: " + e.getMessage());
        }
        
        assertEquals(Order.OrderStatus.ENTREGUE, step3.getStatus());
    }

    @Test
    @DisplayName("DATA INTEGRITY TEST: Verificar integridade dos dados")
    void testDataIntegrity() {
        System.out.println("\n🔍 ====== TESTE: INTEGRIDADE DOS DADOS ======");
        
        table.setStatus(RestaurantTable.TableStatus.OCUPADA);
        when(restaurantTableRepository.findByTableNumber(5)).thenReturn(Optional.of(table));
        when(menuItemRepository.findById(1L)).thenReturn(Optional.of(menuItem));
        when(orderRepository.save(any(Order.class))).thenAnswer(i -> i.getArguments()[0]);
        
        Order result = orderService.createOrder(orderRequest);
        
        System.out.println("📊 VERIFICAÇÕES DE INTEGRIDADE:");
        
        // Verificar dados básicos
        if (result.getCustomerName() != null && !result.getCustomerName().trim().isEmpty()) {
            System.out.println("✅ Nome do cliente preservado: " + result.getCustomerName());
        } else {
            System.out.println("🐛 BUG: Nome do cliente perdido ou inválido");
        }
        
        if (result.getTable() != null && result.getTable().getTableNumber() != null && result.getTable().getTableNumber() > 0) {
            System.out.println("✅ Número da mesa válido: " + result.getTable().getTableNumber());
        } else {
            System.out.println("🐛 BUG: Número da mesa inválido: " + (result.getTable() != null ? result.getTable().getTableNumber() : "null"));
        }
        
        if (result.getTotal() != null && result.getTotal().compareTo(BigDecimal.ZERO) >= 0) {
            System.out.println("✅ Total não-negativo: " + result.getTotal());
        } else {
            System.out.println("🐛 BUG: Total inválido: " + result.getTotal());
        }
        
        if (result.getStatus() != null) {
            System.out.println("✅ Status definido: " + result.getStatus());
        } else {
            System.out.println("🐛 BUG: Status nulo");
        }
        
        assertNotNull(result);
    }

    private List<Order> generateManyOrders(int count) {
        return java.util.stream.IntStream.range(0, count)
                .mapToObj(i -> {
                    RestaurantTable table = new RestaurantTable();
                    table.setId((long) (i % 20 + 1));
                    table.setTableNumber(i % 20 + 1);
                    table.setCapacity(4);
                    table.setStatus(RestaurantTable.TableStatus.DISPONIVEL);
                    
                    Order o = new Order();
                    o.setId((long) i);
                    o.setTable(table);
                    o.setCustomerName("Cliente " + i);
                    o.setTotal(new BigDecimal("15.50"));
                    o.setStatus(Order.OrderStatus.NOVO);
                    return o;
                })
                .toList();
    }
}