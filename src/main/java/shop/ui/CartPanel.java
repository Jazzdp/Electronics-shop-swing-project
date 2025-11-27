package shop.ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.util.List;
import shop.controllers.CartController;
import shop.controllers.OrderController;
import shop.model.CartItem;
import shop.model.Product;
import shop.util.Palette;

public class CartPanel extends JPanel {
    private final CartController cartController;
    private JPanel cartItemsContainer;
    private JLabel totalLabel;
    private JButton checkoutButton;
    private JButton continueShopping;
    private JPanel emptyCartPanel;
    private JFrame parentWindow;
    private shop.controllers.OrderController orderController; 
    private List<CartItem> cartItems;
   
private final JFrame parentFrame; 

public CartPanel(CartController cartController,
                 OrderController orderController,
                 JFrame parentFrame) {

    this.cartController = cartController;
    this.orderController = orderController;
    this.parentFrame   = parentFrame;
    setLayout(new BorderLayout());
        setBackground(Palette.SURFACE);
           add(createHeader(), BorderLayout.NORTH);

        // Main content with cart items and summary
        add(createMainContent(), BorderLayout.CENTER);

        // Refresh cart display
        refreshCart();
}

   

    private JPanel createHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Palette.SURFACE_ALT);
        header.setBorder(new EmptyBorder(16, 20, 16, 20));

        JLabel title = new JLabel("Shopping Cart");
        title.setFont(new Font("SansSerif", Font.BOLD, 28));
        title.setForeground(Palette.DARK_NAVY);
        header.add(title, BorderLayout.WEST);

        return header;
    }

    private JPanel createMainContent() {
        JPanel main = new JPanel(new BorderLayout(0, 16));
        main.setBackground(Palette.SURFACE);
        main.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Left side - Cart items
        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.setBackground(Palette.SURFACE);

        // Cart items container with scroll
        cartItemsContainer = new JPanel();
        cartItemsContainer.setLayout(new BoxLayout(cartItemsContainer, BoxLayout.Y_AXIS));
        cartItemsContainer.setBackground(Palette.SURFACE);

        JScrollPane scrollPane = new JScrollPane(cartItemsContainer);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(Palette.SURFACE);

        leftPanel.add(scrollPane, BorderLayout.CENTER);

        // Empty cart placeholder (not added by default)
        emptyCartPanel = createEmptyCartPanel();

        // Right side - Cart summary
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        rightPanel.setBackground(Palette.SURFACE_ALT);
        rightPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Palette.CARD_BORDER),
            new EmptyBorder(16, 16, 16, 16)
        ));
        rightPanel.setPreferredSize(new Dimension(280, 0));
        rightPanel.setMaximumSize(new Dimension(280, Integer.MAX_VALUE));


        // Summary title
        JLabel summaryTitle = new JLabel("Order Summary");
        summaryTitle.setFont(new Font("SansSerif", Font.BOLD, 18));
        summaryTitle.setForeground(Palette.DARK_NAVY);
        summaryTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        rightPanel.add(summaryTitle);
        rightPanel.add(Box.createVerticalStrut(12));


        // Subtotal (renamed to Items Total)
       
        
        // Shipping (always free)
        JLabel shippingLabel = new JLabel("Shipping:");
        shippingLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        shippingLabel.setForeground(Palette.MUTED_TEXT);
        JLabel shippingValue = new JLabel("Free");
        shippingValue.setFont(new Font("SansSerif", Font.PLAIN, 14));
        shippingValue.setForeground(new Color(34, 197, 94)); // green
        JPanel shippingPanel = new JPanel(new BorderLayout());
        shippingPanel.setOpaque(false);
        shippingPanel.add(shippingLabel, BorderLayout.WEST);
        shippingPanel.add(shippingValue, BorderLayout.EAST);
        shippingPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        rightPanel.add(shippingPanel);

        rightPanel.add(Box.createVerticalStrut(8));

        // Total
        JPanel totalPanel = new JPanel(new BorderLayout());
        totalPanel.setOpaque(false);
        JLabel totalLbl = new JLabel("Total:");
        totalLbl.setFont(new Font("SansSerif", Font.BOLD, 16));
        totalLbl.setForeground(Palette.DARK_NAVY);
        totalLabel = new JLabel("$0.00");
        totalLabel.setFont(new Font("SansSerif", Font.BOLD, 22));
        totalLabel.setForeground(Palette.PRICE_GREEN);
        totalPanel.add(totalLbl, BorderLayout.WEST);
        totalPanel.add(totalLabel, BorderLayout.EAST);
        totalPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        totalPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        rightPanel.add(totalPanel);

        rightPanel.add(Box.createVerticalStrut(18));

        // Checkout button (smaller text)
        checkoutButton = createCheckoutButton();
        checkoutButton.setFont(new Font("SansSerif", Font.BOLD, 14));
        checkoutButton.setAlignmentX(Component.CENTER_ALIGNMENT);
          
        checkoutButton.addActionListener(e -> {

    JFrame checkoutFrame = new JFrame("Checkout");
    checkoutFrame.setSize(500, 600);
    checkoutFrame.setLocationRelativeTo(this);
    checkoutFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
List<CartItem> items = cartController.getCartItems();

   checkoutFrame.setLayout(new BorderLayout());
    checkoutFrame.add(new CheckoutPanel(
        orderController,
        cartController.getCartItems(), // <-- make sure this list is not empty
        () -> {
              cartController.clearCart(); 
            if (parentFrame != null) parentFrame.dispose(); // safely close
            checkoutFrame.dispose();
        }
    ), BorderLayout.CENTER);

    
    checkoutFrame.setVisible(true);
});
        rightPanel.add(checkoutButton);

        rightPanel.add(Box.createVerticalStrut(12));

        // Continue shopping button
        continueShopping = createContinueShoppingButton();
        continueShopping.setAlignmentX(Component.CENTER_ALIGNMENT);
        rightPanel.add(continueShopping);

        rightPanel.add(Box.createVerticalGlue());

        // Main content layout
        JPanel contentPanel = new JPanel(new BorderLayout(20, 0));
        contentPanel.setBackground(Palette.SURFACE);
        contentPanel.add(leftPanel, BorderLayout.CENTER);
        contentPanel.add(rightPanel, BorderLayout.EAST);

        main.add(contentPanel, BorderLayout.CENTER);
        return main;
    }

    private JPanel createEmptyCartPanel() {
        JPanel empty = new JPanel(new GridBagLayout());
        empty.setBackground(Palette.SURFACE);
        empty.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Palette.CARD_BORDER),
            new EmptyBorder(40, 20, 40, 20)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(0, 0, 16, 0);

        JLabel emptyIcon = new JLabel("🛒");
        emptyIcon.setFont(new Font("SansSerif", Font.PLAIN, 48));
        empty.add(emptyIcon, gbc);

        gbc.gridy = 1;
        JLabel emptyMsg = new JLabel("Your cart is empty");
        emptyMsg.setFont(new Font("SansSerif", Font.PLAIN, 18));
        emptyMsg.setForeground(Palette.MUTED_TEXT);
        empty.add(emptyMsg, gbc);

        return empty;
    }

    public void addCartItem(Product product) {
        try {
            cartController.addToCart(product.getId(), 1);
            refreshCart();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error adding to cart: " + e.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void refreshCart() {
        cartItemsContainer.removeAll();
        List<CartItem> items = cartController.getCartItems();

        // Defensive: try to find summary labels, but don't crash if not found
        JLabel subtotalValue = null;
        try {
            JPanel rightPanel = (JPanel)((JPanel)((BorderLayout)getLayout()).getLayoutComponent(BorderLayout.CENTER)).getComponent(1);
            subtotalValue = (JLabel)((JPanel)rightPanel.getComponent(2)).getComponent(1);
        } catch (Exception ex) {
            System.err.println("[CartPanel] Could not find summary labels: " + ex);
        }

        double subtotal = 0.0;
        for (CartItem item : items) {
            subtotal += item.getSubtotal();
        }
        double total = subtotal; // No tax, shipping is free

        if (subtotalValue != null) subtotalValue.setText("$" + String.format("%.2f", subtotal));
        // shippingValue is always 'Free', no update needed

        // Remove emptyCartPanel if present
        Container leftPanel = cartItemsContainer.getParent().getParent(); // scrollPane -> leftPanel
        if (leftPanel instanceof JPanel) {
            ((JPanel)leftPanel).remove(emptyCartPanel);
        }

        if (items.isEmpty()) {
            // Show empty cart panel
            if (leftPanel instanceof JPanel) {
                ((JPanel)leftPanel).add(emptyCartPanel, BorderLayout.CENTER);
                emptyCartPanel.setVisible(true);
            }
            checkoutButton.setEnabled(false);
        } else {
            // Remove empty cart panel if present
            if (leftPanel instanceof JPanel) {
                ((JPanel)leftPanel).remove(emptyCartPanel);
            }
            emptyCartPanel.setVisible(false);
            checkoutButton.setEnabled(true);
            for (CartItem item : items) {
                JPanel itemRow = createCartItemRow(item);
                cartItemsContainer.add(itemRow);
                cartItemsContainer.add(Box.createVerticalStrut(12));
            }
        }

        cartItemsContainer.add(Box.createVerticalGlue());
        totalLabel.setText("$" + String.format("%.2f", total));
        cartItemsContainer.revalidate();
        cartItemsContainer.repaint();
    }

    private JPanel createCartItemRow(CartItem item) {
        JPanel row = new JPanel(new BorderLayout(12, 0));
        row.setBackground(Palette.SURFACE_ALT);
        row.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Palette.CARD_BORDER),
            new EmptyBorder(12, 12, 12, 12)
        ));
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 120));

        Product product = item.getProduct();

        // Product image
        Image img = ProductCardPanel.loadImageStatic(product.getPicUrl());
        Image scaled = img.getScaledInstance(80, 80, Image.SCALE_SMOOTH);
        JPanel imgPanel = ProductCardPanel.createRoundedImagePanel(scaled, 80, 80);
        imgPanel.setPreferredSize(new Dimension(80, 80));
        row.add(imgPanel, BorderLayout.WEST);

        // Product details
        JPanel detailsPanel = new JPanel();
        detailsPanel.setOpaque(false);
        detailsPanel.setLayout(new BoxLayout(detailsPanel, BoxLayout.Y_AXIS));

        JLabel name = new JLabel(product.getName());
        name.setFont(new Font("SansSerif", Font.BOLD, 14));
        name.setForeground(Palette.DARK_NAVY);
        detailsPanel.add(name);

        JLabel model = new JLabel("Model: " + product.getModelNumber());
        model.setFont(new Font("SansSerif", Font.PLAIN, 12));
        model.setForeground(Palette.MUTED_TEXT);
        detailsPanel.add(model);

        detailsPanel.add(Box.createVerticalStrut(6));

        JLabel price = new JLabel("$" + String.format("%.2f", product.getPrice()));
        price.setFont(new Font("SansSerif", Font.BOLD, 14));
        price.setForeground(Palette.PRICE_GREEN);
        detailsPanel.add(price);

        row.add(detailsPanel, BorderLayout.CENTER);

        // Quantity controls
        JPanel controlsPanel = new JPanel();
        controlsPanel.setOpaque(false);
        controlsPanel.setLayout(new BoxLayout(controlsPanel, BoxLayout.Y_AXIS));

        // Quantity selector
        JPanel quantityPanel = new JPanel();
        quantityPanel.setOpaque(false);
        quantityPanel.setLayout(new BoxLayout(quantityPanel, BoxLayout.X_AXIS));

        JButton minusBtn = createSmallButton("-");
        minusBtn.addActionListener(e -> updateQuantity(product.getId(), item.getQuantity() - 1));
        quantityPanel.add(minusBtn);

        JLabel qtyLabel = new JLabel(String.valueOf(item.getQuantity()));
        qtyLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        qtyLabel.setForeground(Palette.DARK_NAVY);
        qtyLabel.setHorizontalAlignment(SwingConstants.CENTER);
        qtyLabel.setPreferredSize(new Dimension(40, 30));
        quantityPanel.add(Box.createHorizontalStrut(8));
        quantityPanel.add(qtyLabel);
        quantityPanel.add(Box.createHorizontalStrut(8));

        JButton plusBtn = createSmallButton("+");
        plusBtn.addActionListener(e -> updateQuantity(product.getId(), item.getQuantity() + 1));
        quantityPanel.add(plusBtn);

        quantityPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        controlsPanel.add(quantityPanel);

        controlsPanel.add(Box.createVerticalStrut(8));

        JLabel subtotal = new JLabel("Subtotal: $" + String.format("%.2f", item.getSubtotal()));
        subtotal.setFont(new Font("SansSerif", Font.BOLD, 12));
        subtotal.setForeground(Palette.PRICE_GREEN);
        subtotal.setAlignmentX(Component.LEFT_ALIGNMENT);
        controlsPanel.add(subtotal);

        controlsPanel.add(Box.createVerticalStrut(8));

        JButton removeBtn = createRemoveButton();
        removeBtn.addActionListener(e -> removeFromCart(product.getId()));
        removeBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        controlsPanel.add(removeBtn);

        row.add(controlsPanel, BorderLayout.EAST);

        return row;
    }

    private void updateQuantity(Long productId, int newQuantity) {
        try {
            if (newQuantity < 0) {
                return;
            }
            cartController.updateQuantity(productId, newQuantity);
            refreshCart();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error updating quantity: " + e.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void removeFromCart(Long productId) {
        try {
            cartController.removeFromCart(productId);
            refreshCart();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error removing item: " + e.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }


    private JButton createCheckoutButton() {
        return ProductCardPanel.creatButton("Proceed to Checkout", new int[]{0x0F172A, 0x1E293B, 0x0F172A}, 240, 44);
    }

    private JButton createContinueShoppingButton() {
        JButton btn = new JButton("Continue Shopping") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON);

                if (getModel().isPressed()) {
                    g2.setColor(new Color(230, 230, 235));
                } else if (getModel().isRollover()) {
                    g2.setColor(new Color(240, 240, 245));
                } else {
                    g2.setColor(Palette.SURFACE);
                }

                g2.fill(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 20, 20));

                // Border
                g2.setColor(Palette.CARD_BORDER);
                g2.setStroke(new BasicStroke(1.5f));
                g2.draw(new RoundRectangle2D.Double(0, 0, getWidth() - 1, getHeight() - 1, 20, 20));

                // Text
                g2.setColor(Palette.DARK_NAVY);
                g2.setFont(getFont().deriveFont(java.awt.Font.BOLD, 14f));
                java.awt.FontMetrics fm = g2.getFontMetrics();
                int x = (getWidth() - fm.stringWidth(getText())) / 2;
                int y = (getHeight() + fm.getAscent()) / 2 - 3;
                g2.drawString(getText(), x, y);

                g2.dispose();
            }

            @Override
            protected void paintBorder(Graphics g) {}
        };
        btn.setFocusPainted(false);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(240, 44));
        btn.addActionListener(e -> {
            if (parentWindow != null) {
                parentWindow.dispose();
            }
        });
        return btn;
    }

    private JButton createSmallButton(String text) {
        JButton btn = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON);

                if (getModel().isPressed()) {
                    g2.setColor(new Color(15, 23, 42));
                } else if (getModel().isRollover()) {
                    g2.setColor(new Color(30, 41, 59));
                } else {
                    g2.setColor(Palette.DARK_NAVY);
                }

                g2.fill(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 8, 8));

                g2.setColor(Color.WHITE);
                g2.setFont(getFont().deriveFont(java.awt.Font.BOLD, 14f));
                java.awt.FontMetrics fm = g2.getFontMetrics();
                int x = (getWidth() - fm.stringWidth(getText())) / 2;
                int y = (getHeight() + fm.getAscent()) / 2 - 3;
                g2.drawString(getText(), x, y);

                g2.dispose();
            }

            @Override
            protected void paintBorder(Graphics g) {}
        };
        btn.setFocusPainted(false);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(32, 32));
        return btn;
    }

    private JButton createRemoveButton() {
        JButton btn = new JButton("Remove") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON);

                g2.setColor(new Color(220, 38, 38));
                g2.fill(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 6, 6));

                g2.setColor(Color.WHITE);
                g2.setFont(getFont().deriveFont(java.awt.Font.BOLD, 12f));
                java.awt.FontMetrics fm = g2.getFontMetrics();
                int x = (getWidth() - fm.stringWidth(getText())) / 2;
                int y = (getHeight() + fm.getAscent()) / 2 - 3;
                g2.drawString(getText(), x, y);

                g2.dispose();
            }

            @Override
            protected void paintBorder(Graphics g) {}
        };
        btn.setFocusPainted(false);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(80, 28));
        return btn;
    }
}
