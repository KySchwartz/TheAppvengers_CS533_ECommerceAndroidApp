package EcomerceApp.ShoppingApp.Adapters;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import EcomerceApp.ShoppingApp.DbHelper;
import EcomerceApp.ShoppingApp.Models.CartItem;
import EcomerceApp.ShoppingApp.R;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    private List<CartItem> cartItems;
    private DbHelper dbHelper;
    public CartAdapter(List<CartItem> cartItems, DbHelper dbHelper) {
        this.cartItems = cartItems;
        this.dbHelper = dbHelper;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cart_item, parent, false);
        return new CartViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        CartItem currentItem = cartItems.get(position);
        // Bind the data to the views
        holder.productNameTextView.setText(currentItem.getProductName());
        holder.productPriceTextView.setText("$" + String.format("%.2f", currentItem.getPrice()));
        holder.quantityTextView.setText(String.valueOf(currentItem.getQuantity()));
        holder.productImageView.setImageResource(currentItem.getImageUrl());

        // Decrease quantity button
        holder.decreaseQuantityButton.setOnClickListener(v -> {
            int currentQuantity = currentItem.getQuantity();
            if (currentQuantity > 1) {
                currentItem.setQuantity(currentQuantity - 1);
                holder.quantityTextView.setText(String.valueOf(currentItem.getQuantity()));
                // Update database
                //dbHelper.updateCartItem(currentItem); // Implement this method in DbHelper
            }
        });

        // Increase quantity button
        holder.increaseQuantityButton.setOnClickListener(v -> {
            currentItem.setQuantity(currentItem.getQuantity() + 1);
            holder.quantityTextView.setText(String.valueOf(currentItem.getQuantity()));
            // Update database
            // dbHelper.updateCartItem(currentItem); // Implement this method in DbHelper
        });

        // Remove item button
        holder.removeButton.setOnClickListener(v -> {
            // Remove from database
            // dbHelper.removeFromCart(currentItem); // Implement this method in DbHelper
            // Remove from UI
            cartItems.remove(holder.getAdapterPosition());
            notifyItemRemoved(holder.getAdapterPosition());
        });
    }

    @Override
    public int getItemCount() {
        return cartItems.size();
    }

    static class CartViewHolder extends RecyclerView.ViewHolder {
        ImageView productImageView;
        TextView productNameTextView;
        TextView productPriceTextView;
        TextView quantityTextView;
        Button decreaseQuantityButton;
        Button increaseQuantityButton;
        Button removeButton;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            productImageView = itemView.findViewById(R.id.productImageView);
            productNameTextView = itemView.findViewById(R.id.productNameTextView);
            productPriceTextView = itemView.findViewById(R.id.productPriceTextView);
            quantityTextView = itemView.findViewById(R.id.quantityTextView);
            decreaseQuantityButton = itemView.findViewById(R.id.decreaseQuantityButton);
            increaseQuantityButton = itemView.findViewById(R.id.increaseQuantityButton);
            removeButton = itemView.findViewById(R.id.removeButton);
        }
    }
}