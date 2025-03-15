package EcomerceApp.ShoppingApp;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import EcomerceApp.ShoppingApp.Adapters.RecentlyViewedAdapter;
import EcomerceApp.ShoppingApp.Models.Product;
import EcomerceApp.ShoppingApp.R;

public class RecentlyViewedFragment extends Fragment {

    private RecyclerView recyclerView;
    private RecentlyViewedAdapter adapter;
    private List<Product> recentlyViewedList;

    public RecentlyViewedFragment() {
        // Required empty public constructor
    }

    public static RecentlyViewedFragment newInstance(ArrayList<Product> recentlyViewedList) {
        RecentlyViewedFragment fragment = new RecentlyViewedFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList("recentlyViewedList", recentlyViewedList);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recently_viewed, container, false);

        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Retrieve the recently viewed list from arguments
        if (getArguments() != null) {
            recentlyViewedList = getArguments().getParcelableArrayList("recentlyViewedList");
            Log.d("RecentlyViewedFragment", "Received Recently Viewed List: " + recentlyViewedList.size());

            if (recentlyViewedList != null && !recentlyViewedList.isEmpty()) {
                Log.d("RecentlyViewedFragment", "Displaying " + recentlyViewedList.size() + " recently viewed items.");
                adapter = new RecentlyViewedAdapter(recentlyViewedList, getContext());
                recyclerView.setAdapter(adapter);
            } else {
                Log.d("RecentlyViewedFragment", "No recently viewed items to display.");
                Toast.makeText(getContext(), "No recently viewed items", Toast.LENGTH_SHORT).show();
            }
        }

        return view;
    }
}
