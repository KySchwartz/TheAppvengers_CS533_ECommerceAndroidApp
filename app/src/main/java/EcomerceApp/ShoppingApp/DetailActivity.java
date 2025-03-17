package EcomerceApp.ShoppingApp;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.firestore.FirebaseFirestore;

import EcomerceApp.ShoppingApp.databinding.ActivityDetailBinding;

public class DetailActivity extends AppCompatActivity {
    ActivityDetailBinding binded;
    int count = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binded = ActivityDetailBinding.inflate(getLayoutInflater());
        setContentView(binded.getRoot());

        final FirebaseFirestore firestore = FirebaseFirestore.getInstance();

        final DbHelper helper = new DbHelper(this);
        int type = getIntent().getIntExtra("type", 1); // Default type = 1 (Order)

        String imageName = getIntent().getStringExtra("image");
        final int image = getResources().getIdentifier(imageName, "drawable", getPackageName());
        String price = getIntent().getStringExtra("price");
        final String name = getIntent().getStringExtra("name");
        final String description = getIntent().getStringExtra("des");

        binded.detailimage.setImageResource(image);
        binded.detailprice.setText(price);
        binded.orderLabel.setText(name);
        binded.orderText.setText(description);

        if (type == 1) {
            // **This is an order, keep order UI elements visible**
            binded.placeOrder.setVisibility(View.VISIBLE);
            binded.plus.setVisibility(View.VISIBLE);
            binded.minus.setVisibility(View.VISIBLE);
            binded.quantity.setVisibility(View.VISIBLE);

            binded.placeOrder.setOnClickListener(view -> {
                if (binded.nameBox.getText().toString().isEmpty() || binded.phoneBox.getText().toString().isEmpty()) {
                    Toast.makeText(DetailActivity.this, "Missing Required Fields", Toast.LENGTH_SHORT).show();
                } else {
                    boolean insertData = helper.insertOrder(
                            binded.nameBox.getText().toString(),
                            binded.phoneBox.getText().toString(),
                            image,
                            Integer.parseInt(binded.detailprice.getText().toString()),
                            description,
                            name,
                            Integer.parseInt(binded.quantity.getText().toString())
                    );

                    // Add order to Firebase
                     boolean addData = helper.addOrder(
                            binded.nameBox.getText().toString(),
                            binded.phoneBox.getText().toString(),
                            image,
                            Integer.parseInt(binded.detailprice.getText().toString()),
                            description,
                            name,
                            Integer.parseInt(binded.quantity.getText().toString())
                    );

                    if (insertData) {
                        Toast.makeText(DetailActivity.this, "Order Placed!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(DetailActivity.this, "Order Failed!", Toast.LENGTH_SHORT).show();
                    }
                }
            });

            // **Increase Quantity**
            binded.plus.setOnClickListener(view -> {
                count++;
                binded.quantity.setText(String.valueOf(count));
                binded.detailprice.setText(String.valueOf(Integer.parseInt(binded.detailprice.getText().toString()) + Integer.parseInt(price)));
            });

            // **Decrease Quantity**
            binded.minus.setOnClickListener(view -> {
                if (count > 1) {
                    count--;
                    binded.quantity.setText(String.valueOf(count));
                    binded.detailprice.setText(String.valueOf(Integer.parseInt(binded.detailprice.getText().toString()) - Integer.parseInt(price)));
                }
            });

        } else if (type == 2) {
            // **This is a Recently Viewed product, hide order UI elements**
            binded.placeOrder.setVisibility(View.GONE);
            binded.plus.setVisibility(View.GONE);
            binded.minus.setVisibility(View.GONE);
            binded.quantity.setVisibility(View.GONE);
            binded.nameBox.setVisibility(View.GONE);
            binded.phoneBox.setVisibility(View.GONE);
        }
    }
}
