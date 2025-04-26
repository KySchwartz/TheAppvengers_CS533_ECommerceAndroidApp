package EcomerceApp.ShoppingApp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import EcomerceApp.ShoppingApp.Adapters.UserAdapter;
import EcomerceApp.ShoppingApp.Models.User;
import EcomerceApp.ShoppingApp.databinding.ActivityUserListBinding;

public class UserListActivity extends AppCompatActivity {

    private static final String TAG = "UserListActivity";
    private ActivityUserListBinding binding;
    private FirebaseFirestore db;
    private UserAdapter userAdapter;
    private List<User> userList;
    private ProgressBar progressBar;
    private TextView tvNoUsers;
    private Button btnAddTestUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Ensure Firebase is initialized
        try {
            FirebaseApp.initializeApp(this);
            Log.d(TAG, "Firebase initialized successfully");
        } catch (Exception e) {
            Log.e(TAG, "Error initializing Firebase", e);
        }

        binding = ActivityUserListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Initialize Firestore
        db = FirebaseFirestore.getInstance();

        // Initialize views
        progressBar = binding.progressBar;
        tvNoUsers = binding.tvNoUsers;
        btnAddTestUser = binding.btnAddTestUser;

        // Set up the Add Test User button
        btnAddTestUser.setOnClickListener(v -> addTestUser());

        // Initialize RecyclerView
        userList = new ArrayList<>();
        userAdapter = new UserAdapter(userList, this);
        binding.recyclerViewUsers.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerViewUsers.setAdapter(userAdapter);

        // Set title
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("User List");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        // Log Firebase instance
        if (db != null) {
            Log.d(TAG, "Firestore instance created successfully");
        } else {
            Log.e(TAG, "Failed to create Firestore instance");
        }

        // Load users
        loadUsers();
    }

    private void loadUsers() {
        progressBar.setVisibility(View.VISIBLE);
        tvNoUsers.setVisibility(View.GONE);

        Log.d(TAG, "Attempting to fetch users from Firestore...");

        // Now try to query the users collection
        db.collection("users")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    progressBar.setVisibility(View.GONE);
                    Log.d(TAG, "Firestore query succeeded. Documents count: " + queryDocumentSnapshots.size());

                    if (queryDocumentSnapshots.isEmpty()) {
                        Log.d(TAG, "No documents found in 'users' collection");
                    }

                    userList.clear();

                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        Log.d(TAG, "Document ID: " + document.getId());
                        Log.d(TAG, "Document data: " + document.getData().toString());

                        String userId = document.getId();
                        String name = document.getString("CUSTOMER_NAME");
                        String email = document.getString("CUSTOMER_EMAIL");
                        String phone = document.getString("CUSTOMER_PHONE");

                        Log.d(TAG, "Parsed user data - Name: " + name + ", Email: " + email + ", Phone: " + phone);

                        User user = new User(userId, name, email, phone);
                        userList.add(user);
                    }

                    if (userList.isEmpty()) {
                        tvNoUsers.setVisibility(View.VISIBLE);
                        Log.d(TAG, "No users found to display");
                    } else {
                        tvNoUsers.setVisibility(View.GONE);
                        Log.d(TAG, "Found " + userList.size() + " users to display");
                    }

                    userAdapter.updateUserList(userList);
                })
                .addOnFailureListener(e -> {
                    progressBar.setVisibility(View.GONE);
                    tvNoUsers.setVisibility(View.VISIBLE);
                    Log.e(TAG, "Error getting users", e);
                    Toast.makeText(UserListActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void addTestUser() {
        progressBar.setVisibility(View.VISIBLE);

        Map<String, Object> user = new HashMap<>();
        user.put("CUSTOMER_NAME", "Test User");
        user.put("CUSTOMER_EMAIL", "test@example.com");
        user.put("CUSTOMER_PHONE", "1234567890");

        Log.d(TAG, "Attempting to add test user to Firestore...");

        db.collection("users")
                .add(user)
                .addOnSuccessListener(documentReference -> {
                    progressBar.setVisibility(View.GONE);
                    Log.d(TAG, "Test user added with ID: " + documentReference.getId());
                    Toast.makeText(UserListActivity.this, "Test user added successfully", Toast.LENGTH_SHORT).show();
                    loadUsers(); // Reload the list
                })
                .addOnFailureListener(e -> {
                    progressBar.setVisibility(View.GONE);
                    Log.e(TAG, "Error adding test user", e);
                    Toast.makeText(UserListActivity.this, "Error adding test user: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}