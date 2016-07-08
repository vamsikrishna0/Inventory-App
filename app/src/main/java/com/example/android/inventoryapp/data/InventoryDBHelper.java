package com.example.android.inventoryapp.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Vamsi on 7/6/2016.
 */
public class InventoryDBHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 2;

    static final String DATABASE_NAME = "inventory.db";

    public InventoryDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String SQL_CREATE_INVENTORY_TABLE = "CREATE TABLE " + InventoryContract.InventoryEntry.TABLE_NAME + " (" +
                InventoryContract.InventoryEntry._ID + " INTEGER PRIMARY KEY," +
                InventoryContract.InventoryEntry.COLUMN_PRODUCT_NAME + " TEXT UNIQUE NOT NULL, " +
                InventoryContract.InventoryEntry.COLUMN_QUANTITY + " INTEGER NOT NULL, " +
                InventoryContract.InventoryEntry.COLUMN_PRICE + " INTEGER NOT NULL, " +
                InventoryContract.InventoryEntry.COLUMN_PICTURE + " TEXT , " +

                InventoryContract.InventoryEntry.COLUMN_SUPPLIER + " TEXT NOT NULL "+
                " );";

        sqLiteDatabase.execSQL(SQL_CREATE_INVENTORY_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        onCreate(sqLiteDatabase);
    }

    public boolean deleteDatabase(Context context) {
        return context.deleteDatabase(DATABASE_NAME);
    }

    public Cursor readProductDetails(long id){
        SQLiteDatabase db = this.getReadableDatabase();

        String[] whereArgs = new String[]{""+id};
        Cursor cursor = db.query(InventoryContract.InventoryEntry.TABLE_NAME, null,
                InventoryContract.InventoryEntry._ID + "= ?",whereArgs ,null,null,null);
        return cursor;
    }


    public boolean insertProduct(String productName, int quantity, int price, String picture, String supplier) {
        ContentValues values = new ContentValues();

        SQLiteDatabase db = this.getReadableDatabase();
        values.put(InventoryContract.InventoryEntry.COLUMN_PICTURE, picture);
        values.put(InventoryContract.InventoryEntry.COLUMN_PRODUCT_NAME, productName);
        values.put(InventoryContract.InventoryEntry.COLUMN_PRICE, price);
        values.put(InventoryContract.InventoryEntry.COLUMN_SUPPLIER, supplier);
        values.put(InventoryContract.InventoryEntry.COLUMN_QUANTITY, quantity);

        return db.insert(InventoryContract.InventoryEntry.TABLE_NAME,null, values) > 0;
    }

    public boolean updateQuantity(int id, int quantity) {

        ContentValues values = new ContentValues();
        values.put(InventoryContract.InventoryEntry.COLUMN_QUANTITY, quantity);
        SQLiteDatabase db = this.getWritableDatabase();
        return db.update(InventoryContract.InventoryEntry.TABLE_NAME, values,InventoryContract.InventoryEntry._ID +"=" +id, null) > 0;
    }

    public int getQuantity(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        String[] whereArgs = new String[]{""+id};
        String[] columns = new String[]{InventoryContract.InventoryEntry.COLUMN_QUANTITY};
        Cursor cursor = db.query(InventoryContract.InventoryEntry.TABLE_NAME, columns,
                InventoryContract.InventoryEntry._ID + "= ?",whereArgs ,null,null,null);
        cursor.moveToFirst();
        return cursor.getInt(0);

    }
    public boolean deleteProduct(int id) {
        return this.getWritableDatabase().delete(InventoryContract.InventoryEntry.TABLE_NAME,
                InventoryContract.InventoryEntry._ID + "=" + id, null) > 0;
    }
    public Cursor readAll() {
        return this.getReadableDatabase().query(InventoryContract.InventoryEntry.TABLE_NAME, null,null, null, null, null, null);
    }
}
