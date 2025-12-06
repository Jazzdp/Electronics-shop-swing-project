package shop.ui;

import shop.controllers.OrderController;
import shop.model.CartItem;
import shop.model.Order;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;

public class CheckoutPanel extends JPanel {

    private final OrderController orderController;
    private final List<CartItem> cartItems;
    private final Runnable onSuccessCallback;

    public CheckoutPanel(OrderController orderController,
                         List<CartItem> cartItems,
                         Runnable onSuccessCallback) {

        this.orderController = orderController;
        this.cartItems = cartItems;
        this.onSuccessCallback = onSuccessCallback;

        setLayout(new BorderLayout());
        setBackground(new Color(0xF5F6F8));

        // ===== HEADER PANEL =====
        JPanel headerPanel = createHeaderPanel();
        add(headerPanel, BorderLayout.NORTH);

        // ===== CENTER CONTENT =====
        JPanel centerContent = new JPanel();
        centerContent.setLayout(new BoxLayout(centerContent, BoxLayout.Y_AXIS));
        centerContent.setBackground(new Color(0xF5F6F8));
        centerContent.setBorder(new EmptyBorder(30, 0, 30, 0));

        // Page title
        JLabel pageTitle = new JLabel("Checkout");
        pageTitle.setFont(new Font("SansSerif", Font.BOLD, 28));
        pageTitle.setForeground(new Color(0x111827));
        pageTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        centerContent.add(pageTitle);
        centerContent.add(Box.createVerticalStrut(30));

        // Main checkout form card
        JPanel formCard = createFormCard();
        JPanel formCardWrapper = new JPanel();
        formCardWrapper.setLayout(new BoxLayout(formCardWrapper, BoxLayout.X_AXIS));
        formCardWrapper.setOpaque(false);
        formCardWrapper.add(Box.createHorizontalGlue());
        formCardWrapper.add(formCard);
        formCardWrapper.add(Box.createHorizontalGlue());

        centerContent.add(formCardWrapper);
        centerContent.add(Box.createVerticalGlue());

        JScrollPane scrollPane = new JScrollPane(centerContent);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setBorder(null);
        scrollPane.getViewport().setBackground(new Color(0xF5F6F8));

        add(scrollPane, BorderLayout.CENTER);

        // ===== FOOTER PANEL =====
        JPanel footerPanel = createFooterPanel();
        add(footerPanel, BorderLayout.SOUTH);
    }

    private JPanel createHeaderPanel() {
        JPanel header = new JPanel(new BorderLayout(20, 0));
        header.setBackground(new Color(0x1F3A8A));
        header.setBorder(new EmptyBorder(12, 20, 12, 20));
        header.setPreferredSize(new Dimension(0, 60));

        // Left side - store name
        JLabel storeName = new JLabel("Electronics Store Manager");
        storeName.setForeground(Color.WHITE);
        storeName.setFont(new Font("SansSerif", Font.BOLD, 16));
        header.add(storeName, BorderLayout.WEST);

        return header;
    }

    private JPanel createFormCard() {
    JPanel card = new Components.Panel(600, 700);
    card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
    card.setPreferredSize(new Dimension(600, 700));
    card.setMaximumSize(new Dimension(600, Integer.MAX_VALUE));
    card.setBorder(new EmptyBorder(30, 40, 30, 40));

    // Section label
    JLabel sectionLabel = new JLabel("Delivery Information");
    sectionLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
    sectionLabel.setForeground(new Color(0x374151));
    card.add(sectionLabel);
    card.add(Box.createVerticalStrut(20));

    // Input fields
    JTextField nameField = createInputField("Enter your full name");
    JTextField emailField = createInputField("Enter your email address");
    JTextField phoneField = createInputField("Enter your phone number");
    JTextArea addressField = createAddressField("Enter your complete shipping address");

    // Full Name
    card.add(createLabeledInput("Full Name *", nameField));
    card.add(Box.createVerticalStrut(18));

    // Email
    card.add(createLabeledInput("Email *", emailField));
    card.add(Box.createVerticalStrut(18));

    // Phone
    card.add(createLabeledInput("Phone Number *", phoneField));
    card.add(Box.createVerticalStrut(18));

    // Address - aligned label
    JPanel addressRow = new JPanel(new BorderLayout(12, 0));
    addressRow.setOpaque(false);
    JLabel addressLabel = new JLabel("Shipping Address *");
    addressLabel.setFont(new Font("SansSerif", Font.PLAIN, 13));
    addressLabel.setForeground(new Color(0x374151));
    addressLabel.setPreferredSize(new Dimension(120, 60));
    addressLabel.setVerticalAlignment(JLabel.TOP);
    addressRow.add(addressLabel, BorderLayout.WEST);

    JScrollPane addressScroll = new JScrollPane(addressField);
    addressScroll.setBorder(null);
    addressScroll.setPreferredSize(new Dimension(520, 200));
    addressRow.add(addressScroll, BorderLayout.CENTER);
    card.add(addressRow);
    card.add(Box.createVerticalStrut(25));

    // Confirm button
    JButton confirmBtn = createConfirmButton(nameField, emailField, phoneField, addressField);
    card.add(confirmBtn);

    return card;
}

    private JPanel createLabeledInput(String labelText, JTextField field) {
        JPanel row = new JPanel(new BorderLayout(12, 0));
        row.setOpaque(false);

        JLabel label = new JLabel(labelText);
        label.setFont(new Font("SansSerif", Font.PLAIN, 13));
        label.setForeground(new Color(0x374151));
        label.setPreferredSize(new Dimension(120, 60));

        row.add(label, BorderLayout.WEST);
        row.add(field, BorderLayout.CENTER);

        return row;
    }

    private JTextField createInputField(String placeholder) {
        JTextField field = new JTextField() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (getText().isEmpty() && !(FocusManager.getCurrentManager().getFocusOwner() == this)) {
                    Graphics2D g2 = (Graphics2D) g;
                    g2.setColor(new Color(0x9CA3AF));
                    g2.setFont(getFont());
                    g2.drawString(placeholder, getInsets().left + 4, getHeight() / 2 + getFont().getSize() / 2 - 2);
                }
            }
        };
        field.setText("");
        field.setBackground(new Color(0xF3F4F6));
        field.setForeground(new Color(0x111827));
        field.setFont(new Font("SansSerif", Font.PLAIN, 13));
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(0xE5E7EB), 1),
            new EmptyBorder(8, 12, 8, 12)
        ));
        field.setPreferredSize(new Dimension(0, 60));
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));
        return field;
    }

    private JTextArea createAddressField(String placeholder) {
        JTextArea field = new JTextArea() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (getText().isEmpty() && !(FocusManager.getCurrentManager().getFocusOwner() == this)) {
                    Graphics2D g2 = (Graphics2D) g;
                    g2.setColor(new Color(0x9CA3AF));
                    g2.setFont(getFont());
                    g2.drawString(placeholder, getInsets().left + 4, getInsets().top + getFont().getSize());
                }
            }
        };
        field.setText("");
        field.setBackground(new Color(0xF3F4F6));
        field.setForeground(new Color(0x111827));
        field.setFont(new Font("SansSerif", Font.PLAIN, 13));
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(0xE5E7EB), 1),
            new EmptyBorder(8, 12, 8, 12)
        ));
        field.setLineWrap(true);
        field.setWrapStyleWord(true);
        return field;
    }

    private JButton createConfirmButton(JTextField nameField, JTextField emailField,
                                        JTextField phoneField, JTextArea addressField) {
        JButton btn = new JButton("Confirm Order") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Gradient background
                GradientPaint gradient = new GradientPaint(0, 0, new Color(0x020617), 0, getHeight(), new Color(0x0F172A));
                g2.setPaint(gradient);
                g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 8, 8);

                super.paintComponent(g);
            }
        };
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("SansSerif", Font.BOLD, 15));
        btn.setOpaque(false);
        btn.setContentAreaFilled(false);
        btn.setBorder(BorderFactory.createEmptyBorder(12, 20, 12, 20));
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        btn.setPreferredSize(new Dimension(Integer.MAX_VALUE, 50));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btn.addActionListener(e -> {
            try {
                String name = nameField.getText().trim();
                String email = emailField.getText().trim();
                String phone = phoneField.getText().trim();
                String address = addressField.getText().trim();

                Order created = orderController.createOrder(name, email, phone, address, cartItems);

                JOptionPane.showMessageDialog(this,
                    "Order placed successfully!\nOrder ID: " + created.getId(),
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);

                cartItems.clear();
                if (onSuccessCallback != null) onSuccessCallback.run();

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this,
                    ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            }
        });

        return btn;
    }

    private JPanel createFooterPanel() {
        JPanel footer = new JPanel(new BorderLayout(20, 0));
        footer.setBackground(new Color(0xE5E7EB));
        footer.setBorder(new EmptyBorder(12, 20, 12, 20));
        footer.setPreferredSize(new Dimension(0, 45));

        JLabel leftText = new JLabel("Ready");
        leftText.setForeground(new Color(0x374151));
        leftText.setFont(new Font("SansSerif", Font.PLAIN, 13));
        footer.add(leftText, BorderLayout.WEST);

        JLabel rightText = new JLabel("Checkout - " + cartItems.size() + " item" + (cartItems.size() != 1 ? "s" : ""));
        rightText.setForeground(new Color(0x374151));
        rightText.setFont(new Font("SansSerif", Font.PLAIN, 13));
        footer.add(rightText, BorderLayout.EAST);

        return footer;
    }
}
