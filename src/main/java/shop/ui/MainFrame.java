package shop.ui;

import javax.swing.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.awt.*;
import shop.controllers.ProductController;
import shop.repositories.ProductRepository;

public class MainFrame extends JFrame {
    private final ProductController productController;
    
    public MainFrame(ProductController productController) {
        super("ElectroShop");
        this.productController = productController;
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 650);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // navbar
        JPanel topContainer = new JPanel(new BorderLayout());
        topContainer.add(new NavbarPanel(), BorderLayout.NORTH);
        topContainer.add(new SearchPanel(), BorderLayout.SOUTH);
        add(topContainer, BorderLayout.NORTH);

        // Catalogue 
        add(new CategoryPanel(), BorderLayout.WEST);

        // products
        ProductCardPanel productPanel = new ProductCardPanel(productController);
        add(productPanel, BorderLayout.CENTER);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                // Create database connection
                Connection connection = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/your_database", 
                    "username", 
                    "password"
                );
                
                // Create repository and controller
                ProductRepository productRepository = new ProductRepository(connection);
                ProductController productController = new ProductController(productRepository);
                
                // Create and show frame
                MainFrame frame = new MainFrame(productController);
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, 
                    "Error connecting to database: " + e.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}