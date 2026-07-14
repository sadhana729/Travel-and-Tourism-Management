// ==================== threads/BookingProcessorThread.java ====================
package threads;

import models.Booking;

public class BookingProcessorThread extends Thread {
    private Booking booking;
    
    public BookingProcessorThread(Booking booking) {
        this.booking = booking;
    }
    
    @Override
    public void run() {
        System.out.println("\n[Thread " + Thread.currentThread().getId() + "] Processing booking: " + booking.getBookingId());
        try {
            Thread.sleep(2000);
            booking.processPayment(booking.getTravelPackage().calculatePrice());
            System.out.println("[Thread " + Thread.currentThread().getId() + "] Booking processed: " + booking.getBookingId());
        } catch (InterruptedException e) {
            System.err.println("Thread interrupted: " + e.getMessage());
        }
    }
}