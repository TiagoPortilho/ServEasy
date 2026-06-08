package com.tiagoportilho.ServEasy.service;

import com.tiagoportilho.ServEasy.exception.ResourceNotFoundException;
import com.tiagoportilho.ServEasy.model.MenuItem;
import com.tiagoportilho.ServEasy.repository.MenuItemIngredientRepository;
import com.tiagoportilho.ServEasy.repository.MenuItemRepository;
import com.tiagoportilho.ServEasy.repository.StockItemRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("MenuService")
class MenuServiceTest {

    @Mock private MenuItemRepository menuItemRepository;
    @Mock private MenuItemIngredientRepository ingredientRepository;
    @Mock private StockItemRepository stockItemRepository;
    @InjectMocks private MenuService menuService;

    private MenuItem item;

    @BeforeEach
    void setUp() {
        item = new MenuItem();
        item.setId(1L);
        item.setName("Hambúrguer Clássico");
        item.setPrice(new BigDecimal("29.90"));
        item.setCategory(MenuItem.Category.LANCHES);
        item.setIsAvailable(true);
    }

    @Nested @DisplayName("getAllMenuItems")
    class GetAll {

        @Test
        @DisplayName("retorna lista vazia quando não há itens")
        void returns_empty_when_no_items() {
            when(menuItemRepository.findAll()).thenReturn(Collections.emptyList());
            assertThat(menuService.getAllMenuItems()).isEmpty();
        }

        @Test
        @DisplayName("retorna todos os itens")
        void returns_all_items() {
            when(menuItemRepository.findAll()).thenReturn(List.of(item));
            assertThat(menuService.getAllMenuItems()).hasSize(1);
        }
    }

    @Nested @DisplayName("getMenuItemById")
    class GetById {

        @Test
        @DisplayName("retorna item quando encontrado")
        void returns_item_when_found() {
            when(menuItemRepository.findById(1L)).thenReturn(Optional.of(item));
            assertThat(menuService.getMenuItemById(1L)).isPresent();
        }

        @Test
        @DisplayName("retorna empty quando não encontrado")
        void returns_empty_when_not_found() {
            when(menuItemRepository.findById(99L)).thenReturn(Optional.empty());
            assertThat(menuService.getMenuItemById(99L)).isEmpty();
        }
    }

    @Nested @DisplayName("getAvailableMenuItems")
    class GetAvailable {

        @Test
        @DisplayName("retorna apenas itens disponíveis")
        void returns_only_available_items() {
            when(menuItemRepository.findAvailableMenuItemsWithIngredients()).thenReturn(List.of(item));
            List<MenuItem> result = menuService.getAvailableMenuItems();
            assertThat(result).hasSize(1);
            assertThat(result.get(0).getIsAvailable()).isTrue();
        }
    }

    @Nested @DisplayName("toggleAvailability")
    class Toggle {

        @Test
        @DisplayName("alterna de true para false")
        void toggles_from_true_to_false() {
            item.setIsAvailable(true);
            when(menuItemRepository.findById(1L)).thenReturn(Optional.of(item));
            when(menuItemRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

            MenuItem result = menuService.toggleAvailability(1L);

            assertThat(result.getIsAvailable()).isFalse();
        }

        @Test
        @DisplayName("alterna de false para true")
        void toggles_from_false_to_true() {
            item.setIsAvailable(false);
            when(menuItemRepository.findById(1L)).thenReturn(Optional.of(item));
            when(menuItemRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

            MenuItem result = menuService.toggleAvailability(1L);

            assertThat(result.getIsAvailable()).isTrue();
        }

        @Test
        @DisplayName("lança ResourceNotFoundException quando item não encontrado")
        void throws_when_not_found() {
            when(menuItemRepository.findById(99L)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> menuService.toggleAvailability(99L))
                    .isInstanceOf(ResourceNotFoundException.class);
        }
    }

    @Nested @DisplayName("deleteMenuItem")
    class Delete {

        @Test
        @DisplayName("chama deleteById no repositório")
        void calls_delete() {
            doNothing().when(menuItemRepository).deleteById(1L);
            menuService.deleteMenuItem(1L);
            verify(menuItemRepository).deleteById(1L);
        }
    }
}
