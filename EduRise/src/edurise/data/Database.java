package edurise.data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public final class Database {
    // To use MySQL instead, change this URL (e.g. jdbc:mysql://localhost:3306/edurise)
    // and add the MySQL driver; AUTOINCREMENT becomes AUTO_INCREMENT.
    private static final String URL = "jdbc:sqlite:edurise.db";

    private Database() { }

    public static Connection connect() throws SQLException {
        return DriverManager.getConnection(URL);
    }

    /** Creates the tables on first run and loads the sample questions. */
    public static void init() {
        try (Connection c = connect(); Statement s = c.createStatement()) {
            s.execute("CREATE TABLE IF NOT EXISTS users ("
                    + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + "username TEXT UNIQUE NOT NULL, full_name TEXT NOT NULL, password_hash TEXT NOT NULL)");
            s.execute("CREATE TABLE IF NOT EXISTS questions ("
                    + "id INTEGER PRIMARY KEY AUTOINCREMENT, topic TEXT, stage INTEGER, "
                    + "type TEXT, text TEXT, options TEXT, answer TEXT)");
            s.execute("CREATE TABLE IF NOT EXISTS results ("
                    + "id INTEGER PRIMARY KEY AUTOINCREMENT, user_id INTEGER, topic TEXT, stage INTEGER, "
                    + "correct INTEGER, total INTEGER, score INTEGER, taken_at TEXT)");
        } catch (SQLException e) {
            throw new IllegalStateException("Database setup failed", e);
        }
        new QuestionRepository().seedIfEmpty();
    }
}
