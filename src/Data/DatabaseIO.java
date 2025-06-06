package Data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import Bank.Bank;
import Bank.BankAccount;
import Bank.SavingsAccount;
import Bank.CurrentAccount;
import Bank.StudentAccount;

public class DatabaseIO {
  private static final String DB_URL = "jdbc:postgresql://localhost:5432/banking";
  private static final String USER = "postgres";
  private static final String PASS = "etr";
  // private static Bank bank = null;

  public static void initialize() {
    try {
      Class.forName("org.postgresql.Driver");
      createTables();
    } catch (ClassNotFoundException e) {
      System.err.println(
          "PostgreSQL JDBC Driver not found. Please add the PostgreSQL JDBC driver to your classpath.");
      e.printStackTrace();
    }
  }

  private static void createTables() {
    try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
        Statement stmt = conn.createStatement()) {
      // Create accounts table if it doesn't exist
      stmt.execute("CREATE TABLE IF NOT EXISTS accounts (" + "id SERIAL PRIMARY KEY, " + // Auto-incrementing
                                                                                         // primary
                                                                                         // key
          "name VARCHAR(255), " + // Account holder's name
          "balance DOUBLE PRECISION, " + // Current account balance
          "min_balance DOUBLE PRECISION, " + // Minimum required balance
          "acc_num VARCHAR(255) UNIQUE, " + // Unique account number
          "type VARCHAR(50), " + // Account type (Savings/Current/Student)
          "max_with_limit DOUBLE PRECISION, " + // Maximum withdrawal limit
          "trade_license VARCHAR(255), " + // Trade license for business accounts
          "institution_name VARCHAR(255), " + // Institution name for student accounts
          "age INTEGER, " + // Age of account holder
          "gender VARCHAR(10), " + // Gender of account holder
          "address VARCHAR(255), " + // Address of account holder
          "phone_number VARCHAR(20))"); // Phone number of account holder

      // Create transactions table if it doesn't exist
      stmt.execute("CREATE TABLE IF NOT EXISTS transactions (" + "id SERIAL PRIMARY KEY, " + // Auto-incrementing
                                                                                             // primary
                                                                                             // key
          "account_id INTEGER, " + // Foreign key to accounts table
          "amount DOUBLE PRECISION, " + // Transaction amount
          "type VARCHAR(50), " + // Transaction type (Deposit/Withdraw)
          "timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " + // Transaction timestamp
          "FOREIGN KEY (account_id) REFERENCES accounts(id) ON DELETE CASCADE)"); // Cascading
                                                                                  // delete

      System.out.println("Database tables checked/created successfully");
    } catch (SQLException e) {
      System.err.println("Error creating tables: " + e.getMessage());
      e.printStackTrace();
    }
  }

  public static void saveBank(Bank bank) {
    try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS)) {
      conn.setAutoCommit(false); // Start transaction for data consistency
      try {
        // Save accounts
        for (BankAccount acc : bank.getAccounts()) {
          if (acc != null) {
            // Check if account already exists in database
            String checkSql = "SELECT id FROM accounts WHERE acc_num = ?";
            try (PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {
              checkStmt.setString(1, acc.acc_num);
              ResultSet rs = checkStmt.executeQuery();

              if (rs.next()) {
                // Account exists - update its information
                String updateSql = "UPDATE accounts SET name = ?, balance = ?, min_balance = ?, "
                    + "type = ?, max_with_limit = ?, trade_license = ?, institution_name = ?, "
                    + "age = ?, gender = ?, address = ?, phone_number = ? " + "WHERE acc_num = ?";
                try (PreparedStatement updateStmt = conn.prepareStatement(updateSql)) {
                  // Set common fields for all account types
                  updateStmt.setString(1, acc.getName());
                  updateStmt.setDouble(2, acc.getbalance());
                  updateStmt.setDouble(3, acc.getMinBalance());

                  // Set type-specific fields based on account type
                  if (acc instanceof SavingsAccount) {
                    updateStmt.setString(4, "Savings");
                    updateStmt.setDouble(5, ((SavingsAccount) acc).getMaxWithLimit());
                    updateStmt.setString(6, null);
                    updateStmt.setString(7, null);
                    updateStmt.setInt(8, ((SavingsAccount) acc).getAge());
                    updateStmt.setString(9, ((SavingsAccount) acc).getGender());
                    updateStmt.setString(10, ((SavingsAccount) acc).getAddress());
                    updateStmt.setString(11, ((SavingsAccount) acc).getPhoneNumber());
                  } else if (acc instanceof CurrentAccount) {
                    updateStmt.setString(4, "Current");
                    updateStmt.setDouble(5, 0);
                    updateStmt.setString(6, ((CurrentAccount) acc).getTradeLicenseNumber());
                    updateStmt.setString(7, null);
                    updateStmt.setInt(8, 0);
                    updateStmt.setString(9, "");
                    updateStmt.setString(10, "");
                    updateStmt.setString(11, "");
                  } else if (acc instanceof StudentAccount) {
                    updateStmt.setString(4, "Student");
                    updateStmt.setDouble(5, ((StudentAccount) acc).getMaxWithLimit());
                    updateStmt.setString(6, null);
                    updateStmt.setString(7, ((StudentAccount) acc).getInstitutionName());
                    updateStmt.setInt(8, 0);
                    updateStmt.setString(9, "");
                    updateStmt.setString(10, "");
                    updateStmt.setString(11, "");
                  }
                  updateStmt.setString(12, acc.acc_num);
                  updateStmt.executeUpdate();
                }
              } else {
                // Account doesn't exist - create new account
                String insertSql = "INSERT INTO accounts (name, balance, min_balance, acc_num, "
                    + "type, max_with_limit, trade_license, institution_name, "
                    + "age, gender, address, phone_number) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
                try (PreparedStatement insertStmt = conn.prepareStatement(insertSql)) {
                  // Set common fields for all account types
                  insertStmt.setString(1, acc.getName());
                  insertStmt.setDouble(2, acc.getbalance());
                  insertStmt.setDouble(3, acc.getMinBalance());
                  insertStmt.setString(4, acc.acc_num);

                  // Set type-specific fields based on account type
                  if (acc instanceof SavingsAccount) {
                    insertStmt.setString(5, "Savings");
                    insertStmt.setDouble(6, ((SavingsAccount) acc).getMaxWithLimit());
                    insertStmt.setString(7, null);
                    insertStmt.setString(8, null);
                    insertStmt.setInt(9, ((SavingsAccount) acc).getAge());
                    insertStmt.setString(10, ((SavingsAccount) acc).getGender());
                    insertStmt.setString(11, ((SavingsAccount) acc).getAddress());
                    insertStmt.setString(12, ((SavingsAccount) acc).getPhoneNumber());
                  } else if (acc instanceof CurrentAccount) {
                    insertStmt.setString(5, "Current");
                    insertStmt.setDouble(6, 0);
                    insertStmt.setString(7, ((CurrentAccount) acc).getTradeLicenseNumber());
                    insertStmt.setString(8, null);
                    insertStmt.setInt(9, 0);
                    insertStmt.setString(10, "");
                    insertStmt.setString(11, "");
                    insertStmt.setString(12, "");
                  } else if (acc instanceof StudentAccount) {
                    insertStmt.setString(5, "Student");
                    insertStmt.setDouble(6, ((StudentAccount) acc).getMaxWithLimit());
                    insertStmt.setString(7, null);
                    insertStmt.setString(8, ((StudentAccount) acc).getInstitutionName());
                    insertStmt.setInt(9, 0);
                    insertStmt.setString(10, "");
                    insertStmt.setString(11, "");
                    insertStmt.setString(12, "");
                  }
                  insertStmt.executeUpdate();
                }
              }
            }
          }
        }
        conn.commit(); // Commit all changes
        System.out.println("Bank data saved successfully");
      } catch (SQLException e) {
        conn.rollback(); // Rollback changes if any error occurs
        System.err.println("Error saving bank data: " + e.getMessage());
        e.printStackTrace();
      }
    } catch (SQLException e) {
      System.err.println("Database connection error: " + e.getMessage());
      e.printStackTrace();
    }
  }

  public static void saveTransaction(String accountNum, double amount, String type) {
    try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS)) {
      conn.setAutoCommit(false); // Start transaction for data consistency
      try {
        // First get the account ID using the account number
        String getAccountIdSql = "SELECT id FROM accounts WHERE acc_num = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(getAccountIdSql)) {
          pstmt.setString(1, accountNum);
          ResultSet rs = pstmt.executeQuery();
          if (rs.next()) {
            int accountId = rs.getInt("id");
            System.out
                .println("Found account ID: " + accountId + " for account number: " + accountNum);

            // Insert the transaction record
            String insertTransactionSql = "INSERT INTO transactions (account_id, amount, type) VALUES (?, ?, ?)";
            try (PreparedStatement insertStmt = conn.prepareStatement(insertTransactionSql)) {
              insertStmt.setInt(1, accountId);
              insertStmt.setDouble(2, amount);
              insertStmt.setString(3, type);
              int rowsAffected = insertStmt.executeUpdate();
              System.out.println("Transaction saved: Account=" + accountNum + ", Amount=" + amount
                  + ", Type=" + type + ", Rows affected: " + rowsAffected);

              if (rowsAffected == 0) {
                throw new SQLException("Failed to insert transaction");
              }
            }
          } else {
            System.out.println("Account not found: " + accountNum);
            throw new SQLException("Account not found: " + accountNum);
          }
        }
        conn.commit(); // Commit the transaction
        System.out.println("Transaction committed successfully");
      } catch (SQLException e) {
        conn.rollback(); // Rollback on error
        System.err.println("Error saving transaction: " + e.getMessage());
        e.printStackTrace();
        throw e;
      }
    } catch (SQLException e) {
      System.err.println("Database connection error: " + e.getMessage());
      e.printStackTrace();
    }
  }

  public static Bank loadBank() {
    Bank bank = new Bank();
    try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM accounts")) {
      while (rs.next()) {
        String name = rs.getString("name");
        double balance = rs.getDouble("balance");
        double min_balance = rs.getDouble("min_balance");
        String acc_num = rs.getString("acc_num");
        String type = rs.getString("type");
        double max_with_limit = rs.getDouble("max_with_limit");
        String trade_license = rs.getString("trade_license");
        String institution_name = rs.getString("institution_name");
        int age = rs.getInt("age");
        String gender = rs.getString("gender");
        String address = rs.getString("address");
        String phone_number = rs.getString("phone_number");

        BankAccount acc = null;
        if ("Savings".equals(type)) {
          acc = new SavingsAccount(name, balance, max_with_limit, age, gender, address, phone_number);
        } else if ("Current".equals(type)) {
          acc = new CurrentAccount(name, balance, trade_license);
        } else if ("Student".equals(type)) {
          acc = new StudentAccount(name, balance, institution_name);
        }
        if (acc != null) {
          acc.setAccNum(acc_num); // Set the account number from database
          bank.addAccount(acc);
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return bank;
  }
}
