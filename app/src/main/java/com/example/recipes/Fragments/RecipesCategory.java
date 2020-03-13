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

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.recipes.Adapters.MyAdapter;
import com.example.recipes.Model.Meal;
import com.example.recipes.R;
import com.example.recipes.Utils.DatabaseHelper;

import java.util.ArrayList;
import java.util.List;


public class RecipesCategory extends Fragment implements MyAdapter.OnMealListener {
    DatabaseHelper databaseHelper;

    TextView textViewCategory;
    RecyclerView recyclerView;
    MyAdapter myAdapter;
    List<Meal> meals;

    private OnFragmentInteractionListener mListener;

    public RecipesCategory() {
        // Required empty public constructor
    }


    public static RecipesCategory newInstance(String param1, String param2) {
        RecipesCategory fragment = new RecipesCategory();
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
        View myView=inflater.inflate(R.layout.fragment_recipes_category, container, false);
        databaseHelper=new DatabaseHelper(this.getContext());
        meals=new ArrayList<>();

        recyclerView=myView.findViewById(R.id.recyclerCategory);
        recyclerView.setHasFixedSize(true);
        recyclerView.setClickable(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext(),RecyclerView.VERTICAL,false));

        String category=RecipesCategoryArgs.fromBundle(getArguments()).getCtagoryName();

        Cursor cursor=databaseHelper.getMealsFromCategory(category);
        cursor.moveToFirst();
        meals.add(new Meal(cursor.getString(2),cursor.getString(3),cursor.getString(4)));
        while (cursor.moveToNext()){
            meals.add(new Meal(cursor.getString(2),cursor.getString(3),cursor.getString(4)));
        }

        myAdapter=new MyAdapter(meals,this.getContext(),RecipesCategory.this,R.layout.cell_recipe_category);
        recyclerView.setAdapter(myAdapter);

       // textViewCategory=myView.findViewById(R.id.categoryName);
       // textViewCategory.setText(category);






        return myView;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
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
    public void onMealClick(View v, String mealId, int position) {
        NavDirections action=RecipesCategoryDirections.actionRecipesCategoryToMeal(mealId);
        Navigation.findNavController(v).navigate(action);
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
