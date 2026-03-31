-- ============================================================
--  BankingSystem Database Schema
--  Run this file in MySQL to set up the database
-- ============================================================

CREATE DATABASE IF NOT EXISTS banking_system;
USE banking_system;

-- ---------------------------------------------------------------
-- Table: accounts
-- ---------------------------------------------------------------
CREATE TABLE IF NOT EXISTS accounts (
    account_number VARCHAR(12) PRIMARY KEY,
    full_name      VARCHAR(100) NOT NULL,
    email          VARCHAR(150) UNIQUE NOT NULL,
    phone_number   VARCHAR(15)  NOT NULL,
    password       VARCHAR(255) NOT NULL,   -- stored as SHA-256 hash
    balance        DOUBLE       NOT NULL DEFAULT 0.0,
    created_at     TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- ---------------------------------------------------------------
-- Table: transactions
-- ---------------------------------------------------------------
CREATE TABLE IF NOT EXISTS transactions (
    transaction_id   INT AUTO_INCREMENT PRIMARY KEY,
    account_number   VARCHAR(12)  NOT NULL,
    type             ENUM('DEPOSIT','WITHDRAWAL') NOT NULL,
    amount           DOUBLE       NOT NULL,
    balance_after    DOUBLE       NOT NULL,
    description      VARCHAR(255),
    transaction_date TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (account_number) REFERENCES accounts(account_number)
        ON DELETE CASCADE
);

-- ---------------------------------------------------------------
-- Indexes for faster lookups
-- ---------------------------------------------------------------
CREATE INDEX idx_transactions_account ON transactions(account_number);
CREATE INDEX idx_transactions_type    ON transactions(account_number, type);
CREATE INDEX idx_transactions_date    ON transactions(transaction_date);

-- ---------------------------------------------------------------
-- Sample data (optional – comment out for production)
-- ---------------------------------------------------------------
-- INSERT INTO accounts VALUES ('ACC000000001','John Doe','john@example.com','9876543210',
--     SHA2('password123',256), 5000.00, NOW());
