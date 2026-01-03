package com.tiagoportilho.ServEasy.controller.api;

import com.tiagoportilho.ServEasy.dto.ApiResponse;
import com.tiagoportilho.ServEasy.dto.MenuItemDto;
import com.tiagoportilho.ServEasy.model.MenuItem;
import com.tiagoportilho.ServEasy.service.MenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/menu")
@RequiredArgsConstructor
public class MenuController {

    private final MenuService menuService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<MenuItemDto>>> getAllMenuItems() {
        try {
            List<MenuItemDto> menuItems = menuService.getAllMenuItemsAsDto();
            return ResponseEntity.ok(ApiResponse.success(menuItems));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.error("Erro ao buscar itens do menu: " + e.getMessage()));
        }
    }

    @GetMapping("/available")
    public ResponseEntity<ApiResponse<List<MenuItem>>> getAvailableMenuItems() {
        try {
            List<MenuItem> menuItems = menuService.getAvailableMenuItems();
            return ResponseEntity.ok(ApiResponse.success(menuItems));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.error("Erro ao buscar itens disponíveis: " + e.getMessage()));
        }
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<ApiResponse<List<MenuItem>>> getMenuItemsByCategory(@PathVariable MenuItem.Category category) {
        try {
            List<MenuItem> menuItems = menuService.getMenuItemsByCategory(category);
            return ResponseEntity.ok(ApiResponse.success(menuItems));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.error("Erro ao buscar itens por categoria: " + e.getMessage()));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<MenuItemDto>> getMenuItemById(@PathVariable Long id) {
        try {
            Optional<MenuItemDto> menuItem = menuService.getMenuItemDtoById(id);
            if (menuItem.isPresent()) {
                return ResponseEntity.ok(ApiResponse.success(menuItem.get()));
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.error("Erro ao buscar item: " + e.getMessage()));
        }
    }

    @GetMapping("/{id}/simple")
    public ResponseEntity<ApiResponse<MenuItem>> getMenuItemSimpleById(@PathVariable Long id) {
        try {
            Optional<MenuItem> menuItem = menuService.getMenuItemById(id);
            if (menuItem.isPresent()) {
                return ResponseEntity.ok(ApiResponse.success(menuItem.get()));
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.error("Erro ao buscar item: " + e.getMessage()));
        }
    }

    @PostMapping
    public ResponseEntity<ApiResponse<MenuItem>> createMenuItem(@RequestBody MenuItemDto menuItemDto) {
        try {
            MenuItem savedItem = menuService.saveMenuItemWithIngredients(menuItemDto);
            return ResponseEntity.ok(ApiResponse.success("Item criado com sucesso", savedItem));
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Erro ao criar item: " + e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<MenuItem>> updateMenuItem(@PathVariable Long id, @RequestBody MenuItemDto menuItemDto) {
        try {
            menuItemDto.setId(id);
            MenuItem updatedItem = menuService.saveMenuItemWithIngredients(menuItemDto);
            return ResponseEntity.ok(ApiResponse.success("Item atualizado com sucesso", updatedItem));
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Erro ao atualizar item: " + e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteMenuItem(@PathVariable Long id) {
        try {
            menuService.deleteMenuItem(id);
            return ResponseEntity.ok(ApiResponse.success("Item deletado com sucesso", null));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Erro ao deletar item: " + e.getMessage()));
        }
    }

    @PatchMapping("/{id}/toggle-availability")
    public ResponseEntity<ApiResponse<MenuItem>> toggleAvailability(@PathVariable Long id) {
        try {
            MenuItem updatedItem = menuService.toggleAvailability(id);
            return ResponseEntity.ok(ApiResponse.success("Disponibilidade alterada com sucesso", updatedItem));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Erro ao alterar disponibilidade: " + e.getMessage()));
        }
    }

    @GetMapping("/check-stock")
    public ResponseEntity<ApiResponse<Boolean>> checkStockAvailability() {
        try {
            boolean hasStock = menuService.hasStockItems();
            return ResponseEntity.ok(ApiResponse.success(hasStock));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.error("Erro ao verificar estoque: " + e.getMessage()));
        }
    }
}