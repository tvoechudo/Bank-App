package com.company;

import java.util.ArrayList;

public class Admin {
    protected ArrayList<Client> clients = new ArrayList<>();
    protected ArrayList<Account> accounts = new ArrayList<>();

    public void addClient(Client client) {
        clients.add(client);
    }

    public void addAccount(Account account){
        accounts.add(account);
    }

    public ArrayList<Client> findClient (String name){
        ArrayList<Client> found_clients = new ArrayList<>();
        for (Client client: clients) {
            if (client.getName().equals(name)) {
                found_clients.add(client);
            }
        }
        return found_clients;
    }

    public void deleteAccount(Account account){
        accounts.remove(account);
    }

    public void deleteClient(Client client){
        clients.remove(client);
    }

    public void showClients(){
        System.out.println();
        System.out.println("Clients: " + clients.size());
        System.out.println();
        for (Client client: clients){
           System.out.println(client.getName());
       }
    }

    public void showAccounts(){
        int saving = 0;
        int checking = 0;
        double totalBalance = 0;
        System.out.println();
        System.out.println("Accounts: " + accounts.size());
        System.out.println();
        for (Account account: accounts) {
            System.out.println(account.mainInfo());
            if (account.getType().equals("Saving")) {saving++;}
            else {checking++;}
            totalBalance += account.getBalance();
        }
        System.out.println();
        System.out.println("Saving accounts: " + saving);
        System.out.println("Checking accounts: " + checking);
        System.out.println("Total balance: " + totalBalance);
    }

}
