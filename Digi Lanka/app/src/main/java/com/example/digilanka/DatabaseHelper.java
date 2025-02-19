package com.example.digilanka;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "digilanka.db";
    private static final int DATABASE_VERSION = 1;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // You can implement table creation here if necessary
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Handle database upgrade as needed
    }

    public boolean checkNICExists(String nicNumber) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM citizens WHERE nic_number = ?";
        Cursor cursor = db.rawQuery(query, new String[]{nicNumber});
        boolean exists = (cursor.getCount() > 0);
        cursor.close();
        return exists;
    }
}