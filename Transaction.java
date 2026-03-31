package banking.model;

import java.time.LocalDateTime;

/**
 * Represents a single bank transaction (maps to the 'transactions' table).
 */
public class Transaction {

    public enum Type { DEPOSIT, WITHDRAWAL }

    private int           transactionId;
    private String        accountNumber;
    private Type          type;
    private double        amount;
    private double        balanceAfter;
    private String        description;
    private LocalDateTime transactionDate;

    // ── Constructors ──────────────────────────────────────────────────────

    public Transaction() {}

    public Transaction(int transactionId, String accountNumber, Type type,
                       double amount, double balanceAfter,
                       String description, LocalDateTime transactionDate) {
        this.transactionId   = transactionId;
        this.accountNumber   = accountNumber;
        this.type            = type;
        this.amount          = amount;
        this.balanceAfter    = balanceAfter;
        this.description     = description;
        this.transactionDate = transactionDate;
    }

    // ── Getters & Setters ─────────────────────────────────────────────────

    public int    getTransactionId()              { return transactionId; }
    public void   setTransactionId(int id)        { this.transactionId = id; }

    public String getAccountNumber()              { return accountNumber; }
    public void   setAccountNumber(String n)      { this.accountNumber = n; }

    public Type   getType()                       { return type; }
    public void   setType(Type t)                 { this.type = t; }

    public double getAmount()                     { return amount; }
    public void   setAmount(double a)             { this.amount = a; }

    public double getBalanceAfter()               { return balanceAfter; }
    public void   setBalanceAfter(double b)       { this.balanceAfter = b; }

    public String getDescription()                { return description; }
    public void   setDescription(String d)        { this.description = d; }

    public LocalDateTime getTransactionDate()     { return transactionDate; }
    public void setTransactionDate(LocalDateTime d) { this.transactionDate = d; }

    @Override
    public String toString() {
        return String.format("[%s] %s  ₹%.2f  (Balance after: ₹%.2f)  %s",
                transactionDate, type, amount, balanceAfter, description);
    }
}
