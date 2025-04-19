package EcomerceApp.ShoppingApp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

import EcomerceApp.ShoppingApp.Adapters.OrdersListAdapter;
import EcomerceApp.ShoppingApp.Models.OrdersModel;
import EcomerceApp.ShoppingApp.databinding.OrderAvtivityBinding;

public class OrderActivity extends AppCompatActivity {
    private OrderAvtivityBinding binding;
    private FirebaseFirestore firestore;
    private FirebaseAuth firebaseAuth;
    private OrdersListAdapter adapter;
    private List<OrdersModel> ordersList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = OrderAvtivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Initialize Firebase instances
        firestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        // Initialize RecyclerView
        ordersList = new ArrayList<>();
        adapter = new OrdersListAdapter(ordersList, this);
        binding.orderRecyclerview.setAdapter(adapter);
        binding.orderRecyclerview.setLayoutManager(new LinearLayoutManager(this));

        // Fetch orders from Firestore
        fetchOrdersFromFirestore();
    }

    private void fetchOrdersFromFirestore() {
        String userId = firebaseAuth.getCurrentUser().getUid(); // Get the current user's UID

        firestore.collection("orders")
                .whereEqualTo("userId", userId) // Filter orders by the current user's ID
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        List<OrdersModel> fetchedOrders = queryDocumentSnapshots.toObjects(OrdersModel.class);
                        ordersList.clear();
                        ordersList.addAll(fetchedOrders);
                        adapter.notifyDataSetChanged(); // Notify the adapter of data changes
                    } else {
                        Toast.makeText(OrderActivity.this, "No orders found", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(OrderActivity.this, "Error fetching orders: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.e("OrderActivity", "Error fetching orders", e);
                });
    }
}
