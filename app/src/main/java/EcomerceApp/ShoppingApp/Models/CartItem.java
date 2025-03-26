package EcomerceApp.ShoppingApp.Models;

public class CartItem {
    private String productId;
    private int quantity;
    private double price;
    private String productName;
    private int imageUrl;
    // ... other fields ...

    public CartItem() {
        // Default constructor required for Firestore
    }

    public CartItem(String productId, int quantity, double price, String productName,int imageUrl) {
        this.productId = productId;
        this.quantity = quantity;
        this.price = price;
        this.productName = productName;
        this.imageUrl = imageUrl;
        // ... initialize other fields ...
    }
    //... getters and setters for each field ...
    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getImageUrl() {
        return imageUrl;
    }
    public void setImageUrl(int imageUrl) {
        this.imageUrl = imageUrl;
    }


}
