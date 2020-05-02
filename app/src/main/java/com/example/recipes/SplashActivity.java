package com.example.recipes;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;

import com.example.recipes.Adapters.MyAdapter;
import com.example.recipes.Fragments.MainPage;
import com.example.recipes.Model.Meal;
import com.example.recipes.Model.MealsObj;
import com.example.recipes.Utils.DatabaseHelper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class SplashActivity extends AppCompatActivity {
    private static int TIME_OUT_SPLASH=2000;

    String [] categories={"Beef", "Breakfast", "Chicken","Dessert","Goat","Lamb",
            "Pasta","Pork","Seafood","Side","Starter","Vegan","Vegetarian","Miscellaneous"};
    MealsObj meals;
    DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        db=new DatabaseHelper(this);
       // db.deleteAllData();

        Cursor cursor = db.getAllMeals();
        if (cursor.getCount() == 0){
            for (int i = 0; i < categories.length; i++) {
                getAllMeals(categories[i]);
            }
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        },TIME_OUT_SPLASH);

    }

    public void getAllMeals(final String categoryName){

        meals = new MealsObj();
        String url = "https://www.themealdb.com/api/json/v1/1/filter.php?c="+categoryName;
        final OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(url).build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String serverResponse = response.body().string();
                if (response.isSuccessful()) {

                    GsonBuilder gsonBuilder = new GsonBuilder();
                    Gson gson = gsonBuilder.create();

                    meals = gson.fromJson(serverResponse, MealsObj.class);
                    for(Meal meal:meals.meals) {
                        db.insertData(categoryName, meal.idMeal, meal.strMeal, meal.strMealThumb);
                    }
                }
            }
        });
    }
}
