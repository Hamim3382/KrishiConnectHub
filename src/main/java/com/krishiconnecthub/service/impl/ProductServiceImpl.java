package com.krishiconnecthub.service.impl;

import com.krishiconnecthub.dto.ProductDTO;
import com.krishiconnecthub.model.Product;
import com.krishiconnecthub.model.User;
import com.krishiconnecthub.repository.ProductRepository;
import com.krishiconnecthub.repository.UserRepository;
import com.krishiconnecthub.service.ProductService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    public ProductServiceImpl(ProductRepository productRepository, UserRepository userRepository) {
        this.productRepository = productRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Product createProduct(ProductDTO productDTO) {
        User farmer = userRepository.findById(productDTO.getFarmerId())
                .orElseThrow(() -> new IllegalArgumentException("Farmer not found with ID: " + productDTO.getFarmerId()));

        Product product = new Product();
        product.setName(productDTO.getName());
        product.setCategory(productDTO.getCategory());
        product.setPrice(productDTO.getPrice());
        product.setQuantity(productDTO.getQuantity());
        product.setLocation(productDTO.getLocation());
        product.setFarmer(farmer);

        return productRepository.save(product);
    }

    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public Optional<Product> getProductById(Long id) {
        return productRepository.findById(id);
    }

    @Override
    public void deleteProduct(Long id) {
        if (!productRepository.existsById(id)) {
            throw new IllegalArgumentException("Product not found with ID: " + id);
        }
        productRepository.deleteById(id);
    }
}