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
import java.util.concurrent.atomic.AtomicReference;
// product model not referenced directly in this frame
import shop.repositories.ProductRepository;
import shop.ui.SearchPanel;
import shop.ui.NavbarPanel;
import shop.ui.CategoryPanel;

public class MainFrame extends JFrame {
    private final ProductController productController;
    private javax.swing.Timer searchTimer;
    
    public MainFrame(ProductController productController) {
        super("ElectroShop");
        this.productController = productController;
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        
        JPanel container = new JPanel(new BorderLayout());
       container.setBorder(new EmptyBorder(0, 0, 0, 0));
        // navbar
        JPanel topContainer = new JPanel(new BorderLayout());
        topContainer.add(new NavbarPanel(), BorderLayout.NORTH);
        SearchPanel searchPanel = new SearchPanel();
        topContainer.add(searchPanel, BorderLayout.SOUTH);

        // Catalogue

        // Use the real ProductCardPanel which renders cards
        ProductCardPanel productPanel = new ProductCardPanel(productController);
        ProductListPanel listPanel = new ProductListPanel(productController);
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