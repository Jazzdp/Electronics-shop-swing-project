package shop.ui;
import javax.swing.border.AbstractBorder;
import javax.swing.border.EmptyBorder;

import java.sql.Connection;
import java.sql.DriverManager;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

import shop.controllers.ProductController;
import shop.model.Product;
import shop.repositories.ProductRepository;
import shop.ui.SearchPanel;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class CartPanel extends JPanel {
    private List<Product> cartItems;
    private JPanel cartItemsPanel;
    private JLabel subtotalLabel, taxLabel, totalLabel;
    private double taxRate = 0.10; // 10% tax
    
    public CartPanel() {
        this.cartItems = new ArrayList<>();
        initializeUI();
    }
    public static void main(String[]arg) {
    	JFrame frame = new JFrame();
    	frame.add(new CartPanel());
    	frame.setVisible(true);
    	frame.pack();
    }
    private void initializeUI() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        
        // Header avec "Continue Shopping"
        JPanel headerPanel = createHeaderPanel();
        
        // Section du panier (dynamique)
        JPanel cartPanel = createCartPanel();
        
        // Order Summary (dynamique)
        JPanel orderSummaryPanel = createOrderSummaryPanel();
        
        // Bouton Proceed to Checkout
        JPanel checkoutPanel = createCheckoutPanel();
        
        // Organisation verticale
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        mainPanel.add(headerPanel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        mainPanel.add(cartPanel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        mainPanel.add(orderSummaryPanel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        mainPanel.add(checkoutPanel);
        
        add(mainPanel, BorderLayout.CENTER);
        
        // Initialiser avec un panier vide
        updateCartDisplay();
    }
    
    // Méthode pour ajouter un produit au panier
    public void addProductToCart(Product product) {
        cartItems.add(product);
        updateCartDisplay();
    }
    
    // Méthode pour supprimer un produit du panier
    public void removeProductFromCart(Product product) {
        cartItems.remove(product);
        updateCartDisplay();
    }
    
    // Méthode pour vider le panier
    public void clearCart() {
        cartItems.clear();
        updateCartDisplay();
    }
    
    // Méthode pour mettre à jour l'affichage du panier
    private void updateCartDisplay() {
        // Vider le panel des items
        cartItemsPanel.removeAll();
        
        if (cartItems.isEmpty()) {
            // Panier vide
            JLabel emptyLabel = new JLabel("Your cart is empty");
            emptyLabel.setFont(new Font("Arial", Font.ITALIC, 16));
            emptyLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
            cartItemsPanel.add(emptyLabel);
        } else {
            // Afficher tous les produits
            for (Product product : cartItems) {
                JPanel productPanel = createProductPanel(product);
                cartItemsPanel.add(productPanel);
                cartItemsPanel.add(Box.createRigidArea(new Dimension(0, 10)));
            }
        }
        
        // Mettre à jour les totaux
        updateOrderSummary();
        
        // Rafraîchir l'affichage
        cartItemsPanel.revalidate();
        cartItemsPanel.repaint();
    }
    
    private void updateOrderSummary() {
        double subtotal = calculateSubtotal();
        double tax = subtotal * taxRate;
        double total = subtotal + tax;
        
        subtotalLabel.setText(String.format("$%.2f", subtotal));
        taxLabel.setText(String.format("$%.2f", tax));
        totalLabel.setText(String.format("$%.2f", total));
        
        // Mettre à jour le nombre d'items dans le label subtotal
        Component[] components = ((JPanel) subtotalLabel.getParent().getParent()).getComponents();
        for (Component comp : components) {
            if (comp instanceof JPanel) {
                JPanel rowPanel = (JPanel) comp;
                Component[] rowComponents = rowPanel.getComponents();
                if (rowComponents.length > 0 && rowComponents[0] instanceof JLabel) {
JLabel label = (JLabel) rowComponents[0];
                    if (label.getText().contains("Subtotal")) {
                        label.setText("Subtotal (" + cartItems.size() + " items):");
                    }
                }
            }
        }
    }
    
    private double calculateSubtotal() {
        double subtotal = 0;
        for (Product product : cartItems) {
            subtotal += product.getPrice();
        }
        return subtotal;
    }
    
    private JPanel createHeaderPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        
        JButton continueShoppingBtn = new JButton("Continue Shopping");
        continueShoppingBtn.setForeground(new Color(0, 122, 255));
        continueShoppingBtn.setBackground(Color.WHITE);
        continueShoppingBtn.setBorder(BorderFactory.createEmptyBorder());
        continueShoppingBtn.setFont(new Font("Arial", Font.PLAIN, 16));
        
        panel.add(continueShoppingBtn, BorderLayout.WEST);
        return panel;
    }
    
    private JPanel createCartPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);
        
        // Titre Shopping Cart
        JLabel titleLabel = new JLabel("Shopping Cart");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        // Panel pour les items du panier (dynamique)
        cartItemsPanel = new JPanel();
        cartItemsPanel.setLayout(new BoxLayout(cartItemsPanel, BoxLayout.Y_AXIS));
        cartItemsPanel.setBackground(Color.WHITE);
        cartItemsPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        cartItemsPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        
        // Ligne de séparation
        JSeparator separator = new JSeparator();
        separator.setAlignmentX(Component.LEFT_ALIGNMENT);
        separator.setMaximumSize(new Dimension(500, 1));
        
        panel.add(titleLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 15)));
        panel.add(cartItemsPanel);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(separator);
        
        return panel;
    }
    
    private JPanel createProductPanel(Product product) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setMaximumSize(new Dimension(500, 80));
        panel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Informations du produit
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBackground(Color.WHITE);
        
        JLabel nameLabel = new JLabel(product.getName());
        nameLabel.setFont(new Font("Arial", Font.BOLD, 16));
        
        JLabel brandLabel = new JLabel("Samsung");
        brandLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        
        JLabel modelLabel = new JLabel("Model: " + product.getModelNumber());
        modelLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        
        JLabel priceLabel = new JLabel(String.format("$%.2f", product.getPrice()));
        priceLabel.setFont(new Font("Arial", Font.BOLD, 14));
        priceLabel.setForeground(new Color(0, 122, 255));
        
        infoPanel.add(nameLabel);
        infoPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        infoPanel.add(brandLabel);
        infoPanel.add(Box.createRigidArea(new Dimension(0, 2)));
        infoPanel.add(modelLabel);
        infoPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        infoPanel.add(priceLabel);
        
        // Bouton supprimer
        JButton removeBtn = new JButton("Remove");
        removeBtn.setForeground(Color.RED);
        removeBtn.setBackground(Color.WHITE);
        removeBtn.setBorder(BorderFactory.createLineBorder(Color.RED));
removeBtn.addActionListener(e -> removeProductFromCart(product));
        
        panel.add(infoPanel, BorderLayout.CENTER);
        panel.add(removeBtn, BorderLayout.EAST);
        
        return panel;
    }
    
    private JPanel createOrderSummaryPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel titleLabel = new JLabel("Order Summary");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        // Tableau des prix
        JPanel pricePanel = new JPanel();
        pricePanel.setLayout(new BoxLayout(pricePanel, BoxLayout.Y_AXIS));
        pricePanel.setBackground(Color.WHITE);
        pricePanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        pricePanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        
        // Initialiser les labels avec des valeurs par défaut
        subtotalLabel = new JLabel("$0.00");
        taxLabel = new JLabel("$0.00");
        totalLabel = new JLabel("$0.00");
        
        addPriceRow(pricePanel, "Subtotal (0 items):", subtotalLabel);
        addPriceRow(pricePanel, "Tax (10%):", taxLabel);
        
        // Ligne de séparation pour le total
        JSeparator separator = new JSeparator();
        separator.setMaximumSize(new Dimension(300, 1));
        separator.setAlignmentX(Component.LEFT_ALIGNMENT);
        pricePanel.add(separator);
        pricePanel.add(Box.createRigidArea(new Dimension(0, 5)));
        
        addPriceRow(pricePanel, "Total:", totalLabel);
        
        panel.add(titleLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(pricePanel);
        
        return panel;
    }
    
    private void addPriceRow(JPanel panel, String label, JLabel valueLabel) {
        JPanel rowPanel = new JPanel(new BorderLayout());
        rowPanel.setBackground(Color.WHITE);
        rowPanel.setMaximumSize(new Dimension(300, 25));
        
        JLabel labelComponent = new JLabel(label);
        
        labelComponent.setFont(new Font("Arial", Font.PLAIN, 14));
        valueLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        
        if (label.equals("Total:")) {
            labelComponent.setFont(new Font("Arial", Font.BOLD, 16));
            valueLabel.setFont(new Font("Arial", Font.BOLD, 16));
        }
        
        rowPanel.add(labelComponent, BorderLayout.WEST);
        rowPanel.add(valueLabel, BorderLayout.EAST);
        
        panel.add(rowPanel);
        panel.add(Box.createRigidArea(new Dimension(0, 5)));
    }
    
    private JPanel createCheckoutPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setMaximumSize(new Dimension(400, 60));
        
        JButton checkoutBtn = new JButton("Proceed to Checkout");
        checkoutBtn.setBackground(new Color(0, 122, 255));
        checkoutBtn.setForeground(Color.WHITE);
        checkoutBtn.setFont(new Font("Arial", Font.BOLD, 16));
        checkoutBtn.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        checkoutBtn.setFocusPainted(false);
        
        panel.add(checkoutBtn, BorderLayout.CENTER);
        
        return panel;
    }
    
    // Getters pour les données du panier
    public List<Product> getCartItems() {
        return new ArrayList<>(cartItems);
    }
    
    public double getTotalAmount() {
        return calculateSubtotal() + (calculateSubtotal() * taxRate);
    }
    
    public int getCartItemsCount() {
        return cartItems.size();
    }
}
