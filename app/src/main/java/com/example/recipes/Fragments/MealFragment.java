package com.example.recipes.Fragments;

import android.app.Activity;
import android.content.Context;
import androidx.preference.PreferenceManager;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

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
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
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
    TextView mealName, instructions;
    ImageView mealImage;
    ImageView favBtn;
    ImageButton toVideo;
   // Set<String> favoriteMeals;
    private TableLayout ingredientsTable;
    private DatabaseHelper databaseHelper;
    boolean favorite;
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
        View myView=inflater.inflate(R.layout.fragment_meal, container, false);
        String mealId= MealFragmentArgs.fromBundle(getArguments()).getMealId();
        ingredientsTable=myView.findViewById(R.id.ingredientsTable);

        //Fetch meal from JSON
        getTheMeal(this.getContext(),myView,mealId);

        favBtn=myView.findViewById(R.id.favoriteBtn);
        databaseHelper=new DatabaseHelper(this.getContext());
        if(databaseHelper.isFavorite(mealId)){
            favBtn.setImageResource(R.drawable.full_heart);
        }else {
            favBtn.setImageResource(R.drawable.empty_heart);
        }

        //Add to favorites
        addToFavorites(myView,mealId);


        return myView;
    }

    public void addToFavorites(final View myView, final String mealId){

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

        getFavorites();
            }
        });
    }

    public void getFavorites(){

       Cursor cursor=databaseHelper.getAllFavorites();
        List<String > fav=new ArrayList<>();
        Log.e(TAG, cursor.getCount()+"" );

        cursor.moveToFirst();
        for (int i = 0; i < cursor.getCount(); i++) {
            fav.add(cursor.getString(0));
            cursor.moveToNext();
        }

        Log.e(TAG, fav+"" );
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
                                if((currentMeal.ingredientsArr[i].trim())!=null||(currentMeal.measuresArr[i].trim())!=null){
                                    showIngredientsAndMeasures(context,i);
                                }else {
                                    break;
                                }
                            }

                            instructions=myView.findViewById(R.id.instructions);
                            instructions.setText(currentMeal.strInstructions);

                        }
                    });

                {
                        //db.insertData(categoryName, meal.idMeal, meal.strMeal, meal.strMealThumb);
                    }
                }
            }
        });
    }


    // Load the ingredients and measures to the view
    public void showIngredientsAndMeasures(final Context context,final int i){
        TableRow row=new TableRow(context);
        TableLayout.LayoutParams TablelayoutParams=new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT,TableLayout.LayoutParams.MATCH_PARENT);
        row.setLayoutParams(TablelayoutParams);
        row.setPadding(0,0,0,5);

        //Add Ingredients to table
        TextView ingredient=new TextView(context);
        ingredient.setText(currentMeal.ingredientsArr[i]);
        ingredient.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,TableRow.LayoutParams.WRAP_CONTENT,0.5f));
        ingredient.setTextSize(22);
        row.addView(ingredient,0);
        //Add Measures to the table
        TextView measure=new TextView(context);
        measure.setText(currentMeal.measuresArr[i]);
        measure.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,TableRow.LayoutParams.WRAP_CONTENT,0.5f));
        measure.setTextColor(getResources().getColor(R.color.black));
        measure.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
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
