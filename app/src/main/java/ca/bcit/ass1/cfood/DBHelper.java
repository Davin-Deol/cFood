package ca.bcit.ass1.cfood;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by E on 2017-11-22.
 */

public class DBHelper extends SQLiteOpenHelper {

    private static String dbName = "cfood.db";
    private static int version = 13;
    private static String tableZoning = "neighbourhoods";
    private static String tableParks = "parks";
    private static String tableShopping = "shopping";
    private static String tableBusStops = "busstops";
    private static String tableRecreation = "recreation";
    private static String tableSchools = "schools";

    private static String columnID = "_id";
    public static String columnZoneType = "type";
    public static String columnJSONCoord = "coords";
    public static String columnName = "name";
    public static String columnX = "X";
    public static String columnY = "Y";
    public static String columnDesc = "description";
    public static String columnCenterLong = "centerLong";
    public static String columnCenterLat = "centerLat";

    //public static SQLiteDatabase db;

    public DBHelper(Context context) {
        super(context, dbName, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(createNeighbourhoodTable());
//        db.execSQL(createParksTable());
//        db.execSQL(createShoppingTable());
//        db.execSQL(createSchoolsTable());
//        db.execSQL(createRecreationTable());
//        db.execSQL(createBusTable());

        // this.db = db;
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS neighbourhoods");
//        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS parks");
//        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS shopping");
//        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS recreation");
//        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS busstops");
//        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS schools");

        onCreate(sqLiteDatabase);
    }

    public void insertSchool(final SQLiteDatabase db, String name, String x, String y) {

        ContentValues contentValues = new ContentValues();
        contentValues.put(columnName, name);
        contentValues.put(columnX, x);
        contentValues.put(columnY, y);

        db.insert(tableSchools, null, contentValues);
    }

    public void insertRec(final SQLiteDatabase db, String name, String x, String y) {

        ContentValues contentValues = new ContentValues();
        contentValues.put(columnName, name);
        contentValues.put(columnX, x);
        contentValues.put(columnY, y);

        db.insert(tableRecreation, null, contentValues);
    }

    public void insertBusStop(final SQLiteDatabase db, String name, String x, String y) {

        ContentValues contentValues = new ContentValues();
        contentValues.put(columnName, name);
        contentValues.put(columnX, x);
        contentValues.put(columnY, y);

        db.insert(tableBusStops, null, contentValues);
    }

    public void insertShop(final SQLiteDatabase db, String name, String x, String y) {

        ContentValues contentValues = new ContentValues();
        contentValues.put(columnName, name);
        contentValues.put(columnX, x);
        contentValues.put(columnY, y);

        db.insert(tableShopping, null, contentValues);
    }

    public void insertPark(final SQLiteDatabase db, String name, String coords) {

        ContentValues contentValues = new ContentValues();
        contentValues.put(columnName, name);
        contentValues.put(columnJSONCoord, coords);

        db.insert(tableParks, null, contentValues);
    }

    public void insertZone(final SQLiteDatabase db, String zoneType, String zoneCoords, String zoneDesc,
                           float centerLong, float centerLat) {

        ContentValues contentValues = new ContentValues();
        contentValues.put(columnZoneType, zoneType);
        contentValues.put(columnJSONCoord, zoneCoords);
        contentValues.put(columnDesc, zoneDesc);
        contentValues.put(columnCenterLong, centerLong);
        contentValues.put(columnCenterLat, centerLat);

        db.insert(tableZoning, null, contentValues);
    }

    public Cursor getAllSchools(SQLiteDatabase db) {
        Cursor cursor;

        cursor = db.query(tableSchools,
                null, null, null, null, null, null, null);
        cursor.moveToFirst();
        return cursor;
    }

    public Cursor getAllRec(SQLiteDatabase db) {
        Cursor cursor;

        cursor = db.query(tableRecreation,
                null, null, null, null, null, null, null);
        cursor.moveToFirst();
        return cursor;
    }

    public Cursor getAllBusStops(SQLiteDatabase db) {
        Cursor cursor;

        cursor = db.query(tableBusStops,
                null, null, null, null, null, null, null);
        cursor.moveToFirst();
        return cursor;
    }

    public Cursor getAllParks(SQLiteDatabase db) {
        Cursor cursor;

        cursor = db.query(tableParks,
                null, null, null, null, null, null, null);
        cursor.moveToFirst();
        return cursor;
    }

    public Cursor checkTableExists(SQLiteDatabase db) {
        Cursor cursor;
        String query = "SELECT name FROM sqlite_master WHERE type=?" +
                "AND name=?";

        cursor = db.rawQuery(query, new String[] {"table", tableZoning}, null);
        cursor.moveToFirst();
        return cursor;
    }

    public Cursor getAllShops(SQLiteDatabase db) {
        Cursor cursor;

        cursor = db.query(tableShopping,
                null, null, null, null, null, null, null);
        cursor.moveToFirst();
        return cursor;
    }

    public Cursor getZoneCoords(SQLiteDatabase db, String type) {
        Cursor cursor;

        cursor = db.query(tableZoning,
                null, "type=?",
                new String[] {type}, null, null, null, null);
        cursor.moveToFirst();
        return cursor;
    }

    public Cursor getAllZones(SQLiteDatabase db) {
        Cursor cursor;

        cursor = db.query(tableZoning,
                null, null, null, null, null, null, null);
        cursor.moveToFirst();
        return cursor;
    }

    public String createSchoolsTable() {
        String createTable = "CREATE TABLE IF NOT EXISTS "  + tableSchools + " ( " +
                columnID   + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                columnName + " TEXT NOT NULL, " +
                columnX + " TEXT NOT NULL, " +
                columnY + " TEXT NOT NULL)";
        return createTable;
    }

    public String createRecreationTable() {
        String createTable = "CREATE TABLE IF NOT EXISTS "  + tableRecreation + " ( " +
                columnID   + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                columnName + " TEXT NOT NULL, " +
                columnX + " TEXT NOT NULL, " +
                columnY + " TEXT NOT NULL)";
        return createTable;
    }

    public String createBusTable() {
        String createTable = "CREATE TABLE IF NOT EXISTS "  + tableBusStops + " ( " +
                columnID   + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                columnName + " TEXT NOT NULL, " +
                columnX + " TEXT NOT NULL, " +
                columnY + " TEXT NOT NULL)";
        return createTable;
    }

    public String createShoppingTable() {
        String createTable = "CREATE TABLE IF NOT EXISTS "  + tableShopping + " ( " +
                columnID   + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                columnName + " TEXT NOT NULL, " +
                columnX + " TEXT NOT NULL, " +
                columnY + " TEXT NOT NULL)";
        return createTable;
    }

    public String createParksTable() {
        String createTable = "CREATE TABLE IF NOT EXISTS "  + tableParks + " ( " +
                columnID   + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                columnName + " TEXT NOT NULL, " +
                columnJSONCoord + " TEXT NOT NULL)";
        return createTable;
    }

    public String createNeighbourhoodTable() {
        String createTable = "CREATE TABLE IF NOT EXISTS "  + tableZoning + " ( " +
                columnID   + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                columnZoneType + " TEXT NOT NULL, " +
                columnJSONCoord + " TEXT NOT NULL, " +
                columnDesc + " TEXT NOT NULL, " +
                columnCenterLong + " INTEGER NOT NULL, " +
                columnCenterLat + " INTEGER NOT NULL)";
        return createTable;
    }

}
