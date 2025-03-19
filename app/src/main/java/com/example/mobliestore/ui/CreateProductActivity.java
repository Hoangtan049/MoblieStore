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
import com.example.mobliestore.database.DatabaseHelper;
import com.example.mobliestore.model.Category;
import com.example.mobliestore.utils.DataManager;

import java.util.ArrayList;
import java.util.List;

public class CreateProductActivity extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 1;
    Spinner spnCate;
    EditText edtCreateName,  edtCreatePrice, edtCreateStock;
    ImageButton imageCreate;
    Button btnCreate;
    Uri imageUri;
    DatabaseHelper databaseHelper;
    List<Category> categoryList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_product);
        databaseHelper = new DatabaseHelper(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        edtCreateName = findViewById(R.id.edtCreateName);
        edtCreatePrice = findViewById(R.id.edtCreatePrice);
        edtCreateStock = findViewById(R.id.edtCreateStock);
        imageCreate = findViewById(R.id.imageCreate);
        spnCate=findViewById(R.id.spnCate);
        btnCreate = findViewById(R.id.btnCreate);
        loadCategories();
        imageCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openAsset();
            }
        });
        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String Name = edtCreateName.getText().toString().trim();

                double price = Double.parseDouble(edtCreatePrice.getText().toString().trim());
                int stock = Integer.parseInt(edtCreateStock.getText().toString().trim());
                String categoryName =spnCate.getSelectedItem().toString();
                int selectedCategoryId = categoryList.get(spnCate.getSelectedItemPosition()).getId();
                if (imageUri  == null) {
                    Toast.makeText(CreateProductActivity.this, "Vui lòng chọn hình ảnh!", Toast.LENGTH_SHORT).show();
                    return;
                }
                DataManager dataManager = new DataManager(CreateProductActivity.this);

                boolean isAdded = dataManager.addProduct(Name, price,selectedCategoryId , imageUri.toString(),stock);
                if (isAdded) {
                    Toast.makeText(CreateProductActivity.this, "Thêm sản phẩm thành công!", Toast.LENGTH_SHORT).show();
                    finish();

                } else {
                    Toast.makeText(CreateProductActivity.this, "Thêm sản phẩm thất bại!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private  void  loadCategories(){
        DataManager dataManager= new DataManager(this);
        categoryList=dataManager.getAllCategories();
        List<String> categoryName=new ArrayList<>();
        for(Category category:categoryList){
            categoryName.add(category.getName());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item,categoryName);
        spnCate.setAdapter(adapter);
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
            getContentResolver().takePersistableUriPermission(imageUri, Intent.FLAG_GRANT_READ_URI_PERMISSION);
            Glide.with(this).load(imageUri).into(imageCreate);


        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }
}