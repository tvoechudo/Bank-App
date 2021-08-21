package com.company;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;

public class Sql {
    static int saveClient(Statement statement, Client client) throws SQLException {
        String saveClient = String.format("INSERT INTO clients(name, address, password) VALUES(\"%s\", \"%s\", \"%s\")", client.getName(), client.getAddress(), client.getPassword());
        statement.executeUpdate(saveClient);
        ResultSet id = statement.executeQuery("SELECT lAST_INSERT_ID();");
        id.next();
        return id.getInt(1);
    }

    static int saveSaving(Statement statement, SavingAccount account) throws SQLException {
        String saveNewAccount = String.format("INSERT INTO accounts(client_id, balance, type, rate, transactions, opening_date) VALUES (%d, %f, 0, %f, %d, \"%s\")", account.getAccountHolder(), account.getBalance(), account.getRate(), account.getNumberOfFreeTransactions(),account.getDateOfOpening().toString());
        statement.executeUpdate(saveNewAccount);
        ResultSet id = statement.executeQuery("SELECT lAST_INSERT_ID();");
        id.next();
        return id.getInt(1);
    }

    static int saveChecking(Statement statement, CheckingAccount account) throws SQLException {
        String saveNewAccount = String.format("INSERT INTO accounts(client_id, balance, type, rate, transactions, opening_date) VALUES (%d, %f, 1, NULL, NULL, \"%s\")", account.getAccountHolder(), account.getBalance(), account.getDateOfOpening().toString());
        statement.executeUpdate(saveNewAccount);
        ResultSet id = statement.executeQuery("SELECT lAST_INSERT_ID();");
        id.next();
        return id.getInt(1);
    }

    static void updateName (Statement statement, Client client, String newInfo) throws SQLException {
            String changeName = String.format("UPDATE clients SET name = \"%s\" WHERE client_id = %d", newInfo, client.getClientID());
            statement.executeUpdate(changeName);
        }

    static void updateAddress (Statement statement, Client client, String newInfo) throws SQLException {
            String changeName = String.format("UPDATE clients SET address = \"%s\" WHERE client_id = %d", newInfo, client.getClientID());
            statement.executeUpdate(changeName);
    }

    static void updatePassword (Statement statement, Client client, String newInfo) throws SQLException {
        String changePassword = String.format("UPDATE clients SET password = \"%s\" WHERE client_id = %d", newInfo, client.getClientID());
        statement.executeUpdate(changePassword);
    }

    static void deleteAccount (Statement statement, Account account) throws SQLException {
            String deleteAccount = String.format("DELETE FROM accounts WHERE account_id = %d", account.getAccountNumber());
            statement.executeUpdate(deleteAccount);
    }

    static void deleteClient(Statement statement, Client client) throws SQLException {
        String deleteClient = String.format("DELETE FROM clients WHERE client_id = %d", client.getClientID());
        statement.executeUpdate(deleteClient);
    }

    static void updateAccountBalance(Statement statement, Account account) throws SQLException {
        String updateAccountBalance = String.format("UPDATE accounts SET balance = %f WHERE account_id = %d", account.getBalance(), account.getAccountNumber());
        statement.executeUpdate(updateAccountBalance);
    }

    static int accountTransactionsInMonth(Statement statement, Account account) throws SQLException {
        String query = String.format("SELECT count(*) FROM bank_app.transactions " +
                "WHERE account_id = %d AND MONTH(date) = month(now()) AND YEAR(date) = YEAR(now());", account.getAccountNumber());
        ResultSet transactions = statement.executeQuery(query);
        transactions.next();
        return transactions.getInt(1);
    }

    static void addTransaction(Statement statement, Account account, double amount,  String transaction) throws SQLException {
        String addTransaction = String.format("INSERT INTO transactions (account_id, transaction_type, date, amount) VALUES (%d, \"%s\", \"%s\", %f)", account.getAccountNumber(), transaction, LocalDate.now().toString(), amount);
        statement.executeUpdate(addTransaction);
    }

    static String returnPassword(Statement statement, String username) throws SQLException {
        String selectClientPassword = String.format("SELECT password from clients WHERE client_id = \"%s\"", username);
        ResultSet password = statement.executeQuery(selectClientPassword);
        password.next();
        return password.getString(1);
    }

    static void deleteTransactions(Statement statement, Account account) throws SQLException {
        String addTransaction = String.format("DELETE FROM transactions WHERE account_id = %d", account.getAccountNumber());
        statement.executeUpdate(addTransaction);
    }

}


