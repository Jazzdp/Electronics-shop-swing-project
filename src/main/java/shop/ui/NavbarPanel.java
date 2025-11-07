package shop.ui;

import javax.swing.*;
import java.awt.*;
import shop.util.Palette;

public class NavbarPanel extends JPanel {

    public NavbarPanel() {      
          setLayout(new BorderLayout());    
            setBackground(Palette.PRIMARY);    
            setBorder(BorderFactory.createEmptyBorder(10, 12, 10, 12));    
           JLabel title =new JLabel("ElectroShop");    
               title.setForeground(Palette.ON_PRIMARY);     
                  title.setFont(title.getFont().deriveFont(Font.BOLD,18f));      
                    add(title, BorderLayout.WEST);     
                       JButton cartButton = new JButton("Cart");      
                     cartButton.setBackground(Palette.ON_PRIMARY); 
                      cartButton.setForeground(Palette.PRIMARY);    
                          cartButton.setFocusPainted(false);     
                               add(cartButton, BorderLayout.EAST);    }}