package shop.ui;

import javax.swing.*; 

import Components.Tag;
import Components.Button;
import Components.TextLabel;
import Components.ImageContainer;
import Components.Panel;
import java.awt.image.BufferedImage;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import shop.controllers.ProductController;
import shop.model.Product;
import java.net.URL;
import java.util.List;

public class ProductCardPanel extends JPanel {
    private final ProductController productController;
    private JPanel cardsContainer;
    public ProductCardPanel(ProductController productController) {
        this.productController = productController;
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        // Create scrollable container for product cards
        cardsContainer = new JPanel();
        cardsContainer.setLayout(new GridLayout(3,10,10,10));
        cardsContainer.setBackground(Color.WHITE);

        JScrollPane scrollPane = new JScrollPane(cardsContainer);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        
        add(scrollPane, BorderLayout.CENTER);

        // Load all products on initialization
        loadAllProducts();
    }

    // Load all products from database
    public void loadAllProducts() {
        cardsContainer.removeAll();
        List<Product> products = productController.getAllProducts();
        
        if (products == null || products.isEmpty()) {
            JLabel emptyLabel = new JLabel("No products available");
            emptyLabel.setFont(new Font("SansSerif", Font.PLAIN, 18));
            emptyLabel.setForeground(Color.GRAY);
            cardsContainer.add(emptyLabel);
        } else {
            for (Product product : products) {
                JPanel card = createProductCardFromModel(product);
                cardsContainer.add(card);
            }
        }
        
        cardsContainer.revalidate();
        cardsContainer.repaint();
    }

    // Load products by category
    public void loadProductsByCategory(String category) {
        cardsContainer.removeAll();
        List<Product> products = productController.getProductsByCategory(category);
        
        if (products == null || products.isEmpty()) {
            JLabel emptyLabel = new JLabel("No products found in " + category);
            emptyLabel.setFont(new Font("SansSerif", Font.PLAIN, 18));
            emptyLabel.setForeground(Color.GRAY);
            cardsContainer.add(emptyLabel);
        } else {
            for (Product product : products) {
                JPanel card = createProductCardFromModel(product);
                cardsContainer.add(card);
            }
        }
        
        cardsContainer.revalidate();
        cardsContainer.repaint();
    }

 // Create product card from Product model
    private JPanel createProductCardFromModel(Product product) {
        Image img = loadImage(product.getPicUrl());

        String warrantyText = product.getWarrantyMonths() + " " +
                (product.getWarrantyMonths() == 1 ? "Month" : "Months");

        // Adaptation : respecter l’ordre et le nombre de paramètres exacts de createProductCard
        return createProductCard(
            img,                         // Image
            product.getName(),            // productName
                      
            product.getCategory(),        // tagText
            product.getModelNumber(),     // model
            warrantyText,                 // warranty
            product.getPrice()            // price
        );
    }
    private Image getDefaultImage() {
        Image img = new BufferedImage(200, 200, BufferedImage.TYPE_INT_RGB);
        Graphics g = img.getGraphics();
        g.setColor(Color.LIGHT_GRAY);
        g.fillRect(0, 0, 200, 200);
        g.setColor(Color.DARK_GRAY);
        g.drawString("No Image", 70, 100);
        g.dispose();
        return img;
    }

    // Load image from URL or file path
    private Image loadImage(String picUrl) {
        try {
            if (picUrl == null || picUrl.isEmpty()) {
                return getDefaultImage();
            }

            ImageIcon icon;

            // Si c’est une URL distante
            if (picUrl.startsWith("http://") || picUrl.startsWith("https://")) {
                URL url = new URL(picUrl);
                icon = new ImageIcon(url);
            } else {
                // Sinon, on tente en local (fichier)
                icon = new ImageIcon(picUrl);
            }

            Image img = icon.getImage();
            if (img == null || icon.getIconWidth() <= 0 || icon.getIconHeight() <= 0) {
                return getDefaultImage();
            }

            return img;
        } catch (Exception e) {
            System.err.println("Error loading image: " + picUrl + " — " + e.getMessage());
            return getDefaultImage();
        }
    }

    public static JPanel createProductCard(Image productImage, String productName, String tagText, String model, String warranty, double price) {
        JPanel card = new Panel(350,560);
        JPanel content = (JPanel) card.getComponent(0);
        content.setAlignmentX(Component.LEFT_ALIGNMENT);
        content.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));

        //Creating the image component (u can modify the parms values)
        JPanel imagePanel = new ImageContainer(productImage, 310, 260);
        imagePanel.setAlignmentX(Component.CENTER_ALIGNMENT); 
        imagePanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 260)); 
        content.add(imagePanel);
        content.add(Box.createVerticalStrut(15));

        //Filling available width and align left (DO NOT TOUCH IT)
        java.util.function.Consumer<JLabel> prepareLeft = lbl -> {
            lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
            lbl.setHorizontalAlignment(SwingConstants.LEFT);
            lbl.setMaximumSize(new Dimension(Integer.MAX_VALUE, lbl.getPreferredSize().height));
        };

        // Adding the product name (u guys are free to change parms too)
        JLabel textProductName = new TextLabel(productName, "SansSerif", 18, Color.BLACK, JLabel.LEFT, Font.BOLD);
        prepareLeft.accept(textProductName);
        content.add(textProductName);
        content.add(Box.createVerticalStrut(5));


        //Creating the tag that contains the product type(DO NOT TOUCH IT)
        JLabel tagLabel = new Tag(tagText);
        tagLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        tagLabel.setMaximumSize(tagLabel.getPreferredSize()); 
        content.add(tagLabel);
        content.add(Box.createVerticalStrut(10));

        // Adding the model and the warranty of the product (u can also change params here)
        JLabel modelLabel = new TextLabel("Model: " + model, "SansSerif", 14, new Color(0x555555), JLabel.LEFT, Font.PLAIN);
        prepareLeft.accept(modelLabel);
        content.add(modelLabel);
        content.add(Box.createVerticalStrut(5));

        JLabel warrantyLabel = new TextLabel("Warranty: " + warranty, "SansSerif", 14, new Color(0x555555), JLabel.LEFT, Font.PLAIN);
        prepareLeft.accept(warrantyLabel);
        content.add(warrantyLabel);
        content.add(Box.createVerticalStrut(20));

        // Creating a bottom row so we can fit the price and button within the same horizontal space (Do not change it! i mean except for the row height but i do not recomand it, its perfect in my opinion)
        JPanel bottomRow = new JPanel();
        bottomRow.setOpaque(false);
        bottomRow.setLayout(new BoxLayout(bottomRow, BoxLayout.X_AXIS));
        bottomRow.setAlignmentX(Component.LEFT_ALIGNMENT);
        bottomRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60)); // Limit row height

        // Price text field u can guys modify the parms its fine as long as u respect the order
        JLabel priceLabel = new TextLabel("$" + String.format("%.2f", price), "SansSerif", 18, new Color(0x2FA84F), JLabel.LEFT, Font.BOLD);
        priceLabel.setAlignmentY(Component.CENTER_ALIGNMENT);
        priceLabel.setHorizontalAlignment(SwingConstants.LEFT);
        priceLabel.setMaximumSize(new Dimension(Integer.MAX_VALUE, priceLabel.getPreferredSize().height));
        bottomRow.add(priceLabel);
        bottomRow.add(Box.createHorizontalGlue());//This line pushes the button to the right side so do not touch it

        // Adding the button i can change params i tried to simplify the function utilisation as much as possible
        int[] btnColors = { 0x0F172A, 0x1E293B, 0x0F172A }; //Vector(color states) = {pressed, hover, normal} make sure to respect the orders otherwise ur not gonna get the wanted output
                                                           //Also i didn't add input handler so im trusting u guys to put hexadecimal values otherwise its not gonna give the exact wanted color
        JButton addToCartBtn = new Button("Add to Cart", btnColors, 150, 40);
        Dimension btnPref = addToCartBtn.getPreferredSize();
        addToCartBtn.setMaximumSize(new Dimension(btnPref.width, btnPref.height));
        addToCartBtn.setAlignmentY(Component.CENTER_ALIGNMENT);
        bottomRow.add(addToCartBtn);

       //do not touch this lines if ur curious what it does it only adds the bottom row inside the our main panel and refresh it
        content.add(bottomRow);
        card.revalidate();
        card.repaint();

        return card; //we're returning the final assembled product card panel measure de sécurité ? 0/10 u guys need to be careful in ur inputs other wise u will not get the results ur looking for feel free to reach me out when u face issues in customization
    }
}