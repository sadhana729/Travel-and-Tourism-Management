// ==================== models/Customer.java ====================
package models;

public class Customer {
    private String customerId;
    private String name;
    private String email;
    private String phone;
    
    public Customer(String customerId, String name, String email, String phone) {
        this.customerId = customerId;
        this.name = name;
        this.email = email;
        this.phone = phone;
    }
    
    public String getCustomerId() { return customerId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    
    @Override
    public String toString() {
        return "Customer[ID=" + customerId + ", Name=" + name + ", Email=" + email + "]";
    }
}