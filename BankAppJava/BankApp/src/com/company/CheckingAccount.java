package com.company;

import java.sql.Statement;
import java.time.LocalDate;

public class CheckingAccount extends Account{
    private final String type = "Checking";

    public CheckingAccount(int accountHolder, double balance) {
        super(accountHolder, balance);
    }

    public CheckingAccount(int accountNumber, int accountHolder, double balance, LocalDate dateOfOpening){
        super(accountNumber, accountHolder, balance);
        this.dateOfOpening = dateOfOpening;
    }

    public String getType() { return type; }

    @Override
    public void withdraw(double amount, Statement statement) throws Exception {
        if(amount > balance){
                throw new Exception("No sufficient fund");
            }
        else{
                balance -= amount;
            }
    }

    @Override
    public void transfer(double amount, Account object, Statement statement) throws Exception {
        if(amount > balance){
            throw new Exception("No sufficient fund");
        }
        else{
            balance -= amount;
            object.deposit(amount);
        }
    }

    @Override
    public String toString() {
        return  "Account " + getAccountNumber() + "\n" +
                "Type: " + type + "\n" +
                "Date of opening: " + getDateOfOpening().toString() + "\n" +
                "Balance: " + balance + "\n" +
                ".......................................";
    }
}
