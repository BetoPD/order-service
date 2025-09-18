package com.manufacturas.order.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.manufacturas.order.model.Order;
import com.manufacturas.order.model.OrderDetails;
import com.manufacturas.order.model.Product;
import com.manufacturas.order.repository.OrderRepository;
import com.manufacturas.order.repository.OrderDetailsRepository;
import com.manufacturas.order.service.client.ProductFeignClient;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderDetailsRepository orderDetailsRepository;

    @Autowired
    ProductFeignClient productFeignClient;

    public Order createOrder(List<OrderDetails> orderDetails) {
        Order order = new Order();
        Order savedOrder = orderRepository.save(order);

        orderDetails.forEach(detail -> {
            detail.setOrder(savedOrder);
        });

        orderDetails.forEach(detail -> {
            productFeignClient.updateStock(detail.getProductId(), detail.getQuantity());
        });

        List<OrderDetails> savedOrderDetails = orderDetailsRepository.saveAll(orderDetails);

        savedOrder.setOrderDetails(savedOrderDetails);

        return savedOrder;
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public Order getOrderById(Long id) {
        Order order = orderRepository.findById(id).orElse(null);

        if (order != null) {
            order.getOrderDetails().forEach(orderDetail -> {
                Product product = productFeignClient.getProduct(orderDetail.getProductId());
                orderDetail.setProduct(product);
            });
        }

        return order;
    }

    public Order updateOrder(Long id, Order order) {
        return orderRepository.save(order);
    }

    public void deleteOrder(Long id) {
        orderRepository.deleteById(id);
    }

}
