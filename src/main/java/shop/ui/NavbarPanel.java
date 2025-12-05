package shop.ui;

import javax.swing.*;
import shop.ui.ProductCardPanel;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import shop.util.Palette;

public class NavbarPanel extends JPanel {
    private JButton cartButton;
    private int cartItemCount = 0;

    public NavbarPanel() {      
          setLayout(new BorderLayout());    
            setBackground(Palette.PRIMARY);    
            setBorder(BorderFactory.createEmptyBorder(10, 12, 10, 12));    
           JLabel title =new JLabel("ElectroShop");    
               title.setForeground(Palette.ON_PRIMARY);     
                  title.setFont(title.getFont().deriveFont(Font.BOLD,18f));      
                    add(title, BorderLayout.WEST);     
                       int[] btnColors = { 0x0F172A, 0x1E293B, 0x0F172A };
                     
                       cartButton = creatButton("🛒 Cart (0)", btnColors, 140, 36);      
                     cartButton.setBackground(Palette.ON_PRIMARY); 
                      cartButton.setForeground(Palette.PRIMARY);    
                          cartButton.setFocusPainted(false);
                               add(cartButton, BorderLayout.EAST);   
    }

    public JButton getCartButton() {
        return cartButton;
    }
    
    public void updateCartCount(int count) {
        cartItemCount = count;
        cartButton.setText("🛒 Cart (" + count + ")");
    }
    
    public int getCartItemCount() {
        return cartItemCount;
    }
    public static JButton creatButton(String text, int [] colors, int width, int hight ) {
    	//Also here no need to change anything its dynamic
		 JButton button = new JButton(text) {
	            @Override
	            protected void paintComponent(Graphics g) {
	                Graphics2D g2 = (Graphics2D) g.create();
	                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON); // making the edges smooth
	                if (getModel().isPressed()) {
	                    g2.setColor(new Color(colors[0])); //Pressed state
	                } else if (getModel().isRollover()) {
	                    g2.setColor(new Color(colors[1])); //Hover state
	                } else {
	                    g2.setColor(new Color(colors[2])); //Normal state
	                }
	                g2.fill(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 30, 30)); // rounded background (width, height, corner-radius)
	                g2.setColor(Color.WHITE);
	                g2.setFont(getFont().deriveFont(Font.BOLD, 16f));
	                FontMetrics fm = g2.getFontMetrics();
	                int x = (getWidth() - fm.stringWidth(getText())) / 2;
	                int y = (getHeight() + fm.getAscent()) / 2 - 3;
	                g2.drawString(getText(), x, y);
	                g2.dispose();
	            }

	            @Override
	            protected void paintBorder(Graphics g) {} //Remove default border
	        };
	        button.setFocusPainted(false);
	        button.setContentAreaFilled(false);
	        button.setBorderPainted(false);
	        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
	        button.setPreferredSize(new Dimension(width, hight)); 
	        return button;

	}
}