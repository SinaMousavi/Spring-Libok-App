package com.libok.springlibrarymallapp.repository;

import com.libok.springlibrarymallapp.model.order.OrderDetails;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderDetailsRepository extends JpaRepository<OrderDetails, Long> {
}
