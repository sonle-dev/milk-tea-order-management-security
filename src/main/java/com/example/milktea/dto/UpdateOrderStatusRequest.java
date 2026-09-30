package com.example.milktea.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateOrderStatusRequest {

    @NotBlank(message = "Trạng thái không được để trống")
    private String status; // PENDING, CONFIRMED, DELIVERING, COMPLETED, CANCELLED
}