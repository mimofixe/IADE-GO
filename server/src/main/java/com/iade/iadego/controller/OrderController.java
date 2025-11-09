package com.iade.iadego.controller;

import com.iade.iadego.dto.*;
import com.iade.iadego.entity.*;
import com.iade.iadego.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/orders")
@CrossOrigin(origins = "*")
public class OrderController {

    private static final Logger logger = LoggerFactory.getLogger(OrderController.class);

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MenuItemRepository menuItemRepository;

    // POST /api/orders - CRIAR ENCOMENDA

    @PostMapping
    public ResponseEntity<OrderResponse> createOrder(@RequestBody CreateOrderRequest request) {
        logger.info("POST - Creating order for user ID: {}", request.getUserId());

        try {
            // 1. Validar user existe
            Optional<User> userOptional = userRepository.findById(request.getUserId());
            if (userOptional.isEmpty()) {
                logger.error("User not found: {}", request.getUserId());
                return ResponseEntity.badRequest().build();
            }
            User user = userOptional.get();

            // 2. Calcular total e validar items
            BigDecimal totalPrice = BigDecimal.ZERO;
            int totalItemCount = 0;
            List<OrderItem> orderItems = new ArrayList<>();

            for (CreateOrderRequest.OrderItemRequest itemReq : request.getItems()) {
                // Validar menu item existe
                Optional<MenuItem> menuItemOpt = menuItemRepository.findById(itemReq.getItemId());
                if (menuItemOpt.isEmpty()) {
                    logger.error("Menu item not found: {}", itemReq.getItemId());
                    return ResponseEntity.badRequest().build();
                }

                MenuItem menuItem = menuItemOpt.get();
                BigDecimal subtotal = menuItem.getPrice().multiply(new BigDecimal(itemReq.getQuantity()));
                totalPrice = totalPrice.add(subtotal);
                totalItemCount += itemReq.getQuantity();

                // Criar OrderItem (associar depois)
                OrderItem orderItem = new OrderItem();
                orderItem.setMenuItem(menuItem);
                orderItem.setQuantity(itemReq.getQuantity());
                orderItem.setUnitPrice(menuItem.getPrice());
                orderItem.setSubtotal(subtotal);
                orderItems.add(orderItem);
            }

            // 3. Gerar Order ID único
            String orderId = generateOrderId();

            // 4. Criar Order
            Order order = new Order();
            order.setOrderId(orderId);
            order.setUser(user);
            order.setTotalPrice(totalPrice);
            order.setItemCount(totalItemCount);
            order.setOrderStatus("PENDING");

            // Payment status baseado no método
            if ("PAY_NOW".equals(request.getPaymentMethod())) {
                order.setPaymentStatus("PAID");
            } else {
                order.setPaymentStatus("PENDING");
            }

            // 5. Gerar QR Code JSON
            String qrCode = generateQRCodeData(orderId, user, totalPrice, totalItemCount);
            order.setQrCodeData(qrCode);

            // 6. Salvar Order
            Order savedOrder = orderRepository.save(order);
            logger.info("Order created: {}", savedOrder.getOrderId());

            // 7. Salvar OrderItems
            for (OrderItem item : orderItems) {
                item.setOrder(savedOrder);
                orderItemRepository.save(item);
            }

            // 8. Criar Response
            OrderResponse response = buildOrderResponse(savedOrder, orderItems);

            return ResponseEntity.status(HttpStatus.CREATED).body(response);

        } catch (Exception e) {
            logger.error("Error creating order", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // GET /api/orders/{orderId} - VER ENCOMENDA

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderResponse> getOrder(@PathVariable String orderId) {
        logger.info("GET - Fetching order: {}", orderId);

        Optional<Order> orderOpt = orderRepository.findById(orderId);
        if (orderOpt.isEmpty()) {
            logger.warn("Order not found: {}", orderId);
            return ResponseEntity.notFound().build();
        }

        Order order = orderOpt.get();
        List<OrderItem> items = orderItemRepository.findByOrderOrderId(orderId);
        OrderResponse response = buildOrderResponse(order, items);

        return ResponseEntity.ok(response);
    }

    // GET /api/orders/user/{userId} - HISTÓRICO

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<OrderResponse>> getUserOrders(@PathVariable Long userId) {
        logger.info("GET - Fetching orders for user: {}", userId);

        List<Order> orders = orderRepository.findByUserUserIdOrderByCreatedAtDesc(userId);
        List<OrderResponse> responses = new ArrayList<>();

        for (Order order : orders) {
            List<OrderItem> items = orderItemRepository.findByOrderOrderId(order.getOrderId());
            responses.add(buildOrderResponse(order, items));
        }

        logger.info("Found {} orders for user {}", orders.size(), userId);
        return ResponseEntity.ok(responses);
    }

    // PATCH /api/orders/{orderId}/status - ATUALIZAR STATUS

    @PatchMapping("/{orderId}/status")
    public ResponseEntity<OrderResponse> updateOrderStatus(
            @PathVariable String orderId,
            @RequestBody UpdateOrderStatusRequest request) {

        logger.info("PATCH - Updating order {} status", orderId);

        Optional<Order> orderOpt = orderRepository.findById(orderId);
        if (orderOpt.isEmpty()) {
            logger.warn("Order not found: {}", orderId);
            return ResponseEntity.notFound().build();
        }

        Order order = orderOpt.get();

        // Atualizar status se fornecido
        if (request.getOrderStatus() != null) {
            order.setOrderStatus(request.getOrderStatus());
            logger.info("Order status → {}", request.getOrderStatus());
        }

        if (request.getPaymentStatus() != null) {
            order.setPaymentStatus(request.getPaymentStatus());
            logger.info("Payment status → {}", request.getPaymentStatus());
        }

        Order updated = orderRepository.save(order);
        List<OrderItem> items = orderItemRepository.findByOrderOrderId(orderId);

        logger.info("Order updated: {}", orderId);
        return ResponseEntity.ok(buildOrderResponse(updated, items));
    }

    // HELPER METHODS

    private String generateOrderId() {
        long timestamp = Instant.now().getEpochSecond();
        String suffix = generateRandomString(4);
        return "ORD" + timestamp + suffix;
    }

    private String generateRandomString(int length) {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append(chars.charAt((int) (Math.random() * chars.length())));
        }
        return sb.toString();
    }

    private String generateQRCodeData(String orderId, User user, BigDecimal totalPrice, int itemCount) {
        return String.format(
                "{\"type\":\"IADE_GO_PAYMENT\",\"order_id\":\"%s\",\"user_id\":%d,\"student_number\":\"%s\",\"total_price\":%.2f,\"item_count\":%d,\"timestamp\":\"%s\"}",
                orderId,
                user.getUserId(),
                user.getStudentNumber(),
                totalPrice.doubleValue(),
                itemCount,
                LocalDateTime.now()
        );
    }

    private OrderResponse buildOrderResponse(Order order, List<OrderItem> items) {
        OrderResponse response = new OrderResponse();
        response.setOrderId(order.getOrderId());
        response.setUserId(order.getUser().getUserId());
        response.setUserName(order.getUser().getFullName());
        response.setTotalPrice(order.getTotalPrice());
        response.setItemCount(order.getItemCount());
        response.setOrderStatus(order.getOrderStatus());
        response.setPaymentStatus(order.getPaymentStatus());
        response.setQrCodeData(order.getQrCodeData());
        response.setCreatedAt(order.getCreatedAt());

        List<OrderItemResponse> itemResponses = new ArrayList<>();
        for (OrderItem item : items) {
            OrderItemResponse itemResp = new OrderItemResponse();
            itemResp.setItemId(item.getMenuItem().getItemId());
            itemResp.setItemName(item.getMenuItem().getItemName());
            itemResp.setQuantity(item.getQuantity());
            itemResp.setUnitPrice(item.getUnitPrice());
            itemResp.setSubtotal(item.getSubtotal());
            itemResponses.add(itemResp);
        }
        response.setItems(itemResponses);

        return response;
    }
}