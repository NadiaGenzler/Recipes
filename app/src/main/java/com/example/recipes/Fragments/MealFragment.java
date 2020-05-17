package com.example.recipes.Fragments;

import android.app.Activity;
import android.content.Context;

import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.preference.PreferenceManager;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Path;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Layout;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.recipes.Fragments.MealFragmentArgs;
import com.example.recipes.MainActivity;
import com.example.recipes.Model.Meal;
import com.example.recipes.Model.MealsObj;
import com.example.recipes.R;
import com.example.recipes.Utils.DatabaseHelper;
import com.example.recipes.Utils.GlobalVariable;
import com.example.recipes.Utils.NetworkConnection;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.prefs.Preferences;
import java.util.prefs.PreferencesFactory;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static android.content.ContentValues.TAG;


public class MealFragment extends Fragment {

    MealsObj meals;
    Meal currentMeal;
    TextView mealName, categoryName, instructions;
    ImageView mealImage;
    ImageView favBtn;
    ImageButton toVideo;
   // Set<String> favoriteMeals;
    private TableLayout ingredientsTable;

    private OnFragmentInteractionListener mListener;

    public MealFragment() {
        // Required empty public constructor
    }

    public static MealFragment newInstance(String param1, String param2) {
        MealFragment fragment = new MealFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        //Internet check
        NetworkConnection networkConnection=new NetworkConnection(this.getContext(),getActivity());
        if(networkConnection.getInternetStatus()){
            Toast.makeText(this.getContext(), "Network is Availible", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this.getContext(), "Network is 0ff!!!!", Toast.LENGTH_SHORT).show();
        }


        View myView=inflater.inflate(R.layout.fragment_meal, container, false);
        String mealId= MealFragmentArgs.fromBundle(getArguments()).getMealId();
        ingredientsTable=myView.findViewById(R.id.ingredientsTable);

        DatabaseHelper databaseHelper=new DatabaseHelper(this.getContext());
        //Fetch meal from JSON
        getTheMeal(this.getContext(),myView,mealId);

        //Add to favorites
        addToFavorites(myView,mealId,databaseHelper);

        favBtn=myView.findViewById(R.id.favoriteBtn);
        if(databaseHelper.isFavorite(mealId)){
            favBtn.setImageResource(R.drawable.full_heart);
        }else {
            favBtn.setImageResource(R.drawable.empty_heart);
        }




        return myView;
    }

    public void addToFavorites(final View myView, final String mealId,final DatabaseHelper databaseHelper){

        final ImageView favBtn=myView.findViewById(R.id.favoriteBtn);
        favBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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
        });
    }


    public void getTheMeal(final Context context,final View myView,final String mealId){

        meals = new MealsObj();
        String url = "https://www.themealdb.com/api/json/v1/1/lookup.php?i="+mealId;
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
                    currentMeal=meals.meals.get(0);

                    ((MainActivity) getContext()).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            mealName=myView.findViewById(R.id.currentMealName);
                            mealName.setText(currentMeal.strMeal);
                            mealImage=myView.findViewById(R.id.currentMealImage);
                            Picasso.with(context).load(currentMeal.strMealThumb).into(mealImage);

                            videoInstructions(myView);

                            currentMeal.addIngredients();
                            currentMeal.addMeasurs();
                            for (int i = 0; i < 20; i++) {
                                if(!(currentMeal.ingredientsArr[i].trim()).isEmpty()||!(currentMeal.measuresArr[i].trim()).isEmpty()){
                                    showIngredientsAndMeasures(context,i);
                                }else {
                                    break;
                                }
                            }
                            categoryName=myView.findViewById(R.id.categoryName);
                            categoryName.setText("See more from "+currentMeal.strCategory+" category");
                            categoryName.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    NavDirections action= MealFragmentDirections.actionMealToRecipesCategory(currentMeal.strCategory);
                                    Navigation.findNavController(myView).navigate(action);
                                }
                            });

                            instructions=myView.findViewById(R.id.instructions);
                            instructions.setText(currentMeal.strInstructions);


                        }
                    });

                {
                        //databaseHelper.insertData(categoryName, meal.idMeal, meal.strMeal, meal.strMealThumb);
                    }
                }
            }
        });
    }


    // Load the ingredients and measures to the view
    public void showIngredientsAndMeasures(final Context context,final int i){
        TableRow row=new TableRow(context);
        TableLayout.LayoutParams tablelayoutParams=new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT,TableLayout.LayoutParams.MATCH_PARENT);
        row.setLayoutParams(tablelayoutParams);
        row.setPadding(0,0,0,5);

        int half= (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,180,context.getResources().getDisplayMetrics());

        //Add Ingredients to table
        TextView ingredient=new TextView(context);
        ingredient.setText(currentMeal.ingredientsArr[i].trim().substring(0,1).toUpperCase()+currentMeal.ingredientsArr[i].trim().substring(1)+"");
        ingredient.setLayoutParams(new TableRow.LayoutParams(half,TableRow.LayoutParams.WRAP_CONTENT));
        ingredient.setTextSize(22);
        row.addView(ingredient,0);
        //Add Measures to the table
        TextView measure=new TextView(context);
        measure.setText(currentMeal.measuresArr[i].trim());
        measure.setLayoutParams(new TableRow.LayoutParams(half,TableRow.LayoutParams.WRAP_CONTENT));
        measure.setTextColor(getResources().getColor(R.color.black));
     //   measure.setTextAlignment(View.TEXT_ALIGNMENT_);
        measure.setTextSize(20);
        row.addView(measure,1);

        ingredientsTable.addView(row);

        //Add separation line
        View line=new View(context);
        int dp1= (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,1,context.getResources().getDisplayMetrics());
        line.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, dp1));
        line.setBackgroundColor(getResources().getColor(R.color.gray));
        ingredientsTable.addView(line);
    }

    // Load the video instructions if exists
    public void videoInstructions(View myView){
        final String url=currentMeal.strYoutube;
        toVideo=myView.findViewById(R.id.videoImageBtn);
        if (!url.isEmpty()){
            toVideo.setVisibility(View.VISIBLE);
            toVideo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    WebView webView=new WebView(MealFragment.this.getContext());
                    webView.loadUrl(url);
                }
            });
        }
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

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
