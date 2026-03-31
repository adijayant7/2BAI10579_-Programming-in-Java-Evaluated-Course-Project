# 🏦 BankingSystem

A full-featured desktop **Banking Management System** built in **Java Swing** with **MySQL** database connectivity.

---

## ✨ Features

| # | Feature | Description |
|---|---------|-------------|
| 1 | **Account Creation** | Register with name, email, phone & password. Auto-generates a unique account number. |
| 2 | **Login / Logout** | Secure login with SHA-256 hashed passwords. Alerts on success & failure. |
| 3 | **Deposit Amount** | Deposit money with an optional description. Confirmation dialog before processing. |
| 4 | **Withdraw Amount** | Withdraw with balance validation and confirmation alert. |
| 5 | **Balance Check** | View live balance on the dashboard and via a dedicated alert. |
| 6 | **Transaction History** | Sortable view: All Transactions / Deposits only / Withdrawals only. |
| 7 | **Password Change** | Change password securely — requires current password verification. |
| 8 | **Phone Number Change** | Update phone number with password confirmation. |
| 9 | **Switch Account** | Logout from current account to log into another. |

---

## 🛠️ Tech Stack

- **Language:** Java 8+
- **UI:** Java Swing (custom-themed, no external UI library)
- **Database:** MySQL 8.x via JDBC
- **Driver:** `mysql-connector-j`
- **Password security:** SHA-256 hashing

---

## 📁 Project Structure

```
BankingSystem/
├── database/
│   └── banking_schema.sql          ← Run this first in MySQL
├── src/
│   ├── startOfProject/
│   │   └── Main.java               ← ▶ ENTRY POINT – run this
│   └── banking/
│       ├── db/
│       │   └── DBConnection.java   ← MySQL connection config
│       ├── model/
│       │   ├── Account.java
│       │   └── Transaction.java
│       ├── dao/
│       │   ├── AccountDAO.java
│       │   └── TransactionDAO.java
│       ├── service/
│       │   ├── BankingService.java
│       │   └── ServiceResult.java
│       ├── util/
│       │   └── BankingUtils.java
│       └── ui/
│           ├── Theme.java
│           ├── MainFrame.java
│           ├── LoginPanel.java
│           ├── CreateAccountPanel.java
│           ├── DashboardPanel.java
│           ├── DepositPanel.java
│           ├── WithdrawPanel.java
│           ├── TransactionPanel.java
│           ├── ChangePasswordPanel.java
│           └── ChangePhonePanel.java
└── README.md
```

---

## ⚙️ Setup Instructions

### 1. Prerequisites
- Java JDK 8 or higher
- MySQL 8.x server running locally
- MySQL Connector/J JAR (download from [mysql.com](https://dev.mysql.com/downloads/connector/j/))
- Any Java IDE (IntelliJ IDEA recommended) or command line

### 2. Database Setup

Open MySQL Workbench or your MySQL terminal and run:

```sql
source /path/to/BankingSystem/database/banking_schema.sql
```

Or copy-paste the contents of `banking_schema.sql` into your MySQL client.

### 3. Configure DB Credentials

Edit `src/banking/db/DBConnection.java`:

```java
private static final String URL      = "jdbc:mysql://localhost:3306/banking_system?useSSL=false&serverTimezone=UTC";
private static final String USER     = "root";       // ← your MySQL username
private static final String PASSWORD = "root";       // ← your MySQL password
```

### 4. Add MySQL JDBC Driver

**IntelliJ IDEA:**
1. `File → Project Structure → Libraries → + → Java`
2. Select the `mysql-connector-j-x.x.x.jar` file
3. Click OK

**Command Line:**
```bash
javac -cp ".;mysql-connector-j-8.x.x.jar" -d out -sourcepath src src/startOfProject/Main.java
java  -cp ".;mysql-connector-j-8.x.x.jar;out" startOfProject.Main
```

### 5. Run the Application

- In your IDE: Open `src/startOfProject/Main.java` → click **Run**
- The login window will appear

---

## 🖥️ How to Use

1. **First time?** → Click **"Create New Account"** and fill in your details
2. Note down the **Account Number** shown in the success dialog
3. **Login** with your account number and password
4. Use the **dashboard tiles** to navigate to any feature
5. All monetary operations show a **confirmation dialog** before proceeding
6. All results are shown as **alert pop-ups**

---

## 🔒 Security

- Passwords are **never stored in plain text** — SHA-256 hashed before saving to DB
- All DB queries use **PreparedStatements** to prevent SQL injection
- Password change requires the current password as verification
- Phone number change is also password-protected

---

## 📸 Screenshots

> Add screenshots of your running application here.

---

## 👤 Author

**[Your Name]**  
B.Tech / MCA / BSc CS Student  
GitHub: [@yourusername](https://github.com/yourusername)

---

## 📄 License

This project is open-source and available under the [MIT License](LICENSE).
