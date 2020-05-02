package com.example.recipes.Fragments;

import android.app.Activity;
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
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

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

    //Other recipes
    String [] categories={"Beef","Breakfast","Chicken","Dessert","Lamb","Pasta","Pork","Seafood","Side","Starter","Vegan","Vegetarian","Miscellaneous"};
    RecyclerView recyclerView0,recyclerView1,recyclerView2;
    MyAdapter myAdapter;
    ImageView favBtn;

    MealsObj meals;
    List<Meal> meals0,meals1,meals2;

    TextView categoryNameTV;
    int count;

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
        final View myView=inflater.inflate(R.layout.fragment_main_page, container, false);
        databaseHelper =new DatabaseHelper(this.getContext());

//        Cursor cursorGetId=databaseHelper.getAllFavorites();
//        List <String> favsId=new ArrayList<>();
//        cursorGetId.moveToFirst();
//        for (int i = 0; i < cursorGetId.getCount(); i++) {
//            favsId.add(cursorGetId.getString(0));
//            cursorGetId.moveToNext();
//        }
//        Log.e(TAG, " "+favsId );

        //Search Recipes
        searchDropdown(myView);
        searchResult(myView);
        search.clearFocus();
        //Go to favorites
        favList(myView);

        //Handle the Random Meal
        randomMealTV=myView.findViewById(R.id.randomMeal);
        randomMealImage=myView.findViewById(R.id.randomMealImage);
        getRandomMealFromUrl(this.getContext(),myView);
        randomMealImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboardFrom(getContext(),myView);
            NavDirections action = MainPageDirections.actionMainPageToMeal(randomMealId);
            Navigation.findNavController(v).navigate(action);}
        });

        //Handle the meals
        count=0;
        instantiateRecyclerView(myView);
        meals0=new ArrayList<>();
        meals1=new ArrayList<>();
        meals2=new ArrayList<>();
        showTheMealsInRecyclerViews(myView);




        return myView;
    }

    //Get the Random Meal From URL
    public void getRandomMealFromUrl(final Context context, final View myView){
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

                    meals=gson.fromJson(serverResponse,MealsObj.class);
                    randomMeal=meals.meals.get(0);

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

    //Handle the meals
    public void instantiateRecyclerView(View myView){
        recyclerView0=myView.findViewById(R.id.categoryPics0);
        recyclerView0.setHasFixedSize(true);
        recyclerView0.setClickable(true);


        recyclerView1=myView.findViewById(R.id.categoryPics1);
        recyclerView1.setHasFixedSize(true);
        recyclerView1.setClickable(true);

        recyclerView2=myView.findViewById(R.id.categoryPics2);
        recyclerView2.setHasFixedSize(true);
        recyclerView2.setClickable(true);
    }
    public void showTheMealsInRecyclerViews(View myView){
        /////////////////////////////////////////////Solve the problem of repeating nums



        for (int i = 0; i < 3; i++) {
            String categoryNameIdStr="categoryName"+i;
            String categoryButtonIdStr="categoryButton"+i;

            Random r=new Random();
            int randomCaregory=r.nextInt(categories.length);

            Cursor cursor= databaseHelper.getMealsFromCategory(categories[randomCaregory]);

            switch (i){
                case 0:
                    cursor.moveToFirst();
                    for(int j = 0; j <cursor.getCount(); j++){
                        meals0.add(new Meal(cursor.getString(1),cursor.getString(2),cursor.getString(3),cursor.getString(4)));
                        cursor.moveToNext();
                    }
                    myAdapter=new MyAdapter(meals0,this.getContext(),MainPage.this,R.layout.cell_main_page);
                    recyclerView0.setLayoutManager(new LinearLayoutManager(this.getContext(), RecyclerView.HORIZONTAL, false));
                    recyclerView0.setAdapter(myAdapter);
                    break;
                case 1:
                    cursor.moveToFirst();
                    for(int j = 0; j <cursor.getCount(); j++){
                        meals1.add(new Meal(cursor.getString(1),cursor.getString(2),cursor.getString(3),cursor.getString(4)));
                        cursor.moveToNext();
                    }
                    myAdapter=new MyAdapter(meals1,this.getContext(),MainPage.this,R.layout.cell_main_page);
                    recyclerView1.setLayoutManager(new LinearLayoutManager(this.getContext(), RecyclerView.HORIZONTAL, false));
                    recyclerView1.setAdapter(myAdapter);
                    break;
                case 2:
                    cursor.moveToFirst();
                    for(int j = 0; j <cursor.getCount(); j++){
                        meals2.add(new Meal(cursor.getString(1),cursor.getString(2),cursor.getString(3),cursor.getString(4)));
                        cursor.moveToNext();
                    }
                    myAdapter=new MyAdapter(meals2,this.getContext(),MainPage.this,R.layout.cell_main_page);
                    recyclerView2.setLayoutManager(new LinearLayoutManager(this.getContext(), RecyclerView.HORIZONTAL, false));
                    recyclerView2.setAdapter(myAdapter);
                    break;
            }

//            List<Meal> sevenMeals=new ArrayList<>();
//            for (int i = 0; i < 7; i++) {
//                sevenMeals.add(meals.meals.get(i));
//            }
            categoryNameTV=myView.findViewById(getResources().getIdentifier(categoryNameIdStr,"id",this.getContext().getPackageName()));
            categoryNameTV.setText(categories[randomCaregory]);

            final int finalI = randomCaregory;
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
                // Log.e(TAG, "onItemClick: "+mealName+mealId+"");

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

    //Go to favorite list
    public void favList(final View myView){
        myView.findViewById(R.id.favoriteListBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavDirections action=MainPageDirections.actionMainPageToFavoriteMeals();
                Navigation.findNavController(myView).navigate(action);
            }
        });
    }

//    public void showIfFavorite(View cardView,String mealId){
//        favBtn=cardView.findViewById(R.id.favoriteBtn);
//        if(databaseHelper.isFavorite(mealId)){
//            favBtn.setImageResource(R.drawable.full_heart);
//        }else {
//            favBtn.setImageResource(R.drawable.empty_heart);
//        }
//    }
    @Override
    public void onResume() {
        super.onResume();
        search.setText("");
    }

    @Override
    public void onMealClick(View myView,String mealId,int position) {
        hideKeyboardFrom(getContext(),myView);
        NavDirections action = MainPageDirections.actionMainPageToMeal(mealId);
        Navigation.findNavController(myView).navigate(action);
    }

    @Override
    public void onFavoriteClick(View myView, String mealId, int position) {
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

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }







}


