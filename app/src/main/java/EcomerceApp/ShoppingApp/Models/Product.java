package EcomerceApp.ShoppingApp.Models;
import android.os.Parcel;
import android.os.Parcelable;

public class Product implements Parcelable {
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

    // Parcelable implementation
    protected Product(Parcel in) {
        name = in.readString();
        description = in.readString();
        price = in.readString();
        image = in.readString();
    }

    public static final Creator<Product> CREATOR = new Creator<Product>() {
        @Override
        public Product createFromParcel(Parcel in) {
            return new Product(in);
        }

        @Override
        public Product[] newArray(int size) {
            return new Product[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(description);
        dest.writeString(price);
        dest.writeString(image);
    }

    // Getters
    public String getName() { return name; }
    public String getDescription() { return description; }
    public String getPrice() { return price; }
    public String getImage() { return image; }
}
