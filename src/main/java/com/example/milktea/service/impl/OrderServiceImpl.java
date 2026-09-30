package com.example.milktea.service.impl;

import com.example.milktea.dto.OrderItemRequest;
import com.example.milktea.dto.OrderRequest;
import com.example.milktea.dto.OrderResponse;
import com.example.milktea.dto.UpdateOrderStatusRequest;
import com.example.milktea.entity.Order;
import com.example.milktea.entity.OrderItem;
import com.example.milktea.entity.Product;
import com.example.milktea.entity.User;
import com.example.milktea.repository.OrderRepository;
import com.example.milktea.repository.ProductRepository;
import com.example.milktea.repository.UserRepository;
import com.example.milktea.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    public OrderServiceImpl(OrderRepository orderRepository,
                            ProductRepository productRepository,
                            UserRepository userRepository) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public OrderResponse createOrder(String customerEmail, OrderRequest request) {
        User user = userRepository.findByEmail(customerEmail)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy khách hàng!"));

        Order order = Order.builder()
                .user(user)
                .createdDate(LocalDateTime.now())
                .status("PENDING")
                .totalMoney(BigDecimal.ZERO)
                .orderItems(new ArrayList<>())
                .build();

        BigDecimal total = BigDecimal.ZERO;

        for (OrderItemRequest itemReq : request.getItems()) {
            Product product = productRepository.findById(itemReq.getProductId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy sản phẩm ID: " + itemReq.getProductId()));

            OrderItem orderItem = OrderItem.builder()
                    .order(order)
                    .product(product)
                    .quantity(itemReq.getQuantity())
                    .priceBuy(product.getPrice())
                    .build();

            order.getOrderItems().add(orderItem);

            BigDecimal itemTotal = product.getPrice().multiply(BigDecimal.valueOf(itemReq.getQuantity()));
            total = total.add(itemTotal);
        }

        order.setTotalMoney(total);
        Order savedOrder = orderRepository.save(order);

        return mapToResponse(savedOrder);
    }

    @Override
    public List<OrderResponse> getMyOrders(String customerEmail) {
        User user = userRepository.findByEmail(customerEmail)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy khách hàng!"));

        return orderRepository.findByUserOrderByCreatedDateDesc(user)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<OrderResponse> getAllOrders() {
        return orderRepository.findAllByOrderByCreatedDateDesc()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public OrderResponse updateOrderStatus(Long orderId, UpdateOrderStatusRequest request) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy đơn hàng với ID: " + orderId));

        order.setStatus(request.getStatus().toUpperCase());
        Order updatedOrder = orderRepository.save(order);

        return mapToResponse(updatedOrder);
    }

    private OrderResponse mapToResponse(Order order) {
        List<OrderResponse.ItemDetail> itemDetails = order.getOrderItems().stream()
                .map(item -> OrderResponse.ItemDetail.builder()
                        .productId(item.getProduct().getId())
                        .productName(item.getProduct().getName())
                        .quantity(item.getQuantity())
                        .priceBuy(item.getPriceBuy())
                        .subTotal(item.getPriceBuy().multiply(BigDecimal.valueOf(item.getQuantity())))
                        .build())
                .collect(Collectors.toList());

        return OrderResponse.builder()
                .orderId(order.getId())
                .customerEmail(order.getUser().getEmail())
                .createdDate(order.getCreatedDate())
                .status(order.getStatus())
                .totalMoney(order.getTotalMoney())
                .items(itemDetails)
                .build();
    }
}