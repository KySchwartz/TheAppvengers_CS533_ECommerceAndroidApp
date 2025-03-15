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

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.view.Menu;
import android.view.MenuItem;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    private List<Product> recentlyViewedList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Button btnRecentlyViewed = findViewById(R.id.btn_recently_viewed);
        btnRecentlyViewed.setOnClickListener(v -> {
            if (recentlyViewedList.isEmpty()) {
                Toast.makeText(MainActivity.this, "No recently viewed items", Toast.LENGTH_SHORT).show();
            } else {
                Intent intent = new Intent(MainActivity.this, RecentlyViewedActivity.class);
                intent.putParcelableArrayListExtra("recentlyViewedList", new ArrayList<>(recentlyViewedList));
                startActivity(intent);
            }
        });


        // Read JSON from assets
        try {
            InputStream inputStream = getAssets().open("products.json");
            String jsonString = convertStreamToString(inputStream);
            Gson gson = new Gson();
            List<Product> productList = gson.fromJson(jsonString, new TypeToken<List<Product>>(){}.getType());

            Log.d("MainActivity", "Product list size: " + productList.size());

            // Set up RecyclerView
            MainAdapter adapter = new MainAdapter(productList, this, this::addToRecentlyViewed);
            binding.recyclerView.setAdapter(adapter);
            binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));

        } catch (Exception e) {
            Toast.makeText(MainActivity.this, "Error loading JSON: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    // Method to add clicked products to recentlyViewedList
    public void addToRecentlyViewed(Product product) {
        if (!recentlyViewedList.contains(product)) {
            recentlyViewedList.add(product);
            Log.d("MainActivity", "Added to recently viewed: " + product.getName());
        }
        Log.d("MainActivity", "Recently Viewed List: " + recentlyViewedList.size());
    }


    // Open RecentlyViewedFragment and pass recently viewed items
    private void openRecentlyViewedFragment() {
        Log.d("MainActivity", "Opening Recently Viewed Fragment");
        Log.d("MainActivity", "Recently Viewed List Size: " + recentlyViewedList.size());

        if (recentlyViewedList.isEmpty()) {
            Toast.makeText(MainActivity.this, "No recently viewed items", Toast.LENGTH_SHORT).show();
            return;
        }
        ArrayList<Product> recentlyViewedArrayList = new ArrayList<>(recentlyViewedList);
        RecentlyViewedFragment fragment = RecentlyViewedFragment.newInstance(new ArrayList<>(recentlyViewedArrayList));
        binding.recyclerView.setVisibility(View.GONE);
        loadFragment(fragment); // ✅ Make sure this is called
    }

    private void loadFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

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
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            // If there's a fragment, remove it and restore the main list
            getSupportFragmentManager().popBackStack();
            binding.recyclerView.setVisibility(View.VISIBLE);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu); // Loads the menu.xml file
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.orders:
                // Open OrderActivity when clicking "View Orders"
                startActivity(new Intent(MainActivity.this, OrderActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


}
