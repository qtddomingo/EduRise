package edurise.data;

import edurise.model.User;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class UserRepository extends BaseRepository<User> {

    @Override protected String table() { return "users"; }

    @Override
    protected User map(ResultSet rs) throws SQLException {
        return new User(rs.getInt("id"), rs.getString("username"),
                rs.getString("full_name"), rs.getString("password_hash"));
    }

    @Override
    public boolean save(User u) {
        return update("INSERT INTO users(username, full_name, password_hash) VALUES(?,?,?)",
                u.getUsername(), u.getFullName(), u.getPasswordHash());
    }

    public Optional<User> findByUsername(String username) {
        return query("SELECT * FROM users WHERE username = ?", username).stream().findFirst();
    }
}
