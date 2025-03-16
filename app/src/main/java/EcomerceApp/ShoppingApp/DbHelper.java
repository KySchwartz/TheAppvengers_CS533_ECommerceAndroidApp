package EcomerceApp.ShoppingApp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import androidx.annotation.Nullable;

import java.util.ArrayList;

import EcomerceApp.ShoppingApp.Models.OrdersModel;
import EcomerceApp.ShoppingApp.Models.Product;

public class DbHelper extends SQLiteOpenHelper {
    final static String DbName = "foodData.db";
    final static int version = 6;  // Incremented version for new table
    final static String DB_TB = "orders";
    final static String RECENTLY_VIEWED_TB = "recently_viewed";

    public DbHelper(@Nullable Context context) {
        super(context, DbName, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + DB_TB +
                " (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, phone TEXT, price INTEGER, image INTEGER, description TEXT, foodname TEXT, quantity INTEGER)");

        // Creating table for recently viewed products
        db.execSQL("CREATE TABLE " + RECENTLY_VIEWED_TB +
                " (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, description TEXT, price TEXT, image TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + DB_TB);
        db.execSQL("DROP TABLE IF EXISTS " + RECENTLY_VIEWED_TB);
        onCreate(db);
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
}
