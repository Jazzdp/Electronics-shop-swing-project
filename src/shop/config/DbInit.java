package shop.config;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class DbInit {

    private static final String MIGRATION_RESOURCE = "/db/migration/V1__create_schema.sql";

    /**
     * Run the SQL script on the configured datasource if the `db.init` property is set to true.
     * This is intentionally opt-in to avoid accidental changes on production databases.
     */
    public static void runIfRequested() {
        String init = System.getProperty("db.init");
        if (init == null) {
            init = System.getenv("DB_INIT");
        }
        if (init == null) {
            // Try properties loaded by DbConfig
            try {
                init = DbConfig.getProperty("db.init");
            } catch (Exception ignored) {
                init = null;
            }
        }

        boolean shouldInit = "true".equalsIgnoreCase(init);
        if (!shouldInit) {
            return;
        }

        // safety guard: only run if DB URL points to localhost (prevent accidental remote DB changes)
        try {
            String url = DbConfig.getUrl();
            if (url == null || !(url.contains("localhost") || url.contains("127.0.0.1"))) {
                System.err.println("DbInit: db.init=true but DB URL is not localhost — skipping for safety: " + url);
                return;
            }
        } catch (Exception e) {
            System.err.println("DbInit: unable to read DB URL, skipping init: " + e.getMessage());
            return;
        }

        System.out.println("DbInit: running SQL migration script " + MIGRATION_RESOURCE);
        try (InputStream in = DbInit.class.getResourceAsStream(MIGRATION_RESOURCE)) {
            if (in == null) {
                System.err.println("DbInit: migration resource not found: " + MIGRATION_RESOURCE);
                return;
            }
            String sql = readAll(in);
            executeSqlScript(sql);
            System.out.println("DbInit: migration script executed");
        } catch (IOException e) {
            System.err.println("DbInit: I/O error reading migration script: " + e.getMessage());
        }
    }

    private static String readAll(InputStream in) throws IOException {
        StringBuilder sb = new StringBuilder();
        try (BufferedReader r = new BufferedReader(new InputStreamReader(in))) {
            String line;
            while ((line = r.readLine()) != null) {
                sb.append(line).append('\n');
            }
        }
        return sb.toString();
    }

    private static void executeSqlScript(String script) {
        // simple splitter on semicolons; assumes statements end with ; on their own line
        String[] statements = script.split(";\\s*\n");
        try (Connection conn = DbConfig.getConnection()) {
            boolean auto = conn.getAutoCommit();
            try {
                conn.setAutoCommit(false);
                try (Statement st = conn.createStatement()) {
                    for (String s : statements) {
                        String trimmed = s.trim();
                        if (trimmed.isEmpty()) continue;
                        st.execute(trimmed);
                    }
                }
                conn.commit();
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            } finally {
                conn.setAutoCommit(auto);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error executing DB init script", e);
        }
    }
}
