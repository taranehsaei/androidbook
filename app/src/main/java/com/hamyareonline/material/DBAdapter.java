package com.hamyareonline.material;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import java.util.ArrayList;
import java.util.List;

public class DBAdapter {
    public static final String TAG = "hamyareonline.com";
    public static final String SUB_ALL = "a";

    static final String KEY_ID = "id";
    static final String KEY_TITLE = "title";
    static final String KEY_SUBJECT = "subject";
    static final String KEY_IMAGE_ADRS = "img_adrs";
    static final String KEY_CONTENT = "content";

    String[] yek_SH_flashkart = new String[] {KEY_ID, KEY_TITLE,
            KEY_SUBJECT, KEY_IMAGE_ADRS, KEY_CONTENT };

    static final String DATABASE_NAME = "book_DB";
    static final String DATABASE_MAINTABLE = "main";
    static final int DATABASE_VERSION =1;
    static final String CREATE_MAINTABLE =
            "CREATE TABLE \"main\" (\"title\" TEXT, \"content\" TEXT, \"subject\" TEXT, \"img_adrs\" TEXT, \"id\" INTEGER PRIMARY KEY  NOT NULL )";

    final Context context;
    DatabaseHelper DBHelper;
    SQLiteDatabase db;
    
    public DBAdapter(Context ctx)
    {
        this.context = ctx;
        DBHelper = new DatabaseHelper(context);
    }

    private static class DatabaseHelper extends SQLiteOpenHelper
    {
        DatabaseHelper(Context context)
        {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db)
        {
            try {
                db.execSQL(CREATE_MAINTABLE);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
        {
        	 Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                    + newVersion + ", which will destroy all old data");

            db.execSQL("DROP TABLE IF EXISTS "+DATABASE_MAINTABLE);
            onCreate(db);
        }
    }
    public DBAdapter open() throws SQLException
    {
        db = DBHelper.getWritableDatabase();
        return this;
    }
    public void close()
    {
        DBHelper.close();
    }

    public  List<sho_items> getAllItms(String SUBJECT)
    {
        Cursor cursor =
                db.query(true, DATABASE_MAINTABLE, yek_SH_flashkart, KEY_SUBJECT + " == '" + SUBJECT + "'", null,
                        null, null, null, null);
        List<sho_items> items = cursorToList(cursor);
        return items;
    }
    public  List<sho_items> getAllItms()
    {
        	Cursor cursor = db.query(DATABASE_MAINTABLE, yek_SH_flashkart, null, null, null, null, null);
        List<sho_items> items = cursorToList(cursor);
        return items;
    }

    public List<sho_items> findSh_Flower(String name,String sub) throws SQLException {
        Cursor cursor = db.query(true, DATABASE_MAINTABLE, yek_SH_flashkart,
                KEY_TITLE + " LIKE '%" + name + "%' AND "+KEY_SUBJECT+" == '"+sub+"'", null, null, null, null,
                null);
        List<sho_items> nams = cursorToList(cursor);
        cursor.close();
        return nams;
    }
    public List<sho_items> findSh_Flower(String name) throws SQLException {
        Cursor cursor = db.query(true, DATABASE_MAINTABLE, yek_SH_flashkart,
                KEY_TITLE + " LIKE '%" + name + "%'  ", null, null, null, null,
                null);
        List<sho_items> nams = cursorToList(cursor);
        cursor.close();
        return nams;
    }

    private List<sho_items> cursorToList(Cursor cursor) {
    	 List<sho_items> items = new ArrayList<sho_items>();
         if (cursor.getCount() > 0)
         {
        	 while (cursor.moveToNext()) {
                 sho_items mokhatab = new sho_items();
             	 mokhatab.setId(cursor.getInt(cursor.getColumnIndex(KEY_ID)));
             	 mokhatab.setTitle(cursor.getString(cursor.getColumnIndex(KEY_TITLE)));
                 mokhatab.setSubjcet(cursor.getString(cursor.getColumnIndex(KEY_SUBJECT)));
                 mokhatab.setImg_adrs(cursor.getString(cursor.getColumnIndex(KEY_IMAGE_ADRS)));
                 mokhatab.setContent(cursor.getString(cursor.getColumnIndex(KEY_CONTENT)));
                 items.add(mokhatab);
        	 } ;
         }
         return items;
	}
    public sho_items getItm(int ID) throws SQLException
    {
        Cursor cursor =
                db.query(true, DATABASE_MAINTABLE, yek_SH_flashkart, KEY_ID + " == '" + ID + "' ", null,
                null, null, null, null);
        sho_items mokhatab = new sho_items();
        if (cursor != null) {
            cursor.moveToFirst();
            mokhatab.setId(cursor.getInt(cursor.getColumnIndex(KEY_ID)));
            mokhatab.setTitle(cursor.getString(cursor.getColumnIndex(KEY_TITLE)));
            mokhatab.setSubjcet(cursor.getString(cursor.getColumnIndex(KEY_SUBJECT)));
            mokhatab.setImg_adrs(cursor.getString(cursor.getColumnIndex(KEY_IMAGE_ADRS)));
            mokhatab.setContent(cursor.getString(cursor.getColumnIndex(KEY_CONTENT)));
        }
        cursor.close();
        return mokhatab;


    }

}
