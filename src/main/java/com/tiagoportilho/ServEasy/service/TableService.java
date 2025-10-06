package com.tiagoportilho.ServEasy.service;

import com.tiagoportilho.ServEasy.model.RestaurantTable;
import com.tiagoportilho.ServEasy.repository.RestaurantTableRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TableService {
    
    private final RestaurantTableRepository restaurantTableRepository;

    public List<RestaurantTable> getAllTables() {
        return restaurantTableRepository.findAll();
    }

    public Optional<RestaurantTable> getTableById(Long id) {
        return restaurantTableRepository.findById(id);
    }

    public Optional<RestaurantTable> getTableByNumber(Integer tableNumber) {
        return restaurantTableRepository.findByTableNumber(tableNumber);
    }

    public List<RestaurantTable> getAvailableTables() {
        return restaurantTableRepository.findByStatus(RestaurantTable.TableStatus.DISPONIVEL);
    }

    public RestaurantTable saveTable(RestaurantTable table) {
        if (table.getId() == null && restaurantTableRepository.existsByTableNumber(table.getTableNumber())) {
            throw new RuntimeException("Já existe uma mesa com este número");
        }
        return restaurantTableRepository.save(table);
    }

    public void deleteTable(Long id) {
        restaurantTableRepository.deleteById(id);
    }

    public RestaurantTable updateTableStatus(Long id, RestaurantTable.TableStatus status) {
        Optional<RestaurantTable> tableOpt = restaurantTableRepository.findById(id);
        if (tableOpt.isPresent()) {
            RestaurantTable table = tableOpt.get();
            table.setStatus(status);
            return restaurantTableRepository.save(table);
        }
        throw new RuntimeException("Mesa não encontrada");
    }

    public RestaurantTable updateTableStatusByNumber(Integer tableNumber, RestaurantTable.TableStatus status) {
        Optional<RestaurantTable> tableOpt = restaurantTableRepository.findByTableNumber(tableNumber);
        if (tableOpt.isPresent()) {
            RestaurantTable table = tableOpt.get();
            table.setStatus(status);
            return restaurantTableRepository.save(table);
        }
        throw new RuntimeException("Mesa não encontrada");
    }
}