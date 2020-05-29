package com.nadia.recipes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;

import com.nadia.recipes.Fragments.AllCategories;
import com.nadia.recipes.Fragments.FavoriteMeals;
import com.nadia.recipes.Fragments.MainPage;
import com.nadia.recipes.Fragments.MealFragment;
import com.nadia.recipes.Fragments.RecipesCategory;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener , MainPage.OnFragmentInteractionListener,
        MealFragment.OnFragmentInteractionListener, RecipesCategory.OnFragmentInteractionListener, FavoriteMeals.OnFragmentInteractionListener , AllCategories.OnFragmentInteractionListener {

    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView=findViewById(R.id.bottom_nav_menu);

        NavHostFragment navHostFragment= (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        NavigationUI.setupWithNavController(bottomNavigationView,navHostFragment.getNavController());


    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {


        return false;
    }

    @Override
    public void onBackPressed() {

        }
    }


