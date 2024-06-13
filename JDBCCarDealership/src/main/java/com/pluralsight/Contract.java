package com.pluralsight;

public abstract class Contract {
    private String date;
    private String name;
    private String email;
    private Vehicle vehicleSold;
    private double totalPrice;
    private double monthlyPayment;

    public Contract(String date, String name, String email, Vehicle vehicleSold) {
        this.date = date;
        this.name = name;
        this.email = email;
        this.vehicleSold = vehicleSold;
    }

    // Abstract methods
    public abstract double getTotalPrice();

    public abstract double getMonthlyPayment();

    // Getters and setters
    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Vehicle getVehicleSold() {
        return vehicleSold;
    }

    public void setVehicleSold(Vehicle vehicleSold) {
        this.vehicleSold = vehicleSold;
    }
}
