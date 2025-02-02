package login;
public class Order {
    private String orderId;
    private String username;
    private String bikeId;
    private String bikeName;
    private double price;
    private String orderDate;
    private String status;

    public Order(String orderId, String username, String bikeId, String bikeName, double price, String orderDate, String status) {
        this.orderId = orderId;
        this.username = username;
        this.bikeId = bikeId;
        this.bikeName = bikeName;
        this.price = price;
        this.orderDate = orderDate;
        this.status = status;
    }

     Getters
    public String getOrderId() { return orderId; }
    public String getUsername() { return username; }
    public String getBikeId() { return bikeId; }
    public String getBikeName() { return bikeName; }
    public double getPrice() { return price; }
    public String getOrderDate() { return orderDate; }
    public String getStatus() { return status; }

     Setters
    public void setStatus(String status) { this.status = status; }
} 