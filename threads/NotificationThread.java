// ==================== threads/NotificationThread.java ====================
package threads;

import models.Customer;

public class NotificationThread implements Runnable {
    private Customer customer;
    private String message;
    
    public NotificationThread(Customer customer, String message) {
        this.customer = customer;
        this.message = message;
    }
    
    @Override
    public void run() {
        System.out.println("\n[Notification Thread] Sending to " + customer.getName() + ": " + message);
        try {
            Thread.sleep(1000);
            System.out.println("[Notification Thread] Email sent to " + customer.getEmail());
        } catch (InterruptedException e) {
            System.err.println("Notification thread interrupted: " + e.getMessage());
        }
    }
}