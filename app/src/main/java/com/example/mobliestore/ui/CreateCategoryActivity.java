package com.example.mobliestore.ui;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.mobliestore.R;
import com.example.mobliestore.database.DatabaseHelper;
import com.example.mobliestore.utils.DataManager;

public class CreateCategoryActivity extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 1;
    EditText edtNameCate;
    ImageButton imageCreateCate;
    Button btnCreateCate;
    Uri imageUri;
    DatabaseHelper databaseHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_category);
        databaseHelper = new DatabaseHelper(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        edtNameCate = findViewById(R.id.edtNameCate);
        imageCreateCate = findViewById(R.id.imageCreateCate);
        btnCreateCate = findViewById(R.id.btnCreateCate);
        imageCreateCate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openAsset();
            }
        });
        btnCreateCate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String Name = edtNameCate.getText().toString().trim();
                if (imageUri == null) {
                    Toast.makeText(CreateCategoryActivity.this, "Vui lòng chọn hình ảnh!", Toast.LENGTH_SHORT).show();
                    return;
                }
                DataManager userDAO = new DataManager(CreateCategoryActivity.this);
                boolean isAdded = userDAO.addCategory(Name, imageUri.toString());
                if (isAdded) {
                    Toast.makeText(CreateCategoryActivity.this, "Thêm nhãn hiệu thành công!", Toast.LENGTH_SHORT).show();
                    finish();

                } else {
                    Toast.makeText(CreateCategoryActivity.this, "Thêm nhãn hiệu thất bại!", Toast.LENGTH_SHORT).show();
                }
            }
        });

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
            Glide.with(this).load(imageUri).into(imageCreateCate);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }
}