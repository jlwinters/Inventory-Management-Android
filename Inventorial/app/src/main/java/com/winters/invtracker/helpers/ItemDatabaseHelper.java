package com.winters.invtracker.helpers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.winters.invtracker.classes.Item;
import java.util.ArrayList;
import java.util.List;

public class ItemDatabaseHelper extends SQLiteOpenHelper {

    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "4.db";

    private static ItemDatabaseHelper mInvDb;

    public static ItemDatabaseHelper getInstance(Context context) {
        if (mInvDb == null) {
            mInvDb = new ItemDatabaseHelper(context);
        }
        return mInvDb;
    }

    public ItemDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    private static final String ITEM_TABLE_NAME = "ItemTable";
    private static final String COL_ID = "id";
    private static final String COL_TEXT = "text";
    private static final String COL_QUANTITY = "quantity";

    /**
     * Create the item database
     */
    private static final String CREATE_ITEM_TABLE = "CREATE TABLE IF NOT EXISTS " +
            ITEM_TABLE_NAME + " (" +
            COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
            COL_TEXT + " VARCHAR, " +
            COL_QUANTITY + " VARCHAR" + ");";

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_ITEM_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists " + ITEM_TABLE_NAME);
        onCreate(db);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        if (!db.isReadOnly()) {
            // Enable foreign key constraints
            db.setForeignKeyConstraintsEnabled(true);
        }
    }

    // Return a List<ItemEntry> of all items
    public List<Item> getItems() {
        List<Item> itemList = new ArrayList<>();

        String selectQuery = "SELECT * FROM " + ITEM_TABLE_NAME;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Item item = new Item();
                item.setId(Integer.parseInt(cursor.getString(0)));
                item.setText(cursor.getString(1));
                item.setQuantity(cursor.getString(2));

                itemList.add(item);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return itemList;
    }

    // UPDATE ItemEntry
    public int updateItem(Item item) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COL_ID, item.getId());
        values.put(COL_TEXT, item.getText());
        values.put(COL_QUANTITY, item.getQuantity());

        return db.update(ITEM_TABLE_NAME, values, COL_ID + " = ?", new String[] { String.valueOf(item.getId()) });
    }

    public void addItem(Item item) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_TEXT, item.getText());
        values.put(COL_QUANTITY, item.getQuantity());
        db.insert(ITEM_TABLE_NAME, null, values);
        db.close();
    }

    public void deleteItem(Item item) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(ITEM_TABLE_NAME,
                COL_TEXT + " = ?", new String[] { item.getText() });
    }
}