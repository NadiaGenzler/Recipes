package com.example.recipes.Fragments;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.recipes.MainActivity;
import com.example.recipes.Model.Meal;
import com.example.recipes.Model.MealsObj;
import com.example.recipes.Adapters.MyAdapter;
import com.example.recipes.R;
import com.example.recipes.Utils.DatabaseHelper;
import com.example.recipes.Utils.NetworkConnection;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import okhttp3.OkHttpClient;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;

import static android.content.ContentValues.TAG;


public class MainPage extends Fragment implements MyAdapter.OnMealListener {
    //Search
    AutoCompleteTextView search;
    ArrayList allMealsName;
    HashMap <String,String > mealsNameIdDictionary;
    TextView name;

    //Top random meal
    Meal randomMeal;
    TextView randomMealTV;
    ImageView randomMealImage;
    String randomMealId;

    //3 categories with meals
    String [] categories={"Beef","Breakfast","Chicken","Dessert","Lamb","Pasta","Pork","Seafood","Side","Starter","Vegan","Vegetarian","Miscellaneous"};
    RecyclerView recyclerView;
    MyAdapter myAdapter;
    ImageView favBtn;

    MealsObj meal;
    List<Meal> meals0,meals1,meals2;
    TextView categoryNameTV;

    DatabaseHelper databaseHelper;



    private OnFragmentInteractionListener mListener;


    public MainPage() {
        // Required empty public constructor
    }
    public static MainPage newInstance() {
        MainPage fragment = new MainPage();
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

        //Inflate fragment view
        final View myView=inflater.inflate(R.layout.fragment_main_page, container, false);
        databaseHelper =new DatabaseHelper(this.getContext());


        //Search Recipes
        searchDropdown(myView);
        searchResult(myView);
        search.clearFocus();


        //Show Random Main Meal
        randomMealTV=myView.findViewById(R.id.randomMeal);
        randomMealImage=myView.findViewById(R.id.randomMealImage);
        getRandomMealFromUrl(this.getContext());
        randomMealImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboardFrom(getContext(),myView);
            NavDirections action = MainPageDirections.actionMainPageToMeal(randomMealId);
            Navigation.findNavController(v).navigate(action);}
        });


        //Show 3 categories with max 7 meals each
        showTheMealsInRecyclerViews(myView);

        return myView;
    }

    //Get the Random Meal From URL
    public void getRandomMealFromUrl(final Context context){
        String url="https://www.themealdb.com/api/json/v1/1/random.php";
        final OkHttpClient client=new OkHttpClient();
        Request request=new Request.Builder().url(url).build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, e.toString() );
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String serverResponse=response.body().string();
                if(response.isSuccessful()){
                    GsonBuilder gsonBuilder=new GsonBuilder();
                    Gson gson=gsonBuilder.create();

                    meal=gson.fromJson(serverResponse,MealsObj.class);
                    randomMeal=meal.meals.get(0);

                    ((MainActivity) context).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            randomMealId=randomMeal.idMeal;
                            randomMealTV.setText(randomMeal.strMeal);
                            Picasso.with(context).load(randomMeal.strMealThumb).into(randomMealImage);

                        }
                    });

                }
            }
        });

    }

    //Generate 3 random numbers in order to display 3 random categories
    Integer [] threeRandomNumsArray=new Integer [3];
    public void generateRandomNums(){
        Random r=new Random();
        Set<Integer> threeRandomNumsSet=new HashSet<>();

        while (threeRandomNumsSet.size()<=3){
            int randomNum=r.nextInt(categories.length);
            threeRandomNumsSet.add(randomNum);
        }
        threeRandomNumsArray = threeRandomNumsSet.toArray(threeRandomNumsArray);
    }

    //Display 3 recycler views with categories
    public void showTheMealsInRecyclerViews(View myView){

        generateRandomNums();//To display 3 different categories each time

        meals0=new ArrayList<>();
        meals1=new ArrayList<>();
        meals2=new ArrayList<>();

        for (int i = 0; i < 3; i++) {
            String categoryNameIdStr="categoryName"+i;
            String categoryButtonIdStr="categoryButton"+i;
            String recyclerViewIdStr="mealPics"+i;

            recyclerView=myView.findViewById(getResources().getIdentifier(recyclerViewIdStr,"id",this.getContext().getPackageName()));
            recyclerView.setClickable(true);

            int randomCategory=threeRandomNumsArray[i];
            Cursor cursor= databaseHelper.getMealsFromCategory(categories[randomCategory]);
            cursor.moveToFirst();
            int numOfMeals;
            switch (i){
                case 0:
                    numOfMeals=cursor.getCount()<7?cursor.getCount():7;//Display max 7 meals
                    for(int j = 0; j <numOfMeals; j++){
                        meals0.add(new Meal(cursor.getString(1),cursor.getString(2),cursor.getString(3),cursor.getString(4)));
                        cursor.moveToNext();
                    }
                    myAdapter=new MyAdapter(meals0,this.getContext(),MainPage.this,R.layout.cell_main_page);
                    break;
                case 1:
                    numOfMeals=cursor.getCount()<7?cursor.getCount():7;
                    for(int j = 0; j <numOfMeals; j++){
                        meals1.add(new Meal(cursor.getString(1),cursor.getString(2),cursor.getString(3),cursor.getString(4)));
                        cursor.moveToNext();
                    }
                    myAdapter=new MyAdapter(meals1,this.getContext(),MainPage.this,R.layout.cell_main_page);
                    break;
                case 2:
                    numOfMeals=cursor.getCount()<7?cursor.getCount():7;
                    for(int j = 0; j <numOfMeals; j++){
                        meals2.add(new Meal(cursor.getString(1),cursor.getString(2),cursor.getString(3),cursor.getString(4)));
                        cursor.moveToNext();
                    }
                    myAdapter=new MyAdapter(meals2,this.getContext(),MainPage.this,R.layout.cell_main_page);
                    break;
            }

            recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext(), RecyclerView.HORIZONTAL, false));
            recyclerView.setAdapter(myAdapter);


            //Name of Category
            categoryNameTV=myView.findViewById(getResources().getIdentifier(categoryNameIdStr,"id",this.getContext().getPackageName()));
            categoryNameTV.setText(categories[randomCategory]);

            //View more of Category Button
            final int finalI = randomCategory;
            myView.findViewById(getResources().getIdentifier(categoryButtonIdStr,"id",this.getContext().getPackageName()))
                    .setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            NavDirections action=MainPageDirections.actionMainPageToRecipesCategory(categories[finalI]);
                            Navigation.findNavController(v).navigate(action);
                        }
                    });
        }

    }


    //Search Recipes
    public void searchDropdown(View myView){
        Cursor cursor= databaseHelper.getAllMeals();
        cursor.moveToFirst();
        allMealsName=new ArrayList();
        mealsNameIdDictionary=new HashMap<>();

        for (int i = 0; i < cursor.getCount()-1; i++){
            allMealsName.add(cursor.getString(3));
            mealsNameIdDictionary.put(cursor.getString(3),cursor.getString(2));
            cursor.moveToNext();}
        Log.e(TAG, allMealsName.size()+"" );

        search=myView.findViewById(R.id.searchAuto);
        ArrayAdapter adapter=new ArrayAdapter(getContext(),R.layout.cell_drop_down,allMealsName);
        search.setAdapter(adapter);
    }

    public void searchResult(final View myView){
        search.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                name=view.findViewById(R.id.text1);
                String mealName=name.getText().toString();
                String mealId=mealsNameIdDictionary.get(mealName);

                hideKeyboardFrom(getContext(),myView);

                NavDirections action=MainPageDirections.actionMainPageToMeal(mealId);
                Navigation.findNavController(myView).navigate(action);
            }
        });
    }

    public static void hideKeyboardFrom(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }


    @Override
    public void onResume() {
        super.onResume();
        search.setText("");
        NetworkConnection networkConnection=new NetworkConnection(this.getContext(),getActivity());
        networkConnection.getInternetStatus();
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    //Interfaces from MyAdapter
    @Override
    public void onMealClick(View myView,String mealId,int position) {
        hideKeyboardFrom(getContext(),myView);
        NavDirections action = MainPageDirections.actionMainPageToMeal(mealId);
        Navigation.findNavController(myView).navigate(action);
      //  Navigation.setViewNavController(myView,new NavController(myView.getContext())).;
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


    //Fragment Methods
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
        void onFragmentInteraction(Uri uri);
    }



}


