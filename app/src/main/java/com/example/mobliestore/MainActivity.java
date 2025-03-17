package com.example.mobliestore;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mobliestore.R;
import com.example.mobliestore.model.User;
import com.example.mobliestore.ui.HomeActivity;
import com.example.mobliestore.ui.RegisterActivity;
import com.example.mobliestore.utils.DataManager;

public class MainActivity extends AppCompatActivity {
    Button btnLogin, btnRegister ;
    EditText edtUserNameLogin, edtPasswordLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnLogin = findViewById(R.id.btnLogin);
        btnRegister = findViewById(R.id.btnRegister);
        edtUserNameLogin = findViewById(R.id.edtUserNameLogin);
        edtPasswordLogin = findViewById(R.id.edtPasswordLogin);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = edtUserNameLogin.getText().toString();
                String password = edtPasswordLogin.getText().toString();
                if (name.isEmpty()) {
                    edtUserNameLogin.setError("Tên đăng nhập không được trống");

                } else {
                    edtUserNameLogin.setError(null);

                }
                if (password.isEmpty()) {
                    edtPasswordLogin.setError("Mật khẩu không được trống");

                } else {
                    edtPasswordLogin.setError(null);

                }
                if (name.isEmpty() || password.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                    return;
                }
                DataManager dataManager = new DataManager(MainActivity.this);
                User user = dataManager.loginUser(name, password);
                if (user != null) {
                    Toast.makeText(MainActivity.this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
                    SharedPreferences sharedPreferences = getSharedPreferences("USER_PREF", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putInt("user_id", user.getId());
                    editor.putString("username", user.getUsername());
                    editor.apply();

                    startActivity(new Intent(MainActivity.this, HomeActivity.class));


                    finish();
                } else {
                    Toast.makeText(MainActivity.this, "Sai tài khoản ,mật khẩu", Toast.LENGTH_SHORT).show();
                }
            }

        });
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }

}