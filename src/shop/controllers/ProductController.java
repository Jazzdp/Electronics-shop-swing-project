package shop.controllers;

import shop.model.Product;
import shop.repositories.ProductRepository;
import java.util.List;

public class ProductController {
    private final ProductRepository productRepository;

    public ProductController(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Product addProduct(String picUrl, String name,String category, String modelNumber, 
                            int warrantyMonths, double price, String description, 
                            int stockQuantity) {
        validateProductData(name, modelNumber, warrantyMonths, price, stockQuantity);
        
        Product product = new Product(picUrl, name, category, modelNumber, warrantyMonths, 
                                    price, description, stockQuantity);
        return productRepository.save(product);
    }

    public Product updateProduct(Long id, String picUrl, String name, String category ,String modelNumber, 
                               int warrantyMonths, double price, String description, 
                               int stockQuantity) {
        validateProductData(name, modelNumber, warrantyMonths, price, stockQuantity );
        
        Product product = productRepository.findById(id);
        if (product == null) {
            throw new IllegalArgumentException("Product not found with id: " + id);
        }

        product.setPicUrl(picUrl);
        product.setName(name);
        product.setCategory(category);
        product.setModelNumber(modelNumber);
        product.setWarrantyMonths(warrantyMonths);
        product.setPrice(price);
        product.setDescription(description);
        product.setStockQuantity(stockQuantity);

        return productRepository.update(product);
    }

    public void deleteProduct(Long id) {
        Product product = productRepository.findById(id);
        if (product == null) {
            throw new IllegalArgumentException("Product not found with id: " + id);
        }
        productRepository.delete(id);
    }

    public Product getProductById(Long id) {
        Product product = productRepository.findById(id);
        if (product == null) {
            throw new IllegalArgumentException("Product not found with id: " + id);
        }
        return product;
    }

    public List<Product> listProducts() {
        return productRepository.findAll();
    }

    public boolean updateStock(Long productId, int quantity) {
        Product product = productRepository.findById(productId);
        if (product == null) {
            throw new IllegalArgumentException("Product not found with id: " + productId);
        }

        int newStock = product.getStockQuantity() + quantity;
        if (newStock < 0) {
            return false;
        }

        product.setStockQuantity(newStock);
        productRepository.update(product);
        return true;
    }

    private void validateProductData(String name, String modelNumber, 
                                   int warrantyMonths, double price, 
                                   int stockQuantity) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Product name cannot be empty");
        }
        if (modelNumber == null || modelNumber.trim().isEmpty()) {
            throw new IllegalArgumentException("Model number cannot be empty");
        }
        if (warrantyMonths < 0) {
            throw new IllegalArgumentException("Warranty months cannot be negative");
        }
        if (price <= 0) {
            throw new IllegalArgumentException("Price must be greater than zero");
        }
        if (stockQuantity < 0) {
            throw new IllegalArgumentException("Stock quantity cannot be negative");
        }
    }
}
