package com.iade.iadego.dto;

import java.util.List;

public class CreateOrderRequest {
    private Long userId;
    private List<OrderItemRequest> items;
    private String paymentMethod; // "PAY_NOW" ou "PAY_LATER"

    // Constructors
    public CreateOrderRequest() {}

    // Getters and Setters
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public List<OrderItemRequest> getItems() {
        return items;
    }

    public void setItems(List<OrderItemRequest> items) {
        this.items = items;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    // Inner class
    public static class OrderItemRequest {
        private Long itemId;
        private Integer quantity;

        public OrderItemRequest() {}

        public Long getItemId() {
            return itemId;
        }

        public void setItemId(Long itemId) {
            this.itemId = itemId;
        }

        public Integer getQuantity() {
            return quantity;
        }

        public void setQuantity(Integer quantity) {
            this.quantity = quantity;
        }
    }
}