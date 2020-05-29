package com.nadia.recipes.Fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.Toast;

import com.nadia.recipes.Adapters.MyAdapter;
import com.nadia.recipes.Model.Meal;
import com.nadia.recipes.R;
import com.nadia.recipes.Utils.DatabaseHelper;
import com.nadia.recipes.Utils.NetworkConnection;

import java.util.ArrayList;
import java.util.List;

public class FavoriteMeals extends Fragment implements MyAdapter.OnMealListener{

     DatabaseHelper databaseHelper;
    RecyclerView recyclerView;
    List<Meal> favRecipes;
    MyAdapter myAdapter;
    TextView message;
    View myMainView;
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
        //Internet check
        NetworkConnection networkConnection=new NetworkConnection(this.getContext(),getActivity());
        networkConnection.getInternetStatus();


        myMainView=inflater.inflate(R.layout.fragment_favorite_meals, container, false);
        databaseHelper=new DatabaseHelper(this.getContext());

        getFavorites(myMainView);

        return myMainView;
    }

    public void getFavorites(View myMainView){

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
        if(favRecipes.size()==0){
            message=myMainView.findViewById(R.id.noRecipesMessage);
            message.setVisibility(TextView.VISIBLE);
        }

        recyclerView=myMainView.findViewById(R.id.favorites);
        recyclerView.setHasFixedSize(true);
        recyclerView.setClickable(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext(),RecyclerView.VERTICAL,false));

        myAdapter=new MyAdapter(favRecipes,this.getContext(),FavoriteMeals.this,R.layout.cell_favorites);
        recyclerView.setAdapter(myAdapter);

    }
    @Override
    public void onStart() {
        super.onStart();
        NetworkConnection networkConnection=new NetworkConnection(this.getContext(),getActivity());
        networkConnection.getInternetStatus();
    }


    @Override
    public void onMealClick(View myView, String mealId, int position) {
        NavDirections action= FavoriteMealsDirections.actionFavoriteMealsToMeal(mealId);
        Navigation.findNavController(myView).navigate(action);
    }

    @Override
    public void onAddOrRemoveClick(View myView, String mealId, int position) {

        alertBeforeRemoval(myView,mealId,position);

    }

    public void alertBeforeRemoval(final View myView,final String mealId,final int position){
        AlertDialog.Builder builder=new AlertDialog.Builder(this.getContext());
        builder.setMessage(R.string.alertDeleteMessage);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                databaseHelper.removeFromFavorites(mealId);
                Toast.makeText(myView.getContext(),"Recipe was removed from favorites",Toast.LENGTH_LONG).show();
                favRecipes.remove(position);
                myAdapter.notifyItemRemoved(position);

                if(favRecipes.size()==0){
                    message=myMainView.findViewById(R.id.noRecipesMessage);
                    message.setVisibility(TextView.VISIBLE);
                }

            }
        });
        builder.setNegativeButton("Cancle", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
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

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
