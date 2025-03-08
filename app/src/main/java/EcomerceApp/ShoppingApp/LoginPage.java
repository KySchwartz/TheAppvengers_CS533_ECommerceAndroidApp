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
public class LoginPage extends AppCompatActivity {
    ActivityLoginBinding binding;
    String insertemail;
    String insertpass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }

    public void doLoginNow(View view) {
        if(TextUtils.isEmpty(binding.etPassword.getText())||TextUtils.isEmpty(binding.etEmail.getText()))
        {

            Toast.makeText(this, "Missing All Fields Required", Toast.LENGTH_SHORT).show();

        }
        else {
            Cursor cr = getContentResolver().query(DetailProvider.CONTENT_URI,null, null, null, "id");

            while (cr.moveToNext()) {
                String email = cr.getString(1);
                String password = cr.getString(2);
                if (!binding.etEmail.getText().toString().equals(email) && !binding.etPassword.getText().toString().equals(password))
                {
                    Toast.makeText(this, "Invalid Credentials", Toast.LENGTH_SHORT).show();

                }
                else
                {

                    Intent intent =new Intent(this,MainActivity.class);
                    startActivity(intent);
                }

            }




        }



        }





    public void doSingUpNow(View view) {
        Intent intent=new Intent(this,SignUp.class);
        startActivity(intent);
    }


}