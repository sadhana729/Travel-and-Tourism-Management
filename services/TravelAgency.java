// ==================== services/TravelAgency.java ====================
package services;

import models.Customer;
import models.TravelPackage;
import models.Booking;
import java.util.*;

public class TravelAgency {
    private Map<String, Customer> customers;
    private Set<String> destinations;
    private List<TravelPackage> packages;
    private Queue<Booking> pendingBookings;
    
    public TravelAgency() {
        customers = new HashMap<>();
        destinations = new HashSet<>();
        packages = new ArrayList<>();
        pendingBookings = new LinkedList<>();
    }
    
    public void addCustomer(Customer customer) {
        customers.put(customer.getCustomerId(), customer);
    }
    
    public void addDestination(String destination) {
        destinations.add(destination);
    }
    
    public void addPackage(TravelPackage pkg) {
        packages.add(pkg);
        destinations.add(pkg.getDestination());
    }
    
    public void enqueuePendingBooking(Booking booking) {
        pendingBookings.offer(booking);
    }
    
    public Booking processPendingBooking() {
        return pendingBookings.poll();
    }
    
    public void displayStatistics() {
        System.out.println("\n=== Travel Agency Statistics ===");
        System.out.println("Total Customers: " + customers.size());
        System.out.println("Available Destinations: " + destinations.size());
        System.out.println("Total Packages: " + packages.size());
        System.out.println("Pending Bookings: " + pendingBookings.size());
        System.out.println("\nDestinations: " + destinations);
    }

    public Collection<Customer> getCustomers() {
        return customers.values();
    }

    public List<TravelPackage> getPackages() {
        return new ArrayList<>(packages); // Return a copy
    }
}
