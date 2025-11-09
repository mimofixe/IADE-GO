package com.iade.iadego.repository;

import com.iade.iadego.entity.MenuItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface MenuItemRepository extends JpaRepository<MenuItem, Long> {

    List<MenuItem> findByCategoryCategoryId(Long categoryId);

    List<MenuItem> findByIsAvailableTrue();

    List<MenuItem> findByCategoryCategoryIdAndIsAvailableTrue(Long categoryId);
}