# Banking System

A Java-based banking system that supports multiple types of accounts with PostgreSQL database integration.

## Features

### Account Types

#### Savings Account

- Minimum balance requirement (2000)
- Maximum withdrawal limit
- Interest calculation (5% rate)
- Additional personal information:
  - Age
  - Gender
  - Address
  - Phone number

#### Business Account (Current Account)

- Trade license requirement
- No minimum balance
- Unlimited withdrawals

#### Student Account

- Institution name requirement
- Maximum withdrawal limit (20000)
- Minimum balance requirement (100)
- Inherits savings account features

### Database Structure

#### Accounts Table

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
    institution_name VARCHAR(255),
    age INTEGER,
    gender VARCHAR(10),
    address VARCHAR(255),
    phone_number VARCHAR(20)
)
```

#### Transactions Table

```sql
CREATE TABLE transactions (
    id SERIAL PRIMARY KEY,
    account_id INTEGER,
    amount DOUBLE PRECISION,
    type VARCHAR(50),
    timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (account_id) REFERENCES accounts(id) ON DELETE CASCADE
)
```

### Core Features

1. **Account Management**

   - Create different types of accounts
   - View account details
   - Update account information
   - Delete accounts

2. **Transaction Processing**

   - Deposit money
   - Withdraw money
   - Transaction history tracking
   - Balance updates

3. **Data Persistence**

   - PostgreSQL database integration
   - Automatic transaction logging
   - Account data persistence
   - Real-time balance updates

4. **Error Handling**
   - Invalid amount validation
   - Account not found checks
   - Maximum balance exceeded checks
   - Maximum withdrawal limit validation
   - Database connection error handling
   - Transaction failure handling

### User Interface

The system provides a graphical user interface with the following features:

- Account creation forms with field validation
- Transaction processing forms
- Account listing and details view
- Error message dialogs
- Confirmation dialogs

### Technical Details

#### Database Connection

- PostgreSQL database
- JDBC driver integration
- Connection pooling
- Transaction management

#### Security Features

- Account number uniqueness
- Minimum balance enforcement
- Withdrawal limits
- Transaction validation

#### Data Validation

- Input field validation
- Balance checks
- Account existence verification
- Transaction amount validation

## Setup Instructions

1. Install PostgreSQL database
2. Create a database named 'banking'
3. Update database credentials in `DatabaseIO.java`:
   ```java
   private static final String DB_URL = "jdbc:postgresql://localhost:5432/banking";
   private static final String USER = "postgres";
   private static final String PASS = "your_password";
   ```
4. Add PostgreSQL JDBC driver to classpath
5. Compile and run the application

## Group Members

1. Endekalu Zemenu
2. Dawit Demissie
3. Abenezer Fikre
4. Mekbib Tilahun
5. Biruk Abayneh
6. Elsa Solomon

## Contributing

This is a group assignment project. For any issues or suggestions, please contact the group members.

## License

This project is created for educational purposes as part of a group assignment.
