package com.iade.iadego.controller;

import com.iade.iadego.entity.MenuCategory;
import com.iade.iadego.entity.MenuItem;
import com.iade.iadego.repository.MenuCategoryRepository;
import com.iade.iadego.repository.MenuItemRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/menu")
@CrossOrigin(origins = "*")
public class MenuController {

    private static final Logger logger = LoggerFactory.getLogger(MenuController.class);

    @Autowired
    private MenuCategoryRepository menuCategoryRepository;

    @Autowired
    private MenuItemRepository menuItemRepository;

    // CATEGORY ENDPOINTS

    // GET all categories
    @GetMapping("/categories")
    public ResponseEntity<List<MenuCategory>> getAllCategories() {
        logger.info("GET request - Fetching all categories");
        List<MenuCategory> categories = menuCategoryRepository.findAllByOrderByDisplayOrderAsc();
        logger.info("Found {} categories", categories.size());
        return ResponseEntity.ok(categories);
    }

    // GET single category
    @GetMapping("/categories/{id}")
    public ResponseEntity<MenuCategory> getCategory(@PathVariable Long id) {
        logger.info("GET request - Fetching category with ID: {}", id);
        Optional<MenuCategory> category = menuCategoryRepository.findById(id);
        return category.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // POST - Create new category
    @PostMapping("/categories")
    public ResponseEntity<MenuCategory> createCategory(@RequestBody MenuCategory category) {
        logger.info("POST request - Creating category: {}", category.getCategoryName());
        MenuCategory savedCategory = menuCategoryRepository.save(category);
        logger.info("Category created with ID: {}", savedCategory.getCategoryId());
        return ResponseEntity.status(HttpStatus.CREATED).body(savedCategory);
    }

    // PUT - Update category
    @PutMapping("/categories/{id}")
    public ResponseEntity<MenuCategory> updateCategory(@PathVariable Long id, @RequestBody MenuCategory categoryDetails) {
        logger.info("PUT request - Updating category with ID: {}", id);
        Optional<MenuCategory> categoryOptional = menuCategoryRepository.findById(id);

        if (categoryOptional.isPresent()) {
            MenuCategory category = categoryOptional.get();
            category.setCategoryName(categoryDetails.getCategoryName());
            category.setDescription(categoryDetails.getDescription());
            category.setDisplayOrder(categoryDetails.getDisplayOrder());

            MenuCategory updatedCategory = menuCategoryRepository.save(category);
            logger.info("Category updated: {}", updatedCategory.getCategoryName());
            return ResponseEntity.ok(updatedCategory);
        } else {
            logger.warn("Category not found with ID: {}", id);
            return ResponseEntity.notFound().build();
        }
    }

    // DELETE category
    @DeleteMapping("/categories/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        logger.info("DELETE request - Deleting category with ID: {}", id);
        if (menuCategoryRepository.existsById(id)) {
            menuCategoryRepository.deleteById(id);
            logger.info("Category deleted with ID: {}", id);
            return ResponseEntity.noContent().build();
        } else {
            logger.warn("Category not found with ID: {}", id);
            return ResponseEntity.notFound().build();
        }
    }

    // MENU ITEM ENDPOINTS

    // GET all menu items
    @GetMapping("/items")
    public ResponseEntity<List<MenuItem>> getAllMenuItems() {
        logger.info("GET request - Fetching all menu items");
        List<MenuItem> items = menuItemRepository.findAll();
        logger.info("Found {} menu items", items.size());
        return ResponseEntity.ok(items);
    }

    // GET menu items by category
    @GetMapping("/items/category/{categoryId}")
    public ResponseEntity<List<MenuItem>> getItemsByCategory(@PathVariable Long categoryId) {
        logger.info("GET request - Fetching items for category ID: {}", categoryId);
        List<MenuItem> items = menuItemRepository.findByCategoryCategoryId(categoryId);
        logger.info("Found {} items in category {}", items.size(), categoryId);
        return ResponseEntity.ok(items);
    }

    // GET available items only
    @GetMapping("/items/available")
    public ResponseEntity<List<MenuItem>> getAvailableItems() {
        logger.info("GET request - Fetching available items only");
        List<MenuItem> items = menuItemRepository.findByIsAvailableTrue();
        logger.info("Found {} available items", items.size());
        return ResponseEntity.ok(items);
    }

    // GET single menu item
    @GetMapping("/items/{id}")
    public ResponseEntity<MenuItem> getMenuItem(@PathVariable Long id) {
        logger.info("GET request - Fetching menu item with ID: {}", id);
        Optional<MenuItem> item = menuItemRepository.findById(id);
        if (item.isPresent()) {
            logger.info("Found menu item: {}", item.get().getItemName());
            return ResponseEntity.ok(item.get());
        } else {
            logger.warn("Menu item not found with ID: {}", id);
            return ResponseEntity.notFound().build();
        }
    }

    // POST - Create new menu item
    @PostMapping("/items")
    public ResponseEntity<MenuItem> createMenuItem(@RequestBody MenuItemRequest request) {
        logger.info("POST request - Creating menu item: {}", request.getItemName());
        logger.info("Category ID: {}", request.getCategoryId());

        // Buscar a categoria
        Optional<MenuCategory> categoryOptional = menuCategoryRepository.findById(request.getCategoryId());

        if (categoryOptional.isEmpty()) {
            logger.error("Category not found with ID: {}", request.getCategoryId());
            return ResponseEntity.badRequest().build();
        }

        // Criar o menu item
        MenuItem menuItem = new MenuItem();
        menuItem.setItemName(request.getItemName());
        menuItem.setDescription(request.getDescription());
        menuItem.setPrice(new BigDecimal(request.getPrice().toString()));
        menuItem.setImageUrl(request.getImageUrl());
        menuItem.setIsAvailable(request.getIsAvailable() != null ? request.getIsAvailable() : true);
        menuItem.setCategory(categoryOptional.get());

        MenuItem savedItem = menuItemRepository.save(menuItem);

        logger.info("Menu item created with ID: {}", savedItem.getItemId());

        return ResponseEntity.status(HttpStatus.CREATED).body(savedItem);
    }

    // PUT - Update menu item
    @PutMapping("/items/{id}")
    public ResponseEntity<MenuItem> updateMenuItem(@PathVariable Long id, @RequestBody MenuItemRequest request) {
        logger.info("PUT request - Updating menu item with ID: {}", id);
        Optional<MenuItem> itemOptional = menuItemRepository.findById(id);

        if (itemOptional.isPresent()) {
            MenuItem item = itemOptional.get();

            // Atualizar categoria se fornecida
            if (request.getCategoryId() != null) {
                Optional<MenuCategory> categoryOptional = menuCategoryRepository.findById(request.getCategoryId());
                if (categoryOptional.isEmpty()) {
                    logger.error("Category not found with ID: {}", request.getCategoryId());
                    return ResponseEntity.badRequest().build();
                }
                item.setCategory(categoryOptional.get());
            }

            // Atualizar outros campos
            if (request.getItemName() != null) {
                item.setItemName(request.getItemName());
            }
            if (request.getDescription() != null) {
                item.setDescription(request.getDescription());
            }
            if (request.getPrice() != null) {
                item.setPrice(new BigDecimal(request.getPrice().toString()));
            }
            if (request.getImageUrl() != null) {
                item.setImageUrl(request.getImageUrl());
            }
            if (request.getIsAvailable() != null) {
                item.setIsAvailable(request.getIsAvailable());
            }

            MenuItem updatedItem = menuItemRepository.save(item);
            logger.info("Menu item updated: {}", updatedItem.getItemName());
            return ResponseEntity.ok(updatedItem);
        } else {
            logger.warn("Menu item not found with ID: {}", id);
            return ResponseEntity.notFound().build();
        }
    }

    // DELETE menu item
    @DeleteMapping("/items/{id}")
    public ResponseEntity<Void> deleteMenuItem(@PathVariable Long id) {
        logger.info("DELETE request - Deleting menu item with ID: {}", id);
        if (menuItemRepository.existsById(id)) {
            menuItemRepository.deleteById(id);
            logger.info("Menu item deleted with ID: {}", id);
            return ResponseEntity.noContent().build();
        } else {
            logger.warn("Menu item not found with ID: {}", id);
            return ResponseEntity.notFound().build();
        }
    }

    // PATCH - Toggle item availability
    @PatchMapping("/items/{id}/availability")
    public ResponseEntity<MenuItem> toggleAvailability(@PathVariable Long id) {
        logger.info("PATCH request - Toggling availability for item ID: {}", id);
        Optional<MenuItem> itemOptional = menuItemRepository.findById(id);

        if (itemOptional.isPresent()) {
            MenuItem item = itemOptional.get();
            item.setIsAvailable(!item.getIsAvailable());
            MenuItem updatedItem = menuItemRepository.save(item);
            logger.info("Item availability toggled to: {}", updatedItem.getIsAvailable());
            return ResponseEntity.ok(updatedItem);
        } else {
            logger.warn("Menu item not found with ID: {}", id);
            return ResponseEntity.notFound().build();
        }
    }

    // DTO CLASS FOR REQUESTS

    public static class MenuItemRequest {
        private String itemName;
        private String description;
        private Double price;
        private String imageUrl;
        private Boolean isAvailable;
        private Long categoryId;

        // Constructors
        public MenuItemRequest() {}

        // Getters and Setters
        public String getItemName() {
            return itemName;
        }

        public void setItemName(String itemName) {
            this.itemName = itemName;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public Double getPrice() {
            return price;
        }

        public void setPrice(Double price) {
            this.price = price;
        }

        public String getImageUrl() {
            return imageUrl;
        }

        public void setImageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
        }

        public Boolean getIsAvailable() {
            return isAvailable;
        }

        public void setIsAvailable(Boolean isAvailable) {
            this.isAvailable = isAvailable;
        }

        public Long getCategoryId() {
            return categoryId;
        }

        public void setCategoryId(Long categoryId) {
            this.categoryId = categoryId;
        }
    }
}