package EcomerceApp.ShoppingApp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import EcomerceApp.ShoppingApp.Adapters.ProductsAdapter;
import EcomerceApp.ShoppingApp.databinding.ActivityOrderDetailsBinding;

public class OrderDetailsActivity extends AppCompatActivity {
    private ActivityOrderDetailsBinding binding;
    private FirebaseFirestore firestore;
    private ProductsAdapter adapter;
    private List<Map<String, Object>> productsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOrderDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Initialize Firebase
        firestore = FirebaseFirestore.getInstance();

        // Initialize RecyclerView
        productsList = new ArrayList<>();
        adapter = new ProductsAdapter(productsList, this);
        binding.orderDetailsRecyclerView.setAdapter(adapter);
        binding.orderDetailsRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Fetch products for the selected order
        String orderId = getIntent().getStringExtra("orderId");
        fetchOrderDetails(orderId);
    }

    private void fetchOrderDetails(String orderId) {
        firestore.collection("orders").document(orderId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        List<Map<String, Object>> fetchedProducts = (List<Map<String, Object>>) documentSnapshot.get("items");
                        if (fetchedProducts != null) {
                            productsList.clear();
                            productsList.addAll(fetchedProducts);
                            adapter.notifyDataSetChanged();
                        } else {
                            Toast.makeText(this, "No products found in this order", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(this, "Order not found", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Error fetching order details: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.e("OrderDetailsActivity", "Error fetching order details", e);
                });
    }
}