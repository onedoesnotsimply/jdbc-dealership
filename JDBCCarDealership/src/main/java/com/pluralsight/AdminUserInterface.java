package com.pluralsight;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class AdminUserInterface {
    private int password;
    static ArrayList<String> contracts;

    public AdminUserInterface() {
        this.password = 123;
        contracts = new ArrayList<>();
    }

    public boolean checkPassword(int userInput){
        if (userInput==password){
            return true;
        }
        return false;
    }

    public void adminDisplay() {
        init(); // Populate the contracts array
        Collections.reverse(contracts);
        while(true){
            System.out.println("Admin Menu");
            System.out.println("1) List All Contracts");
            System.out.println("2) List Last 10 Contracts");
            System.out.println("3) List All Sales Contracts");
            System.out.println("4) List All Lease Contracts");
            System.out.println("5) Return to User Interface");


            Scanner scanner = new Scanner(System.in);


            int choice = scanner.nextInt();

            switch (choice){
                case 1:
                    getAllContracts();
                    break;
                case 2:
                    getLastTenContracts();
                    break;
                case 3:
                    getAllSalesContracts();
                    break;
                case 4:
                    getAllLeaseContracts();
                    break;
                case 5:
                    UserInterface ui = new UserInterface();
                    ui.display();
                    break;
                default:
                    System.out.println("Invalid input\nInput out of range");
                    break;
            }



        }

    }

    public static void getAllLeaseContracts() {
        for (String contract : contracts) {
            if (contract.split("\\|")[0].equalsIgnoreCase("lease")){
                System.out.println(contract);
            }
        }
    }

    public static void getLastTenContracts() {
        for (int i = 0; (i < 10 && i < contracts.size()); i++){
            System.out.println(contracts.get(i));
        }
    }

    public static void getAllSalesContracts() {
        for (String contract : contracts) {
            if (contract.split("\\|")[0].equalsIgnoreCase("sale")){
                System.out.println(contract);
            }
        }
    }

    private void init() {
        contracts = ContractDataManager.getContracts();
    }

    public static void getAllContracts() {
        for (String contract : contracts){
            System.out.println(contract);
        }
    }
}