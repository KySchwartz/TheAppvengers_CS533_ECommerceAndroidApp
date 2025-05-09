package EcomerceApp.ShoppingApp;

import EcomerceApp.ShoppingApp.Models.Product;
import EcomerceApp.ShoppingApp.Adapters.MainAdapter;
import EcomerceApp.ShoppingApp.databinding.ActivityMainBinding;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.view.Menu;
import android.view.MenuItem;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    DbHelper dbHelper;
    private TextView cartBadge;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize Firebase
        FirebaseApp.initializeApp(this);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        dbHelper = new DbHelper(this); // Initialize database helper

        // Setup "Recently Viewed" Button
        Button btnRecentlyViewed = findViewById(R.id.btn_recently_viewed);
        btnRecentlyViewed.setOnClickListener(v -> {
            List<Product> recentlyViewedList = dbHelper.getRecentlyViewed();
            if (recentlyViewedList.isEmpty()) {
                Toast.makeText(MainActivity.this, "No recently viewed items", Toast.LENGTH_SHORT).show();
            } else {
                Intent intent = new Intent(MainActivity.this, RecentlyViewedActivity.class);
                startActivity(intent);
            }
        });

        // Setup "Users List" Button
        Button btnUsersList = findViewById(R.id.btn_users_list);
        btnUsersList.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, UserListActivity.class);
            startActivity(intent);
        });

        // Read JSON from assets and populate main product list
        try {
            InputStream inputStream = getAssets().open("products.json");
            String jsonString = convertStreamToString(inputStream);
            Gson gson = new Gson();
            // Use correct TypeToken from Gson library
            List<Product> productList = gson.fromJson(jsonString, new TypeToken<List<Product>>(){}.getType());

            // Set up RecyclerView for Main Product List
            MainAdapter adapter = new MainAdapter(productList, this);
            binding.recyclerView.setAdapter(adapter);
            binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));

        } catch (Exception e) {
            Toast.makeText(MainActivity.this, "Error loading JSON: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    // Helper method to read JSON from assets
    public String convertStreamToString(InputStream is) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            return sb.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        dbHelper.setOnCartChangeListener(this::updateCartBadge);
        updateCartBadge(); // Update the cart badge initially
    }

    @Override
    protected void onPause() {
        super.onPause();
        dbHelper.setOnCartChangeListener(null); // Remove the listener to avoid memory leaks
    }

    @Override
    public void onBackPressed() {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        if (fragment != null) {
            findViewById(R.id.fragment_container).setVisibility(View.GONE);
            getSupportFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        MenuItem cartItem = menu.findItem(R.id.action_cart);
        View actionView = cartItem.getActionView();

        // Get the cart_badge TextView from the action view
        cartBadge = actionView.findViewById(R.id.cart_badge);

        // Set an onClickListener for the cart icon
        actionView.setOnClickListener(view -> onOptionsItemSelected(cartItem));

        // Update the cart badge initially
        updateCartBadge();

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.orders) {
            startActivity(new Intent(MainActivity.this, OrderActivity.class));
        } else if (itemId == R.id.nav_about) {
            loadFragment(new AboutFragment()); // Load "About" fragment
        } else if (itemId == R.id.action_cart) {
            // Start the CartActivity
            startActivity(new Intent(this, CartActivity.class));
            return true;
        } else if (itemId == R.id.action_logout) {
            // Call logout method when logout is selected
            logout();
            return true;
        } else if (itemId == R.id.users_list) {
            // Start the UserListActivity
            startActivity(new Intent(this, UserListActivity.class));
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
        return true;
    }

    // Load a fragment
    private void loadFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        findViewById(R.id.fragment_container).setVisibility(View.VISIBLE);
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    // Update cart count
    private void updateCartBadge() {
        if (cartBadge != null) { // Ensure the cartBadge reference is not null
            int cartCount = dbHelper.getCartCount(); // Get the cart count from DbHelper
            if (cartCount > 0) {
                cartBadge.setVisibility(View.VISIBLE); // Show the badge
                cartBadge.setText(String.valueOf(cartCount)); // Set the count as text
            } else {
                cartBadge.setVisibility(View.GONE); // Hide the badge if the cart is empty
            }
        }
    }

    public void logout() {
        FirebaseAuth.getInstance().signOut();

        SessionManager sessionManager = new SessionManager(this);
        sessionManager.logout();

        Intent intent = new Intent(this, LoginPage.class);
        startActivity(intent);
        finish();
    }
}