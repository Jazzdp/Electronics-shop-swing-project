package shop.ui;

import javax.swing.*;
import javax.swing.border.AbstractBorder;
import javax.swing.border.EmptyBorder;

import java.sql.Connection;
import java.sql.DriverManager;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

import shop.controllers.ProductController;
import shop.repositories.ProductRepository;
import shop.ui.SearchPanel;

public class MainFrame extends JFrame {
    private final ProductController productController;
    
    public MainFrame(ProductController productController) {
        super("ElectroShop");
        this.productController = productController;
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        // navbar
        JPanel topContainer = new JPanel(new BorderLayout());
        topContainer.add(new NavbarPanel(), BorderLayout.NORTH);
        topContainer.add( new SearchPanel(), BorderLayout.SOUTH);
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
                    "jdbc:mysql://localhost:3306/electronic_shop", 
                    "root", 
                    "Rayane#2004"
                );
                
                // Create repository and controller
                ProductRepository productRepository = new ProductRepository(connection);
                ProductController productController = new ProductController(productRepository);
                
                // Create and show frame
                MainFrame frame = new MainFrame(productController);
                frame.pack();
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