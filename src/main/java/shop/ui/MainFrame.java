package shop.ui;

import javax.swing.*;
import javax.swing.border.AbstractBorder;
import javax.swing.border.EmptyBorder;

import com.mysql.cj.x.protobuf.MysqlxCrud.Order;

import java.sql.Connection;
import java.sql.DriverManager;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

import shop.controllers.ProductController;
import shop.controllers.CartController;
import java.util.concurrent.atomic.AtomicReference;
// product model not referenced directly in this frame
import shop.repositories.ProductRepository;
import shop.repositories.CartRepository;
import shop.ui.SearchPanel;
import shop.ui.NavbarPanel;
import shop.ui.CategoryPanel;
import shop.controllers.OrderController;
import shop.repositories.OrderRepository;

public class MainFrame extends JFrame {
    private final ProductController productController;
    private final CartController cartController;
    private javax.swing.Timer searchTimer;
    private final OrderController orderController;
    
    public MainFrame(ProductController productController, CartController cartController, OrderController orderController ) {
        super("ElectroShop");
        this.productController = productController;
        this.cartController = cartController;
        this.orderController = orderController;
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        
        JPanel container = new JPanel(new BorderLayout());
       container.setBorder(new EmptyBorder(0, 0, 0, 0));
        // navbar
        JPanel topContainer = new JPanel(new BorderLayout());
        NavbarPanel navbarPanel = new NavbarPanel();
        topContainer.add(navbarPanel, BorderLayout.NORTH);
        SearchPanel searchPanel = new SearchPanel();
        topContainer.add(searchPanel, BorderLayout.SOUTH);

        // Catalogue

        // Use the real ProductCardPanel which renders cards
        ProductCardPanel productPanel = new ProductCardPanel(productController, cartController);
        ProductListPanel listPanel = new ProductListPanel(productController, cartController);
        // start in card view (use AtomicReference so lambda can update it)
        AtomicReference<JComponent> currentCenter = new AtomicReference<>(productPanel);
        container.add(new CategoryPanel(category -> {
            if ("All".equals(category)) {
                productPanel.loadAllProducts();
                listPanel.loadAllProducts();
            } else {
                productPanel.loadProductsByCategory(category);
                listPanel.loadProductsByCategory(category);
            }
        }), BorderLayout.WEST);
        container.add(currentCenter.get(), BorderLayout.CENTER);

        // Toggle between grid (cards) and list views
        searchPanel.getListViewButton().addActionListener(e -> {
            // use the container variable (outer scope) to swap views
            if (currentCenter.get() == productPanel) {
                container.remove(productPanel);
                container.add(listPanel, BorderLayout.CENTER);
                currentCenter.set(listPanel);
            } else {
                container.remove(listPanel);
                container.add(productPanel, BorderLayout.CENTER);
                currentCenter.set(productPanel);
            }
            container.revalidate();
            container.repaint();
        });
        
        // Wire search field to filter products with debounce
        final MainFrame mainFrame = this;
        searchPanel.getSearchField().getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            @Override
            public void insertUpdate(javax.swing.event.DocumentEvent e) {
                scheduleSearch();
            }
            @Override
            public void removeUpdate(javax.swing.event.DocumentEvent e) {
                scheduleSearch();
            }
            @Override
            public void changedUpdate(javax.swing.event.DocumentEvent e) {
                scheduleSearch();
            }
            private void scheduleSearch() {
                if (mainFrame.searchTimer != null && mainFrame.searchTimer.isRunning()) {
                    mainFrame.searchTimer.stop();
                }
                mainFrame.searchTimer = new javax.swing.Timer(300, e -> performSearch());
                mainFrame.searchTimer.setRepeats(false);
                mainFrame.searchTimer.start();
            }
            private void performSearch() {
                String query = searchPanel.getSearchField().getText().trim();
                if (query.isEmpty()) {
                    productPanel.loadAllProducts();
                    listPanel.loadAllProducts();
                } else {
                    productPanel.filterProducts(query);
                    listPanel.filterProducts(query);
                }
            }
        });
        
        container.add(topContainer, BorderLayout.NORTH);
        add(container);
        
        // Wire cart button to show cart window
        navbarPanel.getCartButton().addActionListener(e -> {
            JFrame cartWindow = new JFrame("Shopping Cart");
            cartWindow.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            cartWindow.add(new CartPanel(cartController,orderController, cartWindow));
            cartWindow.setSize(900, 600);
            cartWindow.setLocationRelativeTo(null);
            cartWindow.setVisible(true);
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                // Create database connection
                Connection connection = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/electronic_shop", 
                    "root", 
                    ""
                );
                
                // Create repositories and controllers
                ProductRepository productRepository = new ProductRepository(connection);
                CartRepository cartRepository = new CartRepository(connection, productRepository);
                ProductController productController = new ProductController(productRepository);
                CartController cartController = new CartController(cartRepository, productRepository);
                OrderRepository orderRepository = new OrderRepository(connection, productRepository);
                OrderController orderController = new OrderController(orderRepository, productRepository);
                // Create and show frame
                MainFrame frame = new MainFrame(productController, cartController,orderController);
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