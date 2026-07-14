// ==================== models/DomesticPackage.java ====================
package models;

import interfaces.Bookable;

public class DomesticPackage extends TravelPackage implements Bookable {
    private String state;
    private boolean includesGuide;
    
    public DomesticPackage(String packageId, String destination, double basePrice, 
                          String state, boolean includesGuide) {
        super(packageId, destination, basePrice);
        this.state = state;
        this.includesGuide = includesGuide;
    }
    
    @Override
    public void displayPackageDetails() {
        System.out.println("=== Domestic Package ===");
        System.out.println("ID: " + packageId);
        System.out.println("Destination: " + destination);
        System.out.println("State: " + state);
        System.out.println("Guide Included: " + includesGuide);
        System.out.println("Price: $" + calculatePrice());
    }
    
    @Override
    public void book() {
        System.out.println("Domestic package booked for " + destination);
    }
    
    @Override
    public void cancel() {
        System.out.println("Domestic package cancelled for " + destination);
    }
    
    @Override
    public double calculatePrice() {
        return includesGuide ? basePrice + 50 : basePrice;
    }
}