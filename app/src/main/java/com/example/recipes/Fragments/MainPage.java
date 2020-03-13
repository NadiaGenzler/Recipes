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
import android.widget.ImageView;
import android.widget.TextView;

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
import java.util.Currency;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;

import static android.content.ContentValues.TAG;


public class MainPage extends Fragment implements MyAdapter.OnMealListener {
    //top random meal
    Meal randomMeal;
    TextView randomMealTV;
    ImageView randomMealImage;
    String randomMealId;


    String [] categories={"Pasta","Breakfast","Beef"};
    RecyclerView recyclerView0;
    RecyclerView recyclerView1;
    RecyclerView recyclerView2;

    List<MyAdapter> myAdapters;

    MyAdapter myAdapter;
    MealsObj meals;

    List<Meal> meals0;
    List<Meal> meals1;
    List<Meal> meals2;

    TextView categoryNameTV;
    int count;

    DatabaseHelper db;

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
        randomMealTV=myView.findViewById(R.id.randomMeal);
        randomMealImage=myView.findViewById(R.id.randomMealImage);
        getRandomMeal(this.getContext(),myView);

        randomMealImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            NavDirections action = MainPageDirections.actionMainPageToMeal(randomMealId);
            Navigation.findNavController(v).navigate(action);
            }
        });

        db=new DatabaseHelper(this.getContext());

        count=0;

        instantiateRecyclerView(myView);

        meals0=new ArrayList<>();
        meals1=new ArrayList<>();
        meals2=new ArrayList<>();

        myAdapters=new ArrayList<>();

        showTheMealsInRecyclerViews(myView);

        return myView;
    }


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
        for (int i = 0; i < categories.length; i++) {
            String categoryNameIdStr="categoryName"+i;
            String categoryButtonIdStr="categoryButton"+i;

            Cursor cursor=db.getMealsFromCategory(categories[i]);

            Log.e(TAG, cursor.getCount()+"" );
            switch (i){
                case 0:
                    cursor.moveToFirst();
                    meals0.add(new Meal(cursor.getString(2),cursor.getString(3),cursor.getString(4)));
                    while (cursor.moveToNext()){
                        meals0.add(new Meal(cursor.getString(2),cursor.getString(3),cursor.getString(4)));
                    }
                    myAdapter=new MyAdapter(meals0,this.getContext(),MainPage.this,R.layout.cell_main_page);
                    recyclerView0.setLayoutManager(new LinearLayoutManager(this.getContext(), RecyclerView.HORIZONTAL, false));
                    recyclerView0.setAdapter(myAdapter);
                    break;
                case 1:
                    while (cursor.moveToNext()){
                        meals1.add(new Meal(cursor.getString(2),cursor.getString(3),cursor.getString(4)));
                    }
                    myAdapter=new MyAdapter(meals1,this.getContext(),MainPage.this,R.layout.cell_main_page);
                    recyclerView1.setLayoutManager(new LinearLayoutManager(this.getContext(), RecyclerView.HORIZONTAL, false));
                    recyclerView1.setAdapter(myAdapter);
                    break;
                case 2:
                    while (cursor.moveToNext()){
                        meals2.add(new Meal(cursor.getString(2),cursor.getString(3),cursor.getString(4)));
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
            categoryNameTV.setText(categories[i]);

            final int finalI = i;
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


    @Override
    public void onMealClick(View v,String mealId,int position) {
        NavDirections action = MainPageDirections.actionMainPageToMeal(mealId);
        Navigation.findNavController(v).navigate(action);
    }



    public void getRandomMeal(final Context context,final View myView){
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
}


