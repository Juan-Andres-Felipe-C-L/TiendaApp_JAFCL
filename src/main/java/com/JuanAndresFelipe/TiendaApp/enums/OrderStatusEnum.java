package com.JuanAndresFelipe.TiendaApp.enums;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor 
public enum OrderStatusEnum {
    
    PENDIENTE("PENDIENTE"),
    ENVIADO("ENVIADO"),
    ENTREGADO("ENTREGADO"),
    CANCELADO("CANCELADO");

    private final String status;

    public String getStatus(){
        return status;
    }
}
