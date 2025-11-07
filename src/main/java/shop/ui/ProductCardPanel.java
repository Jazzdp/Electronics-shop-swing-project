package shop.ui;

import javax.swing.*;

import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import shop.controllers.ProductController;
import shop.model.Product;
import java.net.URL;
import java.util.List;

public class ProductCardPanel extends JPanel {
    private final ProductController productController;
    private JPanel cardsContainer;

    public ProductCardPanel(ProductController productController) {
        this.productController = productController;
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        // Create scrollable container for product cards
        cardsContainer = new JPanel();
        cardsContainer.setLayout(new FlowLayout(FlowLayout.LEFT, 20, 20));
        cardsContainer.setBackground(Color.WHITE);

        JScrollPane scrollPane = new JScrollPane(cardsContainer);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        
        add(scrollPane, BorderLayout.CENTER);

        // Load all products on initialization
        loadAllProducts();
    }

    // Load all products from database
    public void loadAllProducts() {
        cardsContainer.removeAll();
        List<Product> products = productController.getAllProducts();
        
        if (products == null || products.isEmpty()) {
            JLabel emptyLabel = new JLabel("No products available");
            emptyLabel.setFont(new Font("SansSerif", Font.PLAIN, 18));
            emptyLabel.setForeground(Color.GRAY);
            cardsContainer.add(emptyLabel);
        } else {
            for (Product product : products) {
                JPanel card = createProductCardFromModel(product);
                cardsContainer.add(card);
            }
        }
        
        cardsContainer.revalidate();
        cardsContainer.repaint();
    }

    // Load products by category
    public void loadProductsByCategory(String category) {
        cardsContainer.removeAll();
        List<Product> products = productController.getProductsByCategory(category);
        
        if (products == null || products.isEmpty()) {
            JLabel emptyLabel = new JLabel("No products found in " + category);
            emptyLabel.setFont(new Font("SansSerif", Font.PLAIN, 18));
            emptyLabel.setForeground(Color.GRAY);
            cardsContainer.add(emptyLabel);
        } else {
            for (Product product : products) {
                JPanel card = createProductCardFromModel(product);
                cardsContainer.add(card);
            }
        }
        
        cardsContainer.revalidate();
        cardsContainer.repaint();
    }

    // Create product card from Product model
    private JPanel createProductCardFromModel(Product product) {
        // Load image from URL or use default
        Image img = loadImage(product.getPicUrl());
        
        // Format warranty text
        String warrantyText = product.getWarrantyMonths() + " " + 
                            (product.getWarrantyMonths() == 1 ? "Month" : "Months");
        
        return createProductCard(
            img,
            product.getName(),
            product.getCategory(),
            product.getModelNumber(),
            warrantyText,
            product.getPrice(),
            product.getId(),
            product.getStockQuantity()
        );
    }

    // Load image from URL or file path
    private Image loadImage(String picUrl) {
        try {
            if (picUrl == null || picUrl.isEmpty()) {
                return getDefaultImage();
            }
            
            // Try loading as URL first
            if (picUrl.startsWith("http://") || picUrl.startsWith("https://")) {
                URL url = new URL(picUrl);
                ImageIcon icon = new ImageIcon(url);
                return icon.getImage();
            }
            
            // Try loading as local file
            ImageIcon icon = new ImageIcon(picUrl);
            if (icon.getImageLoadStatus() == MediaTracker.COMPLETE) {
                return icon.getImage();
            }
            
            return getDefaultImage();
        } catch (Exception e) {
            System.err.println("Error loading image: " + picUrl);
            return getDefaultImage();
        }
    }

    // Get default placeholder image
    private Image getDefaultImage() {
        // Create a simple placeholder image
        return new ImageIcon(createPlaceholderImage()).getImage();
    }

    private java.awt.image.BufferedImage createPlaceholderImage() {
        java.awt.image.BufferedImage img = new java.awt.image.BufferedImage(310, 260, 
            java.awt.image.BufferedImage.TYPE_INT_RGB);
        Graphics2D g2 = img.createGraphics();
        g2.setColor(new Color(0xF0F0F0));
        g2.fillRect(0, 0, 310, 260);
        g2.setColor(Color.GRAY);
        g2.setFont(new Font("SansSerif", Font.PLAIN, 16));
        g2.drawString("No Image", 120, 130);
        g2.dispose();
        return img;
    }

    public static JPanel createProductCard(Image productImage, String productName, 
                                          String category, String model, String warranty, 
                                          double price, Long productId, int stockQuantity) {
        JPanel card = createProductPanel(350, 560);
        JPanel content = (JPanel) card.getComponent(0);
        content.setAlignmentX(Component.LEFT_ALIGNMENT);
        content.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));

        // Image panel
        JPanel imagePanel = createImagePanel(productImage, 310, 260);
        imagePanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        imagePanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 260));
        content.add(imagePanel);
        content.add(Box.createVerticalStrut(15));

        // Prepare left alignment helper
        java.util.function.Consumer<JLabel> prepareLeft = lbl -> {
            lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
            lbl.setHorizontalAlignment(SwingConstants.LEFT);
            lbl.setMaximumSize(new Dimension(Integer.MAX_VALUE, lbl.getPreferredSize().height));
        };

        // Product name
        JLabel textProductName = createProductCarteTextLabel(productName, "SansSerif", 18, 
            Color.BLACK, JLabel.LEFT, Font.BOLD);
        prepareLeft.accept(textProductName);
        content.add(textProductName);
        content.add(Box.createVerticalStrut(5));

        // Category tag
        JLabel tagLabel = createTagLabel(category);
        tagLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        tagLabel.setMaximumSize(tagLabel.getPreferredSize());
        content.add(tagLabel);
        content.add(Box.createVerticalStrut(10));

        // Model
        JLabel modelLabel = createProductCarteTextLabel("Model: " + model, "SansSerif", 14, 
            new Color(0x555555), JLabel.LEFT, Font.PLAIN);
        prepareLeft.accept(modelLabel);
        content.add(modelLabel);
        content.add(Box.createVerticalStrut(5));

        // Warranty
        JLabel warrantyLabel = createProductCarteTextLabel("Warranty: " + warranty, "SansSerif", 
            14, new Color(0x555555), JLabel.LEFT, Font.PLAIN);
        prepareLeft.accept(warrantyLabel);
        content.add(warrantyLabel);
        content.add(Box.createVerticalStrut(5));

        // Stock quantity
        Color stockColor = stockQuantity > 10 ? new Color(0x2FA84F) : 
                          stockQuantity > 0 ? new Color(0xFF9800) : Color.RED;
        String stockText = stockQuantity > 0 ? "In Stock: " + stockQuantity : "Out of Stock";
        JLabel stockLabel = createProductCarteTextLabel(stockText, "SansSerif", 13, 
            stockColor, JLabel.LEFT, Font.BOLD);
        prepareLeft.accept(stockLabel);
        content.add(stockLabel);
        content.add(Box.createVerticalStrut(15));

        // Bottom row with price and button
        JPanel bottomRow = new JPanel();
        bottomRow.setOpaque(false);
        bottomRow.setLayout(new BoxLayout(bottomRow, BoxLayout.X_AXIS));
        bottomRow.setAlignmentX(Component.LEFT_ALIGNMENT);
        bottomRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));

        // Price
        JLabel priceLabel = createProductCarteTextLabel("$" + String.format("%.2f", price), 
            "SansSerif", 18, new Color(0x2FA84F), JLabel.LEFT, Font.BOLD);
        priceLabel.setAlignmentY(Component.CENTER_ALIGNMENT);
        priceLabel.setHorizontalAlignment(SwingConstants.LEFT);
        priceLabel.setMaximumSize(new Dimension(Integer.MAX_VALUE, priceLabel.getPreferredSize().height));
        bottomRow.add(priceLabel);
        bottomRow.add(Box.createHorizontalGlue());

        // Add to Cart button
        int[] btnColors = { 0x0F172A, 0x1E293B, 0x0F172A };
        JButton addToCartBtn = creatButton("Add to Cart", btnColors, 150, 40);
        
        // Disable button if out of stock
        if (stockQuantity <= 0) {
            addToCartBtn.setEnabled(false);
            addToCartBtn.setText("Out of Stock");
        }
        
        addToCartBtn.addActionListener(e -> {
            // Handle add to cart action
            JOptionPane.showMessageDialog(card, 
                "Added " + productName + " to cart!\nProduct ID: " + productId,
                "Success", 
                JOptionPane.INFORMATION_MESSAGE);
        });
        
        Dimension btnPref = addToCartBtn.getPreferredSize();
        addToCartBtn.setMaximumSize(new Dimension(btnPref.width, btnPref.height));
        addToCartBtn.setAlignmentY(Component.CENTER_ALIGNMENT);
        bottomRow.add(addToCartBtn);

        content.add(bottomRow);
        card.revalidate();
        card.repaint();

        return card;
    }

    // Keep all the existing static helper methods below
    public static JPanel createProductPanel(int width, int height) {
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                int arc = 25;
                g2.setColor(new Color(0, 0, 0, 25));
                g2.fill(new RoundRectangle2D.Double(3, 3, getWidth() - 6, getHeight() - 6, arc, arc));
                g2.setColor(Color.WHITE);
                g2.fill(new RoundRectangle2D.Double(0, 0, getWidth() - 6, getHeight() - 6, arc, arc));
                g2.setColor(new Color(0xE5E5E5));
                g2.setStroke(new BasicStroke(1f));
                g2.draw(new RoundRectangle2D.Double(0, 0, getWidth() - 6, getHeight() - 6, arc, arc));
                g2.dispose();
            }

            @Override
            public Dimension getPreferredSize() {
                return new Dimension(width, height);
            }
        };
        panel.setOpaque(false);
        panel.setLayout(new BorderLayout());
        JPanel content = new JPanel();
        content.setOpaque(false);
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.add(content, BorderLayout.CENTER);
        return panel;
    }

    public static JPanel createImagePanel(Image image, int width, int height) {
        return new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                int arc = 25;
                Shape clip = new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), arc, arc);
                g2.setClip(clip);
                g2.setColor(Color.WHITE);
                g2.fill(clip);
                if (image != null) {
                    double panelRatio = (double) getWidth() / getHeight();
                    double imgRatio = (double) image.getWidth(this) / image.getHeight(this);
                    int drawWidth, drawHeight;
                    if (panelRatio > imgRatio) {
                        drawWidth = getWidth();
                        drawHeight = (int) (getWidth() / imgRatio);
                    } else {
                        drawHeight = getHeight();
                        drawWidth = (int) (getHeight() * imgRatio);
                    }
                    int x = (getWidth() - drawWidth) / 2;
                    int y = (getHeight() - drawHeight) / 2;
                    g2.drawImage(image, x, y, drawWidth, drawHeight, this);
                }
                g2.setClip(null);
                g2.setColor(new Color(0xE5E5E5));
                g2.setStroke(new BasicStroke(1f));
                g2.draw(clip);
                g2.dispose();
            }

            @Override
            public Dimension getPreferredSize() {
                return new Dimension(width, height);
            }
        };
    }

    public static JButton creatButton(String text, int[] colors, int width, int hight) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                if (getModel().isPressed()) {
                    g2.setColor(new Color(colors[0]));
                } else if (getModel().isRollover()) {
                    g2.setColor(new Color(colors[1]));
                } else {
                    g2.setColor(new Color(colors[2]));
                }
                g2.fill(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 30, 30));
                g2.setColor(Color.WHITE);
                g2.setFont(getFont().deriveFont(Font.BOLD, 16f));
                FontMetrics fm = g2.getFontMetrics();
                int x = (getWidth() - fm.stringWidth(getText())) / 2;
                int y = (getHeight() + fm.getAscent()) / 2 - 3;
                g2.drawString(getText(), x, y);
                g2.dispose();
            }

            @Override
            protected void paintBorder(Graphics g) {}
        };
        button.setFocusPainted(false);
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(width, hight));
        return button;
    }

    public static JLabel createTagLabel(String text) {
        final String labelText = text == null ? "" : text.trim();
        JLabel label = new JLabel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                try {
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                    Color bg = new Color(0xCFCFD7);
                    Color border = new Color(0xE6E6E6);
                    Color textColor = new Color(0x2E2E37);
                    Font f = getFont().deriveFont(Font.PLAIN, 13f);
                    g2.setFont(f);
                    FontMetrics fm = g2.getFontMetrics();
                    int padH = 14;
                    int padV = 6;
                    int textW = fm.stringWidth(labelText);
                    int textH = fm.getAscent();
                    int w = textW + padH * 2;
                    int h = fm.getHeight() + padV * 2;
                    int arc = h;
                    int bw = Math.max(1, getWidth());
                    int bh = Math.max(1, getHeight());
                    int pillW = Math.min(bw, w);
                    int pillH = Math.min(bh, h);
                    int pillX = (bw - pillW) / 2;
                    int pillY = (bh - pillH) / 2;
                    RoundRectangle2D pill = new RoundRectangle2D.Double(pillX, pillY, pillW - 1, pillH - 1, arc, arc);
                    g2.setColor(bg);
                    g2.fill(pill);
                    g2.setColor(border);
                    g2.setStroke(new BasicStroke(1f));
                    g2.draw(pill);
                    g2.setColor(textColor);
                    int tx = pillX + (pillW - textW) / 2;
                    int ty = pillY + (pillH - fm.getHeight()) / 2 + fm.getAscent();
                    g2.drawString(labelText, tx, ty);
                } finally {
                    g2.dispose();
                }
            }

            @Override
            public Dimension getPreferredSize() {
                Font f = getFont().deriveFont(Font.PLAIN, 13f);
                FontMetrics fm = getFontMetrics(f);
                int padH = 14;
                int padV = 6;
                int w = fm.stringWidth(labelText) + padH * 2;
                int h = fm.getHeight() + padV * 2;
                return new Dimension(w, h);
            }
        };
        label.setOpaque(false);
        label.setFont(new Font("SansSerif", Font.PLAIN, 13));
        label.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        label.setToolTipText(labelText);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        label.setMaximumSize(label.getPreferredSize());
        return label;
    }

    public static JLabel createProductCarteTextLabel(String text, String font, int TextSize, 
                                                     Color TextColor, int Alignment, int FontStyle) {
        JLabel label = new JLabel(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                super.paintComponent(g2);
                g2.dispose();
            }
        };
        label.setFont(new Font(font, FontStyle, TextSize));
        label.setForeground(TextColor);
        label.setHorizontalAlignment(Alignment);
        label.setOpaque(false);
        return label;
    }
}