package EcomerceApp.ShoppingApp.Models;

public class Product {
    private String name;
    private String description;
    private String price;
    private String image;

    // Constructor
    public Product(String name, String description, String price, String image) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.image = image;
    }

    // Getters
    public String getName() { return name; }
    public String getDescription() { return description; }
    public String getPrice() { return price; }
    public String getImage() { return image; }
}
