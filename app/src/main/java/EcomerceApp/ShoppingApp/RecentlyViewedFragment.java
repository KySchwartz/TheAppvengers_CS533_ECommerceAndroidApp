package EcomerceApp.ShoppingApp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import EcomerceApp.ShoppingApp.Adapters.MainAdapter;
import EcomerceApp.ShoppingApp.Models.Product;

public class RecentlyViewedFragment extends Fragment {
    private List<Product> recentlyViewedProducts;

    // Correctly pass an ArrayList<Product> to the fragment
    public static RecentlyViewedFragment newInstance(ArrayList<Product> products) {
        RecentlyViewedFragment fragment = new RecentlyViewedFragment();
        Bundle args = new Bundle();
        args.putSerializable("recentlyViewed", products); // Pass as Serializable
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            // Ensure type safety when retrieving the list
            recentlyViewedProducts = (ArrayList<Product>) getArguments().getSerializable("recentlyViewed");
            if (recentlyViewedProducts == null) {
                recentlyViewedProducts = new ArrayList<>();
            }
        } else {
            recentlyViewedProducts = new ArrayList<>();
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recently_viewed, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Ensure adapter handles an empty list gracefully
        MainAdapter adapter = new MainAdapter(recentlyViewedProducts, getContext(), product -> {});
        recyclerView.setAdapter(adapter);

        return view;
    }
}
