package shop;

import shop.config.DbConfig;
import shop.config.DbInit;
import shop.controllers.ProductController;
import shop.repositories.ProductRepository;
import shop.ui.MainFrame;
import javax.swing.SwingUtilities;
import java.sql.Connection;

public class Main {
  public static void main(String[] args) {
    System.out.println("Welcome to the Shop Application!");
    try {
      // Try to run Flyway migrations if Flyway is on the classpath.
      try {
        String url = DbConfig.getUrl();
        String user = DbConfig.getUsername();
        String pass = DbConfig.getPassword();
        Class.forName("org.flywaydb.core.Flyway");
        // Use reflection so this code still compiles/runs if Flyway isn't present for manual javac runs.
        Class<?> flywayClass = Class.forName("org.flywaydb.core.Flyway");
    
        java.lang.reflect.Method configure = flywayClass.getMethod("configure");
        Object config = configure.invoke(null);
        java.lang.reflect.Method dataSource = config.getClass().getMethod("dataSource", String.class, String.class, String.class);
        Object configured = dataSource.invoke(config, url, user, pass);
        java.lang.reflect.Method load = configured.getClass().getMethod("load");
        Object flyway = load.invoke(configured);
    // Attempt to run Flyway migrations
    try {
      java.lang.reflect.Method migrate = flyway.getClass().getMethod("migrate");
      System.out.println("Running DB migrations (Flyway)...");
      migrate.invoke(flyway);
      System.out.println("Migrations finished.");
    } catch (Exception mfe) {
      System.err.println("Error running migrations: " + mfe.getMessage());
      mfe.printStackTrace();
    }
      } catch (ClassNotFoundException cnf) {
        System.out.println("Flyway not found on classpath; skipping DB migrations. Use Gradle to run migrations or build the fat JAR.");
      } catch (Exception mfe) {
        System.err.println("Error running migrations: " + mfe.getMessage());
        mfe.printStackTrace();
      }

      // Run optional DbInit (opt-in) if requested via system property or environment variable
      try {
        DbInit.runIfRequested();
      } catch (Throwable t) {
        System.err.println("DbInit failed: " + t.getMessage());
        t.printStackTrace();
      }

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
