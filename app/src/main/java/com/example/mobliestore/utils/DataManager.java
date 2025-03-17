package com.example.mobliestore.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.mobliestore.database.DatabaseHelper;
import com.example.mobliestore.model.Category;
import com.example.mobliestore.model.Order;
import com.example.mobliestore.model.Product;
import com.example.mobliestore.model.User;

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
    public User getInfomation(String username) {
        open();
        User user = null;
        Cursor cursor = db.rawQuery("SELECT username, email, phone FROM User WHERE username = ?", new String[]{username});
        if (cursor.moveToFirst()) {
            String name = cursor.getString(0);
            String email = cursor.getString(1);
            String phone = cursor.getString(2);
            user = new User();
            user.setUsername(name);
            user.setEmail(email);
            user.setPhone(phone);
        }
        close();
        cursor.close();
        return user;
    }


    // CRUD for Product
    public boolean addProduct(String name, double price, int categoryId, String image, int stock) {
        open();
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_PRODUCT_NAME, name);
        values.put(DatabaseHelper.COLUMN_PRODUCT_PRICE, price);
        values.put(DatabaseHelper.COLUMN_PRODUCT_CATEGORY_ID, categoryId);
        values.put(DatabaseHelper.COLUMN_PRODUCT_IMAGE, image);
        values.put(DatabaseHelper.COLUMN_PRODUCT_STOCK, stock);
        long result = db.insert(DatabaseHelper.TABLE_PRODUCT, null, values);
        return result != -1;
    }

    public List<Product> getAllProducts() {
        open();
        List<Product> productList = new ArrayList<>();

        Cursor cursor = db.rawQuery("SELECT p.id, p.name AS product_name, p.price, p.stock, p.image, " +
                "p.category_id, c.name AS category_name " +
                "FROM " + DatabaseHelper.TABLE_PRODUCT + " p " +
                "LEFT JOIN " + DatabaseHelper.TABLE_CATEGORY + " c ON p.category_id = c.id", null);
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ID));
                String name = cursor.getString(cursor.getColumnIndexOrThrow("product_name"));
                double price = cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_PRODUCT_PRICE));
                int categoryId = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_PRODUCT_CATEGORY_ID));
                String image = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_PRODUCT_IMAGE));
                int stock = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_PRODUCT_STOCK));
                String categoryName = cursor.getString(cursor.getColumnIndexOrThrow("category_name"));
                Product product = new Product(id, name, price, categoryId, image, stock,categoryName);
                productList.add(product);
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
    public boolean addCategory(String name, String image) {
        open();
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_CATEGORY_NAME, name);
        values.put(DatabaseHelper.COLUMN_CATEGORY_IMAGE, image);
        long result = db.insert(DatabaseHelper.TABLE_CATEGORY, null, values);
        return result != -1;
    }

    public List<Category> getAllCategories() {
        open();
        List<Category> categoryList = new ArrayList<>();
        Cursor cursor = db.query(DatabaseHelper.TABLE_CATEGORY, null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ID));
                String name = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_CATEGORY_NAME));
                String image = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_CATEGORY_IMAGE));
                categoryList.add(new Category(id, name, image));
            } while (cursor.moveToNext());
        }
        cursor.close();
        close();
        return categoryList;
    }
    public void deleteAllOrders() {
        open();
        db.delete(DatabaseHelper.TABLE_ORDER, null, null);
        close();
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
    public boolean createOrder(int userId, int productId, int quantity,  double price) {
        open();
        Cursor cursor = db.rawQuery("SELECT name , image FROM " + DatabaseHelper.TABLE_PRODUCT + " WHERE id = ?",
                new String[]{String.valueOf(productId)});
        String productName = "";
        String productImage = "";
        if (cursor.moveToFirst()) {
            productName = cursor.getString(0);
            productImage = cursor.getString(1);
        } else {
            Log.e("createOrder", "Lỗi: Không tìm thấy sản phẩm với ID " + productId);
            cursor.close();
            close();
            return false;
        }
        cursor.close();
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_ORDER_USER_ID, userId);
        values.put(DatabaseHelper.COLUMN_ORDER_PRODUCT_ID, productId);
        values.put(DatabaseHelper.COLUMN_ORDER_QUANTITY, quantity);
        values.put(DatabaseHelper.COLUMN_ORDER_PRODUCT_NAME, productName);
        values.put(DatabaseHelper.COLUMN_ORDER_PRODUCT_PRICE, price);
        values.put(DatabaseHelper.COLUMN_ORDER_PRODUCT_IMAGE, productImage);
       long result=  db.insert(DatabaseHelper.TABLE_ORDER, null, values);

        if (result != -1) {
            boolean stockUpdated = updateStock(productId, quantity);
            if (!stockUpdated) {
                // Nếu cập nhật tồn kho thất bại, hãy xóa đơn hàng để giữ tính toàn vẹn dữ liệu
                db.delete(DatabaseHelper.TABLE_ORDER, DatabaseHelper.COLUMN_ID + " = ?", new String[]{String.valueOf(result)});
                close();
                return false;
            }
            close();
            return true;
        }

        close();
        return false;
    }
    public boolean updateStock(int productId, int quantity) {

        // Lấy số lượng tồn kho hiện tại
        Cursor cursor = db.rawQuery("SELECT " + DatabaseHelper.COLUMN_PRODUCT_STOCK +
                        " FROM " + DatabaseHelper.TABLE_PRODUCT +
                        " WHERE " + DatabaseHelper.COLUMN_ID + " = ?",
                new String[]{String.valueOf(productId)});

        if (cursor.moveToFirst()) {
            int currentStock = cursor.getInt(0);
            if (currentStock >= quantity) { // Kiểm tra nếu tồn kho đủ để bán
                ContentValues values = new ContentValues();
                values.put(DatabaseHelper.COLUMN_PRODUCT_STOCK, currentStock - quantity);
                int updatestock = db.update(DatabaseHelper.TABLE_PRODUCT, values,
                        DatabaseHelper.COLUMN_ID + " = ?",
                        new String[]{String.valueOf(productId)});
                cursor.close();

                return updatestock > 0;
            }
        }
        cursor.close();

        return false; // Trả về false nếu không đủ hàng
    }

    public List<Order> getAllOrders(int userId) {
        open();
        List<Order> orderList = new ArrayList<>();

        // Truy vấn lấy danh sách đơn hàng theo user_id
        String query = "SELECT * FROM " + DatabaseHelper.TABLE_ORDER +
                " WHERE " + DatabaseHelper.COLUMN_ORDER_USER_ID + " = ?";

        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(userId)});

        if (cursor.moveToFirst()) {
            do {
                Order order = new Order();
                order.setId(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ID)));
                order.setUserId(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ORDER_USER_ID)));
                order.setProductId(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ORDER_PRODUCT_ID)));
                order.setQuantity(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ORDER_QUANTITY)));
                order.setProductName(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ORDER_PRODUCT_NAME)));
                order.setProductPrice(cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ORDER_PRODUCT_PRICE)));
                order.setProductImage(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ORDER_PRODUCT_IMAGE)));

                orderList.add(order);
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

    public User loginUser(String username, String password) {
        open();
        Cursor cursor = db.query(DatabaseHelper.TABLE_USER,
                new String[]{
                        DatabaseHelper.COLUMN_ID,
                        DatabaseHelper.COLUMN_USER_USERNAME,
                        DatabaseHelper.COLUMN_USER_PASSWORD,
                        DatabaseHelper.COLUMN_USER_EMAIL,
                        DatabaseHelper.COLUMN_USER_PHONE
                },
                DatabaseHelper.COLUMN_USER_USERNAME + " = ? AND " + DatabaseHelper.COLUMN_USER_PASSWORD + " = ?",
                new String[]{username, password},
                null, null, null);

        User user = null;
        if (cursor.moveToFirst()) {
            int userId = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ID));
            String userName = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_USER_USERNAME));
            String userPassword = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_USER_PASSWORD));
            String userEmail = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_USER_EMAIL));
            String userPhone = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_USER_PHONE));

            user = new User(userId, userName, userPassword, userEmail, userPhone);
        }
        cursor.close();
        close();
        return user;
    }
}
