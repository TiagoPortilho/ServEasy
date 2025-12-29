package com.tiagoportilho.ServEasy.service;

import com.tiagoportilho.ServEasy.model.MenuItem;
import com.tiagoportilho.ServEasy.repository.MenuItemRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Testes unitários para MenuService.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("MenuService Tests")
class MenuServiceTest {

    @Mock
    private MenuItemRepository menuItemRepository;

    @InjectMocks
    private MenuService menuService;

    private MenuItem sampleMenuItem;

    @BeforeEach
    void setUp() {
        sampleMenuItem = new MenuItem();
        sampleMenuItem.setId(1L);
        sampleMenuItem.setName("Pizza Margherita");
        sampleMenuItem.setDescription("Molho de tomate, mussarela e manjericão");
        sampleMenuItem.setPrice(new BigDecimal("35.00"));
        sampleMenuItem.setCategory(MenuItem.Category.PIZZAS);
        sampleMenuItem.setIsAvailable(true);
    }

    @Nested
    @DisplayName("getAllMenuItems")
    class GetAllMenuItemsTests {

        @Test
        @DisplayName("deve retornar lista vazia quando não há itens")
        void shouldReturnEmptyListWhenNoItems() {
            when(menuItemRepository.findAll()).thenReturn(Collections.emptyList());

            List<MenuItem> result = menuService.getAllMenuItems();

            assertThat(result).isEmpty();
            verify(menuItemRepository, times(1)).findAll();
        }

        @Test
        @DisplayName("deve retornar todos os itens do cardápio")
        void shouldReturnAllMenuItems() {
            MenuItem item2 = new MenuItem();
            item2.setId(2L);
            item2.setName("Hambúrguer");
            
            when(menuItemRepository.findAll()).thenReturn(Arrays.asList(sampleMenuItem, item2));

            List<MenuItem> result = menuService.getAllMenuItems();

            assertThat(result).hasSize(2);
            verify(menuItemRepository, times(1)).findAll();
        }
    }

    @Nested
    @DisplayName("getMenuItemById")
    class GetMenuItemByIdTests {

        @Test
        @DisplayName("deve retornar item quando existe")
        void shouldReturnItemWhenExists() {
            when(menuItemRepository.findById(1L)).thenReturn(Optional.of(sampleMenuItem));

            Optional<MenuItem> result = menuService.getMenuItemById(1L);

            assertThat(result).isPresent();
            assertThat(result.get().getName()).isEqualTo("Pizza Margherita");
        }

        @Test
        @DisplayName("deve retornar vazio quando item não existe")
        void shouldReturnEmptyWhenItemNotExists() {
            when(menuItemRepository.findById(999L)).thenReturn(Optional.empty());

            Optional<MenuItem> result = menuService.getMenuItemById(999L);

            assertThat(result).isEmpty();
        }
    }

    @Nested
    @DisplayName("getAvailableMenuItems")
    class GetAvailableMenuItemsTests {

        @Test
        @DisplayName("deve retornar apenas itens disponíveis")
        void shouldReturnOnlyAvailableItems() {
            when(menuItemRepository.findAvailableMenuItemsWithIngredients())
                    .thenReturn(Arrays.asList(sampleMenuItem));

            List<MenuItem> result = menuService.getAvailableMenuItems();

            assertThat(result).hasSize(1);
            assertThat(result.get(0).getIsAvailable()).isTrue();
        }
    }

    @Nested
    @DisplayName("getMenuItemsByCategory")
    class GetMenuItemsByCategoryTests {

        @Test
        @DisplayName("deve retornar itens da categoria especificada")
        void shouldReturnItemsByCategory() {
            when(menuItemRepository.findByCategoryAndIsAvailableTrue(MenuItem.Category.PIZZAS))
                    .thenReturn(Arrays.asList(sampleMenuItem));

            List<MenuItem> result = menuService.getMenuItemsByCategory(MenuItem.Category.PIZZAS);

            assertThat(result).hasSize(1);
            assertThat(result.get(0).getCategory()).isEqualTo(MenuItem.Category.PIZZAS);
        }
    }

    @Nested
    @DisplayName("saveMenuItem")
    class SaveMenuItemTests {

        @Test
        @DisplayName("deve salvar item com sucesso")
        void shouldSaveItemSuccessfully() {
            when(menuItemRepository.save(any(MenuItem.class))).thenReturn(sampleMenuItem);

            MenuItem result = menuService.saveMenuItem(sampleMenuItem);

            assertThat(result).isNotNull();
            assertThat(result.getId()).isEqualTo(1L);
            verify(menuItemRepository, times(1)).save(any(MenuItem.class));
        }
    }

    @Nested
    @DisplayName("deleteMenuItem")
    class DeleteMenuItemTests {

        @Test
        @DisplayName("deve deletar item com sucesso")
        void shouldDeleteItemSuccessfully() {
            doNothing().when(menuItemRepository).deleteById(1L);

            menuService.deleteMenuItem(1L);

            verify(menuItemRepository, times(1)).deleteById(1L);
        }
    }
}
