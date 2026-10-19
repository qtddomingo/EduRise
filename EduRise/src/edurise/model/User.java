package edurise.model;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class User {
    private final int id;
    private final String username;
    private final String fullName;
    private final String passwordHash;

    public User(int id, String username, String fullName, String passwordHash) {
        this.id = id;
        this.username = username;
        this.fullName = fullName;
        this.passwordHash = passwordHash;
    }

    /** Passwords are never stored as plain text (SHA-256, salted with the username). */
    public static String hash(String username, String password) {
        try {
            byte[] bytes = MessageDigest.getInstance("SHA-256")
                    .digest((username + ":" + password).getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (byte b : bytes) sb.append(String.format("%02x", b));
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException(e);
        }
    }

    public int getId() { return id; }
    public String getUsername() { return username; }
    public String getFullName() { return fullName; }
    public String getPasswordHash() { return passwordHash; }
}
