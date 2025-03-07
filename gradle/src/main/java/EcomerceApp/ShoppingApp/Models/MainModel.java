package EcomerceApp.ShoppingApp.Models;

public class MainModel {
    String image;
    String name,description,item_quantity;
    String price;
    public MainModel(String name, String description,String price, String image)
    {
        this.image=image;
        this.name=name;
        this.price=price;
        this.description=description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
