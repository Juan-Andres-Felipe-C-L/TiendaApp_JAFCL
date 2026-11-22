package com.JuanAndresFelipe.TiendaApp.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.JuanAndresFelipe.TiendaApp.enums.OrderStatusEnum;

import lombok.Data;

@Data 
public class OrdersResponseDTO {
    
    private Long id;

    private Long clientId;

    private String username;

    private Long productId;

    private String productName;

    private int quantity;

    private BigDecimal total;

    private OrderStatusEnum status;

    private LocalDateTime createdAt;
}   