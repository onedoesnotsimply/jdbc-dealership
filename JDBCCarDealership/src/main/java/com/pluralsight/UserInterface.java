package com.pluralsight;

import java.time.LocalDate;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class UserInterface {
    static Scanner scanner = new Scanner(System.in);
    private Dealership dealership;

    public UserInterface(){
    }

    public void display() {
        init();
        while(true) {
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
            System.out.println("11) Admin");
            System.out.println("0) Exit");

            try { // Enforce input type
                int choice = scanner.nextInt();
                scanner.nextLine();

                switch (choice){
                    case 1:
                        processGetByPriceRequest();
                        break;
                    case 2:
                        processGetByMakeModelRequest();
                        break;
                    case 3:
                        processGetByYearRequest();
                        break;
                    case 4:
                        processGetByColorRequest();
                        break;
                    case 5:
                        processGetByMileageRequest();
                        break;
                    case 6:
                        processGetByVehicleType();
                        break;
                    case 7:
                        processGetAllVehiclesRequest();
                        break;
                    case 8:
                        processAddVehicleRequest();
                        break;
                    case 9:
                        processRemoveVehicleRequest();
                        break;
                    case 10:
                        processPurchaseOrLeaseRequest();
                        break;
                    case 11:
                        processAdmin();
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
        AdminUserInterface admin = new AdminUserInterface();
        System.out.println("Enter password : ");
        int password = scanner.nextInt();
        if (!admin.checkPassword(password)){
            System.out.println("Incorrect password");
            display();
        } else{
            admin.adminDisplay();
        }
    }

    public void processPurchaseOrLeaseRequest() {
        // Prompt for whether this is a purchase or a lease
        System.out.println("1) Purchase");
        System.out.println("2) Lease");
        int choice = scanner.nextInt(); // Get the choice first
        scanner.nextLine();

        // Prompt for general contract data before using the choice
        System.out.print("Enter the current date : ");
        String date=scanner.nextLine();
        System.out.print("Enter your full name : ");
        String name = scanner.nextLine();
        System.out.print("Enter your email address : ");
        String email = scanner.nextLine();
        System.out.print("Enter the VIN of the vehicle : ");
        int vin = scanner.nextInt();
        scanner.nextLine();
        Vehicle inputVehicle = null;

        // Loop through the dealership inventory to find the correct vehicle
        for (Vehicle vehicle : dealership.getAllVehicles()){
            if (vehicle.getVin()==vin){
                 inputVehicle=vehicle;
            }
        }

        // Make sure that the vehicle exists
        if (inputVehicle==null) {
            // If it doesn't print an error message and send the user back to the homescreen
            System.out.println("Invalid VIN\nVehicle does not exist");
            display();
        }

        // Get the vehicles age for lease condition
        int age = LocalDate.now().getYear()-inputVehicle.getYear();

        // You can't lease a vehicle if it's more than 3 years old
        if ((age > 3) && (choice == 2)) {
            System.out.println("Vehicle too old for lease");
            display();
        }

        if (choice == 1){ // Choice was between 1) Purchase
            // Prompt for SalesContract data
            System.out.print("Enter the sales tax percentage as a whole number : ");
            double salesTax = scanner.nextDouble();
            System.out.print("Enter the contract recording fee : ");
            double recordingFee = scanner.nextDouble();
            System.out.print("Enter the processing fee : ");
            double processingFee = scanner.nextDouble();
            scanner.nextLine();
            System.out.print("Would you like to finance? (Y/N) : ");
            String finance = scanner.nextLine();

            // Create a SalesContract with a default finance input of false
            SalesContract salesContract = new SalesContract(date,name,email,inputVehicle,salesTax,recordingFee,processingFee,false);

            // Check the finance input
            if (finance.equalsIgnoreCase("y")){ // If they did want to finance
                salesContract.setFinanced(true); // Set the finance option to true
            }

            // Write the SalesContract to the csv
            ContractDataManager.saveContract(salesContract);
            System.out.println("Contract saved");

            // Remove the vehicle from the inventory
            dealership.removeVehicle(inputVehicle);
            System.out.println("Vehicle removed from inventory");

            // Save dealership changes
            DealershipFileManager.saveDealership(dealership);

        } else if (choice == 2) { // 2) Lease
            // Prompt for Lease data
            System.out.print("Enter the expected ending value percentage as a whole number: ");
            double expectedEndingVal = scanner.nextDouble();
            System.out.print("Enter the lease fee percentage as a whole number : ");
            double leaseFee = scanner.nextDouble();

            // Instantiate a LeaseContract
            LeaseContract leaseContract = new LeaseContract(date,name,email,inputVehicle,expectedEndingVal,leaseFee);

            // Write the LeaseContract to the csv
            ContractDataManager.saveContract(leaseContract);
            System.out.println("Contract saved");

            // Remove the vehicle from the inventory and save the dealership changes
            dealership.removeVehicle(inputVehicle);
            System.out.println("Vehicle removed from inventory");
            DealershipFileManager.saveDealership(dealership);


        } else {
            System.out.println("Invalid choice");
            scanner.nextLine();
            processPurchaseOrLeaseRequest();
        }
    }

    public void processGetByPriceRequest() {
        // Prompt for price range
        System.out.print("Enter the minimum price : $");
        double min = scanner.nextDouble();
        System.out.print("Enter the maximum price : $");
        double max = scanner.nextDouble();
        scanner.nextLine(); // Consume the newline

        // Call the search method
        displayVehicles(dealership.getVehiclesByPrice(min, max));
    }

    public void processGetByMakeModelRequest() {
        // Prompt
        System.out.print("Enter the make : ");
        String make = scanner.nextLine();
        System.out.print("Enter the model : ");
        String model = scanner.nextLine();

        // Display
        displayVehicles(dealership.getVehiclesByMakeModel(make,model));
    }

    public void processGetByYearRequest() {
        // Prompt
        System.out.print("Enter the minimum year : ");
        int min = scanner.nextInt();
        System.out.print("Enter the maximum year : ");
        int max = scanner.nextInt();
        scanner.nextLine(); // Consume the newline

        // Display
        displayVehicles(dealership.getVehiclesByYear(min,max));
    }

    public void processGetByColorRequest() {
        // Prompt
        System.out.print("Enter a color : ");
        String color = scanner.nextLine();

        // Display
        displayVehicles(dealership.getVehiclesByColor(color));
    }

    public void processGetByMileageRequest() {
        // Prompt
        System.out.print("Enter minimum mileage : ");
        int min = scanner.nextInt();
        System.out.print("Enter maximum mileage : ");
        int max = scanner.nextInt();
        scanner.nextLine(); // Consume the newline

        // Display
        displayVehicles(dealership.getVehiclesByMileage(min,max));
    }

    public void processGetByVehicleType() {
        // Prompt
        System.out.print("Enter the vehicle type : ");
        String vehicleType = scanner.nextLine();

        // Display
        displayVehicles(dealership.getVehiclesByType(vehicleType));
    }

    public void processGetAllVehiclesRequest() {
        // Display vehicles in the inventory
        displayVehicles(dealership.getAllVehicles());
    }

    public void processAddVehicleRequest() {
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

        // Add the vehicle to the dealership
        dealership.addVehicle(vehicle);

        // Print out a confirmation message
        System.out.println("Vehicle added successfully");

        // Save the dealership changes
        DealershipFileManager.saveDealership(dealership);
    }

    public void processRemoveVehicleRequest() {
        // Prompt for the vin of the vehicle to remove
        System.out.print("Enter the VIN of the vehicle you want to remove : ");
        int vin = scanner.nextInt();
        scanner.nextLine(); // Consume the newline

        // Loop through the inventory for the vehicle with the input VIN number
        Vehicle toRemove = null; // Initialize Vehicle
        for (Vehicle vehicle : dealership.getAllVehicles()){
            if (vehicle.getVin() == vin){ // If the vehicle exists
                // Change the value of toRemove to the vehicle
                toRemove = vehicle;
            }
        }
        if (toRemove == null) { // If the vehicle wasn't in the inventory
            System.out.println("Invalid input\nPlease enter a valid VIN"); // Print an error message
        } else {
            // Remove the vehicle
            dealership.removeVehicle(toRemove);
            System.out.println("Vehicle removed successfully"); // Print out a success message

            // Save the dealership changes
            DealershipFileManager.saveDealership(dealership);
        }
    }

    // Private methods
    private void init() {
        this.dealership = DealershipFileManager.getDealership();
    }

    private void displayVehicles(List<Vehicle> vehicles) {
        for (Vehicle vehicle : vehicles) {
            System.out.println(vehicle);
        }
    }
}
