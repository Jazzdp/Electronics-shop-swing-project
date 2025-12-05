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
        JPanel form = new JPanel();
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
        form.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        JTextField nameField = new JTextField(20);
        JTextField emailField = new JTextField(20);
        JTextField phoneField = new JTextField(20);
        JTextArea addressField = new JTextArea(3, 20);
        addressField.setLineWrap(true);
        addressField.setWrapStyleWord(true);

        // Create horizontal panels for each field
        JPanel namePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        namePanel.add(new JLabel("Full Name:"));
        namePanel.add(nameField);
        
        JPanel emailPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        emailPanel.add(new JLabel("Email:"));
        emailPanel.add(emailField);
        
        JPanel phonePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        phonePanel.add(new JLabel("Phone:"));
        phonePanel.add(phoneField);
        
        JPanel addressPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        addressPanel.add(new JLabel("Shipping Address:"));
        JScrollPane addressScroll = new JScrollPane(addressField);
        addressScroll.setPreferredSize(new Dimension(200, 60));
        addressPanel.add(addressScroll);

        form.add(namePanel);
        form.add(emailPanel);
        form.add(phonePanel);
        form.add(addressPanel);

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
