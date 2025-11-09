package com.iade.iadego.repository;

import com.iade.iadego.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, String> {

    List<Order> findByUserUserIdOrderByCreatedAtDesc(Long userId);

    List<Order> findByOrderStatusOrderByCreatedAtDesc(String orderStatus);

    List<Order> findByPaymentStatusOrderByCreatedAtDesc(String paymentStatus);
}