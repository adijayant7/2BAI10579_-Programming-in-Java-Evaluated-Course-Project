package banking.dao;

import banking.db.DBConnection;
import banking.model.Account;
import banking.util.BankingUtils;

import java.sql.*;
import java.time.LocalDateTime;

/**
 * Data-Access Object for the 'accounts' table.
 */
public class AccountDAO {

    private final Connection conn;

    public AccountDAO() {
        this.conn = DBConnection.getConnection();
    }

    // ── Create ────────────────────────────────────────────────────────────

    /**
     * Inserts a new account into the DB.
     * Password is hashed before storing.
     * @return true on success.
     */
    public boolean createAccount(Account account) {
        String sql = "INSERT INTO accounts (account_number, full_name, email, " +
                     "phone_number, password, balance, created_at) VALUES (?,?,?,?,?,?,?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, account.getAccountNumber());
            ps.setString(2, account.getFullName());
            ps.setString(3, account.getEmail());
            ps.setString(4, account.getPhoneNumber());
            ps.setString(5, BankingUtils.hashPassword(account.getPassword()));
            ps.setDouble(6, account.getBalance());
            ps.setTimestamp(7, Timestamp.valueOf(LocalDateTime.now()));
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("[AccountDAO] createAccount error: " + e.getMessage());
            return false;
        }
    }

    // ── Read ──────────────────────────────────────────────────────────────

    /**
     * Finds an account by account number.
     */
    public Account findByAccountNumber(String accountNumber) {
        String sql = "SELECT * FROM accounts WHERE account_number = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, accountNumber);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapRow(rs);
        } catch (SQLException e) {
            System.err.println("[AccountDAO] findByAccountNumber error: " + e.getMessage());
        }
        return null;
    }

    /**
     * Finds an account by email.
     */
    public Account findByEmail(String email) {
        String sql = "SELECT * FROM accounts WHERE email = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapRow(rs);
        } catch (SQLException e) {
            System.err.println("[AccountDAO] findByEmail error: " + e.getMessage());
        }
        return null;
    }

    /**
     * Returns true if an account with that email already exists.
     */
    public boolean emailExists(String email) {
        return findByEmail(email) != null;
    }

    // ── Update ────────────────────────────────────────────────────────────

    /**
     * Updates the balance for the given account.
     */
    public boolean updateBalance(String accountNumber, double newBalance) {
        String sql = "UPDATE accounts SET balance = ? WHERE account_number = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDouble(1, newBalance);
            ps.setString(2, accountNumber);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("[AccountDAO] updateBalance error: " + e.getMessage());
            return false;
        }
    }

    /**
     * Changes the password (accepts plain-text; hashes internally).
     */
    public boolean updatePassword(String accountNumber, String newPlainPassword) {
        String sql = "UPDATE accounts SET password = ? WHERE account_number = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, BankingUtils.hashPassword(newPlainPassword));
            ps.setString(2, accountNumber);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("[AccountDAO] updatePassword error: " + e.getMessage());
            return false;
        }
    }

    /**
     * Changes the phone number.
     */
    public boolean updatePhoneNumber(String accountNumber, String newPhone) {
        String sql = "UPDATE accounts SET phone_number = ? WHERE account_number = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, newPhone);
            ps.setString(2, accountNumber);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("[AccountDAO] updatePhoneNumber error: " + e.getMessage());
            return false;
        }
    }

    // ── Helpers ───────────────────────────────────────────────────────────

    private Account mapRow(ResultSet rs) throws SQLException {
        Account a = new Account();
        a.setAccountNumber(rs.getString("account_number"));
        a.setFullName(rs.getString("full_name"));
        a.setEmail(rs.getString("email"));
        a.setPhoneNumber(rs.getString("phone_number"));
        a.setPassword(rs.getString("password"));
        a.setBalance(rs.getDouble("balance"));
        a.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        return a;
    }
}
