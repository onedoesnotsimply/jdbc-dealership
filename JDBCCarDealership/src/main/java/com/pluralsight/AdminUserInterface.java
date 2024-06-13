package com.pluralsight;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class AdminUserInterface {
    private int password;
    private DataSource dataSource;
    //static ArrayList<String> contracts;

    public AdminUserInterface(DataSource dataSource) {
        this.dataSource=dataSource;
        this.password = 123;
        //contracts = new ArrayList<>();
    }

    public boolean checkPassword(int userInput){
        if (userInput==password){
            return true;
        }
        return false;
    }

    public void adminDisplay() {
        //init(); // Populate the contracts array
        //Collections.reverse(contracts);
        while(true){
            System.out.println("Admin Menu");
            System.out.println("1) List All Sales Contracts");
            System.out.println("2) List All Lease Contracts");
            System.out.println("3) List Last 10 Contracts");
            System.out.println("5) Return to User Interface");

            Scanner scanner = new Scanner(System.in);
            int choice = scanner.nextInt();

            switch (choice){
                case 1:
                    getAllSalesContracts(salesDao);
                    break;
                case 2:
                    getAllLeaseContracts(leaseDao);
                    break;
                case 3:
                    getLastTenContracts();
                    break;
                case 4:
                    UserInterface ui = new UserInterface(dataSource);
                    ui.display();
                    break;
                default:
                    System.out.println("Invalid input\nInput out of range");
                    break;
            }
        }
    }

    public static void getAllLeaseContracts(LeaseDao leaseDao) {
        List<LeaseContract> leaseContracts = leaseDao.getAllLeaseContracts();
        leaseContracts.forEach(System.out::println);
    }

    public static void getLastTenContracts() {
        for (int i = 0; (i < 10 && i < contracts.size()); i++){
            System.out.println(contracts.get(i));
        }
    }

    public static void getAllSalesContracts(SalesDao salesDao) {
        List<SalesContract> salesContracts = salesDao.getAllSalesContracts();
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