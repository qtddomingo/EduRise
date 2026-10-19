package edurise.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/** Shared JDBC logic for every repository, written once using generics. */
public abstract class BaseRepository<T> implements Repository<T> {

    protected abstract String table();
    protected abstract T map(ResultSet rs) throws SQLException;   // row -> object

    // PreparedStatement keeps all queries safe from SQL injection
    protected List<T> query(String sql, Object... params) {
        try (Connection c = Database.connect(); PreparedStatement ps = c.prepareStatement(sql)) {
            for (int i = 0; i < params.length; i++) ps.setObject(i + 1, params[i]);
            try (ResultSet rs = ps.executeQuery()) {
                List<T> rows = new ArrayList<>();
                while (rs.next()) rows.add(map(rs));
                return rows;
            }
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    protected boolean update(String sql, Object... params) {
        try (Connection c = Database.connect(); PreparedStatement ps = c.prepareStatement(sql)) {
            for (int i = 0; i < params.length; i++) ps.setObject(i + 1, params[i]);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public List<T> findAll() {
        return query("SELECT * FROM " + table() + " ORDER BY id");
    }

    @Override
    public Optional<T> findById(int id) {
        return query("SELECT * FROM " + table() + " WHERE id = ?", id).stream().findFirst();
    }
}
