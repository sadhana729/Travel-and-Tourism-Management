// ==================== services/FileHandler.java ====================
package services;

import models.Booking;
import java.io.*;
import java.util.List;

public class FileHandler {
    public static void saveBookingsToFile(List<Booking> bookings, String filename) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            for (Booking booking : bookings) {
                writer.write(booking.getBookingId() + "," + 
                           booking.getCustomer().getName() + "," +
                           booking.getTravelPackage().getDestination() + "," +
                           booking.getPaymentStatus());
                writer.newLine();
            }
            System.out.println("Bookings saved to " + filename);
        } catch (IOException e) {
            System.err.println("Error saving bookings: " + e.getMessage());
        }
    }
    
    public static void readBookingsFromFile(String filename) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            System.out.println("\n=== Reading from file ===");
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
        } catch (FileNotFoundException e) {
            System.err.println("File not found: " + filename);
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        }
    }
}