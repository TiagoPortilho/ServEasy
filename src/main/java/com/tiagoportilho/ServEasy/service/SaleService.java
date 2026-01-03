package com.tiagoportilho.ServEasy.service;

import com.tiagoportilho.ServEasy.model.Sale;
import com.tiagoportilho.ServEasy.repository.SaleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SaleService {

    private final SaleRepository saleRepository;

    public Sale saveSale(Sale sale) {
        return saleRepository.save(sale);
    }

    public List<Sale> getAllSales() {
        return saleRepository.findAll();
    }

    public List<Sale> getSalesByTable(Integer tableNumber) {
        return saleRepository.findByTableNumber(tableNumber);
    }

    public List<Sale> getSalesBetween(LocalDateTime start, LocalDateTime end) {
        return saleRepository.findByCreatedAtBetween(start, end);
    }

    public BigDecimal getTotalSalesAmount(LocalDateTime start, LocalDateTime end) {
        BigDecimal total = saleRepository.getTotalSalesAmountBetween(start, end);
        return total != null ? total : BigDecimal.ZERO;
    }

    public Long getTotalItemsSold(LocalDateTime start, LocalDateTime end) {
        Long total = saleRepository.getTotalItemsSoldBetween(start, end);
        return total != null ? total : 0L;
    }

    public List<Sale> getTodaysSales() {
        LocalDateTime startOfDay = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0);
        LocalDateTime endOfDay = LocalDateTime.now().withHour(23).withMinute(59).withSecond(59);
        return getSalesBetween(startOfDay, endOfDay);
    }

    public BigDecimal getTodaysTotalSales() {
        LocalDateTime startOfDay = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0);
        LocalDateTime endOfDay = LocalDateTime.now().withHour(23).withMinute(59).withSecond(59);
        return getTotalSalesAmount(startOfDay, endOfDay);
    }

    public Long getTodaysCancelledOrders() {
        LocalDateTime startOfDay = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0);
        LocalDateTime endOfDay = LocalDateTime.now().withHour(23).withMinute(59).withSecond(59);
        Long cancelled = saleRepository.getTotalCancelledOrdersBetween(startOfDay, endOfDay);
        return cancelled != null ? cancelled : 0L;
    }
}