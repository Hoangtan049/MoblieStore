package com.example.mobliestore.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.mobliestore.database.DatabaseHelper;

import java.util.ArrayList;
import java.util.List;

public class DataManager {
    DatabaseHelper dbHelper;
    SQLiteDatabase db;
    public DataManager(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    public void open() {
        db = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    // CRUD for Product
    public void addProduct(String name, double price, int categoryId, String image, int stock) {
        open();
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_PRODUCT_NAME, name);
        values.put(DatabaseHelper.COLUMN_PRODUCT_PRICE, price);
        values.put(DatabaseHelper.COLUMN_PRODUCT_CATEGORY_ID, categoryId);
        values.put(DatabaseHelper.COLUMN_PRODUCT_IMAGE, image);
        values.put(DatabaseHelper.COLUMN_PRODUCT_STOCK, stock);
        db.insert(DatabaseHelper.TABLE_PRODUCT, null, values);
        close();
    }

    public List<String> getAllProducts() {
        open();
        List<String> productList = new ArrayList<>();
        Cursor cursor = db.query(DatabaseHelper.TABLE_PRODUCT, null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                productList.add(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_PRODUCT_NAME)));
            } while (cursor.moveToNext());
        }
        cursor.close();
        close();
        return productList;
    }

    public void updateProduct(int id, String name, double price, int categoryId, String image, int stock) {
        open();
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_PRODUCT_NAME, name);
        values.put(DatabaseHelper.COLUMN_PRODUCT_PRICE, price);
        values.put(DatabaseHelper.COLUMN_PRODUCT_CATEGORY_ID, categoryId);
        values.put(DatabaseHelper.COLUMN_PRODUCT_IMAGE, image);
        values.put(DatabaseHelper.COLUMN_PRODUCT_STOCK, stock);
        db.update(DatabaseHelper.TABLE_PRODUCT, values, DatabaseHelper.COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
        close();
    }

    public void deleteProduct(int id) {
        open();
        db.delete(DatabaseHelper.TABLE_PRODUCT, DatabaseHelper.COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
        close();
    }

    // CRUD for Category
    public void addCategory(String name, String image) {
        open();
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_CATEGORY_NAME, name);
        values.put(DatabaseHelper.COLUMN_CATEGORY_IMAGE, image);
        db.insert(DatabaseHelper.TABLE_CATEGORY, null, values);
        close();
    }

    public List<String> getAllCategories() {
        open();
        List<String> categoryList = new ArrayList<>();
        Cursor cursor = db.query(DatabaseHelper.TABLE_CATEGORY, null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                categoryList.add(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_CATEGORY_NAME)));
            } while (cursor.moveToNext());
        }
        cursor.close();
        close();
        return categoryList;
    }
    public void deleteCategory(int id) {
        open();
        db.delete(DatabaseHelper.TABLE_CATEGORY, DatabaseHelper.COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
        close();
    }
    public void updateCategory(int id, String name, String image) {
        open();
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_CATEGORY_NAME, name);
        values.put(DatabaseHelper.COLUMN_CATEGORY_IMAGE, image);
        db.update(DatabaseHelper.TABLE_CATEGORY, values, DatabaseHelper.COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
        close();
    }
    // Order functionality
    public void placeOrder(int productId, int quantity, String productName, double price, String image) {
        open();
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_ORDER_PRODUCT_ID, productId);
        values.put(DatabaseHelper.COLUMN_ORDER_QUANTITY, quantity);
        values.put(DatabaseHelper.COLUMN_ORDER_PRODUCT_NAME, productName);
        values.put(DatabaseHelper.COLUMN_ORDER_PRODUCT_PRICE, price);
        values.put(DatabaseHelper.COLUMN_ORDER_PRODUCT_IMAGE, image);
        db.insert(DatabaseHelper.TABLE_ORDER, null, values);
        close();
    }

    public List<String> getAllOrders() {
        open();
        List<String> orderList = new ArrayList<>();
        Cursor cursor = db.query(DatabaseHelper.TABLE_ORDER, null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                orderList.add(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ORDER_PRODUCT_NAME)));
            } while (cursor.moveToNext());
        }
        cursor.close();
        close();
        return orderList;
    }

    // User Authentication
    public boolean registerUser(String username, String password, String email, String phone) {
        open();
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_USER_USERNAME, username);
        values.put(DatabaseHelper.COLUMN_USER_PASSWORD, password);
        values.put(DatabaseHelper.COLUMN_USER_EMAIL, email);
        values.put(DatabaseHelper.COLUMN_USER_PHONE, phone);
        long result = db.insert(DatabaseHelper.TABLE_USER, null, values);
        close();
        return result != -1;
    }

    public boolean loginUser(String username, String password) {
        open();
        Cursor cursor = db.query(DatabaseHelper.TABLE_USER,
                new String[]{DatabaseHelper.COLUMN_ID},
                DatabaseHelper.COLUMN_USER_USERNAME + " = ? AND " + DatabaseHelper.COLUMN_USER_PASSWORD + " = ?",
                new String[]{username, password},
                null, null, null);
        boolean loginSuccess = cursor.getCount() > 0;
        cursor.close();
        close();
        return loginSuccess;
    }
}
