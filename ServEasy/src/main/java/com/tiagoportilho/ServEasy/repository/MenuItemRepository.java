package com.tiagoportilho.ServEasy.repository;

import com.tiagoportilho.ServEasy.model.MenuItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MenuItemRepository extends JpaRepository<MenuItem, Long> {
    List<MenuItem> findByIsAvailableTrue();
    List<MenuItem> findByCategory(MenuItem.Category category);
    List<MenuItem> findByCategoryAndIsAvailableTrue(MenuItem.Category category);
}