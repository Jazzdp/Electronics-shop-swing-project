package shop.ui;

import javax.swing.*;
import Components.Button;
import java.awt.*;
import shop.util.Palette;


public class CategoryPanel extends JPanel {
   
private static final int[] btnCols = {
		0x0F172A, 0x1E293B, 0x0F172A
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
                   JButton b = new Button(c, btnCols, 160, 36);       
                     b.setAlignmentX(Component.LEFT_ALIGNMENT);           
                      b.setMaximumSize(new Dimension(160,36));      
                     b.setFocusPainted(false);       
          add(b);        
            add(Box.createRigidArea(new Dimension(0, 6)));        }    }}
