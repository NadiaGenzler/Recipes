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
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.recipes.Adapters.MyAdapter;
import com.example.recipes.Model.Meal;
import com.example.recipes.R;
import com.example.recipes.Utils.DatabaseHelper;
import com.example.recipes.Utils.GlobalVariable;
import com.example.recipes.Utils.NetworkConnection;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;


public class RecipesCategory extends Fragment implements MyAdapter.OnMealListener {
    DatabaseHelper databaseHelper;
    TextView categoryName, mealsCount;
    HashMap<String,String> categoryPics;
    RecyclerView recyclerView;
    MyAdapter myAdapter;
    List<Meal> meals;
    ImageView favBtn;

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
        //Internet check
        NetworkConnection networkConnection=new NetworkConnection(this.getContext(),getActivity());
        if(networkConnection.getInternetStatus()){
            Toast.makeText(this.getContext(), "Network is Availible", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this.getContext(), "Network is 0ff!!!!", Toast.LENGTH_SHORT).show();
        }


        View myView=inflater.inflate(R.layout.fragment_recipes_category, container, false);
        String currentCategory=RecipesCategoryArgs.fromBundle(getArguments()).getCtagoryName();

        databaseHelper=new DatabaseHelper(this.getContext());
        meals=new ArrayList<>();

        //Get current category pic from pics dictionary
        categoryImageDictionary();
        String imageCategoryUrl=categoryPics.get(currentCategory);
        ImageView categoryImageView=myView.findViewById(R.id.categoryImageView);
        Picasso.with(this.getContext()).load(imageCategoryUrl).into(categoryImageView);
        ImageView roundImageView=myView.findViewById(R.id.roundImageView);
        Picasso.with(this.getContext()).load(imageCategoryUrl).into(roundImageView);

        categoryName=myView.findViewById(R.id.categoryName);
        categoryName.setText(currentCategory);

        //Get the list from Database
        Cursor cursor=databaseHelper.getMealsFromCategory(currentCategory);
        cursor.moveToFirst();
        meals.add(new Meal(cursor.getString(1),cursor.getString(2),cursor.getString(3),cursor.getString(4)));
        while (cursor.moveToNext()){
            meals.add(new Meal(cursor.getString(1),cursor.getString(2),cursor.getString(3),cursor.getString(4)));
        }

        //myAdapter=new MyAdapter(meals,this.getContext(),RecipesCategory.this,R.layout.cell_recipe_category);

        recyclerView=myView.findViewById(R.id.recyclerCategory);
        recyclerView.setHasFixedSize(true);
        recyclerView.setClickable(true);
        //recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext(),RecyclerView.VERTICAL,false));
        //recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL));
        recyclerView.setAdapter(new MyAdapter(meals,this.getContext(),RecipesCategory.this,R.layout.cell_recipe_category));

        mealsCount=myView.findViewById(R.id.mealsCount);
        mealsCount.setText(cursor.getCount()+" Recipes");

        return myView;
    }

    //Category Description Images
    public void categoryImageDictionary(){
        categoryPics=new HashMap<>();
        categoryPics.put("Beef","https://images.pexels.com/photos/675951/pexels-photo-675951.jpeg?auto=compress&cs=tinysrgb&dpr=2&w=500");
        categoryPics.put("Breakfast","https://images.pexels.com/photos/2113556/pexels-photo-2113556.jpeg?auto=compress&cs=tinysrgb&dpr=2&w=500");
        categoryPics.put("Chicken","https://images.pexels.com/photos/1624487/pexels-photo-1624487.jpeg?auto=compress&cs=tinysrgb&dpr=2&w=500");
        categoryPics.put("Dessert","https://images.pexels.com/photos/1099680/pexels-photo-1099680.jpeg?auto=compress&cs=tinysrgb&dpr=2&w=500");
        categoryPics.put("Goat","https://images.pexels.com/photos/3906290/pexels-photo-3906290.jpeg?auto=compress&cs=tinysrgb&dpr=2&w=500");
        categoryPics.put("Lamb","https://images.pexels.com/photos/323682/pexels-photo-323682.jpeg?auto=compress&cs=tinysrgb&dpr=2&w=500");
        categoryPics.put("Pasta","https://images.pexels.com/photos/803963/pexels-photo-803963.jpeg?auto=compress&cs=tinysrgb&dpr=2&w=500");
        categoryPics.put("Pork","https://images.pexels.com/photos/416471/pexels-photo-416471.jpeg?auto=compress&cs=tinysrgb&dpr=2&w=500");
        categoryPics.put("Seafood","https://images.pexels.com/photos/566344/pexels-photo-566344.jpeg?auto=compress&cs=tinysrgb&dpr=2&w=500");
        categoryPics.put("Side","https://images.pexels.com/photos/2781540/pexels-photo-2781540.jpeg?auto=compress&cs=tinysrgb&dpr=2&w=500");
        categoryPics.put("Starter","https://images.pexels.com/photos/3669501/pexels-photo-3669501.jpeg?auto=compress&cs=tinysrgb&dpr=2&w=500");
        categoryPics.put("Vegan","https://images.pexels.com/photos/248509/pexels-photo-248509.jpeg?auto=compress&cs=tinysrgb&dpr=2&w=500");
        categoryPics.put("Vegetarian","https://images.pexels.com/photos/1143754/pexels-photo-1143754.jpeg?auto=compress&cs=tinysrgb&dpr=2&w=500");
        categoryPics.put("Miscellaneous","https://images.pexels.com/photos/3730946/pexels-photo-3730946.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=750&w=1260");
    }

    @Override
    public void onMealClick(View v, String mealId, int position) {
        NavDirections action=RecipesCategoryDirections.actionRecipesCategoryToMeal(mealId);
        Navigation.findNavController(v).navigate(action);
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
    public void onAddOrRemoveClick(View myView, String mealId, int position) {
        favBtn=myView.findViewById(R.id.favoriteBtn);
        if(databaseHelper.isFavorite(mealId)){
            databaseHelper.removeFromFavorites(mealId);
            favBtn.setImageResource(R.drawable.empty_heart);
            Toast.makeText(myView.getContext(),"Recipe was removed from favorites",Toast.LENGTH_LONG).show();
        }else {
            databaseHelper.addToFavorites(mealId);
            favBtn.setImageResource(R.drawable.full_heart);

            Toast.makeText(myView.getContext(),"Recipe was added to favorites",Toast.LENGTH_LONG).show();
        }
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
