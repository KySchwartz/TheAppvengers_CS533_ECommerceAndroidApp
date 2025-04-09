package EcomerceApp.ShoppingApp;
import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;
import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import EcomerceApp.ShoppingApp.databinding.ActivitySignupBinding;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignUp extends AppCompatActivity {
    ActivitySignupBinding binding;
    private FirebaseAuth firebaseAuth;

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignupBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }

    public void doSaveNow(View view) {

        firebaseAuth = FirebaseAuth.getInstance();

        String email = binding.etEmail.getText().toString().trim();
        String password = binding.etPassword.getText().toString().trim();
        String phone = binding.etPhone.getText().toString().trim();
        String name = binding.etUsername.getText().toString().trim();

        // Validate input fields
        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password) || TextUtils.isEmpty(phone) || TextUtils.isEmpty(name)) {
            Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create user with Firebase Authentication
        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    // Get the Firebase user
                    FirebaseUser user = firebaseAuth.getCurrentUser();
                    if (user != null) {
                        String userId = user.getUid(); // Get the FirebaseAuth user ID (UUID)

                        // Create a new user document for Firestore
                        Map<String, Object> userData = new HashMap<>();
                        userData.put("CUSTOMER_EMAIL", email);
                        userData.put("CUSTOMER_PASSWORD", password); // Avoid storing plain passwords in production
                        userData.put("CUSTOMER_NAME", name);
                        userData.put("CUSTOMER_PHONE", phone);

                        // Save the user document in Firestore with the FirebaseAuth user ID
                        db.collection("users").document(userId)
                                .set(userData)
                                .addOnSuccessListener(aVoid -> {
                                    Toast.makeText(SignUp.this, "Account created successfully", Toast.LENGTH_SHORT).show();
                                    Log.d(TAG, "User document created with ID: " + userId);

                                    // Redirect to login or main activity
                                    Intent intent = new Intent(SignUp.this, LoginPage.class);
                                    startActivity(intent);
                                    finish();
                                })
                                .addOnFailureListener(e -> {
                                    Toast.makeText(SignUp.this, "Error saving user data: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                    Log.w(TAG, "Error adding document", e);
                                });
                    }
                } else {
                    // Handle FirebaseAuth errors
                    Toast.makeText(SignUp.this, "Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    Log.e(TAG, "FirebaseAuth error: ", task.getException());
                }
            });
    }

public void doBackNow(View view) {
    Intent intent=new Intent(this,LoginPage.class);
    startActivity(intent);
    }
}