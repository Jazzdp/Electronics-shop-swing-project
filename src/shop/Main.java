package shop;

import shop.config.DbConfig;
import shop.controllers.ProductController;
import shop.repositories.ProductRepository;
import shop.ui.MainFrame;
import javax.swing.SwingUtilities;
import java.sql.Connection;

public class Main {
  public static void main(String[] args) {
    System.out.println("Welcome to the Shop Application!");
    try {
      Connection conn = DbConfig.getConnection();
      ProductRepository productRepository = new ProductRepository(conn);
      ProductController productController = new ProductController(productRepository);

      SwingUtilities.invokeLater(() -> {
        new MainFrame(productController).setVisible(true);
      });
    } catch (Exception e) {
      e.printStackTrace();
      System.exit(1);
    }
  }
}
