package ihmprojet;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
public class ProductCardPanel {
	    //This versions is just a prototype so im gonna change it later
	    //i will apply OOP principles to it to simplify it and make it more readable
	    //Change some functions too to make it more generative and dynamic
	    //Add more comments so u guys can understand what's going on
	    //Optimize it and delete some dead code 
	    //Add input handlers and make sure everything under control (not trusting user input! :>)
	    //Also it contains some artifacts ill fix it later
	    public static void main(String[] args) {
	        JFrame f = new JFrame();
	        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	        f.setLayout(new FlowLayout());
	        Image img = new ImageIcon("photo-1631011714977-a6068c048b7b.jfif").getImage();
	        f.add(createProductCard(img, "iPHone 15 Pro", "Apple", "SmartPhones", "IP15P-256GB", "1 Years", 999.99));//example
	        f.pack();
	        f.setVisible(true);
	    }
	    public static JPanel createProductCard(Image productImage, String productName, String brand, String tagText, String model, String warranty, double price) {
	        JPanel card = createProductPanel(350, 560);
	        JPanel content = (JPanel) card.getComponent(0);
	        content.setAlignmentX(Component.LEFT_ALIGNMENT);
	        content.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));

	        // Creating the image component (u can modify the parms values)
	        JPanel imagePanel = createImagePanel(productImage, 310, 260);
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
	        JLabel textProductName = createProductCarteTextLabel(productName, "SansSerif", 18, Color.BLACK, JLabel.LEFT, Font.BOLD);
	        prepareLeft.accept(textProductName);
	        content.add(textProductName);
	        content.add(Box.createVerticalStrut(5));

	        //Adding the product brand (u guys are free to change parms too)
	        JLabel brandLabel = createProductCarteTextLabel(brand, "SansSerif", 15, new Color(0x555555), JLabel.LEFT, Font.PLAIN);
	        prepareLeft.accept(brandLabel);
	        content.add(brandLabel);
	        content.add(Box.createVerticalStrut(10));

	        //Creating the tag that contains the product type(DO NOT TOUCH IT)
	        JLabel tagLabel = createTagLabel(tagText);
	        tagLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
	        tagLabel.setMaximumSize(tagLabel.getPreferredSize()); 
	        content.add(tagLabel);
	        content.add(Box.createVerticalStrut(10));

	        // Adding the model and the warranty of the product (u can also change params here)
	        JLabel modelLabel = createProductCarteTextLabel("Model: " + model, "SansSerif", 14, new Color(0x555555), JLabel.LEFT, Font.PLAIN);
	        prepareLeft.accept(modelLabel);
	        content.add(modelLabel);
	        content.add(Box.createVerticalStrut(5));

	        JLabel warrantyLabel = createProductCarteTextLabel("Warranty: " + warranty, "SansSerif", 14, new Color(0x555555), JLabel.LEFT, Font.PLAIN);
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
	        JLabel priceLabel = createProductCarteTextLabel("$" + String.format("%.2f", price), "SansSerif", 18, new Color(0x2FA84F), JLabel.LEFT, Font.BOLD);
	        priceLabel.setAlignmentY(Component.CENTER_ALIGNMENT);
	        priceLabel.setHorizontalAlignment(SwingConstants.LEFT);
	        priceLabel.setMaximumSize(new Dimension(Integer.MAX_VALUE, priceLabel.getPreferredSize().height));
	        bottomRow.add(priceLabel);
	        bottomRow.add(Box.createHorizontalGlue());//This line pushes the button to the right side so do not touch it

	        // Adding the button i can change params i tried to simplify the function utilisation as much as possible
	        int[] btnColors = { 0x0F172A, 0x1E293B, 0x0F172A }; //Vector(color states) = {pressed, hover, normal} make sure to respect the orders otherwise ur not gonna get the wanted output
	                                                           //Also i didn't add input handler so im trusting u guys to put hexadecimal values otherwise its not gonna give the exact wanted color
	        JButton addToCartBtn = creatButton("Add to Cart", btnColors, 150, 40);
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


	    public static JPanel createProductPanel(int width, int height) {
	        JPanel panel = new JPanel() {
	            @Override
	            protected void paintComponent(Graphics g) {
	                super.paintComponent(g);
                    //Do not change anything here its dynamic just give it the width and the height and it will give u ready panel
	                //maybe u can change the background color other then that do not touch it
	                Graphics2D g2 = (Graphics2D) g.create();
	                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
	                int arc = 25;
	                g2.setColor(new Color(0, 0, 0, 25));
	                g2.fill(new RoundRectangle2D.Double(3, 3, getWidth() - 6, getHeight() - 6, arc, arc));
	                g2.setColor(Color.WHITE);
	                g2.fill(new RoundRectangle2D.Double(0, 0, getWidth() - 6, getHeight() - 6, arc, arc));
	                g2.setColor(new Color(0xE5E5E5));
	                g2.setStroke(new BasicStroke(1f));
	                g2.draw(new RoundRectangle2D.Double(0, 0, getWidth() - 6, getHeight() - 6, arc, arc));

	                g2.dispose();
	            }

	            @Override
	            public Dimension getPreferredSize() {
	                return new Dimension(width, height);
	            }
	        };
            //Assembling part
	        panel.setOpaque(false);
	        panel.setLayout(new BorderLayout());
	        JPanel content = new JPanel();
	        content.setOpaque(false);
	        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
	        content.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); //Padding
	        panel.add(content, BorderLayout.CENTER);

	        return panel;
	    }
	    public static JPanel createImagePanel(Image image, int width, int height) {
	    	//Again its fully dynamic so do not change anything here
	    	//Just give it the image link, width and the height and it will render for u a ready image panel 
		    return new JPanel() {
		        @Override
		        protected void paintComponent(Graphics g) {
		            super.paintComponent(g);
		            Graphics2D g2 = (Graphics2D) g.create();
		            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		            int arc = 25; 
		            Shape clip = new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), arc, arc);
		            g2.setClip(clip);
		            g2.setColor(Color.WHITE);
		            g2.fill(clip);
		            if (image != null) {
		                double panelRatio = (double) getWidth() / getHeight();
		                double imgRatio = (double) image.getWidth(this) / image.getHeight(this);
		                int drawWidth, drawHeight;
		                if (panelRatio > imgRatio) {
		                    drawWidth = getWidth();
		                    drawHeight = (int) (getWidth() / imgRatio);
		                } else {
		                    drawHeight = getHeight();
		                    drawWidth = (int) (getHeight() * imgRatio);
		                }
		                int x = (getWidth() - drawWidth) / 2;
		                int y = (getHeight() - drawHeight) / 2;
		                g2.drawImage(image, x, y, drawWidth, drawHeight, this);
		            }
		            g2.setClip(null);
		            g2.setColor(new Color(0xE5E5E5));
		            g2.setStroke(new BasicStroke(1f));
		            g2.draw(clip);

		            g2.dispose();
		        }

		        @Override
		        public Dimension getPreferredSize() {
		            return new Dimension(width, height);
		        }
		    };
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
	    public static JLabel createTagLabel(String text) {
	    	//its not fully dynamic imma change it later 
	        final String labelText = text == null ? "" : text.trim();
	        JLabel label = new JLabel() {
	            @Override
	            protected void paintComponent(Graphics g) {
	                Graphics2D g2 = (Graphics2D) g.create();
	                try {
	                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
	                    g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

	                   //u guys are free to change the colors here depending on ur needs
	                    Color bg = new Color(0xCFCFD7);       //This one is for the pill background
	                    Color border = new Color(0xE6E6E6);   //This one for the subtle border
	                    Color textColor = new Color(0x2E2E37); //And no need to explain this one too

	                    //U can also change the font here depending on what u want it to be
	                    Font f = getFont().deriveFont(Font.PLAIN, 13f);
	                    g2.setFont(f);
	                    FontMetrics fm = g2.getFontMetrics();

	                    //Padding do not change it except if u really know what ur doing
	                    int padH = 14; //Horizontal padding
	                    int padV = 6;  //Vertical padding
                        //Text field do not change it its perfect
	                    int textW = fm.stringWidth(labelText);
	                    int textH = fm.getAscent();
	                    int w = textW + padH * 2;
	                    int h = fm.getHeight() + padV * 2;
	                    if (getWidth() < w || getHeight() < h) {
	                        // requested size may be used by layout manager
	                        // don't fail—we'll draw within current size but keep centering logic
	                    }
	                    int arc = h;
	                    //Drawing the background do not change anything here
	                    int bx = 0;
	                    int by = 0;
	                    int bw = Math.max(1, getWidth());
	                    int bh = Math.max(1, getHeight());
	                    int pillW = Math.min(bw, w);
	                    int pillH = Math.min(bh, h);
	                    int pillX = (bw - pillW) / 2;
	                    int pillY = (bh - pillH) / 2;
	                    RoundRectangle2D pill = new RoundRectangle2D.Double(pillX, pillY, pillW - 1, pillH - 1, arc, arc);
	                    g2.setColor(bg);
	                    g2.fill(pill);
	                    g2.setColor(border);
	                    g2.setStroke(new BasicStroke(1f));
	                    g2.draw(pill);
	                    //Drawing the text inside it
	                    g2.setColor(textColor);
	                    int tx = pillX + (pillW - textW) / 2;
	                    int ty = pillY + (pillH - fm.getHeight()) / 2 + fm.getAscent();
	                    g2.drawString(labelText, tx, ty);
	                } finally {
	                    g2.dispose();
	                }
	            }

	            @Override
	            public Dimension getPreferredSize() {  
	                Font f = getFont().deriveFont(Font.PLAIN, 13f);
	                FontMetrics fm = getFontMetrics(f);
	                int padH = 14;
	                int padV = 6;
	                int w = fm.stringWidth(labelText) + padH * 2;
	                int h = fm.getHeight() + padV * 2;
	                return new Dimension(w, h);
	            }
	        };
	        label.setOpaque(false);
	        label.setFont(new Font("SansSerif", Font.PLAIN, 13));
	        label.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
	        label.setToolTipText(labelText); //Extra feature it does not help in anything it just describe what is written with the same text that was written inside the pill, u can add ur own explanation tho 
	        label.setAlignmentX(Component.LEFT_ALIGNMENT);
	        label.setMaximumSize(label.getPreferredSize());
	        return label;
	    }
	    public static JLabel createProductCarteTextLabel(String text, String font, int TextSize, Color TextColor, int Alignment, int  FontStyle) {
		   //No need to explain this one its fully dynamic so do not change it
	    	JLabel label = new JLabel(text) {
		        @Override
		        protected void paintComponent(Graphics g) {
		            Graphics2D g2 = (Graphics2D) g.create();
		            g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		            super.paintComponent(g2);
		            g2.dispose();
		        }
		    };
		    label.setFont(new Font(font, FontStyle, TextSize));
		    label.setForeground(TextColor);
		    label.setHorizontalAlignment(Alignment);
		    label.setOpaque(false);
		    return label;
		}

}
