package com.company;

import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;

public class SavingAccount extends Account{

    private final String type = "Saving";
    private final double transactionFee = 1.00;

    private double rate;
    private int numberOfFreeTransactions;


    public SavingAccount(int accountHolder, double balance, double rate, int numberOfFreeTransactions){
        super(accountHolder, balance);
        this.rate = rate;
        this.numberOfFreeTransactions = numberOfFreeTransactions;
    }

    public SavingAccount(int accountNumber, int accountHolder, double balance, double rate, int numberOfFreeTransactions, LocalDate dateOfopening){
        super(accountNumber, accountHolder, balance);
        this.rate = rate;
        this.numberOfFreeTransactions = numberOfFreeTransactions;
        this.dateOfOpening = dateOfopening;
    }

    public String getType() { return type; }

    public double getTransactionFee() {
        return transactionFee;
    }

    public double getRate() {
        return rate;
    }

    public int getNumberOfFreeTransactions() {
        return numberOfFreeTransactions;
    }

    public boolean applyFee(Statement statement) throws SQLException {
        int transactions = Sql.accountTransactionsInMonth(statement, this);
        return transactions >= numberOfFreeTransactions;
    }

    @Override
    public void withdraw(double amount, Statement statement) throws Exception {
        // check for fee
        if (applyFee(statement)){
            if (amount + transactionFee > balance){
                throw new Exception("No sufficient fund");
            }
            else{
                    balance -= (amount + transactionFee);
                }
        }
        else {
            if (amount > balance){
                throw new Exception("No sufficient fund");
            } else{
                    balance -= amount;
            }
        }
    }

    @Override
    public void transfer(double amount, Account object, Statement statement) throws Exception {
        if (!applyFee(statement) || this.getAccountHolder() == object.getAccountHolder()){
            if (amount > balance){
                throw new Exception("No sufficient fund");
            } else{
                balance -= amount;
                object.deposit(amount);
            }
        }
        else {
            if (amount + transactionFee > balance) {
                throw new Exception("No sufficient fund");
            } else {
                balance -= (amount + transactionFee);
                object.deposit(amount);
            }
        }
    }

    public double calculateInterest(int months) {
        return (balance * Math.pow(1+rate/12, months/12)) - balance;
    }

    @Override
    public String toString() {
        return  "Account " + getAccountNumber() + "\n" +
                "Type: " + type + "\n" +
                "Date of opening: " + getDateOfOpening().toString() + "\n" +
                "Rate: " + rate + "\n" +
                "FreeTransactions: " + numberOfFreeTransactions + "\n" +
                "Balance: " + balance + "\n" +
                ".......................................";

    }
}
