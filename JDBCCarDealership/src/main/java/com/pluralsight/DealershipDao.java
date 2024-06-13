package com.pluralsight;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DealershipDao {
    private DataSource dataSource;

    public DealershipDao(DataSource dataSource){
        this.dataSource=dataSource;
    }

    public List<Dealership> getAllDealerships() {
        List<Dealership> dealerships = new ArrayList<>();
        String query = "SELECT * FROM dealerships";

        try(Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();){

            while (resultSet.next()){
                int id = resultSet.getInt("dealership_id");
                String name = resultSet.getString("name");
                String address = resultSet.getString("address");
                String phone = resultSet.getString("phone");

                Dealership dealership = new Dealership(id, name,address,phone);
                dealerships.add(dealership);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return dealerships;
    }
}
