package com.pluralsight;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class VehicleDao {
    private DataSource dataSource;

    public VehicleDao(DataSource dataSource){
        this.dataSource=dataSource;
    }

    // Get vehicles in a price range
    public List<Vehicle> getVehiclesByPrice(int dealershipId, double minPrice, double maxPrice) {
        List<Vehicle> vehicles = new ArrayList<>();
        String query = "SELECT * FROM vehicles WHERE vin IN " +
                "(SELECT vin FROM inventory WHERE dealership_id = ?) " +
                "AND price BETWEEN ? AND ? AND sold = 0";

        try(Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(query)){

            preparedStatement.setInt(1, dealershipId);
            preparedStatement.setDouble(2,minPrice);
            preparedStatement.setDouble(3,maxPrice);

            try(ResultSet resultSet = preparedStatement.executeQuery();){
                if (resultSet.next()){
                    do {

                        int vin = resultSet.getInt("vin");
                        int year = resultSet.getInt("year");
                        String make = resultSet.getString("make");
                        String model = resultSet.getString("model");
                        String vehicleType = resultSet.getString("vehicleType");
                        String color = resultSet.getString("color");
                        int odometer = resultSet.getInt("odometer");
                        double price = resultSet.getDouble("price");

                        Vehicle vehicle = new Vehicle(vin,year,make,model,vehicleType,color,odometer,price);
                        vehicles.add(vehicle);

                    } while (resultSet.next());
                } else {
                    System.out.println("No vehicles found within that range");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return vehicles;
    }


    // Get all vehicles
    public List<Vehicle> getAllVehicles(int dealershipId) {
        List<Vehicle> vehicles = new ArrayList<>();
        String query = "SELECT * FROM vehicles WHERE vin IN (SELECT vin FROM inventory WHERE dealership_id = ?) AND sold = 0";

        try(Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(query);){

            preparedStatement.setInt(1,dealershipId);

            try(ResultSet resultSet = preparedStatement.executeQuery();){
                while(resultSet.next()){
                    int vin = resultSet.getInt("vin");
                    int year = resultSet.getInt("year");
                    String make = resultSet.getString("make");
                    String model = resultSet.getString("model");
                    String vehicleType = resultSet.getString("vehicleType");
                    String color = resultSet.getString("color");
                    int odometer = resultSet.getInt("odometer");
                    double price = resultSet.getDouble("price");

                    Vehicle vehicle = new Vehicle(vin,year,make,model,vehicleType,color,odometer,price);
                    vehicles.add(vehicle);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return vehicles;
    }
}
