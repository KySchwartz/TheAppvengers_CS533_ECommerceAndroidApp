package EcomerceApp.ShoppingApp.Adapters;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

import EcomerceApp.ShoppingApp.DbHelper;
import EcomerceApp.ShoppingApp.DetailActivity;
import EcomerceApp.ShoppingApp.MainActivity;
import EcomerceApp.ShoppingApp.Models.OrdersModel;
import EcomerceApp.ShoppingApp.OrderAvtivity;
import EcomerceApp.ShoppingApp.R;
public class OrdersAdapter extends RecyclerView.Adapter<OrdersAdapter.viewHolder> {
    ArrayList<OrdersModel> list;
    Context context;
    public OrdersAdapter(ArrayList<OrdersModel> list, Context context)
    {
        this.list=list;
        this.context=context;
    }




    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.ordersample,parent,false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        final  OrdersModel model = list.get(position);
        holder.orderImage.setImageResource(model.getOrderImage());
        holder.soldItemName.setText(model.getSoldItemName());
        holder.orderNumber.setText("#"+model.getOrderNumber());
        holder.orderPrice.setText(model.getPrice());
//        holder.customername.setText("Customer Name:"+model.getCustomername());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context, DetailActivity.class);
                intent.putExtra("type",2);
                intent.putExtra("Id",Integer.parseInt( model.getOrderNumber()));
                context.startActivity(intent);
            }
        });
      holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
          @Override
          public boolean onLongClick(View view) {
              new AlertDialog.Builder(context)
                      .setTitle("Delete Item")
                      .setMessage("Are you sure to delete this Order")
                      .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                          @Override
                          public void onClick(DialogInterface dialogInterface, int i) {
                              int del=Integer.parseInt(model.getOrderNumber());
                              DbHelper helper=new DbHelper(context);
                                if(helper.deleteOrder(del)>0)
                                {
                                    Toast.makeText(context, "##Successfully Deleted", Toast.LENGTH_SHORT).show();
                                    //Refresh After De


                                }
                                else {
                                    Toast.makeText(context, "!!!Error Not Deleted ", Toast.LENGTH_SHORT).show();
                                }
                          }
                      })
                      .setNegativeButton("No", new DialogInterface.OnClickListener() {
                          @Override
                          public void onClick(DialogInterface dialogInterface, int i) {

                          }
                      }).show();
           return false;
          }
      });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder{
        ImageView orderImage;
        TextView soldItemName,orderNumber,orderPrice,customername;

        public viewHolder(@NonNull View itemView) {
            super(itemView);
            orderImage=itemView.findViewById(R.id.orderImage);
            soldItemName=itemView.findViewById(R.id.orderName);
            orderNumber=itemView.findViewById(R.id.orderNumber);
            orderPrice=itemView.findViewById(R.id.priceOrder);
//            customername=itemView.findViewById(R.id.CustomerName);
        }

    }
}
