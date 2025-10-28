package shop.controllers;

import shop.model.Order;
import shop.model.Cart;
import shop.model.CartItem;
import shop.repositories.OrderRepository;
import shop.repositories.ProductRepository;
import java.util.List;
import java.util.regex.Pattern;

public class OrderController {
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private static final Pattern EMAIL_PATTERN = 
        Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");
    private static final Pattern PHONE_PATTERN = 
        Pattern.compile("^\\+?[0-9]{8,}$");

    public OrderController(OrderRepository orderRepository, ProductRepository productRepository) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
    }

    public Order createOrder(String customerName, String customerEmail, String customerPhone,
                           String shippingAddress, Cart cart) {
        // Validate inputs
        validateOrderData(customerName, customerEmail, customerPhone, shippingAddress);
        
        if (cart.getItems().isEmpty()) {
            throw new IllegalArgumentException("Cart cannot be empty");
        }

        // Check stock availability and update stock
        for (CartItem item : cart.getItems()) {
            if (item.getQuantity() > item.getProduct().getStockQuantity()) {
                throw new IllegalStateException("Insufficient stock for product: " + 
                                             item.getProduct().getName());
            }
        }

        // Create and save order
        Order order = new Order(customerName, customerEmail, customerPhone, 
                              shippingAddress, cart.getItems());
        order = orderRepository.save(order);

        // Update stock levels
        updateStockLevels(cart.getItems());

        return order;
    }

    public Order updateOrderStatus(Long orderId, String newStatus) {
        Order order = orderRepository.findById(orderId);
        if (order == null) {
            throw new IllegalArgumentException("Order not found with id: " + orderId);
        }

        validateOrderStatus(newStatus);
        order.setStatus(newStatus);
        return orderRepository.update(order);
    }

    public Order getOrderById(Long id) {
        Order order = orderRepository.findById(id);
        if (order == null) {
            throw new IllegalArgumentException("Order not found with id: " + id);
        }
        return order;
    }

    public List<Order> listOrders() {
        return orderRepository.findAll();
    }

    public Order cancelOrder(Long orderId) {
        Order order = orderRepository.findById(orderId);
        if (order == null) {
            throw new IllegalArgumentException("Order not found with id: " + orderId);
        }

        if (!"PENDING".equals(order.getStatus())) {
            throw new IllegalStateException("Can only cancel orders in PENDING status");
        }

        // Restore stock levels
        for (CartItem item : order.getItems()) {
            productRepository.findById(item.getProduct().getId()).setStockQuantity(
                item.getProduct().getStockQuantity() + item.getQuantity()
            );
        }

        order.setStatus("CANCELLED");
        return orderRepository.update(order);
    }

    private void updateStockLevels(List<CartItem> items) {
        for (CartItem item : items) {
            item.getProduct().setStockQuantity(
                item.getProduct().getStockQuantity() - item.getQuantity()
            );
            productRepository.update(item.getProduct());
        }
    }

    private void validateOrderData(String customerName, String customerEmail,
                                 String customerPhone, String shippingAddress) {
        if (customerName == null || customerName.trim().isEmpty()) {
            throw new IllegalArgumentException("Customer name cannot be empty");
        }
        if (customerEmail == null || !EMAIL_PATTERN.matcher(customerEmail).matches()) {
            throw new IllegalArgumentException("Invalid email format");
        }
        if (customerPhone == null || !PHONE_PATTERN.matcher(customerPhone).matches()) {
            throw new IllegalArgumentException("Invalid phone number format");
        }
        if (shippingAddress == null || shippingAddress.trim().isEmpty()) {
            throw new IllegalArgumentException("Shipping address cannot be empty");
        }
    }

    private void validateOrderStatus(String status) {
        if (status == null || status.trim().isEmpty()) {
            throw new IllegalArgumentException("Status cannot be empty");
        }
        if (!List.of("PENDING", "PROCESSING", "SHIPPED", "DELIVERED", "CANCELLED")
                .contains(status.toUpperCase())) {
            throw new IllegalArgumentException("Invalid order status");
        }
    }
}
