package EcomerceApp.ShoppingApp;

import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;
import EcomerceApp.ShoppingApp.Adapters.RecentlyViewedAdapter;
import EcomerceApp.ShoppingApp.Models.Product;
import EcomerceApp.ShoppingApp.databinding.ActivityRecentlyViewedBinding;
//Recently Viewed activity
public class RecentlyViewedActivity extends AppCompatActivity {
    ActivityRecentlyViewedBinding binding;
    //Create list to hold recently viewed items: a list of products
    private List<Product> recentlyViewedList = new ArrayList<>();
    private RecentlyViewedAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRecentlyViewedBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Get the recently viewed list
        recentlyViewedList = getIntent().getParcelableArrayListExtra("recentlyViewedList");

        if (recentlyViewedList == null || recentlyViewedList.isEmpty()) {
            Toast.makeText(this, "No recently viewed items", Toast.LENGTH_SHORT).show();
            return;
        }

        // RecyclerView
        adapter = new RecentlyViewedAdapter(recentlyViewedList, this);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerView.setAdapter(adapter);
    }
}
