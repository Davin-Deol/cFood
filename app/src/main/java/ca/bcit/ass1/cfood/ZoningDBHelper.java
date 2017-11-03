package ca.bcit.ass1.cfood;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by E on 2017-11-02.
 */

public class ZoningDBHelper extends SQLiteOpenHelper {

    private static String dbName = "cfood.db";
    private static int version = 1;
    private static String tableZoning = "zoning";
    private static String columnID = "_id";
    public static String columnZoneType = "type";
    public static String columnJSONCoord = "coords";
    //public static SQLiteDatabase db;

    public ZoningDBHelper(Context context) {
        super(context, dbName, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String createTable;

        createTable = "CREATE TABLE IF NOT EXISTS "  + tableZoning + " ( " +
                columnID   + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                columnZoneType + " TEXT NOT NULL, " +
                columnJSONCoord + " TEXT NOT NULL)";

        db.execSQL(createTable);
        // this.db = db;
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public void insertZone(final SQLiteDatabase db, String zoneType, String zoneCoords) {

        ContentValues contentValues = new ContentValues();
        contentValues.put(columnZoneType, zoneType);
        contentValues.put(columnJSONCoord, zoneCoords);

        db.insert(tableZoning, null, contentValues);
    }

    public Cursor getAllZones(SQLiteDatabase db) {
        Cursor cursor;

        cursor = db.query(tableZoning,
                null, null, null, null, null, null, null);
        cursor.moveToFirst();

        return cursor;
    }
}
