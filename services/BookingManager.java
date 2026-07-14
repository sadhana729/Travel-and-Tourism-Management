// ==================== services/BookingManager.java ====================
package services;

import models.Booking;
import models.Customer;
import models.TravelPackage;
import exceptions.BookingNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class BookingManager {
    private List<Booking> bookings;
    
    public BookingManager() {
        this.bookings = new ArrayList<>();
    }
    
    // Method Overloading
    public void addBooking(Booking booking) {
        bookings.add(booking);
        System.out.println("Booking added: " + booking.getBookingId());
    }
    
    public void addBooking(String bookingId, Customer customer, TravelPackage pkg, String date) {
        Booking booking = new Booking(bookingId, customer, pkg, date);
        bookings.add(booking);
        System.out.println("Booking created and added: " + bookingId);
    }
    
    public void addBooking(Customer customer, TravelPackage pkg) {
        String bookingId = "BK" + System.currentTimeMillis();
        String date = java.time.LocalDate.now().toString();
        Booking booking = new Booking(bookingId, customer, pkg, date);
        bookings.add(booking);
        System.out.println("Auto-generated booking: " + bookingId);
    }
    
    public Booking findBooking(String bookingId) throws BookingNotFoundException {
        for (Booking booking : bookings) {
            if (booking.getBookingId().equals(bookingId)) {
                return booking;
            }
        }
        throw new BookingNotFoundException("Booking not found: " + bookingId);
    }
    
    public List<Booking> getAllBookings() {
        return new ArrayList<>(bookings);
    }
}