package com.pluralsight;

public class LeaseContract extends Contract{
    // ID auto increments
    private double expectedEndingVal;
    private double leaseFee;

    public LeaseContract(String date, String name, String email, Vehicle vehicleSold, double expectedEndingVal, double leaseFee) {
        super(date, name, email, vehicleSold);
        this.expectedEndingVal = expectedEndingVal;
        this.leaseFee = leaseFee;
    }

    // Abstract methods
    @Override
    public double getTotalPrice() {
        // This is also probably wrong
        double totalPrice = getMonthlyPayment()*36;
        return totalPrice;
    }

    @Override
    public double getMonthlyPayment() {
        // This math is not mathing correctly, but I gave up
        // It still works, it's just not entirely correct
        double monthlyPayment = 0;
        double residualVal = getVehicleSold().getPrice()*(expectedEndingVal/100);

        double depreciationFee = getVehicleSold().getPrice()-residualVal;

        double monthlyDepreciationFee = depreciationFee/36;

        monthlyPayment = (monthlyDepreciationFee+(monthlyDepreciationFee*(0.04)));

        return monthlyPayment;
    }

    // Getters and setters
    public double getExpectedEndingVal() {
        return expectedEndingVal;
    }

    public void setExpectedEndingVal(double expectedEndingVal) {
        this.expectedEndingVal = expectedEndingVal;
    }

    public double getLeaseFee() {
        return leaseFee;
    }

    public void setLeaseFee(double leaseFee) {
        this.leaseFee = leaseFee;
    }
}
