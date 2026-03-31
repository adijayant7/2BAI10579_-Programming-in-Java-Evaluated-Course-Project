package banking.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

/**
 * Utility methods shared across the application.
 */
public class BankingUtils {

    private BankingUtils() { /* utility class */ }

    // ── Password hashing ──────────────────────────────────────────────────

    /**
     * Returns the SHA-256 hex digest of the given plain-text password.
     */
    public static String hashPassword(String plainText) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(plainText.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (byte b : hash) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 not available", e);
        }
    }

    /**
     * Verifies a plain-text password against a stored hash.
     */
    public static boolean verifyPassword(String plainText, String storedHash) {
        return hashPassword(plainText).equals(storedHash);
    }

    // ── Account number generation ─────────────────────────────────────────

    /**
     * Generates a unique-ish 12-character account number, e.g. "ACC000472918".
     * The DAO will verify uniqueness before inserting.
     */
    public static String generateAccountNumber() {
        Random rand = new Random();
        long num = (long)(rand.nextDouble() * 900_000_000L) + 100_000_000L;
        return "ACC" + num;
    }

    // ── Validation helpers ────────────────────────────────────────────────

    /** Basic email format check. */
    public static boolean isValidEmail(String email) {
        return email != null && email.matches("^[\\w.+-]+@[\\w-]+\\.[\\w.]+$");
    }

    /** Phone must be 10 digits (Indian format). */
    public static boolean isValidPhone(String phone) {
        return phone != null && phone.matches("^[6-9]\\d{9}$");
    }

    /** Password must be at least 6 characters. */
    public static boolean isValidPassword(String password) {
        return password != null && password.length() >= 6;
    }

    /** Amount must be positive. */
    public static boolean isValidAmount(double amount) {
        return amount > 0;
    }
}
