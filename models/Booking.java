

// ==================== models/Booking.java ====================
package models;

import interfaces.Payable;

public class Booking implements Payable {
    private String bookingId;
    private Customer customer;
    private TravelPackage travelPackage;
    private String bookingDate;
    private String paymentStatus;
    
    public Booking(String bookingId, Customer customer, TravelPackage travelPackage, String bookingDate) {
        this.bookingId = bookingId;
        this.customer = customer;
        this.travelPackage = travelPackage;
        this.bookingDate = bookingDate;
        this.paymentStatus = "Pending";
    }
    
    @Override
    public void processPayment(double amount) {
        System.out.println("Processing payment of $" + amount + " for booking " + bookingId);
        paymentStatus = "Completed";
    }
    
    @Override
    public String getPaymentStatus() {
        return paymentStatus;
    }
    
    public String getBookingId() { return bookingId; }
    public Customer getCustomer() { return customer; }
    public TravelPackage getTravelPackage() { return travelPackage; }
    public String getBookingDate() { return bookingDate; }
    
    public void displayBookingDetails() {
        System.out.println("\n=== Booking Details ===");
        System.out.println("Booking ID: " + bookingId);
        System.out.println("Customer: " + customer.getName());
        System.out.println("Destination: " + travelPackage.getDestination());
        System.out.println("Booking Date: " + bookingDate);
        System.out.println("Payment Status: " + paymentStatus);
    }

    @Override
    public String toString() {
        return bookingId + ": " + customer.getName() + " -> " +
                travelPackage.getDestination() + " [" + paymentStatus + "]";
    }
}

