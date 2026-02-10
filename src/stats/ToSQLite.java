package stats;

import java.io.BufferedReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.*;

public final class ToSQLite {

    private ToSQLite() {}

    public static void export(Path csvPath, Path dbPath) throws Exception {

        String url = "jdbc:sqlite:" + dbPath.toAbsolutePath();

        try (Connection conn = DriverManager.getConnection(url)) {
            createTable(conn);
            loadCsv(conn, csvPath);
        }
    }

    private static void createTable(Connection conn) throws SQLException {
        String ddl = """
            DROP TABLE IF EXISTS customer_summary;
            CREATE TABLE customer_summary (
              date TEXT,
              storeId INTEGER,
              customerId INTEGER,
              boughtMilk INTEGER,
              boughtCereal INTEGER,
              boughtBabyFood INTEGER,
              boughtDiapers INTEGER,
              boughtBread INTEGER,
              boughtPB INTEGER,
              boughtJam INTEGER
            );
            """;
        try (Statement st = conn.createStatement()) {
            st.executeUpdate(ddl);
        }
    }
    private static void loadCsv(Connection conn, Path csvPath) throws Exception {
        String insert = """
            INSERT INTO customer_summary
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
            """;
        try (PreparedStatement ps = conn.prepareStatement(insert);
             BufferedReader br = Files.newBufferedReader(csvPath)) {

            br.readLine();

            String line;
            while ((line = br.readLine()) != null) {
                String[] t = line.split(",");

                ps.setString(1, t[0]);
                ps.setInt(2, Integer.parseInt(t[1]));
                ps.setInt(3, Integer.parseInt(t[2]));

                for (int i = 3; i < t.length; i++) {
                    ps.setInt(i + 1, Integer.parseInt(t[i]));
                }
                ps.addBatch();
            }
            ps.executeBatch();
        }
    }
}
