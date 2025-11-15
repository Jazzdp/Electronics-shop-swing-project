package shop.ui;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import shop.util.Palette;


public class SearchPanel extends JPanel {

    private final JButton listView;
    private final JTextField searchField;

    public SearchPanel() {        
        setLayout(new BorderLayout());   
        setBackground(Palette.SURFACE_ALT);   
        setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));   
        JPanel inner = new JPanel(new BorderLayout(8, 0));     
        inner.setOpaque(false);     
        searchField = new JTextField();
        searchField.setBackground(Palette.SEARCH_BG);   
        searchField.setFont(new Font("SansSerif", Font.PLAIN, 14));
        searchField.setBorder(BorderFactory.createCompoundBorder(          
            new LineBorder(Palette.CARD_BORDER, 2, true),         
            BorderFactory.createEmptyBorder(6, 8, 6, 8)));     
        inner.add(searchField, BorderLayout.CENTER); 
        int[] listCols = {0x0F172A, 0x1E293B, 0x0F172A}; 
        listView = ProductCardPanel.creatButton("List view", listCols, 110, 36);  
        inner.add(listView, BorderLayout.EAST);    
        add(inner, BorderLayout.CENTER);  
    }

    public JButton getListViewButton() {
        return listView;
    }

    public JTextField getSearchField() {
        return searchField;
    }
        }