package banking.model;

import java.time.LocalDateTime;

/**
 * Represents a bank account (maps to the 'accounts' table).
 */
public class Account {

    private String        accountNumber;
    private String        fullName;
    private String        email;
    private String        phoneNumber;
    private String        password;       // SHA-256 hash
    private double        balance;
    private LocalDateTime createdAt;

    // ── Constructors ──────────────────────────────────────────────────────

    public Account() {}

    public Account(String accountNumber, String fullName, String email,
                   String phoneNumber, String password, double balance,
                   LocalDateTime createdAt) {
        this.accountNumber = accountNumber;
        this.fullName      = fullName;
        this.email         = email;
        this.phoneNumber   = phoneNumber;
        this.password      = password;
        this.balance       = balance;
        this.createdAt     = createdAt;
    }

    // ── Getters & Setters ─────────────────────────────────────────────────

    public String getAccountNumber()               { return accountNumber; }
    public void   setAccountNumber(String n)       { this.accountNumber = n; }

    public String getFullName()                    { return fullName; }
    public void   setFullName(String n)            { this.fullName = n; }

    public String getEmail()                       { return email; }
    public void   setEmail(String e)               { this.email = e; }

    public String getPhoneNumber()                 { return phoneNumber; }
    public void   setPhoneNumber(String p)         { this.phoneNumber = p; }

    public String getPassword()                    { return password; }
    public void   setPassword(String p)            { this.password = p; }

    public double getBalance()                     { return balance; }
    public void   setBalance(double b)             { this.balance = b; }

    public LocalDateTime getCreatedAt()            { return createdAt; }
    public void          setCreatedAt(LocalDateTime d) { this.createdAt = d; }

    @Override
    public String toString() {
        return String.format("Account[%s | %s | ₹%.2f]", accountNumber, fullName, balance);
    }
}
