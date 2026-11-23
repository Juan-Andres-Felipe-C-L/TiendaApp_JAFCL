package com.JuanAndresFelipe.TiendaApp.dto;

import com.JuanAndresFelipe.TiendaApp.enums.OrderStatusEnum;

import lombok.Data;

@Data 
public class ChangeOrdersStatusDTO {
    
    private OrderStatusEnum status;
}
