package com.iade.iadego.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class OrderResponse {
    private String orderId;
    private Long userId;
    private String userName;
    private BigDecimal totalPrice;
    private Integer