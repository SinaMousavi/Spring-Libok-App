package com.libok.springlibrarymallapp.repository;

import com.libok.springlibrarymallapp.model.order.Order;
import com.libok.springlibrarymallapp.model.order.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findAll();
    List<Order> findAllByStatus(OrderStatus orderStatus);
}