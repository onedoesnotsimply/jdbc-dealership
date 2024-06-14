package com.pluralsight;

import javax.sql.DataSource;
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
        SalesDao salesDao = new SalesDao(dataSource);
        LeaseDao leaseDao = new LeaseDao(dataSource);

        while(true){
            System.out.println("""
                    Admin Menu
                    --------------------
                    1) List All Sales Contracts
                    2) List All Lease Contracts
                    3) List Last 10 Sales Contracts
                    4) List Last 10 Lease Contracts
                    0) Return to Dealership Select
                    """);

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
                    getLastTenSalesContracts(salesDao);
                    break;
                case 4:
                    getLastTenLeaseContracts(leaseDao);
                    break;
                case 0:
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

    public static void getLastTenSalesContracts(SalesDao salesDao) {
        List<SalesContract> salesContracts = salesDao.getLastTenSalesContracts();
        salesContracts.forEach(System.out::println);
    }

    public static void getLastTenLeaseContracts(LeaseDao leaseDao) {
        List<LeaseContract> leaseContracts = leaseDao.getLastTenLeaseContracts();
        leaseContracts.forEach(System.out::println);
    }

    public static void getAllSalesContracts(SalesDao salesDao) {
        List<SalesContract> salesContracts = salesDao.getAllSalesContracts();
        salesContracts.forEach(System.out::println);
    }

}