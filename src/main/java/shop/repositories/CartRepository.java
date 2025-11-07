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

    public void saveCartItem(CartItem item) {
        String sql = "INSERT INTO cart_items (product_id, quantity) VALUES (?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, item.getProduct().getId());
            stmt.setInt(2, item.getQuantity());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error saving cart item", e);
        }
    }

    public void updateCartItemQuantity(Long productId, int quantity) {
        String sql = "UPDATE cart_items SET quantity = ? WHERE product_id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, quantity);
            stmt.setLong(2, productId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error updating cart item quantity", e);
        }
    }

    public void removeCartItem(Long productId) {
        String sql = "DELETE FROM cart_items WHERE product_id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, productId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error removing cart item", e);
        }
    }

    public void clearCart() {
        String sql = "DELETE FROM cart_items";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error clearing cart", e);
        }
    }

    public List<CartItem> getCartItems() {
        String sql = "SELECT ci.product_id, ci.quantity FROM cart_items ci";
        List<CartItem> items = new ArrayList<>();

        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                CartItem item = new CartItem();
                item.setProduct(productRepository.findById(rs.getLong("product_id")));
                item.setQuantity(rs.getInt("quantity"));
                items.add(item);
            }
            return items;
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching cart items", e);
        }
    }

    public boolean existsInCart(Long productId) {
        String sql = "SELECT COUNT(*) FROM cart_items WHERE product_id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, productId);

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
