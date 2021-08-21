package com.company;

import java.sql.*;
import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {

        try {
            System.out.println("Welcome to the Bank App!");
            Class.forName("com.mysql.cj.jdbc.Driver").getDeclaredConstructor().newInstance();
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/bank_app", "root", "tatiana");
            Statement statement = conn.createStatement();


            do {
                System.out.println("You are in the login menu. Type 0 in username field to quit.");
                String username = Menu.readData("Username: ");
                if (username.equals("0")) {
                    break;
                }
                String password = Menu.readData("Password: ");
                if (username.equals("admin") && password.equals("aDmIn")) {
                    adminMenu(statement);
                }
                try {

                    if (Sql.returnPassword(statement, username).equals(password)) {
                        clientMenu(statement, username);
                    } else {
                        System.out.println("Invalid password");
                    }
                } catch (SQLException e) {
                    System.out.println("No such user");
                }
            } while (true);

            conn.close();
            System.out.println("Thank you for using the Bank App");
        }
        catch (Exception e){
            System.out.println("Oops! Something went wrong. Please try again later.");
        }

   }

    public static void adminMenu(Statement statement) throws Exception {
        // menu for admin -- start
        Admin admin = new Admin();

        //add all clients to the admin object
        ResultSet allClients = statement.executeQuery("SELECT * FROM clients");
        while (allClients.next()){
            Client client = new Client(allClients.getInt(1), allClients.getString(2), allClients.getString(3), allClients.getString(4));
            admin.addClient(client);
        }

        //add all accounts to the admin object and add account to the client objects
        ResultSet allAccounts = statement.executeQuery("SELECT * FROM accounts");
        while(allAccounts.next()) {
            if (allAccounts.getInt("type") == 0) {
                SavingAccount savingAccount = new SavingAccount(allAccounts.getInt(1), allAccounts.getInt(2), allAccounts.getDouble(3), allAccounts.getDouble(5), allAccounts.getInt(6), allAccounts.getDate(7).toLocalDate());
                admin.addAccount(savingAccount);
                for (Client client : admin.clients) {
                    if (client.getClientID() == savingAccount.getAccountHolder()) {
                        client.addAccount(savingAccount);
                        break;
                    }
                }
            }
            if (allAccounts.getInt("type") == 1) {
                CheckingAccount checkingAccount = new CheckingAccount(allAccounts.getInt(1), allAccounts.getInt(2), allAccounts.getDouble(3), allAccounts.getDate(7).toLocalDate());
                admin.addAccount(checkingAccount);
                for (Client client : admin.clients) {
                    if (client.getClientID() == checkingAccount.getAccountHolder()) {
                        client.addAccount(checkingAccount);
                        break;
                    }
                }
            }
        }

        boolean quit = false;
        do {
            int command = Menu.adminOptions();
            switch (command) {
                case 1:       // create a new client
                    Client newClient = Menu.addNewClient();
                    int id = Sql.saveClient(statement, newClient);
                    newClient.setClientID(id);
                    admin.addClient(newClient);
                    System.out.println("Login: " + newClient.getClientID());
                    System.out.println("Password: " + newClient.getPassword());
                    break;
                case 2: // work with client
                    String clientName = Menu.readData("Enter the client's name: ");
                    ArrayList<Client> found_clients = admin.findClient(clientName);

                    if (found_clients.size() == 1) {  // only one client with such name
                        Client client = found_clients.get(0);
                        workWithClient(admin, client, statement); //menu for operation with client
                    } else if (found_clients.size() == 0) { // no clients with such name
                        System.out.println("No such client");
                    } else { // more than one client with such name
                        int no = 1;
                        for (Client client : found_clients) {
                            System.out.println("\u001B[33m" + no + ") " + "\u001B[0m" + client.mainInfo());
                            no++;
                        }
                        int client_no = Menu.readCommand("Enter the number of client: ") - 1;
                        Client client = found_clients.get(client_no);
                        workWithClient(admin, client, statement); //menu for operation with client
                    }
                    break;
                case 3: // show all clients
                    admin.showClients();
                    break;
                case 4: // show all accounts
                    admin.showAccounts();
                    break;
                case 5:
                    quit = true;
                    break; //quit
                case 0:
                    System.out.println("No such command! Please try again.");

                    break;
            }
        } while (!quit);

    }
    public static void workWithClient(Admin admin, Client client, Statement statement) throws Exception {
        boolean back = false;
        do {
            System.out.println((client.mainInfo()));
            client.showAccounts();
            int command = Menu.workWithClient();
            switch (command) {
                case 1: // Show client info
                    System.out.println(client);
                    break;
                case 2: // Change Client Info
                    int commandChange = Menu.changeClientInfo();
                    switch (commandChange) {
                        case 1:
                            String name = Menu.readData("Enter the new name: ");
                            client.setName(name);
                            Sql.updateName(statement, client, name);
                            break;
                        case 2:
                            String address = Menu.readData("Enter the new address: ");
                            client.setAddress(address);
                            Sql.updateAddress(statement, client, address);
                            break;
                        case 3: // Back
                            break;
                        case 0:
                            System.out.println("No such command! Please try again.");
                            break;
                    }
                    break;
                case 3: // Create new account
                    int commandNewAccount = Menu.createAccount();
                    switch (commandNewAccount) {
                        case 1: // Saving Account
                            SavingAccount newSaving = Menu.createSavingAccount(client);
                            int save_id = Sql.saveSaving(statement, newSaving);
                            newSaving.setAccountNumber(save_id);
                            client.addAccount(newSaving);
                            admin.addAccount(newSaving);
                            System.out.println("Saving account created");
                            break;
                        case 2: // Checking Account
                            CheckingAccount newChecking = Menu.createCheckingAccount(client);
                            int check_id = Sql.saveChecking(statement, newChecking);
                            newChecking.setAccountNumber(check_id);
                            client.addAccount(newChecking);
                            admin.addAccount(newChecking);
                            System.out.println("Checking account created");
                            break;
                        case 3: // Back
                            break;
                        case 0:
                            System.out.println("No such command! Please try again.");
                            break;
                    }
                    break;
                case 4: // Work with account
                    client.showAccounts();
                    int accountNumber = Menu.readInt("Enter the account number: ");
                    Account workingAccount = client.selectAccount(accountNumber);
                    if (workingAccount == null) {
                        System.out.println("No such account! Please try again.");
                    }
                    else {
                        OperationsInAccount(admin, workingAccount, statement);
                    }
                    break;
                case 5: // Delete Account
                    client.showAccounts();
                    int accountNo = Menu.readInt("Enter the account number: ");
                    Account selected_account = client.selectAccount(accountNo);
                    if (selected_account == null) {
                        System.out.println("No such account! Please try again.");
                    } else {
                        admin.deleteAccount(selected_account);
                        client.deleteAccount(selected_account);
                        Sql.deleteTransactions(statement, selected_account);
                        Sql.deleteAccount(statement, selected_account);
                        System.out.println("Account deleted");
                    }
                    break;
                case 6: // Delete client
                    try{
                        admin.deleteClient(client); //go to the main menu
                        Sql.deleteClient(statement, client);
                        System.out.println("Client deleted");
                        back = true;
                    }
                    catch (SQLException e){
                        System.out.println("You can't delete client with existing account");
                    }
                    break;
                case 7:
                    back = true;
                    break;
                case 0:
                    System.out.println("No such command! Please try again.");
                    break;
            }
        } while(!back);
    }

    public static void clientMenu(Statement statement, String username) throws Exception {
        // menu for client -- start, the username here is client_id

        // get the client info
        String query = String.format("SELECT * FROM clients where client_id = \"%s\"", username);

        ResultSet currentClient = statement.executeQuery(query);
        currentClient.next();
        Client client = new Client(currentClient.getInt(1), currentClient.getString(2), currentClient.getString(3), currentClient.getString(4));

        // find all accounts of this client
        query = String.format("SELECT * FROM accounts where client_id = \"%s\"", username);
        ResultSet clientAccounts = statement.executeQuery(query);

        while(clientAccounts.next()) {
            if (clientAccounts.getInt("type") == 0) {
                SavingAccount savingAccount = new SavingAccount(clientAccounts.getInt(1), clientAccounts.getInt(2), clientAccounts.getDouble(3), clientAccounts.getDouble(5), clientAccounts.getInt(6), clientAccounts.getDate(7).toLocalDate());
                client.addAccount(savingAccount);

            }
            if (clientAccounts.getInt("type") == 1) {
                CheckingAccount checkingAccount = new CheckingAccount(clientAccounts.getInt(1), clientAccounts.getInt(2), clientAccounts.getDouble(3), clientAccounts.getDate(7).toLocalDate());
                client.addAccount(checkingAccount);
            }
        }

        boolean quit = false;
        do {
            int command = Menu.clientOptions();
            switch (command) {
                case 1:  // Display balance
                    System.out.println(client);
                    break;
                case 2:  // Deposit
                    client.showAccounts();
                    int depositAccountNumber = Menu.readInt("Enter the account number: ");
                    Account depositAccount = client.selectAccount(depositAccountNumber);
                    if (depositAccount == null) {
                        System.out.println("No such account! Please try again.");
                    }
                    else {
                        double depositAmount = Menu.readValue("How much do you want to deposit: ");
                        depositAccount.deposit(depositAmount);
                        Sql.updateAccountBalance(statement, depositAccount);
                    }
                    break;
                case 3:  // Withdraw
                    client.showAccounts();
                    int withdrawAccountNumber = Menu.readInt("Enter the account number: ");
                    Account withdrawAccount = client.selectAccount(withdrawAccountNumber);
                    if (withdrawAccount == null) {
                        System.out.println("No such account! Please try again.");
                    }
                    else {
                        double withdrawAmount = Menu.readValue("How much do you want to withdraw: ");

                        if (withdrawAccount.getType().equals("Saving")) {
                            SavingAccount savingAccount = (SavingAccount) withdrawAccount;
                            if (savingAccount.applyFee(statement)) {
                                System.out.println("You will be charge " + savingAccount.getTransactionFee() + " transaction fee");
                            } else {
                                int transaction_num = Sql.accountTransactionsInMonth(statement, savingAccount);
                                System.out.println("You have done "+ transaction_num + " transactions in this month.");
                                System.out.println("Current transaction is free.");
                            }
                        } else {
                            System.out.println("Transaction is free");
                        }
                        int command1 = Menu.readCommand("Type 1 to confirm ");
                        if (command1 == 1) {
                            try {
                                withdrawAccount.withdraw(withdrawAmount, statement);
                                Sql.updateAccountBalance(statement, withdrawAccount);
                                Sql.addTransaction(statement, withdrawAccount, withdrawAmount, "withdraw");
                            }
                            catch (Exception e){
                                System.out.println(e.getMessage());
                                break;
                            }
                        }
                        break;
                    }
                    break;
                case 4:  // Transfer
                    client.showAccounts();
                    int transferAccountNumber = Menu.readInt("Enter the account number you transfer money from: ");
                    int receivingAccountNumber = Menu.readInt("Enter the receiving account number: ");

                    Account transferAccount = client.selectAccount(transferAccountNumber);
                    Account receiveAccount = client.selectAccount(receivingAccountNumber);

                    if (transferAccount == null)
                    {
                        System.out.println("Your account information incorrect! Please try again.");
                    }
                    else if (receiveAccount == null) {
                        System.out.println("Receiving account information incorrect! Please try again.");
                    }
                    else {
                        double transferAmount = Menu.readValue("How much do you want to transfer: ");
                        if (transferAccount.getType().equals("Saving")) {
                            SavingAccount savingAccount = (SavingAccount) transferAccount;
                            if (savingAccount.applyFee(statement)) {
                                System.out.println("You will be charge " + savingAccount.getTransactionFee() + " transaction fee");
                            } else {
                                int transaction_num = Sql.accountTransactionsInMonth(statement, savingAccount);
                                System.out.println("You have done "+ transaction_num + " transactions in this month.");
                                System.out.println("Current transaction is free.");
                            }
                        }
                        else {
                            System.out.println("Transaction is free");
                        }

                        int confirm = Menu.readCommand("Type 1 to confirm ");
                        if (confirm == 1) {
                            try {
                                transferAccount.transfer(transferAmount, receiveAccount, statement);
                                Sql.updateAccountBalance(statement, transferAccount);
                                Sql.updateAccountBalance(statement, receiveAccount);
                                Sql.addTransaction(statement, transferAccount, transferAmount, "transfer");
                            } catch (Exception e) {
                                System.out.println(e.getMessage());
                                break;
                            }

                        }
                        break;
                    }
                    break;
                case 5:  // Change Address
                    String address = Menu.readData("Enter the new address: ");
                    client.setAddress(address);
                    Sql.updateAddress(statement, client, address);
                    break;
                case 6:  // Change Password
                    String password = Menu.readData("Enter the new password: ");
                    client.setPassword(password);
                    Sql.updatePassword(statement, client, password);
                    break;
                case 7:  // Pay bills
                    client.showAccounts();
                    int accountNumber = Menu.readInt("Choose the account number to pay the bill: ");
                    Account paybillAccount = client.selectAccount(accountNumber);
                    if (paybillAccount == null) {
                        System.out.println("No such account! Please try again.");
                    }
                    else if (paybillAccount.getType().equals("Saving")) {
                        System.out.println("We suggest you use Checking account to pay your bills.");
                    }
                    else {
                        int bill_number = Menu.readInt("Please enter your bill number: ");
                        double billAmount = Menu.readValue("How much do you want to pay: ");

                        System.out.println("Transaction is free");

                        int command1 = Menu.readCommand("Type 1 to confirm ");
                        if (command1 == 1) {
                            try {
                                paybillAccount.withdraw(billAmount, statement);
                                Sql.updateAccountBalance(statement, paybillAccount);
                                Sql.addTransaction(statement, paybillAccount, billAmount, "paybill");
                                System.out.println("Bill Number: " + bill_number + " has been paid.");
                            }
                            catch (Exception e){
                                System.out.println(e.getMessage());
                                break;
                            }
                        }
                        break;
                    }
                    break;
                case 8:
                    client.showAccounts();
                    int accountNo = Menu.readInt("Choose the account number to calculate interest rate: ");
                    Account calculateRateAccount = client.selectAccount(accountNo);
                    if (calculateRateAccount == null) {
                        System.out.println("No such account! Please try again.");
                    }
                    else if (calculateRateAccount.getType().equals("Checking")) {
                    System.out.println("Checking accounts have no rate.");
                    }
                    else {
                        SavingAccount calculateInterest = (SavingAccount) calculateRateAccount;
                        int months = Menu.readInt("How much months will you hold your deposit: ");
                        System.out.println("Your earned interest: " + calculateInterest.calculateInterest(months));
                    }
                    break;
                case 9:  //quit
                    quit = true;
                    break;
                case 0:
                    System.out.println("No such command! Please try again.");

                    break;
            }
        } while (!quit);

    }

    public static void OperationsInAccount(Admin admin, Account account, Statement statement) throws Exception {
        boolean back = false;
        do{
            System.out.println(account.mainInfo());
            int accountOperation = Menu.selectAccount();
            switch (accountOperation) {
                case 1:
                    // Withdraw
                    double withdrawAmount = Menu.readValue("How much do you want to withdraw: ");

                    if (account.getType().equals("Saving")) {
                        SavingAccount savingAccount = (SavingAccount) account;
                        if (savingAccount.applyFee(statement)) {
                            System.out.println("You will be charge " + savingAccount.getTransactionFee() + " transaction fee");
                        } else {
                            System.out.println("Transaction is free");
                        }
                    } else {
                        System.out.println("Transaction is free");
                    }
                    int command = Menu.readCommand("Type 1 to confirm ");
                    if (command == 1) {
                        try {
                            account.withdraw(withdrawAmount, statement);
                            Sql.updateAccountBalance(statement, account);
                            Sql.addTransaction(statement, account, withdrawAmount, "withdraw");
                        }
                        catch (Exception e){
                            System.out.println(e.getMessage());
                            break;
                        }
                    }
                    else break;
                    break;
                case 2:
                    // Deposit
                    double depositAmount = Menu.readValue("How much do you want to deposit: ");
                    account.deposit(depositAmount);
                    Sql.updateAccountBalance(statement, account);
                    break;
                case 3:
                    // Transfer
                    double transferAmount = Menu.readValue("How much do you want to transfer: ");
                    if (account.getType().equals("Saving")) {
                        SavingAccount savingAccount = (SavingAccount) account;
                        if (savingAccount.applyFee(statement)) {
                            System.out.println("You will be charge " + savingAccount.getTransactionFee() + " transaction fee");
                        } else {
                            System.out.println("Transaction is free");
                        }
                    }
                    else {
                        System.out.println("Transaction is free");
                    }
                    int receivingAccountNumber = Menu.readInt("Enter the account number to receive the money: ");
                    Account receivingAccount = null;
                    for (Account oneAccount : admin.accounts) {
                        if (oneAccount.getAccountNumber() == receivingAccountNumber) {
                            receivingAccount = oneAccount;
                        }
                    }
                    if (receivingAccount == null) {
                            System.out.println("Receiving account does not exist.");
                    }
                    else {
                        int confirm = Menu.readCommand("Type 1 to confirm ");
                        if (confirm == 1) {
                            try {
                                account.transfer(transferAmount, receivingAccount, statement);
                                Sql.updateAccountBalance(statement, account);
                                Sql.updateAccountBalance(statement, receivingAccount);
                                Sql.addTransaction(statement, account, transferAmount, "transfer");
                            } catch (Exception e) {
                                System.out.println(e.getMessage());
                                break;
                            }
                        }
                        else break;
                    }
                    break;
                case 4:
                    back = true;
                    break;
                case 0:
                    System.out.println("No such command! Please try again.");
                    break;
            }
        }while(!back);
    }


}


