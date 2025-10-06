package com.tiagoportilho.ServEasy.dto;

import com.tiagoportilho.ServEasy.model.MenuItem;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;
import java.util.ArrayList;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MenuItemDto {
    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private MenuItem.Category category;
    private String imageUrl;
    private Boolean isAvailable;
    private List<IngredientDto> ingredients = new ArrayList<>();
}