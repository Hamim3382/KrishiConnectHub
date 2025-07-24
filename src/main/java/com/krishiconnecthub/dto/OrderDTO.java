package com.krishiconnecthub.dto;

public class OrderDTO {
    private Long buyerId;
    private Long productId;
    private Integer quantity;

    // Getters and Setters
    public Long getBuyerId() { return buyerId; }
    public void setBuyerId(Long buyerId) { this.buyerId = buyerId; }
    public Long getProductId() { return productId; }
    public void setProductId(Long productId) { this.productId = productId; }
    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }
}