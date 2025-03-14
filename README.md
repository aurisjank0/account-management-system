# Project 

This project is a Spring Boot application for managing customer accounts and transactions.

## Features

- Create customer accounts
- Deposit and withdraw funds
- Retrieve account balance
- View the last 10 transactions

## Access the Swagger UI to explore the API:
   [http://localhost:8777/api/swagger-ui/index.html](http://localhost:8777/api/swagger-ui/index.html)

## API Endpoints

### Account Management

- `POST /api/accounts` - Create a new account
- `POST /api/accounts/{accountId}/deposit` - Deposit funds into an account
- `POST /api/accounts/{accountId}/withdraw` - Withdraw funds from an account
- `GET /api/accounts/{accountId}/balance` - Get the balance of an account
- `GET /api/accounts/{accountId}/transactions` - Get the last 10 transactions of an account


## Example Data

* Customer 1: Test customer 1  
```
    Account ID: 1 Account Number: 1234567890
```

* Customer 2: Test customer 2
```
    Account ID: 2 Account Number: 0987654321
  ```