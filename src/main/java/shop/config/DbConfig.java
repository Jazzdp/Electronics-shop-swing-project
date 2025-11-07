package shop.config;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DbConfig {
    // kept public API the same; use classpath-root resource name
    private static final String PROPERTIES_FILE = "app.properties";
    private static Properties properties;
    private static Connection connection;

    static {
        loadProperties();
    }

    private static void loadProperties() {
        properties = new Properties();

        // Try class loader first (works for Gradle, IDEs and fat jars)
        try (InputStream input = Thread.currentThread()
                                         .getContextClassLoader()
                                         .getResourceAsStream(PROPERTIES_FILE)) {
            if (input == null) {
                // fallback: try relative to this class's package (very safe)
                try (InputStream input2 = DbConfig.class.getResourceAsStream("/" + PROPERTIES_FILE)) {
                    if (input2 == null) {
                        throw new RuntimeException("Unable to find " + PROPERTIES_FILE + " on the classpath. "
                                + "Make sure it is located at src/main/resources/" + PROPERTIES_FILE);
                    }
                    properties.load(input2);
                }
            } else {
                properties.load(input);
            }

            // Try loading JDBC driver if provided (non-fatal)
            String driver = properties.getProperty("db.driver");
            if (driver != null && !driver.isBlank()) {
                try {
                    Class.forName(driver);
                } catch (ClassNotFoundException e) {
                    // warn but allow UI-only runs
                    System.err.println("Warning: JDBC driver class not found on classpath: " + driver + " (" + e.getMessage() + ")");
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Error loading database properties from " + PROPERTIES_FILE, e);
        }
    }

    /**
     * Returns a JDBC Connection using the configured properties.
     * Same signature as before.
     */
    public static Connection getConnection() throws SQLException {
        if (properties == null) {
            loadProperties(); // try to recover
        }

        String url = properties.getProperty("db.url");
        String user = properties.getProperty("db.username");
        String pass = properties.getProperty("db.password");

        if (url == null || url.isBlank()) {
            throw new SQLException("DB URL not configured (db.url missing in " + PROPERTIES_FILE + ")");
        }

        // create or reuse connection
        if (connection == null || connection.isClosed()) {
            connection = DriverManager.getConnection(url, user, pass);
        }
        return connection;
    }

    // unchanged accessors for DbInit and other classes
    public static String getUrl() {
        if (properties == null) loadProperties();
        return properties.getProperty("db.url");
    }

    public static String getUsername() {
        if (properties == null) loadProperties();
        return properties.getProperty("db.username");
    }

    public static String getPassword() {
        if (properties == null) loadProperties();
        return properties.getProperty("db.password");
    }

    public static String getProperty(String key) {
        if (properties == null) loadProperties();
        return properties.getProperty(key);
    }

    public static void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                throw new RuntimeException("Error closing database connection", e);
            } finally {
                connection = null;
            }
        }
    }
}
