package com.company;

import java.sql.Date;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;

public abstract class Account {
    protected int accountNumber;
    protected int accountHolder;
    protected double balance;
    protected LocalDate dateOfOpening;
    private String type;

    public Account(int accountNumber, int accountHolder, double balance) {
        this.accountNumber = accountNumber;
        this.accountHolder = accountHolder;
        this.balance = balance;
        this.dateOfOpening = LocalDate.now();
    }

    public Account(int accountHolder, double balance) {
        this.accountHolder = accountHolder;
        this.balance = balance;
        this.dateOfOpening = LocalDate.now();
    }

    public void setAccountNumber(int accountNumber) {
        this.accountNumber = accountNumber;
    }

    public int getAccountNumber() {
        return accountNumber;
    }

    public int getAccountHolder() {
        return accountHolder;
    }

    public double getBalance() {
        return balance;
    }

    public String getType() { return type; }

    public LocalDate getDateOfOpening() {
        return dateOfOpening;
    }

    public void deposit(double amount){
        balance += amount;
    }

    public abstract void withdraw(double amount, Statement statement) throws Exception;

    public abstract void transfer(double amount, Account object, Statement statement) throws Exception;

    public String mainInfo(){
        return accountNumber + " " + " " + getType() + " " + balance;
    }
}
