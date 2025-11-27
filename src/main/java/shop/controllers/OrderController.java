package shop.controllers;

import shop.model.Order;
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

    // ✅ THIS IS THE ONLY VALID CONSTRUCTOR
    public OrderController(OrderRepository orderRepository,
                           ProductRepository productRepository) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
    }

    public Order createOrder(String customerName, String customerEmail, String customerPhone,
                             String shippingAddress, List<CartItem> items) {

        validateOrderData(customerName, customerEmail, customerPhone, shippingAddress);

        if (items == null || items.isEmpty())
            throw new IllegalArgumentException("Cart cannot be empty");

        for (CartItem item : items) {
            if (item.getQuantity() > item.getProduct().getStockQuantity())
                throw new IllegalStateException("Not enough stock for: " +
                        item.getProduct().getName());
        }

        Order order = new Order(customerName, customerEmail, customerPhone, shippingAddress, items);
        order = orderRepository.save(order);

        updateStockLevels(items);

        return order;
    }

    public Order updateOrderStatus(Long orderId, String newStatus) {
        Order order = orderRepository.findById(orderId);
        if (order == null)
            throw new IllegalArgumentException("Order not found");

        validateOrderStatus(newStatus);
        order.setStatus(newStatus);

        return orderRepository.update(order);
    }

    public Order getOrderById(Long id) {
        Order order = orderRepository.findById(id);
        if (order == null)
            throw new IllegalArgumentException("Order not found");
        return order;
    }

    public List<Order> listOrders() {
        return orderRepository.findAll();
    }

    public Order cancelOrder(Long orderId) {
        Order order = orderRepository.findById(orderId);
        if (order == null)
            throw new IllegalArgumentException("Order not found");

        if (!"PENDING".equals(order.getStatus()))
            throw new IllegalStateException("Can only cancel PENDING orders");

        for (CartItem item : order.getItems()) {
            var product = productRepository.findById(item.getProduct().getId());
            if (product == null) continue;
            product.setStockQuantity(product.getStockQuantity() + item.getQuantity());
            productRepository.update(product);
        }

        order.setStatus("CANCELLED");
        return orderRepository.update(order);
    }

    private void updateStockLevels(List<CartItem> items) {
        for (CartItem item : items) {
            var product = productRepository.findById(item.getProduct().getId());
            if (product == null) continue;
            product.setStockQuantity(product.getStockQuantity() - item.getQuantity());
            productRepository.update(product);
        }
    }

    private void validateOrderData(String name, String email, String phone, String address) {
        if (name == null || name.isBlank())
            throw new IllegalArgumentException("Name cannot be empty");

        if (email == null || !EMAIL_PATTERN.matcher(email).matches())
            throw new IllegalArgumentException("Invalid email");

        if (phone == null || !PHONE_PATTERN.matcher(phone).matches())
            throw new IllegalArgumentException("Invalid phone");

        if (address == null || address.isBlank())
            throw new IllegalArgumentException("Address cannot be empty");
    }

    private void validateOrderStatus(String status) {
        if (!List.of("PENDING", "PROCESSING", "SHIPPED", "DELIVERED", "CANCELLED")
                .contains(status.toUpperCase()))
            throw new IllegalArgumentException("Invalid status");
    }
}
