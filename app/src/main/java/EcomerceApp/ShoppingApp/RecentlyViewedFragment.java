package EcomerceApp.ShoppingApp;

import android.content.Context;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import EcomerceApp.ShoppingApp.Adapters.RecentlyViewedAdapter;
import EcomerceApp.ShoppingApp.Models.Product;


public class RecentlyViewedFragment extends Fragment {
    private RecyclerView recyclerView;
    private RecentlyViewedAdapter adapter;
    private List<Product> recentlyViewedList;

    public RecentlyViewedFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recently_viewed, container, false);
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        recentlyViewedList = loadRecentlyViewedProducts();
        adapter = new RecentlyViewedAdapter(recentlyViewedList, getContext());
        recyclerView.setAdapter(adapter);

        return view;
    }

    private List<Product> loadRecentlyViewedProducts() {
        List<Product> productList = new ArrayList<>();
        try {
            InputStream is = getActivity().getAssets().open("products.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            String jsonString = new String(buffer, "UTF-8");

            JSONArray jsonArray = new JSONArray(jsonString);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String name = jsonObject.getString("name");
                String description = jsonObject.getString("description");
                String price = "$" + jsonObject.getString("price"); // Add $ symbol
                String image = jsonObject.getString("image"); // Image name only
                productList.add(new Product(name, description, price, image));
            }
        } catch (Exception e) {
            Log.e("JSON_ERROR", "Error loading products.json", e);
        }
        return productList;
    }

}
