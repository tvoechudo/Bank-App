package com.company;

import java.util.ArrayList;
import java.util.Random;

public class Client {
    private int clientID;
    private String name;
    private String address;
    private String password;
    private ArrayList <Account> accounts = new ArrayList<>();

    public Client(String name, String address) {
        this.name = name;
        this.address = address;
        this.password = getRandomString();
    }

    public Client(int id, String name, String address, String password) {
        this.clientID = id;
        this.name = name;
        this.address = address;
        this.password = password;
    }

    public int getClientID() {
        return clientID;
    }

    public void setClientID(int id){
        this.clientID = id;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void addAccount(Account account){
        accounts.add(account);
    }

    public void showAccounts(){
        for (Account account: accounts) {
            System.out.println(account.mainInfo());
        }
    }

    public Account selectAccount(int input_no){
        for (Account account: accounts) {
            if (account.getAccountNumber() == input_no){
                return account;
            }
        }
        return null;
    }

    public void deleteAccount(Account account){
        accounts.remove(account);
    }

    @Override
    public String toString() {
        StringBuilder accounts_list = new StringBuilder();
        for (Account account: accounts) {
            String s = account.toString() + "\n";
            accounts_list.append(s);
        }
        return "---------------------------------------" + "\n" +
                name + "\n" +
               "Login: " + clientID + '\n' +
               "Address: " + address + '\n' +
               "---------------------------------------" + "\n" +
               "Accounts: " + "\n" + accounts_list;
    }

    public String mainInfo(){
        return "\n" + name + "\n" +
                "Login: " + clientID + '\n' +
                "Address: " + address + '\n';
    }

    private String getRandomString(){
        String alphaNumeric = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder line = new StringBuilder();
        Random random = new Random();
        int length = 8;
        for(int i = 0; i < length; i++) {
            int index = random.nextInt(alphaNumeric.length());
            char randomChar = alphaNumeric.charAt(index);
            line.append(randomChar);
        }
        return line.toString();
    }


}
