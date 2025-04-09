package EcomerceApp.ShoppingApp;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import java.util.List;
import java.util.UUID;

import EcomerceApp.ShoppingApp.Models.CartItem;
import EcomerceApp.ShoppingApp.databinding.ActivityDetailBinding;

public class DetailActivity extends AppCompatActivity {
    ActivityDetailBinding binded;
    int count = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binded = ActivityDetailBinding.inflate(getLayoutInflater());
        setContentView(binded.getRoot());

        final DbHelper helper = new DbHelper(this);
        int type = getIntent().getIntExtra("type", 1); // Default type = 1 (Order)

        String imageName = getIntent().getStringExtra("image");
        final int image = getResources().getIdentifier(imageName, "drawable", getPackageName());
        String price = getIntent().getStringExtra("price");
        final String name = getIntent().getStringExtra("name");
        final String description = getIntent().getStringExtra("des");
        final String productId = getIntent().getStringExtra("id"); // Add id from extra

        binded.detailimage.setImageResource(image);
        binded.detailprice.setText(price);
        binded.orderLabel.setText(name);
        binded.orderText.setText(description);

        if (type == 1) {
            // **This is an order, keep order UI elements visible**
            binded.placeOrder.setVisibility(View.VISIBLE);
            binded.plus.setVisibility(View.VISIBLE);
            binded.minus.setVisibility(View.VISIBLE);
            binded.quantity.setVisibility(View.VISIBLE);
            binded.addToCart.setVisibility(View.VISIBLE);

            binded.placeOrder.setOnClickListener(view -> {
                Log.d("DetailActivity", "Order Now Button Clicked");
                // Get product details
                String productName = name;
                double productPrice = Double.parseDouble(price); // Convert String to double
                int productQuantity = Integer.parseInt(binded.quantity.getText().toString());
                String productDescription = description;
                // Calculate total price
                double totalPrice = productPrice * productQuantity;
                // Generate a unique orderId
                String orderId = UUID.randomUUID().toString();
                // Get the current user ID (assuming FirebaseAuth is used)
                String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                // Get the current timestamp for the order date
                long orderDate = System.currentTimeMillis();
                // Create a single CartItem for the current product
                CartItem cartItem = new CartItem(productId, productQuantity, productPrice, productName, image);
                // Add the order to the database
                helper.addOrder(orderId, userId, List.of(cartItem), totalPrice, orderDate, aVoid -> {
                    // Order added successfully
                    Log.d("DetailActivity", "Order placed successfully!");
                    Toast.makeText(DetailActivity.this, "Order placed successfully!", Toast.LENGTH_SHORT).show();
                    // Navigate to the OrderActivity
                    Intent intent = new Intent(DetailActivity.this, OrderActivity.class);
                    startActivity(intent);
                }, e -> {
                    // Error adding order
                    Log.w("DetailActivity", "Error placing order.", e);
                    Toast.makeText(DetailActivity.this, "Error placing order.", Toast.LENGTH_SHORT).show();
                });
            });

            // **Add to Cart**
            // Add Click Listener to Add to Cart Button
            binded.addToCart.setOnClickListener(view -> {
                // Get product details (you may need to adjust these based on how you are getting your data)
                //String productId = intent.getStringExtra("productId"); // Assuming you are passing the ID from mainActivity
                String productName = name;
                double productPrice = Double.parseDouble(price); // Convert String to double
                int productQuantity = 1;

                Log.d("DetailActivity", "Button Clicked");

                // Create a CartItem
                CartItem cartItem = new CartItem(productId, productQuantity, productPrice, productName, image);
                // Add the item to the cart
                boolean success = helper.addToCart(cartItem);
                // Provide feedback to the user
                if (success) {
                    Toast.makeText(DetailActivity.this, "Added to Cart", Toast.LENGTH_SHORT).show();
                    // You might want to update the cart count in MainActivity here
                } else {
                    Toast.makeText(DetailActivity.this, "Failed to add to Cart", Toast.LENGTH_SHORT).show();
                }
            });

            // **Increase Quantity**
            binded.plus.setOnClickListener(view -> {
                count++;
                binded.quantity.setText(String.valueOf(count));
                binded.detailprice.setText(String.valueOf(Integer.parseInt(binded.detailprice.getText().toString()) + Integer.parseInt(price)));
            });

            // **Decrease Quantity**
            binded.minus.setOnClickListener(view -> {
                if (count > 1) {
                    count--;
                    binded.quantity.setText(String.valueOf(count));
                    binded.detailprice.setText(String.valueOf(Integer.parseInt(binded.detailprice.getText().toString()) - Integer.parseInt(price)));
                }
            });

        } else if (type == 2) {
            // **This is a Recently Viewed product, hide order UI elements**
            binded.placeOrder.setVisibility(View.GONE);
            binded.plus.setVisibility(View.GONE);
            binded.minus.setVisibility(View.GONE);
            binded.quantity.setVisibility(View.GONE);
            binded.addToCart.setVisibility(View.GONE);

        }
    }
}