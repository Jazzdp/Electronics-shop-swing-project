package shop.config;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DbConfig {
    private static final String PROPERTIES_FILE = "/resources/app.properties";
    private static Properties properties;
    private static Connection connection;

    static {
        loadProperties();
    }

    private static void loadProperties() {
        properties = new Properties();
        try (InputStream input = DbConfig.class.getResourceAsStream(PROPERTIES_FILE)) {
            if (input == null) {
                throw new RuntimeException("Unable to find " + PROPERTIES_FILE);
            }
            properties.load(input);
            
            // Load the JDBC driver
            Class.forName(properties.getProperty("db.driver"));
        } catch (IOException e) {
            throw new RuntimeException("Error loading database properties", e);
        } catch (ClassNotFoundException e) {
            // JDBC driver not found on classpath. Allow application to start for UI-only
            // runs; connection attempts will still fail if no driver is present.
            System.err.println("Warning: JDBC driver not found on classpath: " + e.getMessage());
        }
    }

    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            connection = DriverManager.getConnection(
                properties.getProperty("db.url"),
                properties.getProperty("db.username"),
                properties.getProperty("db.password")
            );
        }
        return connection;
    }

    // Helpful accessors for other classes (e.g. migration/bootstrap code)
    public static String getUrl() {
        return properties.getProperty("db.url");
    }

    public static String getUsername() {
        return properties.getProperty("db.username");
    }

    public static String getPassword() {
        return properties.getProperty("db.password");
    }

    /**
     * Generic accessor for properties loaded from app.properties.
     */
    public static String getProperty(String key) {
        return properties.getProperty(key);
    }

    public static void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                throw new RuntimeException("Error closing database connection", e);
            }
        }
    }
}
