# Banking System

A Java-based banking system that provides a graphical user interface for managing different types of bank accounts and transactions. This project was developed as a group assignment to demonstrate object-oriented programming concepts and database integration.

## Features

- **Multiple Account Types**

  - Savings Accounts
  - Business Accounts
  - Student Accounts

- **Account Management**

  - Create new accounts
  - View account details
  - Update account information
  - Delete accounts

- **Transaction Operations**

  - Deposit money
  - Withdraw money
  - View transaction history

- **Database Integration**
  - PostgreSQL database for persistent storage
  - Transaction management
  - Account data persistence

## Technical Stack

- **Programming Language**: Java
- **Database**: PostgreSQL
- **Database Driver**: PostgreSQL JDBC Driver (postgresql-42.7.5.jar)
- **GUI Framework**: Java Swing

## Prerequisites

- Java Development Kit (JDK)
- PostgreSQL Database Server
- PostgreSQL JDBC Driver

## Installation

1. Clone the repository:

   ```bash
   git clone https://github.com/KALU-c/BankingSystem.git
   ```

2. Set up PostgreSQL:

   - Install PostgreSQL if not already installed
   - Create a new database named 'banking'
   - Set up user credentials:
     - Username: postgres
     - Password: etr

3. Add PostgreSQL JDBC Driver:
   - Download postgresql-42.7.5.jar
   - Place it in the project root directory

## Compilation

Compile the project using:

```bash
javac -cp postgresql-42.7.5.jar src/Application.java src/Data/DatabaseIO.java src/Data/FileIO.java src/Bank/Bank.java src/Bank/BankAccount.java src/Bank/SavingsAccount.java src/Bank/CurrentAccount.java src/Bank/StudentAccount.java src/GUI/GUIForm.java src/GUI/Login.java src/GUI/Menu.java src/GUI/AddAccount.java src/GUI/AddCurrentAccount.java src/GUI/AddSavingsAccount.java src/GUI/AddStudentAccount.java src/GUI/DisplayList.java src/GUI/DepositAcc.java src/GUI/WithdrawAcc.java src/Exceptions/AccNotFound.java src/Exceptions/InvalidAmount.java src/Exceptions/MaxBalance.java src/Exceptions/MaxWithdraw.java
```

## Running the Application

Run the application using:

```bash
java -cp postgresql-42.7.5.jar Application
```

## Project Structure

```
src/
├── Application.java
├── Bank/
│   ├── Bank.java
│   ├── BankAccount.java
│   ├── SavingsAccount.java
│   ├── CurrentAccount.java
│   └── StudentAccount.java
├── Data/
│   ├── DatabaseIO.java
│   └── FileIO.java
├── GUI/
│   ├── GUIForm.java
│   ├── Login.java
│   ├── Menu.java
│   ├── AddAccount.java
│   ├── AddCurrentAccount.java
│   ├── AddSavingsAccount.java
│   ├── AddStudentAccount.java
│   ├── DisplayList.java
│   ├── DepositAcc.java
│   └── WithdrawAcc.java
└── Exceptions/
    ├── AccNotFound.java
    ├── InvalidAmount.java
    ├── MaxBalance.java
    └── MaxWithdraw.java
```

## Database Schema

### Accounts Table

```sql
CREATE TABLE accounts (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255),
    balance DOUBLE PRECISION,
    min_balance DOUBLE PRECISION,
    acc_num VARCHAR(255) UNIQUE,
    type VARCHAR(50),
    max_with_limit DOUBLE PRECISION,
    trade_license VARCHAR(255),
    institution_name VARCHAR(255)
);
```

### Transactions Table

```sql
CREATE TABLE transactions (
    id SERIAL PRIMARY KEY,
    account_id INTEGER,
    amount DOUBLE PRECISION,
    type VARCHAR(50),
    timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (account_id) REFERENCES accounts(id) ON DELETE CASCADE
);
```

## Group Members

1. Endekalu Zemenu
2. Dawit Demissie
3. Abenezer Fikre
4. Mekbib Tilahun
5. Biruk Abayneh
6. Elsa Solomon

## Features by Account Type

### Savings Account

- Minimum balance requirement
- Maximum withdrawal limit
- Interest calculation

### Business Account

- Trade license requirement
- No minimum balance
- Unlimited withdrawals

### Student Account

- Institution name requirement
- Maximum withdrawal limit
- Special student benefits

## Error Handling

The system implements comprehensive error handling for:

- Invalid amounts
- Account not found
- Maximum balance exceeded
- Maximum withdrawal limit exceeded
- Database connection issues
- Transaction failures

## Contributing

This is a group assignment project. For any issues or suggestions, please contact the group members.

## License

This project is created for educational purposes as part of a group assignment.
