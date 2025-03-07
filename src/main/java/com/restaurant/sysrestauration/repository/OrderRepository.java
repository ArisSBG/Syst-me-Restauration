package com.restaurant.sysrestauration.repository;

import com.restaurant.sysrestauration.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
