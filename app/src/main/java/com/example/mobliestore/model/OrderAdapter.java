package com.example.mobliestore.model;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.mobliestore.R;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {
    Context context;
    List<Order> list;


    public OrderAdapter(Context context, List<Order> list) {
        this.context = context;
        this.list = list;
    }
    @NonNull
    @Override
    public OrderAdapter.OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_order, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderAdapter.OrderViewHolder holder, int position) {
        Order orders= list.get(position);
        holder.TxtQuantityOrderUser.setText(String.valueOf(orders.getQuantity()));
        NumberFormat formatter = NumberFormat.getInstance(Locale.US);
        String formattedPrice = formatter.format(orders.getProductPrice()) + " VND";
        holder.txtToTalUser.setText(formattedPrice);

        holder.txtProductNameOrderUser.setText(orders.getProductName());

        Uri imageUri = Uri.parse(orders.getProductImage());
        Glide.with(context).load(imageUri).into(holder.imgProductOrderUser);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class OrderViewHolder extends RecyclerView.ViewHolder {
        TextView TxtQuantityOrderUser,txtToTalUser,txtProductNameOrderUser;
        ImageView imgProductOrderUser;
        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            txtProductNameOrderUser=itemView.findViewById(R.id.txtProductNameOrderUser);
            TxtQuantityOrderUser=itemView.findViewById(R.id.TxtQuantityOrderUser);

            txtToTalUser=itemView.findViewById(R.id.txtToTalUser);

            imgProductOrderUser=itemView.findViewById(R.id.imgProductOrderUser);
        }
    }
}
