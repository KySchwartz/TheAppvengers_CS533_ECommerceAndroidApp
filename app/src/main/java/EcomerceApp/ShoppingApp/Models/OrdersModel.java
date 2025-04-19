package EcomerceApp.ShoppingApp.Models;

import java.util.List;
import java.util.Map;

public class OrdersModel {
    private String orderId;
    int orderImage;
    String soldItemName,orderPrice,orderNumber ,customername;

    // New fields for Firebase integration
    private long orderDate; // Timestamp for the order date
    private double totalAmount; // Total amount for the order
    private List<Map<String, Object>> products; // List of products in the order

    public OrdersModel()
    {
//        int orderImage,String soldItemName,String price,String orderNumber
//        this.orderImage=orderImage;
//        this.soldItemName=soldItemName;
//        this.orderPrice=price;
//        this.orderNumber=orderNumber;
    }

    public int getOrderImage() {
        return orderImage;
    }
    public void setOrderImage(int orderImage) {
        this.orderImage = orderImage;
    }

    public String getSoldItemName() {
        return soldItemName;
    }

    public void setSoldItemName(String soldItemName) {
        this.soldItemName = soldItemName;
    }

    public String getPrice() {
        return orderPrice;
    }

    public void setPrice(String price) {
        this.orderPrice = price;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public void setCustomername(String customername)
    {
      this.customername=customername;
    }

    public String getCustomername() {
        return customername;
    }

    // New getters and setters for Firebase integration
    public long getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(long orderDate) {
        this.orderDate = orderDate;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public List<Map<String, Object>> getProducts() {
        return products;
    }

    public void setProducts(List<Map<String, Object>> products) {
        this.products = products;
    }
}


