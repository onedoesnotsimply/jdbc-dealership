package com.pluralsight;

import java.io.*;
import java.util.ArrayList;

public class ContractDataManager {

    public static void saveContract(Contract contract) {
        try{
            // Create our BufferedWriter
            BufferedWriter bfw = new BufferedWriter(new FileWriter("contracts.csv",true));

            // Initialize file string
            String inputString = String.format("|%s|%s|%s|%d|%d|%s|%s|%s|%s|%d|%.2f",
                    contract.getDate(),contract.getName(),contract.getEmail(), // Basic contract data
                    contract.getVehicleSold().getVin(),contract.getVehicleSold().getYear(), // Vehicle data
                    contract.getVehicleSold().getMake(),contract.getVehicleSold().getModel(), // Vehicle data
                    contract.getVehicleSold().getVehicleType(),contract.getVehicleSold().getColor(), // Vehicle data
                    contract.getVehicleSold().getOdometer(),contract.getVehicleSold().getPrice()); // Vehicle data);

            // Check what type of contract the user is saving
            if (contract instanceof SalesContract){

                String wasFinanced;
                if (((SalesContract) contract).isFinanced()){
                    wasFinanced = "YES";
                } else {
                    wasFinanced = "NO";
                }
                String salesInputString = String.format("|%.2f|%.2f|%.2f|%.2f|%s|%.2f",
                        ((SalesContract) contract).getSalesTaxAmount(),((SalesContract) contract).getRecordingFee(), // Sales contract data
                        ((SalesContract) contract).getProcessingFee(), contract.getTotalPrice(), wasFinanced, contract.getMonthlyPayment());// Sales contract data


                bfw.write("SALE"+inputString+salesInputString+"\n");

            } else if (contract instanceof LeaseContract) { // A LeaseContract
                String leaseInputString = String.format("|%.2f|%.2f|%.2f|%.2f",
                        ((LeaseContract) contract).getExpectedEndingVal(),((LeaseContract) contract).getLeaseFee(),
                        contract.getTotalPrice(),contract.getMonthlyPayment());

                bfw.write("LEASE"+inputString+leaseInputString+"\n");

            }
            bfw.close();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static ArrayList<String> getContracts(){
        try {
            // Initialize the contracts ArrayList
            ArrayList<String> contracts = new ArrayList<>();

            BufferedReader bfr = new BufferedReader(new FileReader("contracts.csv"));

            String input;
            while((input = bfr.readLine()) != null) {
                contracts.add(input);
            }
            return contracts;

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
