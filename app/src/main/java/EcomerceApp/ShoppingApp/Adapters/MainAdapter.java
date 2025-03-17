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
import EcomerceApp.ShoppingApp.DbHelper;
import EcomerceApp.ShoppingApp.R;

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.ViewHolder> {
    private List<Product> productList;
    private Context context;

    public MainAdapter(List<Product> products, Context context) {
        this.productList = products;
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
        Product product = productList.get(position);

        holder.productName.setText(product.getName());
        holder.productPrice.setText(product.getPrice());
        holder.productDescription.setText(product.getDescription());

        int imageResId = context.getResources().getIdentifier(
                product.getImage(), "drawable", context.getPackageName()
        );
        holder.productImage.setImageResource(imageResId);

        holder.itemView.setOnClickListener(v -> {
            // Save to recently viewed in database
            DbHelper dbHelper = new DbHelper(context);
            dbHelper.insertRecentlyViewed(product);

            // Open DetailActivity with product info
            Intent intent = new Intent(context, DetailActivity.class);
            intent.putExtra("name", product.getName());
            intent.putExtra("price", product.getPrice());
            intent.putExtra("des", product.getDescription());
            intent.putExtra("image", product.getImage());
            intent.putExtra("type", 1);

            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }
}
