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
import com.example.mobliestore.ui.UpdateCategoryActivity;
import com.example.mobliestore.utils.DataManager;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {
    Context context;
    List<Category> list;

    public CategoryAdapter(Context context, List<Category> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public CategoryAdapter.CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_category, parent, false);
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryAdapter.CategoryViewHolder holder, int position) {
        Category categories=list.get(position);
        holder.txtcateName.setText(categories.getName());
        Uri imageUri = Uri.parse(categories.getImage());
        Glide.with(context).load(imageUri).into(holder.cateImg);
        holder.btnDeleteCate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int currentPosition= holder.getAdapterPosition();
                DataManager dataManager= new DataManager(context);
                dataManager.deleteCategory(list.get(currentPosition).getId());
                list.remove(list.get(currentPosition));
                notifyItemRemoved(currentPosition);
            }
        });
        holder.btnUpdateCate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int currentPosition= holder.getAdapterPosition();
                Category currentCate= list.get(currentPosition);
                Intent intent= new Intent(context, UpdateCategoryActivity.class);
                intent.putExtra(DatabaseHelper.COLUMN_ID,currentCate.getId());
                intent.putExtra(DatabaseHelper.COLUMN_CATEGORY_NAME,currentCate.getName());
                intent.putExtra(DatabaseHelper.COLUMN_CATEGORY_IMAGE,currentCate.getImage());
                context.startActivity(intent);


            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class CategoryViewHolder extends RecyclerView.ViewHolder {
        ImageView cateImg;
        TextView txtcateName;
        ImageButton btnUpdateCate,btnDeleteCate;
        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            cateImg=itemView.findViewById(R.id.cateImg);
            txtcateName=itemView.findViewById(R.id.txtcateName);
            btnDeleteCate=itemView.findViewById(R.id.btnDeleteCate);
            btnUpdateCate=itemView.findViewById(R.id.btnUpdateCate);
        }
    }
}
