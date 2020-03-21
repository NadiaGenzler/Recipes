package com.example.recipes.Utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "meals_db.db";
    public static final String TABLE_NAME = "MEALS_WITH_CATEGORIES";
    public static final String COL_1_ID = "ID";
    public static final String COL_2_CATEGORY = "CATEGORY";
    public static final String COL_3_MEAL_ID = "MEAL_ID";
    public static final String COL_4_NAME = "NAME";
    public static final String COL_5_IMAGE = "IMAGE";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("create table " + TABLE_NAME +
                " (ID INTEGER PRIMARY KEY AUTOINCREMENT, CATEGORY TEXT, MEAL_ID TEXT, NAME TEXT,IMAGE TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " +TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    public boolean insertData(String category, String mealId, String name,String image)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2_CATEGORY, category);
        contentValues.put(COL_3_MEAL_ID, mealId);
        contentValues.put(COL_4_NAME, name);
        contentValues.put(COL_5_IMAGE, image);
        long result = db.insert(TABLE_NAME, null, contentValues);
        if (result == -1)
            return false;
        return true;
    }


    public Cursor getAllMeals()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor response = db.rawQuery("select * from " + TABLE_NAME, null);
        return response;
    }


    public Cursor getMealsFromCategory(String category)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor response = db.rawQuery("select * from " + TABLE_NAME +" where CATEGORY = '"+category+"'" , null);
        return response;
    }

    public Cursor getMealByName(String mealName){
        SQLiteDatabase db=this.getWritableDatabase();
        Cursor response=db.rawQuery("select * from"+TABLE_NAME+"where NAME ='"+mealName+"'",null);
        return response;
    }

//    public Integer deleteData(String name)
//    {
//        SQLiteDatabase db = this.getWritableDatabase();
//        return db.delete(TABLE_NAME, "NAME = ?",new String[] {name});
//    }
    public Integer deleteAllData()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME,"",null);
    }
}
