
// ==================== TravelTourismManagementSystem.java (MAIN) ====================
import models.*;
import services.*;
import threads.*;
import exceptions.*;
import interfaces.*;
import java.util.List;

public class TravelTourismManagementSystem {
    public static void main(String[] args) {
        System.out.println("========================================");
        System.out.println("TRAVEL & TOURISM MANAGEMENT SYSTEM");
        System.out.println("========================================\n");
        
        TravelAgency agency = new TravelAgency();
        BookingManager bookingManager = new BookingManager();
        
        // Creating customers
        Customer c1 = new Customer("C001", "John Doe", "john@email.com", "123-456-7890");
        Customer c2 = new Customer("C002", "Jane Smith", "jane@email.com", "098-765-4321");
        
        agency.addCustomer(c1);
        agency.addCustomer(c2);
        
        // Creating packages
        TravelPackage pkg1 = new DomesticPackage("PKG001", "Goa", 500, "Goa", true);
        TravelPackage pkg2 = new InternationalPackage("PKG002", "Paris", 2000, "France", true, 2);
        TravelPackage pkg3 = new DomesticPackage("PKG003", "Kerala", 600, "Kerala", false);
        
        agency.addPackage(pkg1);
        agency.addPackage(pkg2);
        agency.addPackage(pkg3);
        
        // Displaying package details
        pkg1.displayPackageDetails();
        System.out.println();
        pkg2.displayPackageDetails();
        
        // Creating bookings
        System.out.println("\n=== Creating Bookings ===");
        Booking b1 = new Booking("BK001", c1, pkg1, "2025-11-01");
        bookingManager.addBooking(b1);
        
        bookingManager.addBooking("BK002", c2, pkg2, "2025-11-15");
        bookingManager.addBooking(c1, pkg3);
        
        // Exception Handling
        System.out.println("\n=== Testing Exception Handling ===");
        try {
            Booking foundBooking = bookingManager.findBooking("BK001");
            foundBooking.displayBookingDetails();
            
            Booking notFound = bookingManager.findBooking("BK999");
        } catch (BookingNotFoundException e) {
            System.err.println("Error: " + e.getMessage());
        }
        
        // Interface implementation
        System.out.println("\n=== Testing Bookable Interface ===");
        if (pkg1 instanceof Bookable) {
            Bookable bookable = (Bookable) pkg1;
            bookable.book();
            System.out.println("Price: $" + bookable.calculatePrice());
        }
        
        // Collections Framework
        agency.displayStatistics();
        
        // File Handling
        System.out.println("\n=== File Handling ===");
        FileHandler.saveBookingsToFile(bookingManager.getAllBookings(), "bookings.txt");
        FileHandler.readBookingsFromFile("bookings.txt");
        
        // Threads
        System.out.println("\n=== Multi-threading Demo ===");
        List<Booking> allBookings = bookingManager.getAllBookings();
        
        if (allBookings.size() > 0) {
            BookingProcessorThread thread1 = new BookingProcessorThread(allBookings.get(0));
            thread1.start();
        }
        
        Thread thread2 = new Thread(new NotificationThread(c1, "Your booking is confirmed!"));
        thread2.start();
        
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        System.out.println("\n========================================");
        System.out.println("System demonstration completed!");
        System.out.println("========================================");
    }
}

// ==================== COMPILATION & EXECUTION INSTRUCTIONS ====================
/*
OPTION 1: SEPARATE FILES (Recommended for large projects)
---------------------------------------------------------
1. Create the directory structure as shown at the top
2. Save each class in its respective file
3. Compile from the project root:
   javac TravelTourismManagementSystem.java
   (This will automatically compile all referenced classes)
4. Run:
   java TravelTourismManagementSystem

OPTION 2: SINGLE FILE (Easier for testing/submission)
------------------------------------------------------
1. Remove all "package" declarations
2. Save everything in one file: TravelTourismManagementSystem.java
3. Compile: javac TravelTourismManagementSystem.java
4. Run: java TravelTourismManagementSystem

OPTION 3: USE THE CODE AS-IS (with packages)
---------------------------------------------
If you want to use packages:
1. Create folders: interfaces/, exceptions/, models/, services/, threads/
2. Save each class in its corresponding folder
3. From the root directory, compile:
   javac -d . TravelTourismManagementSystem.java
4. Run:
   java TravelTourismManagementSystem
*/