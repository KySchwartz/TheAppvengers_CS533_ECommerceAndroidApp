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
import EcomerceApp.ShoppingApp.MainActivity;
import EcomerceApp.ShoppingApp.R;

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.ViewHolder> {
    private List<Product> productList;
    private Context context;
    private OnProductClickListener productClickListener;

    public interface OnProductClickListener {
        void onProductClick(Product product);
    }

    public MainAdapter(List<Product> products, Context context, OnProductClickListener listener) {
        this.productList = products;
        this.context = context;
        this.productClickListener = listener;
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
        Product product = productList.get(position);

        holder.productName.setText(product.getName());
        holder.productPrice.setText(product.getPrice());
        holder.productDescription.setText(product.getDescription());
        holder.productImage.setImageResource(context.getResources().getIdentifier(product.getImage(), "drawable", context.getPackageName()));

        int imageResId = context.getResources().getIdentifier(
                product.getImage(), "drawable", context.getPackageName()
        );
        holder.productImage.setImageResource(imageResId);

        holder.itemView.setOnClickListener(v -> {
            // ✅ Save clicked product to Recently Viewed (only if the context is MainActivity)
            if (context instanceof MainActivity) {
                ((MainActivity) context).addToRecentlyViewed(product);
            }

            // ✅ Open DetailActivity with product details
            Intent intent = new Intent(context, DetailActivity.class);
            intent.putExtra("name", product.getName());
            intent.putExtra("price", product.getPrice());
            intent.putExtra("des", product.getDescription());
            intent.putExtra("image", product.getImage());

            if (context instanceof MainActivity) {
                intent.putExtra("type", 1);  // Normal product click (order)
            } else {
                intent.putExtra("type", 2);  // Recently viewed click
            }

            context.startActivity(intent);
        });

    }

    @Override
    public int getItemCount() {
        return productList.size();
    }
}
