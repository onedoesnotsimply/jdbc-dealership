package com.pluralsight;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class SalesDao {
    private DataSource dataSource;

    public SalesDao(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public List<SalesContract> getAllSalesContracts() {
        return null;
    }

    public void addSalesContract(int dealershipId, SalesContract salesContract){
        String query1 = "INSERT INTO sales_contracts " +
                "(dealership_id,sales_date,name,email,vin,year,make,model,vehicleType,color,odometer,price,financed) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        String query2 = "UPDATE vehicles SET sold = 1 WHERE vin = ?";

        try(Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement1 = connection.prepareStatement(query1);
            PreparedStatement preparedStatement2 = connection.prepareStatement(query2);) {

            preparedStatement1.setInt(1, dealershipId);
            preparedStatement1.setDate(2, Date.valueOf(salesContract.getDate()));
            preparedStatement1.setString(3,salesContract.getName());
            preparedStatement1.setString(4, salesContract.getEmail());
            preparedStatement1.setInt(5,salesContract.getVehicleSold().getVin());
            preparedStatement1.setInt(6,salesContract.getVehicleSold().getYear());
            preparedStatement1.setString(7,salesContract.getVehicleSold().getMake());
            preparedStatement1.setString(8, salesContract.getVehicleSold().getModel());
            preparedStatement1.setString(9,salesContract.getVehicleSold().getVehicleType());
            preparedStatement1.setString(10, salesContract.getVehicleSold().getColor());
            preparedStatement1.setInt(11, salesContract.getVehicleSold().getOdometer());
            preparedStatement1.setDouble(12, salesContract.getVehicleSold().getPrice());
            preparedStatement1.setBoolean(13, salesContract.isFinanced());

            preparedStatement2.setInt(1, salesContract.getVehicleSold().getVin());

            int rows1 = preparedStatement1.executeUpdate();
            int rows2 = preparedStatement2.executeUpdate();

            if (rows1 > 0 && rows2 > 0){
                System.out.println("Contract added successfully");
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
}
