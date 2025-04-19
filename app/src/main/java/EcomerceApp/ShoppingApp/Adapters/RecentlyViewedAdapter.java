package EcomerceApp.ShoppingApp.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import EcomerceApp.ShoppingApp.Models.Product;
import EcomerceApp.ShoppingApp.DetailActivity;
import EcomerceApp.ShoppingApp.R;

public class RecentlyViewedAdapter extends RecyclerView.Adapter<RecentlyViewedAdapter.ViewHolder> {
    private List<Product> recentlyViewedList;
    private Context context;

    public RecentlyViewedAdapter(List<Product> recentlyViewedList, Context context) {
        this.recentlyViewedList = recentlyViewedList;
        this.context = context;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView productName, productPrice, productDescription;
        ImageView productImage;

        public ViewHolder(View itemView) {
            super(itemView);
            productName = itemView.findViewById(R.id.name);
            productPrice = itemView.findViewById(R.id.mainOrderPrice);
            productDescription = itemView.findViewById(R.id.description);
            productImage = itemView.findViewById(R.id.imageView);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sample_mainfood, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Product product = recentlyViewedList.get(position);

        holder.productName.setText(product.getName());
        holder.productPrice.setText(product.getPrice());
        holder.productDescription.setText(product.getDescription());
        holder.productImage.setImageResource(context.getResources().getIdentifier(
                product.getImage(), "drawable", context.getPackageName()
        ));

        // Open DetailActivity when item is clicked
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, DetailActivity.class);
            intent.putExtra("name", product.getName());
            intent.putExtra("price", product.getPrice());
            intent.putExtra("des", product.getDescription());
            intent.putExtra("image", product.getImage());
            intent.putExtra("type", 2); // Indicate that it's a recently viewed product

            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return recentlyViewedList.size();
    }
}
