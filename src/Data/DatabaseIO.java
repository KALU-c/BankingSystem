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
  private static Bank bank = null;

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
      // Create accounts table
      stmt.execute("CREATE TABLE IF NOT EXISTS accounts (" + "id SERIAL PRIMARY KEY, "
          + "name VARCHAR(255), " + "balance DOUBLE PRECISION, " + "min_balance DOUBLE PRECISION, "
          + "acc_num VARCHAR(255), " + "type VARCHAR(50), " + "max_with_limit DOUBLE PRECISION, "
          + "trade_license VARCHAR(255), " + "institution_name VARCHAR(255))");
      // Create transactions table
      stmt.execute("CREATE TABLE IF NOT EXISTS transactions (" + "id SERIAL PRIMARY KEY, "
          + "account_id INTEGER REFERENCES accounts(id), " + "amount DOUBLE PRECISION, "
          + "type VARCHAR(50), " + "timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP)");
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public static void saveBank(Bank bank) {
    try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS)) {
      // Clear existing data
      try (Statement stmt = conn.createStatement()) {
        stmt.execute("DELETE FROM transactions");
        stmt.execute("DELETE FROM accounts");
      }

      // Save accounts
      for (BankAccount acc : bank.getAccounts()) {
        if (acc != null) {
          String sql =
              "INSERT INTO accounts (name, balance, min_balance, acc_num, type, max_with_limit, trade_license, institution_name) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
          try (PreparedStatement pstmt =
              conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, acc.getName());
            pstmt.setDouble(2, acc.getbalance());
            pstmt.setDouble(3, acc.getMinBalance());
            pstmt.setString(4, acc.acc_num);
            if (acc instanceof SavingsAccount) {
              pstmt.setString(5, "Savings");
              pstmt.setDouble(6, ((SavingsAccount) acc).getMaxWithLimit());
              pstmt.setString(7, null);
              pstmt.setString(8, null);
            } else if (acc instanceof CurrentAccount) {
              pstmt.setString(5, "Current");
              pstmt.setDouble(6, 0);
              pstmt.setString(7, ((CurrentAccount) acc).getTradeLicenseNumber());
              pstmt.setString(8, null);
            } else if (acc instanceof StudentAccount) {
              pstmt.setString(5, "Student");
              pstmt.setDouble(6, ((StudentAccount) acc).getMaxWithLimit());
              pstmt.setString(7, null);
              pstmt.setString(8, ((StudentAccount) acc).getInstitutionName());
            }
            pstmt.executeUpdate();
          }
        }
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public static void saveTransaction(String accountNum, double amount, String type) {
    try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS)) {
      // First get the account ID
      String getAccountIdSql = "SELECT id FROM accounts WHERE acc_num = ?";
      try (PreparedStatement pstmt = conn.prepareStatement(getAccountIdSql)) {
        pstmt.setString(1, accountNum);
        ResultSet rs = pstmt.executeQuery();
        if (rs.next()) {
          int accountId = rs.getInt("id");

          // Insert the transaction
          String insertTransactionSql =
              "INSERT INTO transactions (account_id, amount, type) VALUES (?, ?, ?)";
          try (PreparedStatement insertStmt = conn.prepareStatement(insertTransactionSql)) {
            insertStmt.setInt(1, accountId);
            insertStmt.setDouble(2, amount);
            insertStmt.setString(3, type);
            insertStmt.executeUpdate();
          }
        }
      }
    } catch (SQLException e) {
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

        BankAccount acc = null;
        if ("Savings".equals(type)) {
          acc = new SavingsAccount(name, balance, max_with_limit);
        } else if ("Current".equals(type)) {
          acc = new CurrentAccount(name, balance, trade_license);
        } else if ("Student".equals(type)) {
          acc = new StudentAccount(name, balance, institution_name);
        }
        if (acc != null) {
          bank.addAccount(acc);
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return bank;
  }
}
