import java.sql.*;

public class Database {
    private static Connection conn;

    public static void connect() {
        try {

            conn = DriverManager.getConnection("jdbc:sqlite:guessing_game.db");
            conn.setAutoCommit(false);
            System.out.println("Connected to the database.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void createTable() {
        String sql = "CREATE TABLE IF NOT EXISTS results (" +
                "name TEXT, " +
                "time DOUBLE, " +
                "guesses INTEGER)";
        try (Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            System.out.println("Table 'results' created or already exists.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void insertResult(String name, double time, int guesses) {
        String sql = "INSERT INTO results(name, time, guesses) VALUES(?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);
            pstmt.setDouble(2, time);
            pstmt.setInt(3, guesses);
            pstmt.executeUpdate();
            conn.commit();
            System.out.println("Inserted result: " + name + ", Time: " + time + ", Guesses: " + guesses);
        } catch (SQLException e) {
            System.out.println("Error inserting data: " + e.getMessage());
        }
    }

    public static void showBestScore() {
        String sql = "SELECT name, time, guesses FROM results ORDER BY time, guesses LIMIT 1";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                System.out.println("Best Score: " + rs.getString("name"));
                System.out.println("Time: " + rs.getDouble("time") + " seconds");
                System.out.println("Guesses: " + rs.getInt("guesses"));
            } else {
                System.out.println("No records found.");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void close() {
        try {
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
