package com.tiagoportilho.ServEasy.service;

import com.tiagoportilho.ServEasy.dto.IngredientDto;
import com.tiagoportilho.ServEasy.dto.MenuItemDto;
import com.tiagoportilho.ServEasy.model.MenuItem;
import com.tiagoportilho.ServEasy.model.MenuItemIngredient;
import com.tiagoportilho.ServEasy.model.StockItem;
import com.tiagoportilho.ServEasy.repository.MenuItemRepository;
import com.tiagoportilho.ServEasy.repository.MenuItemIngredientRepository;
import com.tiagoportilho.ServEasy.repository.StockItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MenuService {
    
    private final MenuItemRepository menuItemRepository;
    private final MenuItemIngredientRepository ingredientRepository;
    private final StockItemRepository stockItemRepository;

    public List<MenuItem> getAllMenuItems() {
        return menuItemRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<MenuItemDto> getAllMenuItemsAsDto() {
        List<MenuItem> menuItems = menuItemRepository.findAll();
        return menuItems.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public List<MenuItem> getAvailableMenuItems() {
        return menuItemRepository.findAvailableMenuItemsWithIngredients();
    }

    public List<MenuItem> getMenuItemsByCategory(MenuItem.Category category) {
        return menuItemRepository.findByCategoryAndIsAvailableTrue(category);
    }

    public Optional<MenuItem> getMenuItemById(Long id) {
        return menuItemRepository.findById(id);
    }

    public MenuItem saveMenuItem(MenuItem menuItem) {
        return menuItemRepository.save(menuItem);
    }

    public void deleteMenuItem(Long id) {
        menuItemRepository.deleteById(id);
    }

    public MenuItem toggleAvailability(Long id) {
        Optional<MenuItem> menuItemOpt = menuItemRepository.findById(id);
        if (menuItemOpt.isPresent()) {
            MenuItem menuItem = menuItemOpt.get();
            menuItem.setIsAvailable(!menuItem.getIsAvailable());
            return menuItemRepository.save(menuItem);
        }
        throw new RuntimeException("Item do menu não encontrado");
    }

    public Optional<MenuItemDto> getMenuItemDtoById(Long id) {
        Optional<MenuItem> menuItemOpt = menuItemRepository.findById(id);
        if (menuItemOpt.isPresent()) {
            MenuItem menuItem = menuItemOpt.get();
            MenuItemDto dto = convertToDto(menuItem);
            return Optional.of(dto);
        }
        return Optional.empty();
    }

    @Transactional
    public MenuItem saveMenuItemWithIngredients(MenuItemDto dto) {
        // Verificar se existem itens no estoque
        List<StockItem> stockItems = stockItemRepository.findAll();
        if (stockItems.isEmpty()) {
            throw new IllegalStateException("Não é possível criar pratos sem itens no estoque. Por favor, cadastre itens no estoque primeiro.");
        }

        // Verificar se os ingredientes selecionados existem no estoque
        for (IngredientDto ingredient : dto.getIngredients()) {
            if (!stockItemRepository.existsById(ingredient.getStockItemId())) {
                throw new IllegalStateException("Item do estoque com ID " + ingredient.getStockItemId() + " não encontrado.");
            }
        }

        // Criar ou atualizar o MenuItem
        MenuItem menuItem;
        if (dto.getId() != null) {
            // Atualização - remover ingredientes existentes
            Optional<MenuItem> existingOpt = menuItemRepository.findById(dto.getId());
            if (existingOpt.isPresent()) {
                menuItem = existingOpt.get();
                // Limpar ingredientes existentes
                ingredientRepository.deleteByMenuItemId(dto.getId());
            } else {
                throw new RuntimeException("Item do menu não encontrado para atualização");
            }
        } else {
            // Novo item
            menuItem = new MenuItem();
        }

        // Preencher dados básicos do MenuItem
        menuItem.setName(dto.getName());
        menuItem.setDescription(dto.getDescription());
        menuItem.setPrice(dto.getPrice());
        menuItem.setCategory(dto.getCategory());
        menuItem.setImageUrl(dto.getImageUrl());
        menuItem.setIsAvailable(dto.getIsAvailable() != null ? dto.getIsAvailable() : true);

        // Salvar o MenuItem primeiro
        menuItem = menuItemRepository.save(menuItem);

        // Criar e salvar os ingredientes
        final MenuItem finalMenuItem = menuItem;
        List<MenuItemIngredient> ingredients = dto.getIngredients().stream()
                .map(ingredientDto -> {
                    MenuItemIngredient ingredient = new MenuItemIngredient();
                    ingredient.setMenuItem(finalMenuItem);
                    
                    StockItem stockItem = stockItemRepository.findById(ingredientDto.getStockItemId())
                            .orElseThrow(() -> new RuntimeException("Item do estoque não encontrado"));
                    ingredient.setStockItem(stockItem);
                    ingredient.setQuantity(ingredientDto.getQuantity());
                    ingredient.setUnit(ingredientDto.getUnit());
                    ingredient.setShowUnit(ingredientDto.getShowUnit() != null ? ingredientDto.getShowUnit() : true);
                    
                    return ingredient;
                })
                .collect(Collectors.toList());

        ingredientRepository.saveAll(ingredients);
        
        return menuItem;
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

        // Buscar ingredientes com fetch join para evitar lazy loading
        List<MenuItemIngredient> ingredients = ingredientRepository.findByMenuItemIdWithStockItem(menuItem.getId());
        List<IngredientDto> ingredientDtos = ingredients.stream()
                .map(ingredient -> {
                    IngredientDto ingredientDto = new IngredientDto();
                    ingredientDto.setStockItemId(ingredient.getStockItem().getId());
                    ingredientDto.setStockItemName(ingredient.getStockItem().getName());
                    ingredientDto.setQuantity(ingredient.getQuantity());
                    ingredientDto.setUnit(ingredient.getUnit());
                    ingredientDto.setShowUnit(ingredient.getShowUnit() != null ? ingredient.getShowUnit() : true);
                    return ingredientDto;
                })
                .collect(Collectors.toList());

        dto.setIngredients(ingredientDtos);
        return dto;
    }

    public boolean hasStockItems() {
        return stockItemRepository.count() > 0;
    }
}