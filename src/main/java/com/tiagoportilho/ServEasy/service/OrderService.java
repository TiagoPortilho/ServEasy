package com.tiagoportilho.ServEasy.service;

import com.tiagoportilho.ServEasy.dto.OrderRequest;
import com.tiagoportilho.ServEasy.model.MenuItem;
import com.tiagoportilho.ServEasy.model.Order;
import com.tiagoportilho.ServEasy.model.OrderItem;
import com.tiagoportilho.ServEasy.model.RestaurantTable;
import com.tiagoportilho.ServEasy.repository.MenuItemRepository;
import com.tiagoportilho.ServEasy.repository.OrderRepository;
import com.tiagoportilho.ServEasy.repository.RestaurantTableRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@SuppressWarnings("null")
public class OrderService {
    
    private final OrderRepository orderRepository;
    private final MenuItemRepository menuItemRepository;
    private final RestaurantTableRepository restaurantTableRepository;

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public List<Order> getOrdersByStatus(Order.OrderStatus status) {
        return orderRepository.findByStatusOrderByCreatedAtAsc(status);
    }

    public List<Order> getNewOrders() {
        return orderRepository.findByStatusOrderByCreatedAtAsc(Order.OrderStatus.NOVO);
    }

    public List<Order> getOrdersInProgress() {
        return orderRepository.findByStatusOrderByCreatedAtAsc(Order.OrderStatus.EM_ANDAMENTO);
    }

    public List<Order> getReadyOrders() {
        return orderRepository.findByStatusOrderByCreatedAtAsc(Order.OrderStatus.PRONTO);
    }

    @SuppressWarnings("null")
    public Optional<Order> getOrderById(Long id) {
        return orderRepository.findById(id);
    }

    public List<Order> getOrdersByTable(Integer tableNumber) {
        Optional<RestaurantTable> tableOpt = restaurantTableRepository.findByTableNumber(tableNumber);
        if (tableOpt.isEmpty()) {
            return new ArrayList<>();
        }
        return orderRepository.findByTable(tableOpt.get());
    }

    public List<Order> getTodaysOrders() {
        LocalDateTime startOfDay = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0);
        LocalDateTime endOfDay = LocalDateTime.now().withHour(23).withMinute(59).withSecond(59);
        return orderRepository.findByCreatedAtBetween(startOfDay, endOfDay);
    }

    @Transactional
    public Order createOrder(OrderRequest orderRequest) {
        // Validar dados de entrada obrigatórios
        if (orderRequest.getTableNumber() == null || orderRequest.getTableNumber() <= 0) {
            throw new IllegalArgumentException("Número da mesa é obrigatório e deve ser maior que zero");
        }
        
        // Verificar se a mesa existe
        Optional<RestaurantTable> tableOpt = restaurantTableRepository.findByTableNumber(orderRequest.getTableNumber());
        if (tableOpt.isEmpty()) {
            throw new IllegalArgumentException("Mesa número " + orderRequest.getTableNumber() + " não encontrada");
        }
        
        RestaurantTable table = tableOpt.get();
        
        // Verificar se a mesa está disponível
        if (table.getStatus() == RestaurantTable.TableStatus.OCUPADA) {
            throw new IllegalArgumentException("Mesa número " + orderRequest.getTableNumber() + " já está ocupada");
        }
        
        if (table.getStatus() == RestaurantTable.TableStatus.MANUTENCAO) {
            throw new IllegalArgumentException("Mesa número " + orderRequest.getTableNumber() + " está em manutenção");
        }
        
        if (orderRequest.getCustomerName() == null || orderRequest.getCustomerName().trim().isEmpty()) {
            throw new IllegalArgumentException("Nome do cliente é obrigatório");
        }
        
        if (orderRequest.getItems() == null || orderRequest.getItems().isEmpty()) {
            throw new IllegalArgumentException("Pelo menos um item deve ser adicionado ao pedido");
        }

        Order order = new Order();
        order.setTable(table);
        order.setCustomerName(orderRequest.getCustomerName().trim());
        order.setNotes(orderRequest.getObservations());
        order.setStatus(Order.OrderStatus.NOVO);

        List<OrderItem> orderItems = new ArrayList<>();
        BigDecimal total = BigDecimal.ZERO;
        int validItemsCount = 0;

        for (OrderRequest.OrderItemRequest itemRequest : orderRequest.getItems()) {
            // 🔧 BUG FIX: Validar quantidade positiva
            if (itemRequest.getQuantity() == null || itemRequest.getQuantity() <= 0) {
                throw new IllegalArgumentException(
                    "Quantidade deve ser maior que zero para o item ID: " + itemRequest.getMenuItemId());
            }
            
            @SuppressWarnings("null")
            Optional<MenuItem> menuItemOpt = menuItemRepository.findById(itemRequest.getMenuItemId());
            if (menuItemOpt.isPresent()) {
                MenuItem menuItem = menuItemOpt.get();
                
                // 🔧 BUG FIX: Verificar se o item está disponível
                if (menuItem.getIsAvailable() == null || !menuItem.getIsAvailable()) {
                    throw new IllegalArgumentException(
                        "Item não disponível: " + menuItem.getName());
                }
                
                OrderItem orderItem = new OrderItem();
                orderItem.setOrder(order);
                orderItem.setMenuItem(menuItem);
                orderItem.setQuantity(itemRequest.getQuantity());
                orderItem.setUnitPrice(menuItem.getPrice());
                orderItem.setNotes(itemRequest.getNotes());
                
                BigDecimal subtotal = menuItem.getPrice().multiply(BigDecimal.valueOf(itemRequest.getQuantity()));
                orderItem.setSubtotal(subtotal);
                total = total.add(subtotal);
                
                orderItems.add(orderItem);
                validItemsCount++;
            } else {
                throw new IllegalArgumentException(
                    "Item não encontrado no cardápio com ID: " + itemRequest.getMenuItemId());
            }
        }

        // 🔧 BUG FIX: Garantir que pelo menos um item válido foi adicionado
        if (validItemsCount == 0) {
            throw new IllegalArgumentException("Nenhum item válido foi encontrado para criar o pedido");
        }

        order.setItems(orderItems);
        order.setTotal(total);

        return orderRepository.save(order);
    }

    @SuppressWarnings("null")
    public Order updateOrderStatus(Long orderId, Order.OrderStatus newStatus) {
        Optional<Order> orderOpt = orderRepository.findById(orderId);
        if (orderOpt.isPresent()) {
            Order order = orderOpt.get();
            
            // 🔧 BUG FIX: Validar transições de status permitidas
            if (!isValidStatusTransition(order.getStatus(), newStatus)) {
                throw new IllegalStateException(
                    String.format("Transição inválida de %s para %s", 
                        order.getStatus(), newStatus));
            }
            
            order.setStatus(newStatus);
            return orderRepository.save(order);
        }
        throw new RuntimeException("Pedido não encontrado");
    }

    @SuppressWarnings("null")
    public void cancelOrder(Long orderId) {
        // 🔧 BUG FIX: Verificar se pode cancelar antes de tentar
        Optional<Order> orderOpt = orderRepository.findById(orderId);
        if (orderOpt.isPresent()) {
            Order order = orderOpt.get();
            if (order.getStatus() == Order.OrderStatus.ENTREGUE) {
                throw new IllegalStateException("Não é possível cancelar um pedido já entregue");
            }
            if (order.getStatus() == Order.OrderStatus.CANCELADO) {
                throw new IllegalStateException("Pedido já está cancelado");
            }
        }
        updateOrderStatus(orderId, Order.OrderStatus.CANCELADO);
    }

    public Order markAsInProgress(Long orderId) {
        return updateOrderStatus(orderId, Order.OrderStatus.EM_ANDAMENTO);
    }

    public Order markAsReady(Long orderId) {
        return updateOrderStatus(orderId, Order.OrderStatus.PRONTO);
    }

    public Order markAsDelivered(Long orderId) {
        return updateOrderStatus(orderId, Order.OrderStatus.ENTREGUE);
    }
    
    // 🔧 BUG FIX: Método para validar transições de status
    private boolean isValidStatusTransition(Order.OrderStatus currentStatus, Order.OrderStatus newStatus) {
        // Se é o mesmo status, permite
        if (currentStatus == newStatus) {
            return true;
        }
        
        return switch (currentStatus) {
            case NOVO -> newStatus == Order.OrderStatus.EM_ANDAMENTO || 
                        newStatus == Order.OrderStatus.CANCELADO;
            
            case EM_ANDAMENTO -> newStatus == Order.OrderStatus.PRONTO || 
                               newStatus == Order.OrderStatus.CANCELADO;
            
            case PRONTO -> newStatus == Order.OrderStatus.ENTREGUE || 
                          newStatus == Order.OrderStatus.CANCELADO;
            
            case ENTREGUE -> false; // Pedidos entregues não podem mudar de status
            
            case CANCELADO -> false; // Pedidos cancelados não podem mudar de status
            
            default -> false;
        };
    }
}