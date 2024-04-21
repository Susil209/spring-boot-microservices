package com.spring.microservices.orderservice.service;

import com.spring.microservices.orderservice.client.InventoryClient;
import com.spring.microservices.orderservice.model.Order;
import com.spring.microservices.orderservice.repository.OrderRepository;
import com.spring.microservices.orderservice.request.OrderRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;
    private final InventoryClient inventoryClient;

    public void placeOrder(OrderRequest orderRequest){

        boolean inStock = inventoryClient.isInStock(orderRequest.skuCode(), orderRequest.quantity());

        if(inStock){
            var order = mapToOrder(orderRequest);
            orderRepository.save(order);
        }else{
            throw new RuntimeException("Product with sku-code " + orderRequest.skuCode() + "is not in stock");
        }

    }

    private static Order mapToOrder(OrderRequest orderRequest) {
        Order order = new Order();
        order.setOrderNumber(UUID.randomUUID().toString());
        order.setPrice(orderRequest.price());
        order.setQuantity(orderRequest.quantity());
        order.setSkuCode(orderRequest.skuCode());
        return order;
    }
}
