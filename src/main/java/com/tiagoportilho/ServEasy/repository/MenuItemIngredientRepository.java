package com.tiagoportilho.ServEasy.repository;

import com.tiagoportilho.ServEasy.model.MenuItemIngredient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MenuItemIngredientRepository extends JpaRepository<MenuItemIngredient, Long> {
    
    @Query("SELECT mii FROM MenuItemIngredient mii WHERE mii.menuItem.id = :menuItemId")
    List<MenuItemIngredient> findByMenuItemId(@Param("menuItemId") Long menuItemId);
    
    @Query("SELECT mii FROM MenuItemIngredient mii JOIN FETCH mii.stockItem WHERE mii.menuItem.id = :menuItemId")
    List<MenuItemIngredient> findByMenuItemIdWithStockItem(@Param("menuItemId") Long menuItemId);
    
    @Query("SELECT mii FROM MenuItemIngredient mii WHERE mii.stockItem.id = :stockItemId")
    List<MenuItemIngredient> findByStockItemId(@Param("stockItemId") Long stockItemId);
    
    void deleteByMenuItemId(Long menuItemId);
}