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

            // Set up RecyclerView
            MainAdapter adapter = new MainAdapter(productList, this, this::addToRecentlyViewed);
            binding.recyclerView.setAdapter(adapter);
            binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));

        } catch (Exception e) {
            Toast.makeText(MainActivity.this, "Error loading JSON: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    // Add clicked products to recentlyViewedList
    public void addToRecentlyViewed(Product product) {
        if (!recentlyViewedList.contains(product)) {
            recentlyViewedList.add(product);
        }
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
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);

        if (fragment != null) {
            // Hide the fragment container instead of exiting the app
            findViewById(R.id.fragment_container).setVisibility(View.GONE);
            getSupportFragmentManager().popBackStack();
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
                startActivity(new Intent(MainActivity.this, OrderActivity.class));
                break;
            case R.id.nav_about:
                loadFragment(new AboutFragment());  // Load "About" fragment
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + item.getItemId());
        }
        return super.onOptionsItemSelected(item);
    }


    // Load fragment
    private void loadFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        // Make sure the fragment container is visible
        findViewById(R.id.fragment_container).setVisibility(View.VISIBLE);

        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }



}
