package shop.ui;

import javax.swing.*;
import java.awt.*;
import shop.util.Palette;


public class CategoryPanel extends JPanel {
   
private static final int[] btnCols = {
    Palette.PRIMARY.getRGB(),
    Palette.ON_PRIMARY.getRGB()
};

  public CategoryPanel() {     
    setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));     
   setBackground(Palette.SURFACE);       
    setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));     
       JLabel heading = new JLabel("Categories");  
             heading.setAlignmentX(Component.LEFT_ALIGNMENT);   
                 heading.setFont(heading.getFont().deriveFont(Font.BOLD, 14f)); 
                   add(heading);   
 add(Box.createRigidArea(new Dimension(0, 8)));      
   String[] cats = {"All", "Phones", "Laptops", "Ipads", "Audio", "Accessories", "Smart Watches"};     
      for (String c : cats) {     
                   JButton b = ProductCardPanel.creatButton(c, btnCols, 160, 36);       
                     b.setAlignmentX(Component.LEFT_ALIGNMENT);           
                      b.setMaximumSize(new Dimension(160,36));      
                     b.setFocusPainted(false);       
          add(b);        
            add(Box.createRigidArea(new Dimension(0, 6)));        }    }}
