package EcomerceApp.ShoppingApp;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import EcomerceApp.ShoppingApp.databinding.ActivityDetailBinding;
public class DetailActivity extends AppCompatActivity {
    ActivityDetailBinding binded;
    int count=1;
    int localvarprice;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binded = ActivityDetailBinding.inflate(getLayoutInflater());
        setContentView(binded.getRoot());
            final DbHelper helper = new DbHelper(this);
        if (getIntent().getIntExtra("type", 0) == 1) {
            final int image = getIntent().getIntExtra("image", 0);
            int price = Integer.parseInt(getIntent().getStringExtra("price"));
            final String name = getIntent().getStringExtra("name");
            final String description = getIntent().getStringExtra("des");
            int item_quantity=Integer.parseInt(binded.quantity.getText().toString());
            binded.detailimage.setImageResource(image);
            binded.detailprice.setText(String.valueOf(price));
            binded.orderLabel.setText(name);
            binded.orderText.setText(description);
            //database insertion
            binded.placeOrder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    if (binded.nameBox.getText().toString().isEmpty() || binded.phoneBox.getText().toString().isEmpty()) {
                        Toast.makeText(DetailActivity.this, "Missing All Filed Required", Toast.LENGTH_SHORT).show();
                    } else {
                        boolean insertdata = helper.insertOrder(binded.nameBox.getText().toString(), binded.phoneBox.getText().toString(), image,Integer.parseInt(binded.detailprice.getText().toString()), description, name, Integer.parseInt(binded.quantity.getText().toString())
                        );
                        if (insertdata) {
                            Toast.makeText(DetailActivity.this, "Data Inserted", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(DetailActivity.this, "Oops! Something wrong ##Data Not Inserted", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            });


            //increase or decrease quantity buttons
            binded.plus.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                   count=count+1;
                   binded.quantity.setText(String.valueOf(count));
                   binded.detailprice.setText(String.valueOf(Integer.parseInt(binded.detailprice.getText().toString())+price));
                    Toast.makeText(DetailActivity.this, "Quantity Increased "+binded.detailprice.getText(), Toast.LENGTH_SHORT).show();
                }
            });
            //decrease quantity
            binded.minus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if(!binded.quantity.getText().equals("1"))
                    {
                        count=count-1;
                        binded.quantity.setText(String.valueOf(count));
                        binded.detailprice.setText(String.valueOf(Integer.parseInt(binded.detailprice.getText().toString())-price));
                        Toast.makeText(DetailActivity.this, "Quantity Decreased "+binded.detailprice.getText(), Toast.LENGTH_SHORT).show();

                    }
                }
            });

        }
        else
        {

            int id=getIntent().getIntExtra("Id",0);
            Cursor cursor=helper.getOrderById(id);
            final String Price=String.valueOf(cursor.getInt(3));
            localvarprice=Integer.parseInt(Price);
            final int image=cursor.getInt(4);
            final String  description=cursor.getString(5);
            final String name=cursor.getString(6);
            Toast.makeText(this, cursor.getString(0), Toast.LENGTH_SHORT).show();
            binded.detailimage.setImageResource(image);
            binded.detailprice.setText(Price);
            binded.orderLabel.setText(name);
            binded.orderText.setText(description);
            int quantity=Integer.parseInt(binded.quantity.getText().toString());
            binded.nameBox.setText(cursor.getString(1));
            binded.phoneBox.setText(cursor.getString(2));
            binded.placeOrder.setText("Update Now");
            binded.placeOrder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                 boolean updated=helper.UpdateOrder(
                            binded.nameBox.getText().toString(), binded.phoneBox.getText().toString(),image,Integer.parseInt(binded.detailprice.getText().toString()),description, name, quantity,id);
                 if(updated)
                 {
                     Toast.makeText(DetailActivity.this, "Record Updated", Toast.LENGTH_SHORT).show();
                 }
                 else
                 {
                     Toast.makeText(DetailActivity.this, "Oops! Not Updated", Toast.LENGTH_SHORT).show();
                 }
                }
            });

//increase or decrease quantity buttons
            binded.plus.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    count=count+1;
                    binded.quantity.setText(String.valueOf(count));
                    binded.detailprice.setText(String.valueOf(Integer.parseInt(binded.detailprice.getText().toString())+localvarprice));
                    Toast.makeText(DetailActivity.this, "Quantity Increased "+binded.detailprice.getText(), Toast.LENGTH_SHORT).show();
                }
            });
            //decrease quantity
            binded.minus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if(!binded.quantity.getText().equals("1"))
                    {
                        count=count-1;
                        binded.quantity.setText(String.valueOf(count));
                        binded.detailprice.setText(String.valueOf(Integer.parseInt(binded.detailprice.getText().toString())-localvarprice));
                        Toast.makeText(DetailActivity.this, "Quantity Decreased "+binded.detailprice.getText(), Toast.LENGTH_SHORT).show();

                    }
                }
            });
        }
        }
    }
