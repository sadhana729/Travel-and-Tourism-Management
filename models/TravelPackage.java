
// ==================== models/TravelPackage.java ====================
package models;

public abstract class TravelPackage {
    protected String packageId;
    protected String destination;
    protected double basePrice;
    
    public TravelPackage(String packageId, String destination, double basePrice) {
        this.packageId = packageId;
        this.destination = destination;
        this.basePrice = basePrice;
    }
    
    public abstract void displayPackageDetails();
    public abstract double calculatePrice();
    
    public String getDestination() {
        return destination;
    }
    
    public String getPackageId() {
        return packageId;
    }
    
    public double getBasePrice() {
        return basePrice;
    }
}