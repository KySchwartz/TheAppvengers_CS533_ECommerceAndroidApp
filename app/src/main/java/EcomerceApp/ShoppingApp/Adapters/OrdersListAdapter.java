package EcomerceApp.ShoppingApp.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import EcomerceApp.ShoppingApp.Models.OrdersModel;
import EcomerceApp.ShoppingApp.OrderDetailsActivity;
import EcomerceApp.ShoppingApp.R;

public class OrdersListAdapter extends RecyclerView.Adapter<OrdersListAdapter.OrderViewHolder> {
    private List<OrdersModel> ordersList;
    private Context context;

    public OrdersListAdapter(List<OrdersModel> ordersList, Context context) {
        this.ordersList = ordersList;
        this.context = context;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.order_list_item, parent, false);
        return new OrderViewHolder(view);
    }

    // Displays the list of orders
    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        OrdersModel order = ordersList.get(position);

        // Format the order date
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String formattedDate = sdf.format(new Date(order.getOrderDate()));

        holder.orderDateTextView.setText("Order Date: " + formattedDate);
        holder.orderIdTextView.setText("Order ID: " + order.getOrderId());
        holder.totalAmountTextView.setText("Total Amount: $" + String.format("%.2f", order.getTotalAmount()));

        // Set click listener to navigate to OrderDetailsActivity
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, OrderDetailsActivity.class);
            intent.putExtra("orderId", order.getOrderId());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return ordersList.size();
    }

    // View to display order list
    public static class OrderViewHolder extends RecyclerView.ViewHolder {
        TextView orderDateTextView, orderIdTextView, totalAmountTextView;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            orderDateTextView = itemView.findViewById(R.id.orderDateTextView);
            orderIdTextView = itemView.findViewById(R.id.orderIdTextView);
            totalAmountTextView = itemView.findViewById(R.id.totalAmountTextView);
        }
    }
}