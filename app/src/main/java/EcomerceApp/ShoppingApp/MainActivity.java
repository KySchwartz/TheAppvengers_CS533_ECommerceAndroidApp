package EcomerceApp.ShoppingApp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import android.view.Menu;
import android.view.MenuItem;

import java.io.BufferedReader;  // For reading from InputStream

import android.content.res.AssetManager;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.util.ArrayList;
import java.util.List;
import EcomerceApp.ShoppingApp.Models.Product;
import EcomerceApp.ShoppingApp.Adapters.MainAdapter;
import EcomerceApp.ShoppingApp.Models.MainModel;
import EcomerceApp.ShoppingApp.databinding.ActivityMainBinding;
public class MainActivity extends AppCompatActivity {
   ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        // Step 7: Load the fragment
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentContainer, new RecentlyViewedFragment()) // Make sure ID matches your XML
                .commit();

        // Read JSON from assets
        try {
            AssetManager assetManager = getAssets();
            InputStream inputStream = assetManager.open("products.json");
            InputStreamReader reader = new InputStreamReader(inputStream);

            String jsonString = convertStreamToString(inputStream);  // Assume this is the JSON file content
            Gson gson = new Gson();
            List<Product> productList = gson.fromJson(jsonString, new TypeToken<List<Product>>(){}.getType());


            ArrayList<MainModel> list = new ArrayList<>();
            for (Product product : productList) {
                // Get the drawable resource ID based on the image name from JSON
                String imageName = product.getImage();
                int imageResId = getResources().getIdentifier(imageName, "drawable", getPackageName());

                MainModel model = new MainModel(
                        product.getName(),
                        product.getDescription(),
                        product.getPrice(),
                        imageName
                );
                list.add(model);
            }

            // Set up RecyclerView
            MainAdapter adapter = new MainAdapter(list, MainActivity.this);
            binding.recyclerView.setAdapter(adapter);
            binding.recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));

        } catch (Exception e) {
            Toast.makeText(MainActivity.this, "Error loading JSON: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.orders:
            startActivity(new Intent(MainActivity.this, OrderActivity.class));
            break;
            default:
                throw new IllegalStateException("Unexpected value: " + item.getItemId());
        }
        return super.onOptionsItemSelected(item);
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
}
