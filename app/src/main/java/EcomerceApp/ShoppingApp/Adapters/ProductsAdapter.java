package EcomerceApp.ShoppingApp.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Map;

import EcomerceApp.ShoppingApp.R;

public class ProductsAdapter extends RecyclerView.Adapter<ProductsAdapter.ProductViewHolder> {
    private List<Map<String, Object>> productsList;
    private Context context;

    public ProductsAdapter(List<Map<String, Object>> productsList, Context context) {
        this.productsList = productsList;
        this.context = context;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.order_item, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Map<String, Object> product = productsList.get(position);

        // Extract product details from the map
        String productName = (String) product.get("productName");
        Long quantity = (Long) product.get("quantity");
        Double price = (Double) product.get("price");
        int imageResId = ((Long) product.get("imageUrl")).intValue();

        // Bind data to the views
        holder.productNameTextView.setText(productName != null ? productName : "Unknown Product");
        holder.quantityTextView.setText("Quantity: " + (quantity != null ? quantity : 0));
        holder.priceTextView.setText("Price: $" + (price != null ? String.format("%.2f", price) : "0.00"));
        holder.productImageView.setImageResource(imageResId); // Set a default image
    }

    @Override
    public int getItemCount() {
        return productsList.size();
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder {
        TextView productNameTextView, quantityTextView, priceTextView;
        ImageView productImageView;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            productNameTextView = itemView.findViewById(R.id.productNameTextView);
            quantityTextView = itemView.findViewById(R.id.quantityTextView);
            priceTextView = itemView.findViewById(R.id.priceTextView);
            productImageView = itemView.findViewById(R.id.productImageView);
        }
    }
}