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

    // Setters (Optional)
    public void setName(String name) { this.name = name; }
    public void setDescription(String description) { this.description = description; }
    public void setPrice(String price) { this.price = price; }
    public void setImage(String image) { this.image = image; }
}
