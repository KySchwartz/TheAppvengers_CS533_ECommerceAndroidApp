package EcomerceApp.ShoppingApp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import EcomerceApp.ShoppingApp.Adapters.MainAdapter;
import EcomerceApp.ShoppingApp.Models.MainModel;
import EcomerceApp.ShoppingApp.databinding.ActivityMainBinding;
public class MainActivity extends AppCompatActivity {
   ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityMainBinding.inflate(getLayoutInflater());
//        setContentView(R.layout.activity_main);
        setContentView(binding.getRoot());
        ArrayList<MainModel> list =new ArrayList<>();
        list.add(new MainModel(R.drawable.dress,"Stylo Dress","5","A cheery summer fusion look in this sunflower-inspired dress!..."));
        list.add(new MainModel(R.drawable.ring,"Silver Ring","12","Experience vibrant colour palettes and  silhouettes this season in our trendy Fusion collection..."));
        list.add(new MainModel(R.drawable.locket,"Locket ","12","make a bright statement in this cherry lawn fusion top..."));
        list.add(new MainModel(R.drawable.locket,"Turkish Locket","10","Small Simple flower charm Pendant in gold..."));
        list.add(new MainModel(R.drawable.pink,"Pink Dress","20","Golden moving wheel pendant..."));
        MainAdapter adapter =new MainAdapter(list,this);
        binding.recyclerView.setAdapter(adapter);
        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        binding.recyclerView.setLayoutManager(layoutManager);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.orders:
            startActivity(new Intent(MainActivity.this,OrderAvtivity.class));
            break;
            default:
                throw new IllegalStateException("Unexpected value: " + item.getItemId());
        }
        return super.onOptionsItemSelected(item);
    }
}