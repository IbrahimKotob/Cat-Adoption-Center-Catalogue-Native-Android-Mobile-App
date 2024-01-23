package com.example.cattoappo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "cat_database.db";
    private static final int DATABASE_VERSION = 2;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableQuery = "CREATE TABLE IF NOT EXISTS CatTable (" +
                "cat_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "cat_name TEXT," +
                "breed TEXT," +
                "fur_length TEXT," +
                "personality TEXT," +
                "causes_allergy INTEGER," +
                "image_path TEXT)";

        db.execSQL(createTableQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    public long insertCat(String catName, String breed, String furLength, String personality, int causesAllergy, String imagePath) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("cat_name", catName);
        values.put("breed", breed);
        values.put("fur_length", furLength);
        values.put("personality", personality);
        values.put("causes_allergy", causesAllergy);
        values.put("image_path", imagePath); // Add the image_path value
        return db.insert("CatTable", null, values);
    }

    public Cursor getCats() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM CatTable", null);
    }

    public int updateCat(int catId, String catName, String breed, String furLength, String personality, int causesAllergy, String imagePath) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("cat_name", catName);
        values.put("breed", breed);
        values.put("fur_length", furLength);
        values.put("personality", personality);
        values.put("causes_allergy", causesAllergy);
        values.put("image_path", imagePath); // Update the image_path value
        return db.update("CatTable", values, "cat_id=?", new String[]{String.valueOf(catId)});
    }

    public int deleteCat(int catId) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("CatTable", "cat_id=?", new String[]{String.valueOf(catId)});
    }
    public Cursor getCatDetails(int catId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] projection = {"cat_id", "cat_name", "breed", "fur_length", "personality", "causes_allergy", "image_path"};
        String selection = "cat_id=?";
        String[] selectionArgs = {String.valueOf(catId)};
        return db.query("CatTable", projection, selection, selectionArgs, null, null, null);
    }
    public Cursor getCatsByBreed(String breed) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] projection = {"cat_id", "cat_name", "breed", "fur_length", "personality", "causes_allergy", "image_path"};
        String selection = "breed=?";
        String[] selectionArgs = {breed};
        return db.query("CatTable", projection, selection, selectionArgs, null, null, null);
    }



}
