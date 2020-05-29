package com.nadia.recipes.Utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "meals_db.db";
    public static final String MEALS_TABLE = "MEALS_WITH_CATEGORIES";
    public static final String COL_1_ID = "ID";
    public static final String COL_2_CATEGORY = "CATEGORY";
    public static final String COL_3_MEAL_ID = "MEAL_ID";
    public static final String COL_4_NAME = "NAME";
    public static final String COL_5_IMAGE = "IMAGE";
    public static final String FAVORITE_TABLE = "FAVORITE_MEALS";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("create table " + MEALS_TABLE +
                " (ID INTEGER PRIMARY KEY AUTOINCREMENT, CATEGORY TEXT, MEAL_ID TEXT, NAME TEXT,IMAGE TEXT)");
        sqLiteDatabase.execSQL("create table " + FAVORITE_TABLE + " (MEAL_ID TEXT, NAME TEXT,IMAGE TEXT) ");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " +MEALS_TABLE);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " +FAVORITE_TABLE);
        onCreate(sqLiteDatabase);
    }

//    public void deleteTable(){
//        SQLiteDatabase sqLiteDatabase=this.getWritableDatabase();
//        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " +FAVORITE_TABLE);
//    }
//    public void addNewTable(){
//        SQLiteDatabase sqLiteDatabase=this.getWritableDatabase();
//        sqLiteDatabase.execSQL("create table " + FAVORITE_TABLE + " (MEAL_ID TEXT, NAME TEXT,IMAGE TEXT) ");
//    }

    public boolean insertData(String category, String mealId, String name,String image) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2_CATEGORY, category);
        contentValues.put(COL_3_MEAL_ID, mealId);
        contentValues.put(COL_4_NAME, name);
        contentValues.put(COL_5_IMAGE, image);
        long result = db.insert(MEALS_TABLE, null, contentValues);
        if (result == -1)
            return false;
        return true;
    }


    public Cursor getAllMeals(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor response = db.rawQuery("select * from " + MEALS_TABLE, null);
        return response;
    }


    public Cursor getMealsFromCategory(String category){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor response = db.rawQuery("select * from " + MEALS_TABLE +" where CATEGORY = '"+category+"'" , null);
        return response;
    }

    public Cursor getMealById(String mealId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor response = db.rawQuery("select * from " + MEALS_TABLE +" where MEAL_ID = '"+mealId+"'" , null);
        return response;
    }

    public boolean addToFavorites(String mealId){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put(COL_3_MEAL_ID,mealId);
//        contentValues.put(COL_4_NAME, name);
//        contentValues.put(COL_5_IMAGE, image);
        long result = db.insert(FAVORITE_TABLE,null, contentValues);
        if (result == -1)
            return false;
        return true;
    }

    public Integer removeFromFavorites(String mealId){
        SQLiteDatabase db = getWritableDatabase();
        return db.delete(FAVORITE_TABLE, "MEAL_ID = ?",new String[] {mealId});
    }

    public Cursor getAllFavorites(){
        SQLiteDatabase db = getWritableDatabase();
        Cursor response=db.rawQuery("select * from " + FAVORITE_TABLE,null);
        return response;
    }

    public boolean isFavorite(String mealId){
        SQLiteDatabase db = getWritableDatabase();
        Cursor response=db.rawQuery( " select * from " + FAVORITE_TABLE + " where MEAL_ID= '"+mealId+"'", null);
        if(response.getCount()<=0){
            response.close();
            return false;
        }
        response.close();
        return true;
    }
//    public Cursor getMealByName(String mealName){
//        SQLiteDatabase db=this.getWritableDatabase();
//        Cursor response=db.rawQuery("select * from"+MEALS_TABLE+"where NAME ='"+mealName+"'",null);
//        return response;
//    }

//    public Integer deleteData(String name)
//    {
//        SQLiteDatabase db = this.getWritableDatabase();
//        return db.delete(TABLE_NAME, "NAME = ?",new String[] {name});
//    }
    public Integer deleteAllData()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(MEALS_TABLE,"",null);
    }
public Integer deleteAllFavs()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(FAVORITE_TABLE,"",null);
    }
}
