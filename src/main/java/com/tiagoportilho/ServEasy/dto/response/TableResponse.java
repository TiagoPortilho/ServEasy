package com.tiagoportilho.ServEasy.dto.response;

import com.tiagoportilho.ServEasy.model.RestaurantTable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO de resposta para mesas.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TableResponse {

    private Long id;
    private Integer tableNumber;
    private Integer capacity;
    private String status;

    public static TableResponse fromEntity(RestaurantTable table) {
        return TableResponse.builder()
                .id(table.getId())
                .tableNumber(table.getTableNumber())
                .capacity(table.getCapacity())
                .status(table.getStatus() != null ? table.getStatus().name() : "DISPONIVEL")
                .build();
    }
}
