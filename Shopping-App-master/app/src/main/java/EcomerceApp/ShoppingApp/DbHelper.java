package EcomerceApp.ShoppingApp;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import androidx.annotation.Nullable;
import java.util.ArrayList;
import EcomerceApp.ShoppingApp.Models.OrdersModel;
    public  class DbHelper extends SQLiteOpenHelper {
        final static String DbName = "foodData.db";
        final static int version = 5;
        final static String DB_TB = "orders";
        public DbHelper(@Nullable Context context) {
            super(context, DbName, null, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {

            db.execSQL("create table " + DB_TB + " (id Integer PRIMARY KEY AUTOINCREMENT, name text, phone text, price int, image int,description text,foodname text,quantity int)");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int i, int i1) {
            db.execSQL("drop table if exists " + DB_TB);
            onCreate(db);
        }

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
            if (id == -1) {
                return false;
            } else {
                return true;
            }
        }

        public ArrayList<OrdersModel> getOrders() {
            ArrayList<OrdersModel> orders = new ArrayList<>();
            SQLiteDatabase database = this.getWritableDatabase();
            Cursor cursor = database.rawQuery("Select id,foodname,image,price,name from orders", null);
//        if(cursor.moveToFirst())
//        {
            while (cursor.moveToNext()) {
                OrdersModel model = new OrdersModel();
                model.setOrderNumber(cursor.getInt(0) + "");
                model.setSoldItemName(cursor.getString(1));
                model.setOrderImage(cursor.getInt(2));
                model.setPrice(cursor.getInt(3) + "");
                model.setCustomername(cursor.getString(4));
                orders.add(model);
            }
//        }
            cursor.close();
            database.close();
            return orders;
        }

        public Cursor getOrderById(int id) {
            SQLiteDatabase database = this.getWritableDatabase();
            Cursor cursor = database.rawQuery("Select * from orders where id=" + id, null);
            if (cursor != null)
                cursor.moveToFirst();
            return cursor;
        }

        //update order

        public boolean UpdateOrder(String name, String phone, int image, int price, String description, String foodname, int quantity, int id) {
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
            if (row == -1) {
                return false;
            } else {
                return true;
            }
        }
        public int deleteOrder(int id) {
            SQLiteDatabase database = this.getWritableDatabase();
            return database.delete("orders", "id=" + id, null);
        }
    }
