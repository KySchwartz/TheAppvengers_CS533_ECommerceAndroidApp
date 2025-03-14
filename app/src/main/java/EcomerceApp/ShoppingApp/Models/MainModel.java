package EcomerceApp.ShoppingApp.Models;

public class MainModel {
    String image;
    String name,description,item_quantity;
    String price;

    public MainModel(String name, String description, String price, String image) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.image = image;
    }

        public String getName() { return name; }
        public String getDescription() { return description; }
        public String getPrice() { return price; }
        public String getImage() { return image; }
}

