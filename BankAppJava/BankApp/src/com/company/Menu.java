package com.company;

import java.util.Scanner;

public class Menu {
    static Scanner scanner = new Scanner(System.in);
    public static final String TEXT_RESET = "\u001B[0m";
    public static final String TEXT_YELLOW = "\u001B[33m";

    public static int readCommand(String prompt) {
        Scanner input = new Scanner((System.in));
        System.out.print(prompt);
        if (input.hasNextInt())
            return input.nextInt();
        else {
            return 0;
        }
    }

    public static String readData(String prompt){
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }

    public static double readValue(String prompt){
        double value;
        do {
            System.out.print(prompt);
            while (!scanner.hasNextDouble()) {
                System.out.println("Invalid input! Please try again");
                scanner.next();
            }
            value = scanner.nextDouble();
        } while (value <=0);
        return value;
    }

    public static int readInt(String prompt){
        int value;
        do {
            System.out.print(prompt);
            while (!scanner.hasNextInt()) {
                System.out.println("Invalid input! Please try again");
                scanner.next();
            }
            value = scanner.nextInt();
        } while (value <=0);
        return value;
    }

    public static int adminOptions(){
        System.out.println("--------------------------");
        System.out.println(TEXT_YELLOW + "1) Create new client");
        System.out.println("2) Work with client");
        System.out.println("3) Show all clients");
        System.out.println("4) Show all accounts");
        System.out.println("5) Quit"  + TEXT_RESET);
        System.out.println("--------------------------");
        int command = Menu.readCommand( TEXT_YELLOW + "Enter the command number: " + TEXT_RESET);
        if ((command > 0) && (command <= 5)) return command;
        else return 0;
    }

    public static int clientOptions(){
        System.out.println("--------------------------");
        System.out.println(TEXT_YELLOW + "1) Display balance");
        System.out.println("2) Deposit");
        System.out.println("3) Withdraw");
        System.out.println("4) Transfer");
        System.out.println("5) Change Address");
        System.out.println("6) Change Password");
        System.out.println("7) Pay bills");
        System.out.println("8) Calculate interest rate");
        System.out.println("9) Quit"  + TEXT_RESET);
        System.out.println("--------------------------");
        int command = Menu.readCommand( TEXT_YELLOW + "Enter the command number: " + TEXT_RESET);
        if ((command > 0) && (command <= 9)) return command;
        else return 0;
    }

    public static int workWithClient(){
        System.out.println("--------------------------");
        System.out.println(TEXT_YELLOW  + "1) Show information");
        System.out.println("2) Change Information");
        System.out.println("3) Create new account");
        System.out.println("4) Work with account");
        System.out.println("5) Delete account");
        System.out.println("6) Delete client");
        System.out.println("7) Back" + TEXT_RESET);
        System.out.println("--------------------------");
        int command = Menu.readCommand(TEXT_YELLOW + "Enter the command number: " + TEXT_RESET);
        if ((command > 0) && (command <= 7)) return command;
        else return 0;
    }

    public static int changeClientInfo(){
        System.out.println("--------------------------");
        System.out.println(TEXT_YELLOW  + "1) Change name");
        System.out.println("2) Change address");
        System.out.println("3) Back" + TEXT_RESET);
        System.out.println("--------------------------");
        int command = Menu.readCommand(TEXT_YELLOW + "Enter the command number: " + TEXT_RESET);
        if ((command > 0) && (command <= 3)) return command;
        else return 0;
    }

    public static int createAccount(){
        System.out.println("--------------------------");
        System.out.println(TEXT_YELLOW  + "1) Saving Account");
        System.out.println("2) Checking Account");
        System.out.println("3) Back" + TEXT_RESET);
        System.out.println("--------------------------");
        int command = Menu.readCommand(TEXT_YELLOW + "Enter the command number: " + TEXT_RESET);
        if ((command > 0) && (command <= 3)) return command;
        else return 0;
    }

    public static int selectAccount(){
        System.out.println("--------------------------");
        System.out.println(TEXT_YELLOW  + "1) Withdraw");
        System.out.println("2) Deposit");
        System.out.println("3) Transfer");
        System.out.println("4) Back" + TEXT_RESET);
        System.out.println("--------------------------");
        int command = Menu.readCommand(TEXT_YELLOW + "Enter the command number: " + TEXT_RESET);
        if ((command > 0) && (command <= 4)) return command;
        else return 0;
    }

    public static Client addNewClient(){
        String name = Menu.readData("Enter name: ");
        String address = Menu.readData("Enter address: ");
        return new Client(name, address);
    }

    public static SavingAccount createSavingAccount(Client client){
        double balance = Menu.readValue("Enter start balance: ");
        double rate = Menu.readValue("Enter rate: ");
        int transactions = Menu.readInt("Enter the number of free transactions: ");
        return new SavingAccount(client.getClientID(), balance, rate, transactions);
    }

    public static CheckingAccount createCheckingAccount(Client client){
        double balance = Menu.readValue("Enter start balance: ");
        return new CheckingAccount(client.getClientID(), balance);
    }
}
