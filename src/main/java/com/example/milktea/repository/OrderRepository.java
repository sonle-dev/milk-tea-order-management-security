package com.example.milktea.repository;

import com.example.milktea.entity.Order;
import com.example.milktea.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUserOrderByCreatedDateDesc(User user);
    List<Order> findAllByOrderByCreatedDateDesc();
}