package com.example.milktea.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderResponse {
    private Long orderId;
    private String customerEmail;
    private LocalDateTime createdDate;
    private String status;
    private BigDecimal totalMoney;
    private List<ItemDetail> items;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ItemDetail {
        private Long productId;
        private String productName;
        private Integer quantity;
        private BigDecimal priceBuy;
        private BigDecimal subTotal;
    }
}