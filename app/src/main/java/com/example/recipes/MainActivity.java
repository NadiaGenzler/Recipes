package com.example.recipes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.recipes.Fragments.FavoriteMeals;
import com.example.recipes.Fragments.MainPage;
import com.example.recipes.Fragments.MealFragment;
import com.example.recipes.Fragments.RecipesCategory;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener , MainPage.OnFragmentInteractionListener,
        MealFragment.OnFragmentInteractionListener, RecipesCategory.OnFragmentInteractionListener, FavoriteMeals.OnFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        return false;
    }
}
