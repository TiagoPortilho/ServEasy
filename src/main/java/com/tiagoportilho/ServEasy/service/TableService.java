package com.tiagoportilho.ServEasy.service;

import com.tiagoportilho.ServEasy.exception.BusinessException;
import com.tiagoportilho.ServEasy.exception.ResourceNotFoundException;
import com.tiagoportilho.ServEasy.model.RestaurantTable;
import com.tiagoportilho.ServEasy.repository.RestaurantTableRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TableService {

    private final RestaurantTableRepository restaurantTableRepository;

    @Transactional(readOnly = true)
    public List<RestaurantTable> getAllTables() {
        return restaurantTableRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<RestaurantTable> getTableById(Long id) {
        return restaurantTableRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public Optional<RestaurantTable> getTableByNumber(Integer tableNumber) {
        return restaurantTableRepository.findByTableNumber(tableNumber);
    }

    @Transactional(readOnly = true)
    public List<RestaurantTable> getAvailableTables() {
        return restaurantTableRepository.findByStatus(RestaurantTable.TableStatus.DISPONIVEL);
    }

    @Transactional
    public RestaurantTable saveTable(RestaurantTable table) {
        if (table.getId() == null && restaurantTableRepository.existsByTableNumber(table.getTableNumber())) {
            throw new BusinessException("Já existe uma mesa com o número " + table.getTableNumber(),
                    HttpStatus.CONFLICT, "TABLE_NUMBER_DUPLICATE");
        }
        return restaurantTableRepository.save(table);
    }

    @Transactional
    public void deleteTable(Long id) {
        restaurantTableRepository.deleteById(id);
    }

    @Transactional
    public RestaurantTable updateTableStatus(Long id, RestaurantTable.TableStatus status) {
        RestaurantTable table = restaurantTableRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Mesa", id));
        table.setStatus(status);
        return restaurantTableRepository.save(table);
    }

    @Transactional
    public RestaurantTable updateTableStatusByNumber(Integer tableNumber, RestaurantTable.TableStatus status) {
        RestaurantTable table = restaurantTableRepository.findByTableNumber(tableNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Mesa", "número " + tableNumber));
        table.setStatus(status);
        return restaurantTableRepository.save(table);
    }
}
