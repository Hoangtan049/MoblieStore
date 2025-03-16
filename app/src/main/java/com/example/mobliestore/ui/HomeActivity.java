package com.example.mobliestore.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.mobliestore.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomeActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        bottomNavigationView = findViewById(R.id.bottommenu);
        bottomNavigationView.setOnItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.menuProduct) {
                    setFragment(new ProductFragment());
                    return true;
                }
                if (item.getItemId() == R.id.menuCategories) {
                    setFragment(new CategoryFragment());
                    return true;
                }
                if (item.getItemId() == R.id.menuOrders) {
                    setFragment(new OrderFragment());
                    return true;
                }
                if (item.getItemId() == R.id.menuInformation) {
                    setFragment(new InformationFragment());
                    return true;
                }

                return false;
            }
        });
        setFragment( new ProductFragment());
    }
    void setFragment(Fragment fragment){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.mainacitivity,fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

}