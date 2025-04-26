package EcomerceApp.ShoppingApp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    // Splash screen display time in milliseconds
    private static final long SPLASH_DISPLAY_TIME = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // Hide the action bar if it exists
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        // Use Handler to delay loading the main activity
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            // Determine which activity to start
            Intent intent;

            // Check if user is logged in
            SessionManager sessionManager = new SessionManager(SplashActivity.this);
            if (sessionManager.isLoggedIn()) {
                // User is logged in, go to MainActivity
                intent = new Intent(SplashActivity.this, MainActivity.class);
            } else {
                // User is not logged in, go to LoginPage
                intent = new Intent(SplashActivity.this, LoginPage.class);
            }

            startActivity(intent);
            finish(); // Close the splash activity so it's not in the back stack
        }, SPLASH_DISPLAY_TIME);
    }
}