package banking.dao;

import banking.db.DBConnection;
import banking.model.Transaction;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Data-Access Object for the 'transactions' table.
 */
public class TransactionDAO {

    private final Connection conn;

    public TransactionDAO() {
        this.conn = DBConnection.getConnection();
    }

    // ── Create ────────────────────────────────────────────────────────────

    /**
     * Records a new transaction.
     */
    public boolean addTransaction(Transaction t) {
        String sql = "INSERT INTO transactions " +
                     "(account_number, type, amount, balance_after, description, transaction_date) " +
                     "VALUES (?,?,?,?,?,?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, t.getAccountNumber());
            ps.setString(2, t.getType().name());
            ps.setDouble(3, t.getAmount());
            ps.setDouble(4, t.getBalanceAfter());
            ps.setString(5, t.getDescription());
            ps.setTimestamp(6, Timestamp.valueOf(LocalDateTime.now()));
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("[TransactionDAO] addTransaction error: " + e.getMessage());
            return false;
        }
    }

    // ── Read ──────────────────────────────────────────────────────────────

    /**
     * All transactions for an account, newest first.
     */
    public List<Transaction> getAllTransactions(String accountNumber) {
        return fetchTransactions(accountNumber, null);
    }

    /**
     * Only DEPOSIT transactions, newest first.
     */
    public List<Transaction> getDeposits(String accountNumber) {
        return fetchTransactions(accountNumber, Transaction.Type.DEPOSIT);
    }

    /**
     * Only WITHDRAWAL transactions, newest first.
     */
    public List<Transaction> getWithdrawals(String accountNumber) {
        return fetchTransactions(accountNumber, Transaction.Type.WITHDRAWAL);
    }

    // ── Helpers ───────────────────────────────────────────────────────────

    private List<Transaction> fetchTransactions(String accountNumber,
                                                Transaction.Type filter) {
        List<Transaction> list = new ArrayList<>();
        String sql = "SELECT * FROM transactions WHERE account_number = ?";
        if (filter != null) sql += " AND type = ?";
        sql += " ORDER BY transaction_date DESC";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, accountNumber);
            if (filter != null) ps.setString(2, filter.name());
            ResultSet rs = ps.executeQuery();
            while (rs.next()) list.add(mapRow(rs));
        } catch (SQLException e) {
            System.err.println("[TransactionDAO] fetchTransactions error: " + e.getMessage());
        }
        return list;
    }

    private Transaction mapRow(ResultSet rs) throws SQLException {
        Transaction t = new Transaction();
        t.setTransactionId(rs.getInt("transaction_id"));
        t.setAccountNumber(rs.getString("account_number"));
        t.setType(Transaction.Type.valueOf(rs.getString("type")));
        t.setAmount(rs.getDouble("amount"));
        t.setBalanceAfter(rs.getDouble("balance_after"));
        t.setDescription(rs.getString("description"));
        t.setTransactionDate(rs.getTimestamp("transaction_date").toLocalDateTime());
        return t;
    }
}
