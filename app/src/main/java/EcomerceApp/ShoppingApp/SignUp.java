package EcomerceApp.ShoppingApp;
import androidx.appcompat.app.AppCompatActivity;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;
import EcomerceApp.ShoppingApp.Login.DetailProvider;
import EcomerceApp.ShoppingApp.databinding.ActivitySignupBinding;
public class SignUp extends AppCompatActivity {
    ActivitySignupBinding binding;
    ContentValues values = new ContentValues();
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