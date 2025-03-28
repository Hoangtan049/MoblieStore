package com.example.mobliestore.ui;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.mobliestore.R;
import com.example.mobliestore.model.Category;
import com.example.mobliestore.utils.DataManager;

import java.util.ArrayList;
import java.util.List;

public class UpdateProductActivity extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 1;
    EditText edtUpdateName, edtUpdatePrice, edtUpdateStock;
    ImageButton btnImage;
    int productId,categoryId;
    Spinner spnUpdateCate;
    Button btnUpdate;
    Uri imageUri;
    List<Category> categoriesList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_product);
        edtUpdateName = findViewById(R.id.edtUpdateName);
        edtUpdatePrice = findViewById(R.id.edtUpdatePrice);
        edtUpdateStock = findViewById(R.id.edtUpdateStock);
        btnImage = findViewById(R.id.imageUpdate);
        btnUpdate = findViewById(R.id.btnUpdateProduct);
        spnUpdateCate=findViewById(R.id.spnUpdateCate);
        Intent intent = getIntent();
        productId = intent.getIntExtra("product_id", -1);
        String productName = intent.getStringExtra("product_name");
        String productDes = intent.getStringExtra("product_description");
        double productPrice = intent.getDoubleExtra("product_price", 0);
        int productStock = intent.getIntExtra("product_stock", 0);
        String imagepath = intent.getStringExtra("product_image");
        categoryId=intent.getIntExtra("product_category_id",-1);
        edtUpdateName.setText(productName);
        edtUpdatePrice.setText(productPrice + "");
        edtUpdateStock.setText(productStock + "");
        if (imagepath != null) {
            imageUri = Uri.parse(imagepath);
            Glide.with(this).load(imageUri).into(btnImage);
        }
        loadCategories();
        btnImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openAsset();
            }
        });
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String newName = edtUpdateName.getText().toString().trim();
                double newPrice = Double.parseDouble(edtUpdatePrice.getText().toString());
                int newStock = Integer.parseInt(edtUpdateStock.getText().toString());
                int selectedCategoryId = categoriesList.get(spnUpdateCate.getSelectedItemPosition()).getId();
                DataManager dataManager = new DataManager(UpdateProductActivity.this);
                dataManager.updateProduct(productId, newName,  newPrice, newStock, imageUri != null ? imageUri.toString() : null,selectedCategoryId);
                Toast.makeText(UpdateProductActivity.this, "Cập nhật thành công!", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }
    private void loadCategories() {
        DataManager dataManager = new DataManager(this);
        categoriesList = dataManager.getAllCategories();
        List<String> categoryNames = new ArrayList<>();

        int selectedPosition = 0;
        for (int i = 0; i < categoriesList.size(); i++) {
            categoryNames.add(categoriesList.get(i).getName());
            if (categoriesList.get(i).getId() == categoryId) {
                selectedPosition = i;
            }
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, categoryNames);
        spnUpdateCate.setAdapter(adapter);
        spnUpdateCate.setSelection(selectedPosition); // Đặt danh mục hiện tại làm mặc định
    }
    private void openAsset() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            imageUri = data.getData();

            // Giữ quyền truy cập Uri lâu dài
            getContentResolver().takePersistableUriPermission(imageUri, Intent.FLAG_GRANT_READ_URI_PERMISSION);

            Glide.with(this).load(imageUri).into(btnImage);


        }
    }
}