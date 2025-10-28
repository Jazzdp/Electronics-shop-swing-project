package shop.controllers;

import shop.model.CartItem;
import shop.model.Product;
import shop.repositories.CartRepository;
import shop.repositories.ProductRepository;
import java.util.List;

public class CartController {
    private final CartRepository cartRepository;
    private final ProductRepository productRepository;

    public CartController(CartRepository cartRepository, ProductRepository productRepository) {
        this.cartRepository = cartRepository;
        this.productRepository = productRepository;
    }

    public void addToCart(Long userId, Long productId, int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than zero");
        }

        Product product = productRepository.findById(productId);
        if (product == null) {
            throw new IllegalArgumentException("Product not found");
        }

        if (product.getStockQuantity() < quantity) {
            throw new IllegalStateException("Insufficient stock");
        }

        if (cartRepository.existsInCart(userId, productId)) {
            // Update existing cart item
            List<CartItem> cartItems = cartRepository.getCartItems(userId);
            for (CartItem item : cartItems) {
                if (item.getProduct().getId().equals(productId)) {
                    int newQuantity = item.getQuantity() + quantity;
                    if (product.getStockQuantity() < newQuantity) {
                        throw new IllegalStateException("Insufficient stock");
                    }
                    cartRepository.updateCartItemQuantity(userId, productId, newQuantity);
                    return;
                }
            }
        } else {
            // Add new cart item
            CartItem cartItem = new CartItem(product, quantity);
            cartRepository.saveCartItem(userId, cartItem);
        }
    }

    public void updateQuantity(Long userId, Long productId, int quantity) {
        if (quantity < 0) {
            throw new IllegalArgumentException("Quantity cannot be negative");
        }

        Product product = productRepository.findById(productId);
        if (product == null) {
            throw new IllegalArgumentException("Product not found");
        }

        if (quantity > product.getStockQuantity()) {
            throw new IllegalStateException("Insufficient stock");
        }

        if (quantity == 0) {
            removeFromCart(userId, productId);
        } else {
            cartRepository.updateCartItemQuantity(userId, productId, quantity);
        }
    }

    public void removeFromCart(Long userId, Long productId) {
        cartRepository.removeCartItem(userId, productId);
    }

    public void clearCart(Long userId) {
        cartRepository.clearCart(userId);
    }

    public List<CartItem> getCartItems(Long userId) {
        return cartRepository.getCartItems(userId);
    }

    public double getCartTotal(Long userId) {
        return getCartItems(userId).stream()
                .mapToDouble(CartItem::getSubtotal)
                .sum();
    }

    public int getItemCount(Long userId) {
        return getCartItems(userId).stream()
                .mapToInt(CartItem::getQuantity)
                .sum();
    }
}
