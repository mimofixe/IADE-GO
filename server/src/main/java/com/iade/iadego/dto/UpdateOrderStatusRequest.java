package com.iade.iadego.dto;

public class UpdateOrderStatusRequest {
    private String orderStatus; // PENDING, PAID, PREPARING, READY, COMPLETED, CANCELLED
    private String paymentStatus; // PENDING, PAID, FAILED

    // Constructors
    public UpdateOrderStatusRequest() {}

    // Getters and Setters
    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }
}