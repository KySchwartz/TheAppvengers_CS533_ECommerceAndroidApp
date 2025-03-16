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
    private RecyclerView recyclerView;
    private RecentlyViewedAdapter adapter;
    private List<Product> recentlyViewedList;
    private DbHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recently_viewed);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        dbHelper = new DbHelper(this);
        recentlyViewedList = dbHelper.getRecentlyViewed(); // Get items from SQLite

        adapter = new RecentlyViewedAdapter(recentlyViewedList, this);
        recyclerView.setAdapter(adapter);
    }
}
