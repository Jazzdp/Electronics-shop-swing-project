package shop.repositories;

import shop.model.Order;
import shop.model.CartItem;
import shop.model.Product;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderRepository {
    private final Connection connection;
    private final ProductRepository productRepository;

    public OrderRepository(Connection connection, ProductRepository productRepository) {
        this.connection = connection;
        this.productRepository = productRepository;
    }

    public Order save(Order order) {
        String orderSql = "INSERT INTO orders (customer_name, customer_email, customer_phone, " +
                         "shipping_address, order_date, status, total) VALUES (?, ?, ?, ?, ?, ?, ?)";
        
        try {
            connection.setAutoCommit(false);
            
            try (PreparedStatement orderStmt = connection.prepareStatement(orderSql, Statement.RETURN_GENERATED_KEYS)) {
                orderStmt.setString(1, order.getCustomerName());
                orderStmt.setString(2, order.getCustomerEmail());
                orderStmt.setString(3, order.getCustomerPhone());
                orderStmt.setString(4, order.getShippingAddress());
                orderStmt.setTimestamp(5, new Timestamp(order.getOrderDate().getTime()));
                orderStmt.setString(6, order.getStatus());
                orderStmt.setDouble(7, order.getTotal());

                int affected = orderStmt.executeUpdate();
                if (affected == 0) {
                    throw new SQLException("Creating order failed, no rows affected.");
                }

                try (ResultSet generatedKeys = orderStmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        order.setId(generatedKeys.getLong(1));
                    } else {
                        throw new SQLException("Creating order failed, no ID obtained.");
                    }
                }
            }

            // Save order items
            saveOrderItems(order);
            
            connection.commit();
            return order;
            
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException rollbackEx) {
                throw new RuntimeException("Error rolling back transaction", rollbackEx);
            }
            throw new RuntimeException("Error saving order", e);
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                throw new RuntimeException("Error resetting auto-commit", e);
            }
        }
    }

    private void saveOrderItems(Order order) throws SQLException {
        String itemSql = "INSERT INTO order_items (order_id, product_id, quantity, price) VALUES (?, ?, ?, ?)";
        
        try (PreparedStatement itemStmt = connection.prepareStatement(itemSql)) {
            for (CartItem item : order.getItems()) {
                itemStmt.setLong(1, order.getId());
                itemStmt.setLong(2, item.getProduct().getId());
                itemStmt.setInt(3, item.getQuantity());
                itemStmt.setDouble(4, item.getProduct().getPrice());
                itemStmt.addBatch();
            }
            itemStmt.executeBatch();
        }
    }

    public Order update(Order order) {
        String sql = "UPDATE orders SET customer_name = ?, customer_email = ?, " +
                    "customer_phone = ?, shipping_address = ?, status = ?, total = ? " +
                    "WHERE id = ?";
                    
        try {
            connection.setAutoCommit(false);
            
            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.setString(1, order.getCustomerName());
                stmt.setString(2, order.getCustomerEmail());
                stmt.setString(3, order.getCustomerPhone());
                stmt.setString(4, order.getShippingAddress());
                stmt.setString(5, order.getStatus());
                stmt.setDouble(6, order.getTotal());
                stmt.setLong(7, order.getId());

                int affected = stmt.executeUpdate();
                if (affected == 0) {
                    throw new SQLException("Updating order failed, no rows affected.");
                }

                // Delete existing items
                deleteOrderItems(order.getId());
                
                // Save new items
                saveOrderItems(order);
            }
            
            connection.commit();
            return order;
            
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException rollbackEx) {
                throw new RuntimeException("Error rolling back transaction", rollbackEx);
            }
            throw new RuntimeException("Error updating order", e);
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                throw new RuntimeException("Error resetting auto-commit", e);
            }
        }
    }

    private void deleteOrderItems(Long orderId) throws SQLException {
        String sql = "DELETE FROM order_items WHERE order_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, orderId);
            stmt.executeUpdate();
        }
    }

    public Order findById(Long id) {
        String sql = "SELECT o.*, oi.product_id, oi.quantity, oi.price " +
                    "FROM orders o " +
                    "LEFT JOIN order_items oi ON o.id = oi.order_id " +
                    "WHERE o.id = ?";
                    
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (!rs.next()) {
                    return null;
                }
                
                Order order = mapResultSetToOrder(rs);
                do {
                    if (rs.getLong("product_id") != 0) {
                        CartItem item = mapResultSetToOrderItem(rs);
                        order.getItems().add(item);
                    }
                } while (rs.next());
                
                return order;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding order by ID", e);
        }
    }

    public List<Order> findAll() {
        String sql = "SELECT o.*, oi.product_id, oi.quantity, oi.price " +
                    "FROM orders o " +
                    "LEFT JOIN order_items oi ON o.id = oi.order_id " +
                    "ORDER BY o.order_date DESC";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            List<Order> orders = new ArrayList<>();
            Long currentOrderId = null;
            Order currentOrder = null;
            
            while (rs.next()) {
                Long orderId = rs.getLong("id");
                if (!orderId.equals(currentOrderId)) {
                    currentOrder = mapResultSetToOrder(rs);
                    orders.add(currentOrder);
                    currentOrderId = orderId;
                }
                
                if (rs.getLong("product_id") != 0) {
                    CartItem item = mapResultSetToOrderItem(rs);
                    currentOrder.getItems().add(item);
                }
            }
            return orders;
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching all orders", e);
        }
    }

    private Order mapResultSetToOrder(ResultSet rs) throws SQLException {
        Order order = new Order();
        order.setId(rs.getLong("id"));
        order.setCustomerName(rs.getString("customer_name"));
        order.setCustomerEmail(rs.getString("customer_email"));
        order.setCustomerPhone(rs.getString("customer_phone"));
        order.setShippingAddress(rs.getString("shipping_address"));
        order.setOrderDate(rs.getTimestamp("order_date"));
        order.setStatus(rs.getString("status"));
        return order;
    }

    private CartItem mapResultSetToOrderItem(ResultSet rs) throws SQLException {
        Long productId = rs.getLong("product_id");
        Product product = productRepository.findById(productId);
        int quantity = rs.getInt("quantity");
        return new CartItem(product, quantity);
    }
}
