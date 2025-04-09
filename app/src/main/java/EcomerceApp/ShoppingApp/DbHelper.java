package EcomerceApp.ShoppingApp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import EcomerceApp.ShoppingApp.Models.CartItem;
import EcomerceApp.ShoppingApp.Models.OrdersModel;
import EcomerceApp.ShoppingApp.Models.Product;

public class DbHelper extends SQLiteOpenHelper {
    //Firebase is used for orders and SQLLite is used for the cart and recently viewed products
    final static String DbName = "foodData.db";
    final static int version = 6;  // Incremented version for new table
    final static String DB_TB = "orders";
    final static String RECENTLY_VIEWED_TB = "recently_viewed";
    private FirebaseFirestore firestore;
    public static final String TABLE_CART = "cart";
    public static final String CART_ID = "cartid";
    public static final String COLUMN_PRODUCT_ID = "productid";
    public static final String COLUMN_QUANTITY = "quantity";
    public static final String COLUMN_PRICE = "price";
    public static final String COLUMN_PRODUCT_NAME = "productname";
    public static final String COLUMN_IMAGE_URL = "imageUrl";

    public DbHelper(@Nullable Context context) {
        super(context, DbName, null, version);
        firestore = FirebaseFirestore.getInstance();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + DB_TB +
                " (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, phone TEXT, price INTEGER, image INTEGER, description TEXT, foodname TEXT, quantity INTEGER)");

        // Creating table for recently viewed products
        db.execSQL("CREATE TABLE " + RECENTLY_VIEWED_TB +
                " (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, description TEXT, price TEXT, image TEXT)");

        String createCartTable = "CREATE TABLE " + TABLE_CART + " (" + CART_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_PRODUCT_ID + " TEXT, " + COLUMN_QUANTITY + " INTEGER, " + COLUMN_PRICE + " REAL, "
                + COLUMN_PRODUCT_NAME + " TEXT, " + COLUMN_IMAGE_URL + " TEXT)";
        db.execSQL(createCartTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // We are using version 6, so it will run this code when we have a version previous than 7.
        // it will drop the old tables and then will create them again.
        if (oldVersion < 6) {
            db.execSQL("DROP TABLE IF EXISTS " + DB_TB);
            db.execSQL("DROP TABLE IF EXISTS " + RECENTLY_VIEWED_TB);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_CART);
            onCreate(db);
        }
    }

    // Insert an order
    public boolean insertOrder(String name, String phone, int image, int price, String description, String foodname, int quantity) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("phone", phone);
        values.put("image", image);
        values.put("price", price);
        values.put("description", description);
        values.put("foodname", foodname);
        values.put("quantity", quantity);

        long id = db.insert(DB_TB, null, values);
        return id != -1;
    }

    public ArrayList<OrdersModel> getOrders() {
        ArrayList<OrdersModel> orders = new ArrayList<>();
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery("SELECT id, foodname, image, price, name, quantity FROM orders", null);

        while (cursor.moveToNext()) {
            OrdersModel model = new OrdersModel();
            model.setOrderNumber(cursor.getInt(0) + "");
            model.setSoldItemName(cursor.getString(1));
            model.setOrderImage(cursor.getInt(2));
            model.setPrice(cursor.getInt(3) + "");
            model.setCustomername(cursor.getString(4));
            orders.add(model);
        }
        cursor.close();
        database.close();
        return orders;
    }

    public Cursor getOrderById(int id) {
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery("SELECT * FROM orders WHERE id=" + id, null);
        if (cursor != null) cursor.moveToFirst();
        return cursor;
    }

    public boolean updateOrder(String name, String phone, int image, int price, String description, String foodname, int quantity, int id) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("phone", phone);
        values.put("image", image);
        values.put("price", price);
        values.put("description", description);
        values.put("foodname", foodname);
        values.put("quantity", quantity);

        long row = db.update(DB_TB, values, "id=" + id, null);
        return row != -1;
    }

    public int deleteOrder(int id) {
        SQLiteDatabase database = this.getWritableDatabase();
        return database.delete("orders", "id=" + id, null);
    }

    // ******************************
    // RECENTLY VIEWED TABLE METHODS
    // ******************************

    // Insert a recently viewed product
    public void insertRecentlyViewed(Product product) {
        SQLiteDatabase db = getWritableDatabase();

        // Check if the product already exists
        Cursor cursor = db.rawQuery("SELECT * FROM " + RECENTLY_VIEWED_TB + " WHERE name=?", new String[]{product.getName()});
        if (cursor.getCount() > 0) {
            cursor.close();
            return; // Product is already in recently viewed, no need to insert
        }
        cursor.close();

        ContentValues values = new ContentValues();
        values.put("name", product.getName());
        values.put("description", product.getDescription());
        values.put("price", product.getPrice());
        values.put("image", product.getImage());

        db.insert(RECENTLY_VIEWED_TB, null, values);
    }

    // Retrieve recently viewed products
    public ArrayList<Product> getRecentlyViewed() {
        ArrayList<Product> recentlyViewedList = new ArrayList<>();
        SQLiteDatabase database = this.getReadableDatabase();
        Cursor cursor = database.rawQuery("SELECT * FROM " + RECENTLY_VIEWED_TB, null);

        while (cursor.moveToNext()) {
            Product product = new Product(
                    cursor.getString(1),  // name
                    cursor.getString(2),  // description
                    cursor.getString(3),  // price
                    cursor.getString(4)   // image
            );
            recentlyViewedList.add(product);
        }
        cursor.close();
        return recentlyViewedList;
    }

    // Clear recently viewed products
    public void clearRecentlyViewed() {
        SQLiteDatabase database = this.getWritableDatabase();
        database.execSQL("DELETE FROM " + RECENTLY_VIEWED_TB);
    }


    // Add to cart
    public boolean addToCart(CartItem item) {
        SQLiteDatabase db = this.getWritableDatabase();
        // Check if the product already exists
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_CART + " WHERE productname=?", new String[]{item.getProductName()});
        if (cursor.getCount() > 0) {
            // If the product exists, update the quantity
            ContentValues values = new ContentValues();
            values.put(COLUMN_QUANTITY, item.getQuantity() + 1); // Increment the quantity by 1
            int rowsAffected = db.update(TABLE_CART, values, "productname=?", new String[]{item.getProductName()});
            cursor.close();
            return rowsAffected > 0; // Return true if the update was successful
        }
        cursor.close();
        
        // If the product does not exist, insert it
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_PRODUCT_ID, item.getProductId());
        cv.put(COLUMN_QUANTITY, item.getQuantity());
        cv.put(COLUMN_PRICE, item.getPrice());
        cv.put(COLUMN_PRODUCT_NAME, item.getProductName());
        cv.put(COLUMN_IMAGE_URL, item.getImageUrl());
        long insert = db.insert(TABLE_CART, null, cv);
        return insert != -1;
    }

    // Get Cart Items
    public List<CartItem> getCart() {
        List<CartItem> cartItems = new ArrayList<>();
        String query = "SELECT * FROM " + TABLE_CART;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                String productId = cursor.getString(1);
                int quantity = cursor.getInt(2);
                double price = cursor.getDouble(3);
                String productName = cursor.getString(4);
                int imageUrl = cursor.getInt(5);
                CartItem item = new CartItem(productId, quantity, price, productName, imageUrl);
                cartItems.add(item);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return cartItems;
    }

    //Empty cart
    public boolean emptyCart() {
        SQLiteDatabase db = this.getWritableDatabase();
        long delete = db.delete(TABLE_CART, null, null);
        return delete != -1;
    }

    //Add order
    public void addOrder(String orderId, String userId, List<CartItem> items, double totalAmount, long orderDate, OnSuccessListener<Void> onSuccess, OnFailureListener onFailure) {
        Map<String, Object> order = new HashMap<>();
        order.put("orderId", orderId);
        order.put("userId", userId);
        order.put("totalAmount", totalAmount);
        order.put("orderDate", orderDate);
        List<Map<String, Object>> orderItems = new ArrayList<>();
        for (CartItem item : items) {
            Map<String, Object> itemMap = new HashMap<>();
            itemMap.put("productId", item.getProductId());
            itemMap.put("quantity", item.getQuantity());
            itemMap.put("price", item.getPrice());
            itemMap.put("productName", item.getProductName());
            itemMap.put("imageUrl", item.getImageUrl());
            orderItems.add(itemMap);
        }
        order.put("items", orderItems);
        firestore.collection("orders").document(orderId)
                .set(order)
                .addOnSuccessListener(onSuccess)
                .addOnFailureListener(onFailure);
    }

    public int getCartCount() {
        int count = 0;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT SUM(quantity) FROM " + TABLE_CART, null);
        if (cursor.moveToFirst()) {
            count = cursor.getInt(0); // Get the sum of quantities
        }
        cursor.close();
        db.close();
        Log.d("DbHelper", "Cart count: " + count);
        return count;
    }

    public void removeFromCart(CartItem cartItem) {
        SQLiteDatabase db = this.getWritableDatabase();

        // Use the product ID to delete the item from the cart table
        db.delete(TABLE_CART, COLUMN_PRODUCT_NAME + " = ?", new String[]{cartItem.getProductName()});
        Log.d("CartAdapter", "Removed item from cart: " + cartItem.getProductId());
    }

    public void updateCartItemQuantity(CartItem cartItem, int newQuantity) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_QUANTITY, newQuantity);

        // Update the quantity for the given product ID
        db.update(TABLE_CART, values, COLUMN_PRODUCT_NAME + " = ?", new String[]{cartItem.getProductName()});
    }
}
