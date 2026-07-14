
// ==================== models/InternationalPackage.java ====================
package models;

import interfaces.Bookable;

public class InternationalPackage extends TravelPackage implements Bookable {
    private String country;
    private boolean visaRequired;
    private int flightClass;
    
    public InternationalPackage(String packageId, String destination, double basePrice,
                               String country, boolean visaRequired, int flightClass) {
        super(packageId, destination, basePrice);
        this.country = country;
        this.visaRequired = visaRequired;
        this.flightClass = flightClass;
    }
    
    @Override
    public void displayPackageDetails() {
        System.out.println("=== International Package ===");
        System.out.println("ID: " + packageId);
        System.out.println("Destination: " + destination);
        System.out.println("Country: " + country);
        System.out.println("Visa Required: " + visaRequired);
        System.out.println("Flight Class: " + getFlightClassName());
        System.out.println("Price: $" + calculatePrice());
    }
    
    @Override
    public void book() {
        System.out.println("International package booked for " + destination);
    }
    
    @Override
    public void cancel() {
        System.out.println("International package cancelled for " + destination);
    }
    
    @Override
    public double calculatePrice() {
        double price = basePrice;
        if (visaRequired) price += 200;
        price += flightClass * 500;
        return price;
    }
    
    private String getFlightClassName() {
        switch(flightClass) {
            case 1: return "Economy";
            case 2: return "Business";
            case 3: return "First Class";
            default: return "Economy";
        }
    }

    @Override
    public String toString() {
        return destination + " (Domestic)";
    }
}