package com.tiagoportilho.ServEasy.service;

import com.tiagoportilho.ServEasy.dto.OrderRequest;
import com.tiagoportilho.ServEasy.dto.request.OrderItemRequest;
import com.tiagoportilho.ServEasy.exception.BusinessException;
import com.tiagoportilho.ServEasy.exception.ResourceNotFoundException;
import com.tiagoportilho.ServEasy.model.MenuItem;
import com.tiagoportilho.ServEasy.model.Order;
import com.tiagoportilho.ServEasy.model.Order.OrderStatus;
import com.tiagoportilho.ServEasy.model.OrderItem;
import com.tiagoportilho.ServEasy.model.RestaurantTable;
import com.tiagoportilho.ServEasy.repository.MenuItemRepository;
import com.tiagoportilho.ServEasy.repository.OrderRepository;
import com.tiagoportilho.ServEasy.repository.RestaurantTableRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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
    private final RestaurantTableRepository restaurantTableRepository;

    @Transactional(readOnly = true)
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<Order> getOrdersByStatus(OrderStatus status) {
        return orderRepository.findByStatusOrderByCreatedAtAsc(status);
    }

    @Transactional(readOnly = true)
    public List<Order> getNewOrders() {
        return orderRepository.findByStatusOrderByCreatedAtAsc(OrderStatus.NOVO);
    }

    @Transactional(readOnly = true)
    public List<Order> getOrdersInProgress() {
        return orderRepository.findByStatusOrderByCreatedAtAsc(OrderStatus.EM_ANDAMENTO);
    }

    @Transactional(readOnly = true)
    public List<Order> getReadyOrders() {
        return orderRepository.findByStatusOrderByCreatedAtAsc(OrderStatus.PRONTO);
    }

    @Transactional(readOnly = true)
    public Optional<Order> getOrderById(Long id) {
        return orderRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public List<Order> getOrdersByTable(Integer tableNumber) {
        return restaurantTableRepository.findByTableNumber(tableNumber)
                .map(orderRepository::findByTable)
                .orElse(new ArrayList<>());
    }

    @Transactional(readOnly = true)
    public List<Order> getTodaysOrders() {
        LocalDateTime startOfDay = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0);
        LocalDateTime endOfDay = LocalDateTime.now().withHour(23).withMinute(59).withSecond(59);
        return orderRepository.findByCreatedAtBetween(startOfDay, endOfDay);
    }

    @Transactional
    public Order createOrder(OrderRequest orderRequest) {
        RestaurantTable table = restaurantTableRepository.findByTableNumber(orderRequest.getTableNumber())
                .orElseThrow(() -> new ResourceNotFoundException("Mesa", "número " + orderRequest.getTableNumber()));

        if (table.getStatus() == RestaurantTable.TableStatus.DISPONIVEL) {
            throw new BusinessException("Mesa número " + orderRequest.getTableNumber() + " está disponível — não há clientes para atender",
                    HttpStatus.CONFLICT, "TABLE_NOT_OCCUPIED");
        }
        if (table.getStatus() == RestaurantTable.TableStatus.MANUTENCAO) {
            throw new BusinessException("Mesa número " + orderRequest.getTableNumber() + " está em manutenção",
                    HttpStatus.CONFLICT, "TABLE_IN_MAINTENANCE");
        }

        Order order = new Order();
        order.setTable(table);
        order.setCustomerName(orderRequest.getCustomerName() != null ? orderRequest.getCustomerName().trim() : null);
        order.setNotes(orderRequest.getObservations());
        order.setStatus(OrderStatus.NOVO);

        List<OrderItem> orderItems = new ArrayList<>();
        BigDecimal total = BigDecimal.ZERO;

        for (OrderItemRequest itemRequest : orderRequest.getItems()) {
            MenuItem menuItem = menuItemRepository.findById(itemRequest.getMenuItemId())
                    .orElseThrow(() -> new ResourceNotFoundException("Item do cardápio", itemRequest.getMenuItemId()));

            if (Boolean.FALSE.equals(menuItem.getIsAvailable())) {
                throw new BusinessException("Item não disponível: " + menuItem.getName(),
                        HttpStatus.UNPROCESSABLE_ENTITY, "MENU_ITEM_UNAVAILABLE");
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
        }

        order.setItems(orderItems);
        order.setTotal(total);
        return orderRepository.save(order);
    }

    @Transactional
    public Order updateOrderStatus(Long orderId, OrderStatus newStatus) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Pedido", orderId));

        if (!isValidStatusTransition(order.getStatus(), newStatus)) {
            throw new BusinessException(
                    String.format("Transição inválida: %s → %s", order.getStatus(), newStatus),
                    HttpStatus.CONFLICT, "INVALID_STATUS_TRANSITION");
        }

        order.setStatus(newStatus);
        return orderRepository.save(order);
    }

    @Transactional
    public void cancelOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Pedido", orderId));

        if (order.getStatus() == OrderStatus.ENTREGUE) {
            throw new BusinessException("Não é possível cancelar um pedido já entregue",
                    HttpStatus.CONFLICT, "ORDER_ALREADY_DELIVERED");
        }
        if (order.getStatus() == OrderStatus.CANCELADO) {
            throw new BusinessException("Pedido já está cancelado",
                    HttpStatus.CONFLICT, "ORDER_ALREADY_CANCELLED");
        }

        order.setStatus(OrderStatus.CANCELADO);
        orderRepository.save(order);
    }

    @Transactional
    public Order markAsInProgress(Long orderId) {
        return updateOrderStatus(orderId, OrderStatus.EM_ANDAMENTO);
    }

    @Transactional
    public Order markAsReady(Long orderId) {
        return updateOrderStatus(orderId, OrderStatus.PRONTO);
    }

    @Transactional
    public Order markAsDelivered(Long orderId) {
        return updateOrderStatus(orderId, OrderStatus.ENTREGUE);
    }

    @Transactional
    public void deleteOrder(Long orderId) {
        orderRepository.deleteById(orderId);
    }

    private boolean isValidStatusTransition(OrderStatus current, OrderStatus next) {
        if (current == next) return true;
        return switch (current) {
            case NOVO -> next == OrderStatus.EM_ANDAMENTO || next == OrderStatus.CANCELADO;
            case EM_ANDAMENTO -> next == OrderStatus.PRONTO || next == OrderStatus.CANCELADO;
            case PRONTO -> next == OrderStatus.ENTREGUE || next == OrderStatus.CANCELADO;
            default -> false;
        };
    }
}
