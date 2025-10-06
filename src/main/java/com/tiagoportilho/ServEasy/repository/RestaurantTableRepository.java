package com.tiagoportilho.ServEasy.repository;

import com.tiagoportilho.ServEasy.model.RestaurantTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RestaurantTableRepository extends JpaRepository<RestaurantTable, Long> {
    Optional<RestaurantTable> findByTableNumber(Integer tableNumber);
    List<RestaurantTable> findByStatus(RestaurantTable.TableStatus status);
    boolean existsByTableNumber(Integer tableNumber);
}