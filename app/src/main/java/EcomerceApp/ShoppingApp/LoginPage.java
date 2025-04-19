package EcomerceApp.ShoppingApp;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import EcomerceApp.ShoppingApp.Login.DetailProvider;
import EcomerceApp.ShoppingApp.databinding.ActivityLoginBinding;
import com.google.firebase.auth.FirebaseAuth;

public class LoginPage extends AppCompatActivity {
    ActivityLoginBinding binding;
    private FirebaseAuth firebaseAuth;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        firebaseAuth = FirebaseAuth.getInstance();
        sessionManager = new SessionManager(this);

        // Check if user is already logged in
        if (sessionManager.isLoggedIn()) {
            // User is already logged in, redirect to MainActivity
            startActivity(new Intent(LoginPage.this, MainActivity.class));
            finish(); // Close LoginPage so user can't go back to it
        }
    }

    public void doLoginNow(View view) {
        String email = binding.etEmail.getText().toString().trim();
        String password = binding.etPassword.getText().toString().trim();

        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            Toast.makeText(LoginPage.this, "All fields are required", Toast.LENGTH_SHORT).show();
            return;
        }

        // Show loading indicator
        binding.progressBar.setVisibility(View.VISIBLE);
        binding.buttonSignin.setEnabled(false);

        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    // Hide loading indicator
                    binding.progressBar.setVisibility(View.GONE);
                    binding.buttonSignin.setEnabled(true);

                    if (task.isSuccessful()) {
                        // Save login session
                        sessionManager.setLoggedIn(true);
                        sessionManager.saveUserEmail(email);

                        Toast.makeText(LoginPage.this, "Login successful", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(LoginPage.this, MainActivity.class));
                        finish();
                    } else {
                        Toast.makeText(LoginPage.this, "Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void doSingUpNow(View view) {
        Intent intent = new Intent(this, SignUp.class);
        startActivity(intent);
    }
}