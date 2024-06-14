package com.pluralsight;

import javax.sql.DataSource;
import java.time.LocalDate;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class UserInterface {
    static Scanner scanner = new Scanner(System.in);
    private Dealership dealership;

    private DataSource dataSource;

    public UserInterface(DataSource dataSource){
        this.dataSource=dataSource;
    }

    // Replaces innit()
    public void setDealership(){
        DealershipDao dealershipDao = new DealershipDao(dataSource);
        List<Dealership> dealerships = dealershipDao.getAllDealerships();
        System.out.println("\n\tDealership Selection");
        dealerships.forEach(System.out::println);
        System.out.print("Enter the ID of the dealership you would like to select : ");
        int idChoice = scanner.nextInt();

        Dealership dealership = dealerships.get(idChoice-1);

        this.dealership=dealership;
    }

    public void display() {
        VehicleDao vehicleDao = new VehicleDao(dataSource);
        SalesDao salesDao = new SalesDao(dataSource);
        LeaseDao leaseDao = new LeaseDao(dataSource);
        setDealership();

        while(true) {
            System.out.printf("\nWelcome to %s\n",this.dealership.getName());
            System.out.println("Menu Screen");
            System.out.println("1) Search by Price");
            System.out.println("2) Search by Make and Model");
            System.out.println("3) Search by Year");
            System.out.println("4) Search by Color");
            System.out.println("5) Search by Mileage");
            System.out.println("6) Search by Vehicle Type");
            System.out.println("7) Display All Vehicles");
            System.out.println("8) Add a Vehicle");
            System.out.println("9) Remove a Vehicle");
            System.out.println("10) Purchase/Lease a Vehicle");
            System.out.println("11) Admin Screen");
            System.out.println("12) Return to Dealership Select");
            System.out.println("0) Exit");

            try { // Enforce input type
                int choice = scanner.nextInt();
                scanner.nextLine();

                switch (choice){
                    case 1:
                        processGetByPriceRequest(vehicleDao);
                        break;
                    case 2:
                        processGetByMakeModelRequest(vehicleDao);
                        break;
                    case 3:
                        processGetByYearRequest(vehicleDao);
                        break;
                    case 4:
                        processGetByColorRequest(vehicleDao);
                        break;
                    case 5:
                        processGetByMileageRequest(vehicleDao);
                        break;
                    case 6:
                        processGetByVehicleType(vehicleDao);
                        break;
                    case 7:
                        processGetAllVehiclesRequest(vehicleDao);
                        break;
                    case 8:
                        processAddVehicleRequest(vehicleDao);
                        break;
                    case 9:
                        processRemoveVehicleRequest(vehicleDao);
                        break;
                    case 10:
                        processPurchaseOrLeaseRequest(salesDao, leaseDao, vehicleDao);
                        break;
                    case 11:
                        processAdmin();
                        break;
                    case 12:
                        setDealership();
                        break;
                    case 0:
                        scanner.close();
                        System.exit(0);
                    default: // Enforce input range
                        System.out.println("Invalid input\n");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input\n");
                scanner.nextLine(); // Prevent an infinite loop
            }
        }
    }

    public void processAdmin(){
        AdminUserInterface admin = new AdminUserInterface(dataSource);
        System.out.println("Enter password : ");
        int password = scanner.nextInt();
        if (!admin.checkPassword(password)){
            System.out.println("Incorrect password");
            display();
        } else{
            admin.adminDisplay();
        }
    }

    public void processPurchaseOrLeaseRequest(SalesDao salesDao, LeaseDao leaseDao, VehicleDao vehicleDao) {
        // Prompt for whether this is a purchase or a lease
        System.out.println("1) Purchase");
        System.out.println("2) Lease");
        int choice = scanner.nextInt(); // Get the choice first
        scanner.nextLine();

        // Prompt for general contract data before using the choice
        LocalDate date = LocalDate.now();

        System.out.print("Enter your full name : ");
        String name = scanner.nextLine();

        System.out.print("Enter your email address : ");
        String email = scanner.nextLine();

        System.out.print("Enter the VIN of the vehicle : ");
        int vin = scanner.nextInt();
        scanner.nextLine();
        Vehicle inputVehicle = vehicleDao.getVehicleByVin(vin);

        // Make sure that the vehicle exists
        if (inputVehicle==null) {
            // If it doesn't send the user back to the homescreen
            display();
        }

        // Get the vehicles age for lease condition
        int age = LocalDate.now().getYear()-inputVehicle.getYear();

        // You can't lease a vehicle if it's more than 3 years old
        if ((age > 3) && (choice == 2)) {
            System.out.println("Vehicle too old for lease");
            display();
        }

        if (choice == 1){
            // Prompt for SalesContract data
            System.out.print("Would you like to finance? (Y/N) : ");
            String finance = scanner.nextLine();
            // Create a SalesContract with a default finance input of false
            SalesContract salesContract = new SalesContract(this.dealership.getId(),date,name,email,inputVehicle,false);
            // Check the finance input
            if (finance.equalsIgnoreCase("y")){ // If they did want to finance
                salesContract.setFinanced(true); // Set the finance option to true
            }
            // INSERT INTO db
            salesDao.addSalesContract(this.dealership.getId(), salesContract);

        } else if (choice == 2) {
            // Instantiate a LeaseContract
            LeaseContract leaseContract = new LeaseContract(this.dealership.getId(),date,name,email,inputVehicle);
            // INSERT INTO db
            leaseDao.addLeaseContract(this.dealership.getId(), leaseContract);

        } else {
            System.out.println("Invalid choice");
            scanner.nextLine();
            processPurchaseOrLeaseRequest(salesDao, leaseDao, vehicleDao);
        }
    }

    public void processGetByPriceRequest(VehicleDao vehicleDao) {
        // Prompt for price range
        System.out.print("Enter the minimum price : $");
        double min = scanner.nextDouble();
        System.out.print("Enter the maximum price : $");
        double max = scanner.nextDouble();
        scanner.nextLine(); // Consume the newline

        // Call the search method
        List<Vehicle> vehicles = vehicleDao.getVehiclesByPrice(this.dealership.getId(),min,max);
        vehicles.forEach(System.out::println);
    }

    public void processGetByMakeModelRequest(VehicleDao vehicleDao) {
        // Prompt
        System.out.print("Enter the make : ");
        String make = scanner.nextLine();
        System.out.print("Enter the model : ");
        String model = scanner.nextLine();

        // Display
        List<Vehicle> vehicles = vehicleDao.getVehiclesByMakeModel(this.dealership.getId(),make,model);
        vehicles.forEach(System.out::println);
    }

    public void processGetByYearRequest(VehicleDao vehicleDao) {
        // Prompt
        System.out.print("Enter the minimum year : ");
        int min = scanner.nextInt();
        System.out.print("Enter the maximum year : ");
        int max = scanner.nextInt();
        scanner.nextLine(); // Consume the newline

        // Display
        List<Vehicle> vehicles = vehicleDao.getVehiclesByYear(this.dealership.getId(),min,max);
        vehicles.forEach(System.out::println);
    }

    public void processGetByColorRequest(VehicleDao vehicleDao) {
        // Prompt
        System.out.print("Enter a color : ");
        String color = scanner.nextLine();

        // Display
        List<Vehicle> vehicles = vehicleDao.getVehiclesByColor(this.dealership.getId(),color);
        vehicles.forEach(System.out::println);
    }

    public void processGetByMileageRequest(VehicleDao vehicleDao) {
        // Prompt
        System.out.print("Enter minimum mileage : ");
        int min = scanner.nextInt();
        System.out.print("Enter maximum mileage : ");
        int max = scanner.nextInt();
        scanner.nextLine(); // Consume the newline

        // Display
        List<Vehicle> vehicles = vehicleDao.getVehiclesByMileage(this.dealership.getId(),min,max);
        vehicles.forEach(System.out::println);
    }

    public void processGetByVehicleType(VehicleDao vehicleDao) {
        // Prompt
        System.out.print("Enter the vehicle type : ");
        String vehicleType = scanner.nextLine();

        // Display
        List<Vehicle> vehicles = vehicleDao.getVehiclesByType(this.dealership.getId(),vehicleType);
        vehicles.forEach(System.out::println);
    }

    public void processGetAllVehiclesRequest(VehicleDao vehicleDao) {
        // Display vehicles in the inventory
        List<Vehicle> vehicles = vehicleDao.getAllVehicles(this.dealership.getId());
        vehicles.forEach(System.out::println);
    }

    public void processAddVehicleRequest(VehicleDao vehicleDao) {
        // Prompt
        System.out.println("Please enter the following information");

        System.out.print("Vin : ");
        int vin = scanner.nextInt();
        System.out.print("Year : ");
        int year = scanner.nextInt();
        scanner.nextLine(); // Consume the newline

        System.out.print("Make : ");
        String make = scanner.nextLine();
        System.out.print("Model : ");
        String model = scanner.nextLine();
        System.out.print("Vehicle Type : ");
        String vehicleType = scanner.nextLine();
        System.out.print("Color : ");
        String color = scanner.nextLine();

        System.out.print("Odometer reading : ");
        int odometer = scanner.nextInt();
        System.out.print("Price : ");
        double price = scanner.nextDouble();
        scanner.nextLine(); // Consume the newline

        // Create the new vehicle object
        Vehicle vehicle = new Vehicle(vin,year,make,model,vehicleType,color,odometer,price);

        // Add vehicle to db
        vehicleDao.addVehicle(this.dealership.getId(), vehicle);
    }

    public void processRemoveVehicleRequest(VehicleDao vehicleDao) {
        // Prompt for the vin of the vehicle to remove
        System.out.print("Enter the VIN of the vehicle you want to remove : ");
        int vin = scanner.nextInt();
        scanner.nextLine(); // Consume the newline

        vehicleDao.removeVehicle(vin);
    }
}
