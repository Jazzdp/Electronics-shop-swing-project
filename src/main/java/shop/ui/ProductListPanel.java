package shop.ui;

import javax.swing.*;
import java.awt.*;
import shop.controllers.ProductController;
import shop.model.Product;

public class ProductListPanel extends JPanel {
    private final ProductController productController;
    private final JPanel listContainer;

    public ProductListPanel(ProductController productController) {
        this.productController = productController;
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        listContainer = new JPanel();
        listContainer.setLayout(new BoxLayout(listContainer, BoxLayout.Y_AXIS));
        listContainer.setBackground(Color.WHITE);

        JScrollPane scroll = new JScrollPane(listContainer);
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.setBorder(BorderFactory.createEmptyBorder());

        add(scroll, BorderLayout.CENTER);

        loadAllProducts();
    }

    public void loadAllProducts() {
        java.util.List<shop.model.Product> products = productController.getAllProducts();
        updateList(products);
    }

    public void loadProductsByCategory(String category) {
        java.util.List<shop.model.Product> products = productController.getProductsByCategory(category);
        updateList(products);
    }

    public void filterProducts(String query) {
        java.util.List<shop.model.Product> products = productController.getAllProducts();
        if (products != null) {
            String lowerQuery = query.toLowerCase();
            java.util.List<shop.model.Product> filtered = products.stream()
                .filter(p -> (p.getName() != null && p.getName().toLowerCase().contains(lowerQuery)) || 
                            (p.getCategory() != null && p.getCategory().toLowerCase().contains(lowerQuery)) || 
                            (p.getDescription() != null && p.getDescription().toLowerCase().contains(lowerQuery)))
                .collect(java.util.stream.Collectors.toList());
            updateList(filtered);
        }
    }

    private void updateList(java.util.List<shop.model.Product> products) {
        listContainer.removeAll();
        if (products == null || products.isEmpty()) {
            JLabel empty = new JLabel("No products available");
            empty.setFont(new Font("SansSerif", Font.PLAIN, 16));
            empty.setForeground(Color.GRAY);
            empty.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
            listContainer.add(empty);
        } else {
            for (shop.model.Product p : products) {
                JPanel row = createListRow(p);
                row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 120));
                listContainer.add(row);
                listContainer.add(Box.createRigidArea(new Dimension(0, 8)));
            }
        }
        listContainer.add(Box.createVerticalGlue());
        listContainer.revalidate();
        listContainer.repaint();
    }

    private JPanel createListRow(shop.model.Product p) {
        JPanel row = new JPanel(new BorderLayout());
        row.setBackground(Color.WHITE);
        row.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(0xE5E5E5)),
            BorderFactory.createEmptyBorder(8, 8, 8, 8)
        ));

        Image img = ProductCardPanel.loadImageStatic(p.getPicUrl());
        Image scaled = img.getScaledInstance(100, 100, Image.SCALE_SMOOTH);
        JPanel imgPanel = ProductCardPanel.createRoundedImagePanel(scaled, 100, 100);
        imgPanel.setPreferredSize(new Dimension(100, 100));
        row.add(imgPanel, BorderLayout.WEST);

        JPanel center = new JPanel();
        center.setOpaque(false);
        center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));
        JLabel name = new JLabel(p.getName());
        name.setFont(new Font("SansSerif", Font.BOLD, 16));
        center.add(name);
        center.add(Box.createVerticalStrut(6));
        JLabel model = new JLabel("Model: " + p.getModelNumber());
        model.setFont(new Font("SansSerif", Font.PLAIN, 13));
        model.setForeground(new Color(0x555555));
        center.add(model);
        center.add(Box.createVerticalStrut(6));
        JLabel category = new JLabel(p.getCategory());
        category.setFont(new Font("SansSerif", Font.PLAIN, 13));
        category.setForeground(new Color(0x777777));
        center.add(category);
        center.add(Box.createVerticalStrut(6));
        String stockText = p.getStockQuantity() > 0 ? "Stock: " + p.getStockQuantity() : "Out of Stock";
        JLabel stock = new JLabel(stockText);
        stock.setFont(new Font("SansSerif", Font.PLAIN, 12));
        stock.setForeground(p.getStockQuantity() > 0 ? new Color(0x2FA84F) : new Color(0xDC2626));
        center.add(stock);
        row.add(center, BorderLayout.CENTER);

        JPanel right = new JPanel();
        right.setOpaque(false);
        right.setLayout(new BoxLayout(right, BoxLayout.Y_AXIS));
        JLabel price = new JLabel("$" + String.format("%.2f", p.getPrice()));
        price.setFont(new Font("SansSerif", Font.BOLD, 16));
        price.setForeground(new Color(0x2FA84F));
        right.add(price);
        right.add(Box.createVerticalGlue());
        JButton addBtn = ProductCardPanel.creatButton("Add to Cart", new int[]{0x0F172A,0x1E293B,0x0F172A}, 120, 32);
        right.add(addBtn);
        right.setBorder(BorderFactory.createEmptyBorder(4, 8, 4, 4));
        row.add(right, BorderLayout.EAST);

        return row;
    }
}

