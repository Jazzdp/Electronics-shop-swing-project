package shop.repositories;

import shop.model.CartItem;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CartRepository {
    private final Connection connection;
    private final ProductRepository productRepository;

    public CartRepository(Connection connection, ProductRepository productRepository) {
        this.connection = connection;
        this.productRepository = productRepository;
    }

    public void saveCartItem(Long userId, CartItem item) {
        String sql = "INSERT INTO cart_items (user_id, product_id, quantity) VALUES (?, ?, ?)";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, userId);
            stmt.setLong(2, item.getProduct().getId());
            stmt.setInt(3, item.getQuantity());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error saving cart item", e);
        }
    }

    public void updateCartItemQuantity(Long userId, Long productId, int quantity) {
        String sql = "UPDATE cart_items SET quantity = ? WHERE user_id = ? AND product_id = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, quantity);
            stmt.setLong(2, userId);
            stmt.setLong(3, productId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error updating cart item quantity", e);
        }
    }

    public void removeCartItem(Long userId, Long productId) {
        String sql = "DELETE FROM cart_items WHERE user_id = ? AND product_id = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, userId);
            stmt.setLong(2, productId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error removing cart item", e);
        }
    }

    public void clearCart(Long userId) {
        String sql = "DELETE FROM cart_items WHERE user_id = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, userId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error clearing cart", e);
        }
    }

    public List<CartItem> getCartItems(Long userId) {
        String sql = "SELECT ci.product_id, ci.quantity FROM cart_items ci WHERE ci.user_id = ?";
        List<CartItem> items = new ArrayList<>();
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, userId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    CartItem item = new CartItem();
                    item.setProduct(productRepository.findById(rs.getLong("product_id")));
                    item.setQuantity(rs.getInt("quantity"));
                    items.add(item);
                }
            }
            return items;
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching cart items", e);
        }
    }

    public boolean existsInCart(Long userId, Long productId) {
        String sql = "SELECT COUNT(*) FROM cart_items WHERE user_id = ? AND product_id = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, userId);
            stmt.setLong(2, productId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
                return false;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error checking cart item existence", e);
        }
    }
}
