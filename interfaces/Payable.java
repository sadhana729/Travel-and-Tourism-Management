// ==================== interfaces/Payable.java ====================
package interfaces;

public interface Payable {
    void processPayment(double amount);
    String getPaymentStatus();
}