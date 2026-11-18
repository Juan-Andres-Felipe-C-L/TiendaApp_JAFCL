package com.JuanAndresFelipe.TiendaApp.service;

import org.springframework.stereotype.Service;

import com.JuanAndresFelipe.TiendaApp.repository.OrdersRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrdersService {
    
    private final OrdersRepository ordersRepository;
}
