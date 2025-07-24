package com.krishiconnecthub.service;

import com.krishiconnecthub.dto.ProductDTO;
import com.krishiconnecthub.model.Product;

import java.util.List;
import java.util.Optional;

public interface ProductService {
    /**
     * Creates a new product.
     * @param productDTO The product data transfer object.
     * @return The created product.
     */
    Product createProduct(ProductDTO productDTO);

    /**
     * Retrieves all products.
     * @return A list of all products.
     */
    List<Product> getAllProducts();

    /**
     * Finds a product by its ID.
     * @param id The ID of the product.
     * @return An Optional containing the product if found, otherwise empty.
     */
    Optional<Product> getProductById(Long id);

    /**
     * Deletes a product by its ID.
     * @param id The ID of the product to delete.
     */
    void deleteProduct(Long id);
}