package com.tiagoportilho.ServEasy.repository;

import com.tiagoportilho.ServEasy.model.Order;
import com.tiagoportilho.ServEasy.model.RestaurantTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByStatus(Order.OrderStatus status);
    List<Order> findByStatusOrderByCreatedAtAsc(Order.OrderStatus status);
    List<Order> findByTable(RestaurantTable table);
    List<Order> findByCreatedAtBetween(LocalDateTime start, LocalDateTime end);
    List<Order> findByStatusInOrderByCreatedAtDesc(List<Order.OrderStatus> statuses);
    List<Order> findByTableTableNumberAndStatusIn(Integer tableNumber, List<Order.OrderStatus> statuses);
}