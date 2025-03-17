package com.example.mobliestore.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mobliestore.MainActivity;
import com.example.mobliestore.R;
import com.example.mobliestore.utils.DataManager;

public class RegisterActivity extends AppCompatActivity {
    EditText edtUsername, edtPassword, edtConfirmPassword, edtEmail,edtPhone;
    Button btnRegisterUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        edtUsername = findViewById(R.id.edtUsername);
        edtPassword = findViewById(R.id.edtPassWord);
        edtConfirmPassword = findViewById(R.id.edtConfirmPass);
        edtEmail = findViewById(R.id.edtEmail);
        edtPhone=findViewById(R.id.edtPhone);
        btnRegisterUser=findViewById(R.id.btnRegisterUser);
        btnRegisterUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SaveData();
            }
        });
    }
    public void SaveData() {
        String name = edtUsername.getText().toString();
        String password = edtPassword.getText().toString().trim();
        String confirmPassword = edtConfirmPassword.getText().toString().trim();
        String email = edtEmail.getText().toString().trim();
        String phone = edtPhone.getText().toString().trim();
        if (name.isEmpty()) {
            edtUsername.setError("Tên đăng nhập không được trống");

        } else {
            edtUsername.setError(null);

        }
        if (password.isEmpty()) {
            edtPassword.setError("Mật khẩu không được trống");

        } else {
            edtPassword.setError(null);

        }
        if (confirmPassword.isEmpty()) {
            edtConfirmPassword.setError("Mật khẩu không được trống");

        } else {
            edtConfirmPassword.setError(null);

        }
        if (email.isEmpty()) {
            edtEmail.setError("Email không được trống");

        } else if (!email.contains("@gmail")) {
            edtEmail.setError("Email không hợp lệ (phải chứa @)");
        }
        else {
            edtEmail.setError(null);

        }    if (phone.isEmpty()) {
            edtPhone.setError("số điện thoại không được trống");

        } else if (phone.length() != 10 ) {
            edtPhone.setError("Số điện thoại phải có đúng 10 chữ số");
        }else {
            edtPhone.setError(null);

        }


        if (name.isEmpty() || password.isEmpty() || email.isEmpty() || phone.isEmpty() ) {
            Toast.makeText(this, "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!password.equals(confirmPassword)) {
            Toast.makeText(this, "Mật khẩu xác nhận không khớp!", Toast.LENGTH_SHORT).show();
            return;
        }
        DataManager dataManager= new DataManager(this);
        boolean isRegister = dataManager.registerUser(name,password,email,phone);
        if (isRegister) {
            Toast.makeText(this, "Đăng ký thành công!", Toast.LENGTH_SHORT).show();
            finish();
        }else {
            Toast.makeText(this, "Đăng ký không thành công", Toast.LENGTH_SHORT).show();
        }

        Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}