package com.example.milktea.service;

import com.example.milktea.dto.OrderRequest;
import com.example.milktea.dto.OrderResponse;
import com.example.milktea.dto.UpdateOrderStatusRequest;

import java.util.List;

public interface OrderService {
    OrderResponse createOrder(String customerEmail, OrderRequest request);
    List<OrderResponse> getMyOrders(String customerEmail);
    List<OrderResponse> getAllOrders();
    OrderResponse updateOrderStatus(Long orderId, UpdateOrderStatusRequest request);
}