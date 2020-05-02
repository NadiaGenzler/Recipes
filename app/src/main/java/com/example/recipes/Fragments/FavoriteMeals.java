package com.example.recipes.Fragments;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.recipes.Adapters.MyAdapter;
import com.example.recipes.Model.Meal;
import com.example.recipes.R;
import com.example.recipes.Utils.DatabaseHelper;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

public class FavoriteMeals extends Fragment implements MyAdapter.OnMealListener{

     DatabaseHelper databaseHelper;
    RecyclerView recyclerView;
    List<Meal > favRecipes;
    MyAdapter myAdapter;

    private OnFragmentInteractionListener mListener;

    public FavoriteMeals() {
        // Required empty public constructor
    }

    public static FavoriteMeals newInstance(String param1, String param2) {
        FavoriteMeals fragment = new FavoriteMeals();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View myView=inflater.inflate(R.layout.fragment_favorite_meals, container, false);
        databaseHelper=new DatabaseHelper(this.getContext());

        getFavorites(myView);

        return myView;
    }

    public void getFavorites(View myView){

        Cursor cursorGetId=databaseHelper.getAllFavorites();
        List <String> favsId=new ArrayList<>();
        cursorGetId.moveToFirst();
        for (int i = 0; i < cursorGetId.getCount(); i++) {
            favsId.add(cursorGetId.getString(0));
            cursorGetId.moveToNext();
        }

        favRecipes=new ArrayList<>();
        for (String mealId:favsId) {
            Cursor cursorGetRecipe=databaseHelper.getMealById(mealId);
            cursorGetRecipe.moveToFirst();
            favRecipes.add(new Meal(cursorGetRecipe.getString(1),cursorGetRecipe.getString(2),cursorGetRecipe.getString(3),cursorGetRecipe.getString(4)));
        }

        recyclerView=myView.findViewById(R.id.favorites);
        recyclerView.setHasFixedSize(true);
        recyclerView.setClickable(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext(),RecyclerView.VERTICAL,false));

        myAdapter=new MyAdapter(favRecipes,this.getContext(),FavoriteMeals.this,R.layout.cell_favorites);
        recyclerView.setAdapter(myAdapter);

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onMealClick(View myView, String mealId, int position) {
        NavDirections action=FavoriteMealsDirections.actionFavoriteMealsToMeal(mealId);
        Navigation.findNavController(myView).navigate(action);
    }

    @Override
    public void onFavoriteClick(View myView, String mealId, int position) {
        databaseHelper.removeFromFavorites(mealId);
        Toast.makeText(myView.getContext(),"Recipe was removed from favorites",Toast.LENGTH_LONG).show();
        favRecipes.remove(position);
        myAdapter.notifyItemRemoved(position);

    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
