package EcomerceApp.ShoppingApp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;
import java.util.UUID;

import EcomerceApp.ShoppingApp.Adapters.CartAdapter;
import EcomerceApp.ShoppingApp.Models.CartItem;
import EcomerceApp.ShoppingApp.databinding.ActivityCartBinding;
import com.google.firebase.auth.FirebaseAuth;

public class CartActivity extends AppCompatActivity {
    ActivityCartBinding binding;
    DbHelper dbHelper;
    CartAdapter adapter;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCartBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        // Initialize DbHelper
        dbHelper = new DbHelper(this);
        // Set up RecyclerView
        binding.cartRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        // Load cart items from SQLite
        loadCartItems();
        // Initialize Firestore and Firebase Authentication
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        // Setup Checkout button
        binding.checkoutButton.setOnClickListener(view -> {
            // Get the cart items from SQLite
            List<CartItem> cartItems = dbHelper.getCart();
            if(cartItems.isEmpty()){
                Toast.makeText(CartActivity.this, "Your Cart is empty", Toast.LENGTH_SHORT).show();
            } else {
                // Get User Data
                String userId = firebaseAuth.getCurrentUser().getUid();
                // Get Order Date
                long orderDate = System.currentTimeMillis();
                // Get total price
                double totalPrice = 0;
                // Calculate total price
                for (CartItem item : cartItems) {
                    totalPrice += item.getPrice() * item.getQuantity();
                }
                // Generate a unique orderId
                String orderId = UUID.randomUUID().toString();
                //Add Order to database
                dbHelper.addOrder(orderId, userId, cartItems, totalPrice, orderDate, aVoid -> {
                    // Order added successfully
                    //Empty Cart
                    dbHelper.emptyCart();
                    Log.d("CartActivity", "Order added successfully!");
                    Toast.makeText(CartActivity.this, "Order placed successfully!", Toast.LENGTH_SHORT).show();
                    // Go to OrderActivity
                    Intent intent = new Intent(CartActivity.this, OrderActivity.class);
                    startActivity(intent);
                }, e -> {
                    // Error adding order
                    Log.w("CartActivity", "Error adding order.", e);
                    Toast.makeText(CartActivity.this, "Error placing order.", Toast.LENGTH_SHORT).show();
                });
            }
        });
    }

    // Helper method to load cart items from SQLite
    private void loadCartItems() {
        List<CartItem> cartItems = dbHelper.getCart();
        // Update the total price
        double totalPrice = 0;
        for (CartItem item : cartItems) {
            totalPrice += item.getPrice() * item.getQuantity();
        }
        binding.totalPrice.setText("Total: $" + String.format("%.2f", totalPrice));
        // Update the UI
        adapter = new CartAdapter(cartItems, dbHelper);
        binding.cartRecyclerView.setAdapter(adapter);
    }
}
