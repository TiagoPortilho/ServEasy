package com.tiagoportilho.ServEasy.service;

import com.tiagoportilho.ServEasy.dto.OrderRequest;
import com.tiagoportilho.ServEasy.model.MenuItem;
import com.tiagoportilho.ServEasy.model.Order;
import com.tiagoportilho.ServEasy.model.OrderItem;
import com.tiagoportilho.ServEasy.repository.MenuItemRepository;
import com.tiagoportilho.ServEasy.repository.OrderRepository;
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
public class OrderService {
    
    private final OrderRepository orderRepository;
    private final MenuItemRepository menuItemRepository;

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

    public Optional<Order> getOrderById(Long id) {
        return orderRepository.findById(id);
    }

    public List<Order> getOrdersByTable(Integer tableNumber) {
        return orderRepository.findByTableNumber(tableNumber);
    }

    public List<Order> getTodaysOrders() {
        LocalDateTime startOfDay = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0);
        LocalDateTime endOfDay = LocalDateTime.now().withHour(23).withMinute(59).withSecond(59);
        return orderRepository.findByCreatedAtBetween(startOfDay, endOfDay);
    }

    @Transactional
    public Order createOrder(OrderRequest orderRequest) {
        Order order = new Order();
        order.setTableNumber(orderRequest.getTableNumber());
        order.setCustomerName(orderRequest.getCustomerName());
        order.setNotes(orderRequest.getNotes());
        order.setStatus(Order.OrderStatus.NOVO);

        List<OrderItem> orderItems = new ArrayList<>();
        BigDecimal total = BigDecimal.ZERO;

        for (OrderRequest.OrderItemRequest itemRequest : orderRequest.getItems()) {
            Optional<MenuItem> menuItemOpt = menuItemRepository.findById(itemRequest.getMenuItemId());
            if (menuItemOpt.isPresent()) {
                MenuItem menuItem = menuItemOpt.get();
                
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
            }
        }

        order.setItems(orderItems);
        order.setTotal(total);

        return orderRepository.save(order);
    }

    public Order updateOrderStatus(Long orderId, Order.OrderStatus newStatus) {
        Optional<Order> orderOpt = orderRepository.findById(orderId);
        if (orderOpt.isPresent()) {
            Order order = orderOpt.get();
            order.setStatus(newStatus);
            return orderRepository.save(order);
        }
        throw new RuntimeException("Pedido não encontrado");
    }

    public void cancelOrder(Long orderId) {
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
}