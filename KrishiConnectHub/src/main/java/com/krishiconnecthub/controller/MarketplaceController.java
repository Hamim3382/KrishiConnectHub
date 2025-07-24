package com.krishiconnecthub.controller;

import com.krishiconnecthub.dto.ProductDTO;
import com.krishiconnecthub.model.Product;
import com.krishiconnecthub.model.User;
import com.krishiconnecthub.service.ProductService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class MarketplaceController {

    private final ProductService productService;

    public MarketplaceController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/marketplace")
    public String showMarketplace(Model model) {
        List<Product> products = productService.getAllProducts();
        model.addAttribute("products", products);
        return "marketplace"; // Thymeleaf view name
    }

    @GetMapping("/product/add")
    public String showAddProductForm(Model model, HttpSession session, RedirectAttributes redirectAttributes) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            return "redirect:/login";
        }
        if (!"FARMER".equalsIgnoreCase(loggedInUser.getRole())) {
            redirectAttributes.addFlashAttribute("errorMessage", "Only farmers can add products.");
            return "redirect:/marketplace";
        }
        model.addAttribute("product", new ProductDTO());
        return "add-product"; // Thymeleaf view name
    }

    @PostMapping("/product/add")
    public String processAddProduct(@ModelAttribute("product") ProductDTO productDTO, HttpSession session, RedirectAttributes redirectAttributes) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            return "redirect:/login";
        }

        try {
            productDTO.setFarmerId(loggedInUser.getId());
            productService.createProduct(productDTO);
            redirectAttributes.addFlashAttribute("successMessage", "Product added successfully!");
            return "redirect:/marketplace";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error adding product: " + e.getMessage());
            return "redirect:/product/add";
        }
    }
}