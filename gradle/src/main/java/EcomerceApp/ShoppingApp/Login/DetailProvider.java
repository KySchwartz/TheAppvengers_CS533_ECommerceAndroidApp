package EcomerceApp.ShoppingApp.Login;
import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.widget.Toast;
public class DetailProvider extends ContentProvider {

    public static final String DB_NAME = "User.db";
    public static final String DB_TB = "login";
    public static final int DB_VERSION = 5;
    SQLiteDatabase myDB;
    private Object rows;

    public DetailProvider() {

    }

    public static final String AUTHORITY = "EcomerceApp.ShoppingApp.Login.DetailProvider";
    public static final Uri CONTENT_URI = Uri.parse("content://"+ AUTHORITY+ "/login");
    static int EMP = 1;
    static int EMP_ID = 2;
    static UriMatcher myOwnUri = new UriMatcher(UriMatcher.NO_MATCH);
    static {

        myOwnUri.addURI(AUTHORITY,"login", EMP);
        myOwnUri.addURI(AUTHORITY,"login/#", EMP_ID);

    }
    public static class MyDetailHelper extends SQLiteOpenHelper {
        Context ctx;
        public MyDetailHelper(Context ct) {
            super(ct, DB_NAME, null, DB_VERSION);
            this.ctx=ct;
        }
        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("create table "+ DB_TB +" (id INTEGER PRIMARY KEY AUTOINCREMENT, CUSTOMER_EMAIL text,CUSTOMER_PASSWORD text,CUSTOMER_PHONE   text,CUSTOMER_NAME text)");
        }
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int
                newVersion) {
            db.execSQL("drop table if exists " + DB_TB);
        }


    }
    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs)
    {
        // Implement this to handle requests to delete one or more rows.
        throw new UnsupportedOperationException("Not yet implemented");
    }
    @Override
    public String getType(Uri uri) {
        // TODO: Implement this to handle requests for the MIME type of the data
        // at the given URI.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        long row = myDB.insert(DB_TB, null, values);
        if(row > 0){

            uri = ContentUris.withAppendedId(CONTENT_URI,row);
            getContext().getContentResolver().notifyChange(uri,null);
        }
        if(row==-1)
        {
            Toast.makeText(getContext(), "Data Not Inserted", Toast.LENGTH_SHORT).show();
        }
        else
        {
            Toast.makeText(getContext(), "Data Inserted", Toast.LENGTH_SHORT).show();
        }

        return uri;
    }
    @Override
    public boolean onCreate() {
        MyDetailHelper myOwnHelper = new MyDetailHelper(getContext());
        myDB = myOwnHelper.getWritableDatabase();
        if(myDB!= null){
            return true;
        }else {
            return false;
        }
    }
    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        SQLiteQueryBuilder myQuery = new SQLiteQueryBuilder();
        myQuery.setTables(DB_TB);
        Cursor cr = myQuery.query(myDB, null,null,null,null,null,"id");
        cr.setNotificationUri(getContext().getContentResolver(),uri);
        return cr;
    }
    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {

        throw new UnsupportedOperationException("Not yet implemented");
    }

}
