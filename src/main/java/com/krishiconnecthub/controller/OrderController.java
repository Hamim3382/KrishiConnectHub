package com.krishiconnecthub.controller;

import com.krishiconnecthub.dto.OrderDTO;
import com.krishiconnecthub.model.Order;
import com.krishiconnecthub.model.User;
import com.krishiconnecthub.service.OrderService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

//import java.util.Collections;
import java.util.List;

@Controller
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/order/{productId}")
    public String placeOrder(@PathVariable Long productId, @RequestParam Integer quantity, HttpSession session, RedirectAttributes redirectAttributes) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            return "redirect:/login";
        }
        if (!"BUYER".equalsIgnoreCase(loggedInUser.getRole())) {
            redirectAttributes.addFlashAttribute("errorMessage", "Only buyers can place orders.");
            return "redirect:/marketplace";
        }

        try {
            OrderDTO orderDTO = new OrderDTO();
            orderDTO.setProductId(productId);
            orderDTO.setBuyerId(loggedInUser.getId());
            orderDTO.setQuantity(quantity);
            orderService.placeOrder(orderDTO);
            redirectAttributes.addFlashAttribute("successMessage", "Order placed successfully!");
            return "redirect:/orders";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Could not place order: " + e.getMessage());
            return "redirect:/marketplace";
        }
    }

    @GetMapping("/orders")
    public String showOrderHistory(Model model, HttpSession session) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            return "redirect:/login";
        }

        List<Order> orders = orderService.getOrderHistory(loggedInUser.getId());
        model.addAttribute("orders", orders);
        return "order-history"; // Thymeleaf view name
    }
}