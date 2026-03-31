package banking.service;

import banking.dao.AccountDAO;
import banking.dao.TransactionDAO;
import banking.model.Account;
import banking.model.Transaction;
import banking.util.BankingUtils;

import java.util.List;

/**
 * Business-logic layer.  All operations return a {@link ServiceResult}
 * so the UI can display a success / error message without knowing DB details.
 */
public class BankingService {

    private final AccountDAO     accountDAO     = new AccountDAO();
    private final TransactionDAO transactionDAO = new TransactionDAO();

    // ── Account Creation ──────────────────────────────────────────────────

    public ServiceResult createAccount(String fullName, String email,
                                       String phone,    String password) {
        if (fullName == null || fullName.trim().isEmpty())
            return ServiceResult.fail("Full name cannot be empty.");
        if (!BankingUtils.isValidEmail(email))
            return ServiceResult.fail("Invalid email address.");
        if (!BankingUtils.isValidPhone(phone))
            return ServiceResult.fail("Phone must be a valid 10-digit mobile number.");
        if (!BankingUtils.isValidPassword(password))
            return ServiceResult.fail("Password must be at least 6 characters.");
        if (accountDAO.emailExists(email))
            return ServiceResult.fail("An account with this email already exists.");

        String accNo = generateUniqueAccNo();
        Account account = new Account();
        account.setAccountNumber(accNo);
        account.setFullName(fullName.trim());
        account.setEmail(email.trim().toLowerCase());
        account.setPhoneNumber(phone.trim());
        account.setPassword(password);   // AccountDAO hashes it
        account.setBalance(0.0);

        if (accountDAO.createAccount(account))
            return ServiceResult.success("Account created! Your Account Number is: " + accNo, accNo);
        return ServiceResult.fail("Failed to create account. Please try again.");
    }

    // ── Login ─────────────────────────────────────────────────────────────

    public ServiceResult login(String accountNumber, String password) {
        if (accountNumber == null || accountNumber.trim().isEmpty())
            return ServiceResult.fail("Account number cannot be empty.");
        if (password == null || password.trim().isEmpty())
            return ServiceResult.fail("Password cannot be empty.");

        Account account = accountDAO.findByAccountNumber(accountNumber.trim().toUpperCase());
        if (account == null)
            return ServiceResult.fail("No account found with that account number.");
        if (!BankingUtils.verifyPassword(password, account.getPassword()))
            return ServiceResult.fail("Incorrect password.");

        return ServiceResult.success("Login successful! Welcome, " + account.getFullName() + ".",
                                     account.getAccountNumber());
    }

    // ── Balance ───────────────────────────────────────────────────────────

    public double getBalance(String accountNumber) {
        Account a = accountDAO.findByAccountNumber(accountNumber);
        return (a != null) ? a.getBalance() : -1;
    }

    public Account getAccount(String accountNumber) {
        return accountDAO.findByAccountNumber(accountNumber);
    }

    // ── Deposit ───────────────────────────────────────────────────────────

    public ServiceResult deposit(String accountNumber, double amount, String description) {
        if (!BankingUtils.isValidAmount(amount))
            return ServiceResult.fail("Deposit amount must be greater than ₹0.");

        Account account = accountDAO.findByAccountNumber(accountNumber);
        if (account == null)
            return ServiceResult.fail("Account not found.");

        double newBalance = account.getBalance() + amount;
        if (!accountDAO.updateBalance(accountNumber, newBalance))
            return ServiceResult.fail("Failed to update balance.");

        Transaction t = new Transaction();
        t.setAccountNumber(accountNumber);
        t.setType(Transaction.Type.DEPOSIT);
        t.setAmount(amount);
        t.setBalanceAfter(newBalance);
        t.setDescription(description == null || description.trim().isEmpty()
                         ? "Cash Deposit" : description.trim());
        transactionDAO.addTransaction(t);

        return ServiceResult.success(
                String.format("₹%.2f deposited successfully. New Balance: ₹%.2f", amount, newBalance),
                String.valueOf(newBalance));
    }

    // ── Withdraw ──────────────────────────────────────────────────────────

    public ServiceResult withdraw(String accountNumber, double amount, String description) {
        if (!BankingUtils.isValidAmount(amount))
            return ServiceResult.fail("Withdrawal amount must be greater than ₹0.");

        Account account = accountDAO.findByAccountNumber(accountNumber);
        if (account == null)
            return ServiceResult.fail("Account not found.");
        if (account.getBalance() < amount)
            return ServiceResult.fail(
                    String.format("Insufficient funds. Available balance: ₹%.2f", account.getBalance()));

        double newBalance = account.getBalance() - amount;
        if (!accountDAO.updateBalance(accountNumber, newBalance))
            return ServiceResult.fail("Failed to update balance.");

        Transaction t = new Transaction();
        t.setAccountNumber(accountNumber);
        t.setType(Transaction.Type.WITHDRAWAL);
        t.setAmount(amount);
        t.setBalanceAfter(newBalance);
        t.setDescription(description == null || description.trim().isEmpty()
                         ? "Cash Withdrawal" : description.trim());
        transactionDAO.addTransaction(t);

        return ServiceResult.success(
                String.format("₹%.2f withdrawn successfully. New Balance: ₹%.2f", amount, newBalance),
                String.valueOf(newBalance));
    }

    // ── Transaction History ───────────────────────────────────────────────

    public List<Transaction> getAllTransactions(String accountNumber) {
        return transactionDAO.getAllTransactions(accountNumber);
    }

    public List<Transaction> getDeposits(String accountNumber) {
        return transactionDAO.getDeposits(accountNumber);
    }

    public List<Transaction> getWithdrawals(String accountNumber) {
        return transactionDAO.getWithdrawals(accountNumber);
    }

    // ── Password Change ───────────────────────────────────────────────────

    public ServiceResult changePassword(String accountNumber,
                                        String oldPassword, String newPassword) {
        Account account = accountDAO.findByAccountNumber(accountNumber);
        if (account == null)
            return ServiceResult.fail("Account not found.");
        if (!BankingUtils.verifyPassword(oldPassword, account.getPassword()))
            return ServiceResult.fail("Current password is incorrect.");
        if (!BankingUtils.isValidPassword(newPassword))
            return ServiceResult.fail("New password must be at least 6 characters.");
        if (oldPassword.equals(newPassword))
            return ServiceResult.fail("New password must differ from the current password.");

        if (accountDAO.updatePassword(accountNumber, newPassword))
            return ServiceResult.success("Password changed successfully.");
        return ServiceResult.fail("Failed to change password.");
    }

    // ── Phone Number Change ───────────────────────────────────────────────

    public ServiceResult changePhoneNumber(String accountNumber,
                                           String password, String newPhone) {
        Account account = accountDAO.findByAccountNumber(accountNumber);
        if (account == null)
            return ServiceResult.fail("Account not found.");
        if (!BankingUtils.verifyPassword(password, account.getPassword()))
            return ServiceResult.fail("Password is incorrect.");
        if (!BankingUtils.isValidPhone(newPhone))
            return ServiceResult.fail("Invalid phone number. Must be a 10-digit mobile number.");
        if (account.getPhoneNumber().equals(newPhone))
            return ServiceResult.fail("New phone number is the same as the current one.");

        if (accountDAO.updatePhoneNumber(accountNumber, newPhone))
            return ServiceResult.success("Phone number updated to " + newPhone + ".");
        return ServiceResult.fail("Failed to update phone number.");
    }

    // ── Helpers ───────────────────────────────────────────────────────────

    private String generateUniqueAccNo() {
        String accNo;
        do {
            accNo = BankingUtils.generateAccountNumber();
        } while (accountDAO.findByAccountNumber(accNo) != null);
        return accNo;
    }
}
