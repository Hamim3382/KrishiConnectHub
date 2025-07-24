package com.krishiconnecthub.service;

import com.krishiconnecthub.dto.OrderDTO;
import com.krishiconnecthub.model.Order;

import java.util.List;

public interface OrderService {
    /**
     * Places a new order.
     * @param orderDTO The order data transfer object.
     * @return The created order.
     */
    Order placeOrder(OrderDTO orderDTO);

    /**
     * Fetches the order history for a specific buyer.
     * @param buyerId The ID of the buyer.
     * @return A list of orders for the buyer.
     */
    List<Order> getOrderHistory(Long buyerId);
}