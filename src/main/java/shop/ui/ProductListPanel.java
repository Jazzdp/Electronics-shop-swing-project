package shop.ui;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
public class ProductListPanel extends JFrame {

    public ProductListPanel() {
    JPanel panel = createProductPanel(1200,800);
     setLayout(new FlowLayout());
     add(panel);
     setVisible(true);
     pack();
    }
    public static void main(String[]arg) {
    	new ProductListPanel();
    }
    public static JPanel createProductPanel(int width, int height) {
        JPanel panel = new JPanel() {
         
        };
        //Assembling part
        

        return panel;
    }
}

