package shop.repositories;

import shop.model.Product;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ProductRepository {
    private final Connection connection;

    public ProductRepository(Connection connection) {
        this.connection = connection;
    }

    public Product save(Product product) {
        String sql = "INSERT INTO products (pic_url, name,category, model_number, warranty_months, price, description, stock_quantity) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
                    
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, product.getPicUrl());
            stmt.setString(2, product.getName());
            stmt.setString(3, product.getCategory());
            stmt.setString(4, product.getModelNumber());
            stmt.setInt(5, product.getWarrantyMonths());
            stmt.setDouble(6, product.getPrice());
            stmt.setString(7, product.getDescription());
            stmt.setInt(8, product.getStockQuantity());

            int affected = stmt.executeUpdate();
            if (affected == 0) {
                throw new SQLException("Creating product failed, no rows affected.");
            }

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    product.setId(generatedKeys.getLong(1));
                    return product;
                } else {
                    throw new SQLException("Creating product failed, no ID obtained.");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error saving product", e);
        }
    }

    public Product update(Product product) {
        String sql = "UPDATE products SET pic_url = ?, name = ?,category=?, model_number = ?, " +
                    "warranty_months = ?, price = ?, description = ?, stock_quantity = ? " +
                    "WHERE id = ?";
                    
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, product.getPicUrl());
            stmt.setString(2, product.getName());
            stmt.setString(3, product.getCategory());
            stmt.setString(4, product.getModelNumber());
            stmt.setInt(5, product.getWarrantyMonths());
            stmt.setDouble(6, product.getPrice());
            stmt.setString(7, product.getDescription());
            stmt.setInt(8, product.getStockQuantity());
            stmt.setLong(9, product.getId());

            int affected = stmt.executeUpdate();
            if (affected == 0) {
                throw new SQLException("Updating product failed, no rows affected.");
            }
            return product;
        } catch (SQLException e) {
            throw new RuntimeException("Error updating product", e);
        }
    }

    public void delete(Long id) {
        String sql = "DELETE FROM products WHERE id = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting product", e);
        }
    }

    public Product findById(Long id) {
        String sql = "SELECT * FROM products WHERE id = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToProduct(rs);
                }
                return null;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding product by ID", e);
        }
    }

   public List<Product> findByCategory(String category) {
        String sql = "SELECT * FROM products WHERE category = ? ORDER BY name";
        List<Product> products = new ArrayList<>();
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, category);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    products.add(mapResultSetToProduct(rs));
                }
                return products;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding products by category", e);
        }
    }

    public List<Product> findAll() {
        String sql = "SELECT * FROM products ORDER BY name";
        List<Product> products = new ArrayList<>();
        
        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                products.add(mapResultSetToProduct(rs));
            }
            return products;
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching all products", e);
        }
    }

    private Product mapResultSetToProduct(ResultSet rs) throws SQLException {
        Product product = new Product();
        product.setId(rs.getLong("id"));
        product.setPicUrl(rs.getString("pic_url"));
        product.setName(rs.getString("name"));
        product.setCategory(rs.getString("category"));
        product.setModelNumber(rs.getString("model_number"));
        product.setWarrantyMonths(rs.getInt("warranty_months"));
        product.setPrice(rs.getDouble("price"));
        product.setDescription(rs.getString("description"));
        product.setStockQuantity(rs.getInt("stock_quantity"));
        return product;
    }
}