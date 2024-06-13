package com.pluralsight;

import java.io.*;

public class DealershipFileManager {

    public static Dealership getDealership(){
        // Reads information from a csv file and creates a dealership object
        try {
            // Create the buffered reader
            BufferedReader bfr = new BufferedReader(new FileReader("inventory.csv"));

            // Get the first line of the file, which holds the dealership data
            String dealershipInfo = bfr.readLine();
            String[] dealershipTokens = dealershipInfo.split("\\|");
            // Use the info to create the dealership object
            Dealership dealership = new Dealership(dealershipTokens[0], dealershipTokens[1], dealershipTokens[2]);

            // Populate the dealerships inventory with the vehicles within the csv file
            String input;
            while ((input = bfr.readLine()) != null) {
                // Split each vehicle by the | and create Vehicles with the data
                String[] vehicleTokens = input.split("\\|");

                // Parse the values and create vehicles
                Vehicle vehicle = new Vehicle(Integer.parseInt(vehicleTokens[0]),Integer.parseInt(vehicleTokens[1]),vehicleTokens[2],
                        vehicleTokens[3],vehicleTokens[4],vehicleTokens[5],Integer.parseInt(vehicleTokens[6]),Double.parseDouble(vehicleTokens[7]));

                // Add the Vehicle to the Dealership's inventory
                dealership.addVehicle(vehicle);
            }
            bfr.close();
            return dealership;

        } catch (FileNotFoundException e) {
            System.out.println("File not found");
            System.exit(0);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public static void saveDealership(Dealership dealership) {
        try {
            // Create buffered writer
            BufferedWriter bfw = new BufferedWriter(new FileWriter("inventory.csv"));

            // Write the dealership information to the first line
            String dealershipInfo = String.format("%s|%s|%s\n",dealership.getName(),dealership.getAddress(),dealership.getPhone());
            bfw.write(dealershipInfo);

            // Write the inventory in the following lines
            for (Vehicle vehicle : dealership.getAllVehicles()) {
                String formattedVehicle = String.format("%d|%d|%s|%s|%s|%s|%d|%.2f\n",
                        vehicle.getVin(),vehicle.getYear(),vehicle.getMake(),vehicle.getModel(),vehicle.getVehicleType(),
                        vehicle.getColor(),vehicle.getOdometer(),vehicle.getPrice());
                bfw.write(formattedVehicle);
            }
            bfw.close();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
