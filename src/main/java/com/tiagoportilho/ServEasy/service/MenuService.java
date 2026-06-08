package com.tiagoportilho.ServEasy.service;

import com.tiagoportilho.ServEasy.dto.IngredientDto;
import com.tiagoportilho.ServEasy.dto.MenuItemDto;
import com.tiagoportilho.ServEasy.exception.BusinessException;
import com.tiagoportilho.ServEasy.exception.ResourceNotFoundException;
import com.tiagoportilho.ServEasy.model.MenuItem;
import com.tiagoportilho.ServEasy.model.MenuItemIngredient;
import com.tiagoportilho.ServEasy.model.StockItem;
import com.tiagoportilho.ServEasy.repository.MenuItemIngredientRepository;
import com.tiagoportilho.ServEasy.repository.MenuItemRepository;
import com.tiagoportilho.ServEasy.repository.StockItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MenuService {

    private final MenuItemRepository menuItemRepository;
    private final MenuItemIngredientRepository ingredientRepository;
    private final StockItemRepository stockItemRepository;

    @Transactional(readOnly = true)
    public List<MenuItem> getAllMenuItems() {
        return menuItemRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<MenuItemDto> getAllMenuItemsAsDto() {
        return menuItemRepository.findAll().stream()
                .map(this::convertToDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<MenuItem> getAvailableMenuItems() {
        return menuItemRepository.findAvailableMenuItemsWithIngredients();
    }

    @Transactional(readOnly = true)
    public List<MenuItem> getMenuItemsByCategory(MenuItem.Category category) {
        return menuItemRepository.findByCategoryAndIsAvailableTrue(category);
    }

    @Transactional(readOnly = true)
    public Optional<MenuItem> getMenuItemById(Long id) {
        return menuItemRepository.findById(id);
    }

    @Transactional
    public MenuItem saveMenuItem(MenuItem menuItem) {
        return menuItemRepository.save(menuItem);
    }

    @Transactional
    public void deleteMenuItem(Long id) {
        menuItemRepository.deleteById(id);
    }

    @Transactional
    public MenuItem toggleAvailability(Long id) {
        MenuItem menuItem = menuItemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Item do cardápio", id));
        menuItem.setIsAvailable(!Boolean.TRUE.equals(menuItem.getIsAvailable()));
        return menuItemRepository.save(menuItem);
    }

    @Transactional(readOnly = true)
    public Optional<MenuItemDto> getMenuItemDtoById(Long id) {
        return menuItemRepository.findById(id).map(this::convertToDto);
    }

    @Transactional
    public MenuItem saveMenuItemWithIngredients(MenuItemDto dto) {
        if (stockItemRepository.count() == 0) {
            throw new BusinessException(
                    "Não é possível criar pratos sem itens no estoque. Cadastre itens no estoque primeiro.",
                    HttpStatus.UNPROCESSABLE_ENTITY, "NO_STOCK_ITEMS");
        }

        for (IngredientDto ingredient : dto.getIngredients()) {
            if (!stockItemRepository.existsById(ingredient.getStockItemId())) {
                throw new ResourceNotFoundException("Item do estoque", ingredient.getStockItemId());
            }
        }

        MenuItem menuItem;
        if (dto.getId() != null) {
            menuItem = menuItemRepository.findById(dto.getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Item do cardápio", dto.getId()));
            ingredientRepository.deleteByMenuItemId(dto.getId());
        } else {
            menuItem = new MenuItem();
        }

        menuItem.setName(dto.getName());
        menuItem.setDescription(dto.getDescription());
        menuItem.setPrice(dto.getPrice());
        menuItem.setCategory(dto.getCategory());
        menuItem.setImageUrl(dto.getImageUrl());
        menuItem.setIsAvailable(Boolean.TRUE.equals(dto.getIsAvailable()));
        menuItem = menuItemRepository.save(menuItem);

        final MenuItem saved = menuItem;
        List<MenuItemIngredient> ingredients = dto.getIngredients().stream()
                .map(ingredientDto -> {
                    MenuItemIngredient ingredient = new MenuItemIngredient();
                    ingredient.setMenuItem(saved);
                    StockItem stockItem = stockItemRepository.findById(ingredientDto.getStockItemId())
                            .orElseThrow(() -> new ResourceNotFoundException("Item do estoque", ingredientDto.getStockItemId()));
                    ingredient.setStockItem(stockItem);
                    ingredient.setQuantity(ingredientDto.getQuantity());
                    ingredient.setUnit(ingredientDto.getUnit());
                    ingredient.setShowUnit(Boolean.TRUE.equals(ingredientDto.getShowUnit()));
                    return ingredient;
                })
                .toList();

        ingredientRepository.saveAll(ingredients);
        return saved;
    }

    @Transactional(readOnly = true)
    public boolean hasStockItems() {
        return stockItemRepository.count() > 0;
    }

    private MenuItemDto convertToDto(MenuItem menuItem) {
        MenuItemDto dto = new MenuItemDto();
        dto.setId(menuItem.getId());
        dto.setName(menuItem.getName());
        dto.setDescription(menuItem.getDescription());
        dto.setPrice(menuItem.getPrice());
        dto.setCategory(menuItem.getCategory());
        dto.setImageUrl(menuItem.getImageUrl());
        dto.setIsAvailable(menuItem.getIsAvailable());

        List<MenuItemIngredient> ingredients = ingredientRepository.findByMenuItemIdWithStockItem(menuItem.getId());
        List<IngredientDto> ingredientDtos = ingredients.stream()
                .map(ingredient -> {
                    IngredientDto ingredientDto = new IngredientDto();
                    ingredientDto.setStockItemId(ingredient.getStockItem().getId());
                    ingredientDto.setStockItemName(ingredient.getStockItem().getName());
                    ingredientDto.setQuantity(ingredient.getQuantity());
                    ingredientDto.setUnit(ingredient.getUnit());
                    ingredientDto.setShowUnit(Boolean.TRUE.equals(ingredient.getShowUnit()));
                    return ingredientDto;
                })
                .toList();

        dto.setIngredients(ingredientDtos);
        return dto;
    }
}
