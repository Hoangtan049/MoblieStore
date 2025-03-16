package com.example.mobliestore.model;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.mobliestore.R;
import com.example.mobliestore.database.DatabaseHelper;
import com.example.mobliestore.ui.UpdateProductActivity;
import com.example.mobliestore.utils.DataManager;

import java.text.NumberFormat;
import java.time.Instant;
import java.util.List;
import java.util.Locale;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {
    Context context;
    List<Product> list;

    public ProductAdapter(Context context, List<Product> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ProductAdapter.ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_product, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductAdapter.ProductViewHolder holder, int position) {
        Product product= list.get(position);
        holder.txtName.setText(product.getName());

        NumberFormat formatter = NumberFormat.getInstance(Locale.US);
        String formattedPrice = formatter.format(product.getPrice()) + " VND";
        holder.txtPrice.setText(formattedPrice);
        holder.txtStock.setText(product.getStock()+"");
        holder.txtCate.setText(product.getCategoryName());
        Uri imageUri = Uri.parse(product.getImage());
        Glide.with(context).load(imageUri).into(holder.imageProduct);
        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DataManager dataManager= new DataManager(context);
                dataManager.deleteProduct(list.get(holder.getAdapterPosition()).getId());
                list.remove(list.get(holder.getAdapterPosition()));
                notifyItemRemoved(holder.getAdapterPosition());

            }
        });
        holder.btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int currentPosition=holder.getAdapterPosition();

                Product currentProduct= list.get(currentPosition);
                Intent intent = new Intent(context, UpdateProductActivity.class);
                intent.putExtra(DatabaseHelper.COLUMN_ID,currentProduct.getId());
                intent.putExtra(DatabaseHelper.COLUMN_PRODUCT_NAME,currentProduct.getName());
                intent.putExtra(DatabaseHelper.COLUMN_PRODUCT_PRICE,currentProduct.getPrice());
                intent.putExtra(DatabaseHelper.COLUMN_PRODUCT_CATEGORY_ID,currentProduct.getStock());
                intent.putExtra(DatabaseHelper.COLUMN_PRODUCT_IMAGE,currentProduct.getImage());
                intent.putExtra(DatabaseHelper.COLUMN_PRODUCT_STOCK,currentProduct.getCategoryId());
                intent.putExtra(DatabaseHelper.TABLE_PRODUCT,currentProduct.getCategoryName());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ProductViewHolder extends RecyclerView.ViewHolder {
        ImageView imageProduct;
        TextView txtName,txtPrice,txtStock,txtCate;
        ImageButton btnDelete,btnUpdate;
        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            imageProduct=itemView.findViewById(R.id.imageProduct);
            txtName=itemView.findViewById(R.id.txtName);
            txtPrice=itemView.findViewById(R.id.txtPrice);
            txtStock=itemView.findViewById(R.id.txtStock);
            txtCate=itemView.findViewById(R.id.txtCate);
            btnUpdate=itemView.findViewById(R.id.btnUpdate);
            btnDelete=itemView.findViewById(R.id.btnDelete);
        }
    }
}
