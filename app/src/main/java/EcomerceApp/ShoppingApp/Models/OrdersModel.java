package EcomerceApp.ShoppingApp.Models;
public class OrdersModel {
    int orderImage;
    String soldItemName,orderPrice,orderNumber ,customername;
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
}

