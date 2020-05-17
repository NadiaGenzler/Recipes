package com.example.recipes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.recipes.Fragments.AllCategories;
import com.example.recipes.Fragments.FavoriteMeals;
import com.example.recipes.Fragments.MainPage;
import com.example.recipes.Fragments.MealFragment;
import com.example.recipes.Fragments.RecipesCategory;
import com.example.recipes.Utils.NetworkConnection;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener , MainPage.OnFragmentInteractionListener,
        MealFragment.OnFragmentInteractionListener, RecipesCategory.OnFragmentInteractionListener, FavoriteMeals.OnFragmentInteractionListener , AllCategories.OnFragmentInteractionListener {

    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Internet check
//        NetworkConnection networkConnection=new NetworkConnection(getApplicationContext(),this);
//        networkConnection.getInternetStatus();
//        if(networkConnection.getInternetStatus()){
//            Toast.makeText(getApplicationContext(), "Network is Availible", Toast.LENGTH_SHORT).show();
//        }else{
//            Toast.makeText(getApplicationContext(), "Network is 0ff!!!!", Toast.LENGTH_SHORT).show();
//        }
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
}
