package EcomerceApp.ShoppingApp;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import java.util.ArrayList;
import EcomerceApp.ShoppingApp.Adapters.OrdersAdapter;
import EcomerceApp.ShoppingApp.Models.OrdersModel;
import EcomerceApp.ShoppingApp.databinding.OrderAvtivityBinding;
public class OrderAvtivity extends AppCompatActivity {
     OrderAvtivityBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=OrderAvtivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        DbHelper helper = new DbHelper(this);
        ArrayList<OrdersModel> list = helper.getOrders();
        OrdersAdapter adapter=new OrdersAdapter(list,this);
        binding.orderRecyclerview.setAdapter(adapter);
        LinearLayoutManager LayoutManager=new LinearLayoutManager(this);
        binding.orderRecyclerview.setLayoutManager(LayoutManager);

    }

}