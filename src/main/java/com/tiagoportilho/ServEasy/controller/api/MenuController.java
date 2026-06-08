package com.tiagoportilho.ServEasy.controller.api;

import com.tiagoportilho.ServEasy.dto.ApiResponse;
import com.tiagoportilho.ServEasy.dto.MenuItemDto;
import com.tiagoportilho.ServEasy.exception.ResourceNotFoundException;
import com.tiagoportilho.ServEasy.model.MenuItem;
import com.tiagoportilho.ServEasy.service.MenuService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/menu")
@RequiredArgsConstructor
@Tag(name = "Cardápio", description = "Gestão de itens do cardápio: listagem, criação, edição e disponibilidade")
public class MenuController {

    private final MenuService menuService;

    @GetMapping
    @Operation(summary = "Listar cardápio completo", description = "Retorna todos os itens do cardápio com ingredientes. Acesso público.")
    public ResponseEntity<ApiResponse<List<MenuItemDto>>> getAllMenuItems() {
        return ResponseEntity.ok(ApiResponse.success(menuService.getAllMenuItemsAsDto()));
    }

    @GetMapping("/available")
    @Operation(summary = "Itens disponíveis", description = "Retorna apenas os itens marcados como disponíveis. Acesso público.")
    public ResponseEntity<ApiResponse<List<MenuItem>>> getAvailableMenuItems() {
        return ResponseEntity.ok(ApiResponse.success(menuService.getAvailableMenuItems()));
    }

    @GetMapping("/category/{category}")
    @Operation(summary = "Itens por categoria", description = "Retorna itens disponíveis de uma categoria específica. Acesso público.")
    public ResponseEntity<ApiResponse<List<MenuItem>>> getMenuItemsByCategory(
            @Parameter(description = "Categoria do item", example = "PRATO_PRINCIPAL") @PathVariable MenuItem.Category category) {
        return ResponseEntity.ok(ApiResponse.success(menuService.getMenuItemsByCategory(category)));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar item por ID", description = "Retorna um item do cardápio com todos os ingredientes. Retorna 404 se não encontrado.")
    public ResponseEntity<ApiResponse<MenuItemDto>> getMenuItemById(
            @Parameter(description = "ID do item", example = "1") @PathVariable Long id) {
        MenuItemDto dto = menuService.getMenuItemDtoById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Item do cardápio", id));
        return ResponseEntity.ok(ApiResponse.success(dto));
    }

    @GetMapping("/check-stock")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Verificar estoque disponível", description = "Retorna true se há itens no estoque para criação de pratos. Somente ADMIN.")
    public ResponseEntity<ApiResponse<Boolean>> checkStockAvailability() {
        return ResponseEntity.ok(ApiResponse.success(menuService.hasStockItems()));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Criar item no cardápio",
            description = "Cria um novo item com ingredientes. Requer ADMIN. Retorna 422 se não houver itens no estoque.")
    public ResponseEntity<ApiResponse<MenuItem>> createMenuItem(@RequestBody MenuItemDto menuItemDto) {
        MenuItem saved = menuService.saveMenuItemWithIngredients(menuItemDto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Item criado com sucesso", saved));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Atualizar item do cardápio",
            description = "Atualiza um item existente e seus ingredientes. Requer ADMIN. Retorna 404 se não encontrado.")
    public ResponseEntity<ApiResponse<MenuItem>> updateMenuItem(
            @Parameter(description = "ID do item", example = "1") @PathVariable Long id,
            @RequestBody MenuItemDto menuItemDto) {
        menuItemDto.setId(id);
        return ResponseEntity.ok(ApiResponse.success("Item atualizado com sucesso",
                menuService.saveMenuItemWithIngredients(menuItemDto)));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Deletar item do cardápio", description = "Remove permanentemente um item. Requer ADMIN.")
    public ResponseEntity<Void> deleteMenuItem(
            @Parameter(description = "ID do item", example = "1") @PathVariable Long id) {
        menuService.deleteMenuItem(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/toggle-availability")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Alternar disponibilidade",
            description = "Ativa ou desativa a exibição de um item no cardápio. Requer ADMIN. Retorna 404 se não encontrado.")
    public ResponseEntity<ApiResponse<MenuItem>> toggleAvailability(
            @Parameter(description = "ID do item", example = "1") @PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success("Disponibilidade alterada", menuService.toggleAvailability(id)));
    }
}
