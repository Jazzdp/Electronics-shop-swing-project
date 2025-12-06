package shop.ui;

import shop.controllers.OrderController;
import shop.model.CartItem;
import shop.model.Order;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class CheckoutPanel extends JPanel {

    private final OrderController orderController;
    private final List<CartItem> cartItems;  // <-- Inject your cart items here
    private final Runnable onSuccessCallback;  // <-- To close cart window after success

    public CheckoutPanel(OrderController orderController,
                         List<CartItem> cartItems,
                         Runnable onSuccessCallback) {

        this.orderController = orderController;
        this.cartItems = cartItems;
        this.onSuccessCallback = onSuccessCallback;

        setLayout(new BorderLayout());

        JLabel title = new JLabel("Checkout", SwingConstants.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 22));
        add(title, BorderLayout.NORTH);

        // ===== FORM FIELDS =====
        JPanel form = new JPanel(new GridBagLayout());
        form.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        JTextField nameField = new JTextField(25);
        JTextField emailField = new JTextField(25);
        JTextField phoneField = new JTextField(25);
        JTextArea addressField = new JTextArea(3, 25);
        addressField.setLineWrap(true);
        addressField.setWrapStyleWord(true);
        JScrollPane addressScroll = new JScrollPane(addressField);

        // Full Name
        gbc.gridx = 0;
        gbc.gridy = 0;
        JLabel nameLabel = new JLabel("Full Name:");
        nameLabel.setPreferredSize(new Dimension(120, 25));
        form.add(nameLabel, gbc);
        
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        form.add(nameField, gbc);
        
        // Email
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setPreferredSize(new Dimension(120, 25));
        form.add(emailLabel, gbc);
        
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        form.add(emailField, gbc);
        
        // Phone
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        JLabel phoneLabel = new JLabel("Phone:");
        phoneLabel.setPreferredSize(new Dimension(120, 25));
        form.add(phoneLabel, gbc);
        
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        form.add(phoneField, gbc);
        
        // Shipping Address
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        JLabel addressLabel = new JLabel("Shipping Address:");
        addressLabel.setPreferredSize(new Dimension(120, 25));
        form.add(addressLabel, gbc);
        
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        form.add(addressScroll, gbc);

        add(form, BorderLayout.CENTER);

        // ===== PLACE ORDER BUTTON =====
        JButton confirmBtn = new JButton("Place Order");
        confirmBtn.setFont(new Font("SansSerif", Font.BOLD, 18));
        confirmBtn.setBackground(new Color(0x1E293B));
        confirmBtn.setForeground(Color.WHITE);
        confirmBtn.setPreferredSize(new Dimension(150, 45));

        confirmBtn.addActionListener(e -> {
            try {
                String name = nameField.getText().trim();
                String email = emailField.getText().trim();
                String phone = phoneField.getText().trim();
                String address = addressField.getText().trim();

                // 💥 Validate & create order (This will throw exceptions if invalid)
                Order created = orderController.createOrder(
                        name, email, phone, address, cartItems
                );

                // ===== SUCCESS =====
                JOptionPane.showMessageDialog(
                        this,
                        "Order placed successfully!\nOrder ID: " + created.getId(),
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE
                );

                cartItems.clear(); // Clear local cart list
                if (onSuccessCallback != null) onSuccessCallback.run(); // Close the cart window

            } catch (Exception ex) {
                // ===== ERROR =====
                JOptionPane.showMessageDialog(
                        this,
                        ex.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        });

        JPanel bottomPanel = new JPanel();
        bottomPanel.add(confirmBtn);
        add(bottomPanel, BorderLayout.SOUTH);
    }
}
