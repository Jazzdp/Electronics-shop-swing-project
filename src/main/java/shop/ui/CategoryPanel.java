package shop.ui;

import javax.swing.*;
import java.awt.*;
import shop.util.Palette;


/*public class CategoryPanel extends JPanel {
   
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
   String[] cats = {"All", "Phones", "Laptops", "Tablets", "Audio", "Accessories", "Consoles"};     
      for (String c : cats) {     
                   JButton b = ProductCardPanel.creatButton(c, btnCols, 160, 36);       
                     b.setAlignmentX(Component.LEFT_ALIGNMENT);           
                      b.setMaximumSize(new Dimension(160,36));      
                     b.setFocusPainted(false);       
          add(b);        
            add(Box.createRigidArea(new Dimension(0, 6)));     
           } 
            }
        
        
        
        }*/
     import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.function.Consumer;
import javax.swing.*;

public class CategoryPanel extends JPanel {
   
    private static final int[] btnCols = {
        0x0F172A, 0x1E293B, 0x0F172A
    };

    private Consumer<String> categorySelectedCallback;

    public CategoryPanel(Consumer<String> callback) {
        this.categorySelectedCallback = callback;
        
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));     
        setBackground(Palette.SURFACE);       
        setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));     
        JLabel heading = new JLabel("Categories");  
        heading.setAlignmentX(Component.LEFT_ALIGNMENT);   
        heading.setFont(heading.getFont().deriveFont(Font.BOLD, 14f)); 
        add(heading);   
        add(Box.createRigidArea(new Dimension(0, 8)));      
        String[] cats = {"All", "Phones", "Laptops", "Tablets", "Audio", "Accessories", "Consoles"};     
        for (String c : cats) {     
            JButton b = ProductCardPanel.creatButton(c, btnCols, 160, 36);       
            b.setAlignmentX(Component.LEFT_ALIGNMENT);           
            b.setMaximumSize(new Dimension(160, 36));      
            b.setFocusPainted(false);
            // Add action listener to filter products when button is clicked
            b.addActionListener(e -> categorySelectedCallback.accept(c));
            add(b);        
            add(Box.createRigidArea(new Dimension(0, 6)));     
        } 
    }
}