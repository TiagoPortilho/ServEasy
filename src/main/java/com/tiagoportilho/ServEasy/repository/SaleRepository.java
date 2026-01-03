package com.tiagoportilho.ServEasy.repository;

import com.tiagoportilho.ServEasy.model.Sale;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface SaleRepository extends JpaRepository<Sale, Long> {
    
    List<Sale> findByTableNumber(Integer tableNumber);
    
    List<Sale> findByCreatedAtBetween(LocalDateTime start, LocalDateTime end);
    
    @Query("SELECT SUM(s.totalAmount) FROM Sale s WHERE s.createdAt BETWEEN :start AND :end")
    BigDecimal getTotalSalesAmountBetween(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);
    
    @Query("SELECT SUM(s.itemsCount) FROM Sale s WHERE s.createdAt BETWEEN :start AND :end")
    Long getTotalItemsSoldBetween(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);
    @Query("SELECT COALESCE(SUM(s.cancelledOrdersCount), 0) FROM Sale s WHERE s.createdAt BETWEEN :start AND :end")
    Long getTotalCancelledOrdersBetween(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);}