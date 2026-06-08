package com.tiagoportilho.ServEasy.dto.response;

import com.tiagoportilho.ServEasy.model.MenuItem;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MenuItemSimpleDTO {
    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private String category;
    private String imageUrl;
    private Boolean isAvailable;

    public static MenuItemSimpleDTO fromEntity(MenuItem menuItem) {
        MenuItemSimpleDTO dto = new MenuItemSimpleDTO();
        dto.setId(menuItem.getId());
        dto.setName(menuItem.getName());
        dto.setDescription(menuItem.getDescription());
        dto.setPrice(menuItem.getPrice());
        dto.setCategory(menuItem.getCategory() != null ? menuItem.getCategory().name() : null);
        dto.setImageUrl(menuItem.getImageUrl());
        dto.setIsAvailable(menuItem.getIsAvailable());
        return dto;
    }
}
