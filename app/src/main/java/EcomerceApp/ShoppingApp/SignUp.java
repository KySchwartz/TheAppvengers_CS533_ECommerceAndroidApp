package EcomerceApp.ShoppingApp;
import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import EcomerceApp.ShoppingApp.Login.DetailProvider;
import EcomerceApp.ShoppingApp.databinding.ActivitySignupBinding;
public class SignUp extends AppCompatActivity {
    ActivitySignupBinding binding;
    ContentValues values = new ContentValues();

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignupBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }

    public void doSaveNow(View view) {
        String Email = binding.etEmail.getText().toString();
        String Password = binding.etPassword.getText().toString();
        String Phone = binding.etPhone.getText().toString();
        String Name = binding.etUsername.getText().toString();
        values.put("CUSTOMER_EMAIL", Email);
        values.put("CUSTOMER_PASSWORD", Password);
        values.put("CUSTOMER_NAME", Phone);
        values.put("CUSTOMER_PHONE", Name);

        // Create a new user with a first and last name
        Map<String, Object> user = new HashMap<>();
        user.put("CUSTOMER_EMAIL", Email);
        user.put("CUSTOMER_PASSWORD", Password);
        user.put("CUSTOMER_NAME", Phone);
        user.put("CUSTOMER_PHONE", Name);

        // Add a new document with a generated ID
        db.collection("users")
                .add(user)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                    }
                });

        if(TextUtils.isEmpty(Email) || TextUtils.isEmpty(Password) || TextUtils.isEmpty(Phone) || TextUtils.isEmpty(Name))
        {
            Toast.makeText(this, "Missing All Fields Required", Toast.LENGTH_SHORT).show();
        }
        else
        {
            Uri uri = getContentResolver().insert(DetailProvider.CONTENT_URI, values);
        }


        }

    public void doBackNow(View view) {
        Intent intent=new Intent(this,LoginPage.class);
        startActivity(intent);
    }
}