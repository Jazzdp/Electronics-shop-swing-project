package shop.ui;

import shop.controllers.ProductController;
import javax.swing.JFrame;

public class MainFrame extends JFrame {
	private final ProductController productController;

	public MainFrame(ProductController productController) {
		super("Electronics Shop");
		this.productController = productController;
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(800, 600);
		setLocationRelativeTo(null);
		// Minimal placeholder UI; real UI components can be added later
	}
}
