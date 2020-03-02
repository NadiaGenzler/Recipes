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


    String [] categories={"Pasta","Breakfast","Beef"};
    RecyclerView recyclerView0;
    RecyclerView recyclerView1;
    RecyclerView recyclerView2;

    List<MyAdapter> myAdapters;

    MyAdapter myAdapter;
    MealsObj meals;
    List<List<Meal>> allmeals_arr;
    List<Meal> meals0;
    List<Meal> meals1;
    List<Meal> meals2;

    TextView categoryNameTV;
    int count;

    DatabaseHelper db;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;


    public MainPage() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MainPage.
     */
    // TODO: Rename and change types and number of parameters
    public static MainPage newInstance(String param1, String param2) {
        MainPage fragment = new MainPage();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View myView=inflater.inflate(R.layout.fragment_main_page, container, false);
        getRandomMeal(this.getContext(),myView);
        db=new DatabaseHelper(this.getContext());

        count=0;

        instantiateRecyclerView(myView);

        meals0=new ArrayList<>();
        meals1=new ArrayList<>();
        meals2=new ArrayList<>();


        myAdapters=new ArrayList<>();


        for (int i = 0; i < categories.length; i++) {
            String categoryNameIdStr="categoryName"+i;
           // String categoryButtonIdStr="categoryButton"+i;

            Cursor cursor=db.getMealsFromCategory(categories[i]);
            cursor.moveToFirst();
            Log.e(TAG, cursor.getCount()+"" );
            switch (i){
                case 0:
                    while (cursor.moveToNext()){
                        meals0.add(new Meal(cursor.getString(2),cursor.getString(3),cursor.getString(4)));
                    }
                    myAdapter=new MyAdapter(meals0,this.getContext(),MainPage.this);
                    recyclerView0.setLayoutManager(new LinearLayoutManager(this.getContext(), RecyclerView.HORIZONTAL, false));
                    recyclerView0.setAdapter(myAdapter);
                    break;
                case 1:
                    while (cursor.moveToNext()){
                        meals1.add(new Meal(cursor.getString(2),cursor.getString(3),cursor.getString(4)));
                    }
                    myAdapter=new MyAdapter(meals1,this.getContext(),MainPage.this);
                    recyclerView1.setLayoutManager(new LinearLayoutManager(this.getContext(), RecyclerView.HORIZONTAL, false));
                    recyclerView1.setAdapter(myAdapter);
                    break;
                case 2:
                    while (cursor.moveToNext()){
                        meals2.add(new Meal(cursor.getString(2),cursor.getString(3),cursor.getString(4)));
                    }
                    myAdapter=new MyAdapter(meals2,this.getContext(),MainPage.this);
                    recyclerView2.setLayoutManager(new LinearLayoutManager(this.getContext(), RecyclerView.HORIZONTAL, false));
                    recyclerView2.setAdapter(myAdapter);
                    break;
            }


            categoryNameTV=myView.findViewById(getResources().getIdentifier(categoryNameIdStr,"id",this.getContext().getPackageName()));
            categoryNameTV.setText(categories[i]);

        }


//        List<Meal> sevenMeals=new ArrayList<>();
//        for (int i = 0; i < 7; i++) {
//            sevenMeals.add(meals.meals.get(i));
//        }
//
//



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
        Log.d(TAG, "onMealClick: "+position);
        Log.d(TAG, "onMealClick: "+mealId);

        NavDirections action = MainPageDirections.actionMainPageToMeal(mealId);

        Navigation.findNavController(v).navigate(action);
    }



//    public void getTheList(final Context context , String url){
//
//        meals = new MealsObj();
//
//        final OkHttpClient client = new OkHttpClient();
//        Request request = new Request.Builder().url(url).build();
//
//        client.newCall(request).enqueue(new Callback() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//            }
//
//            @Override
//            public void onResponse(Call call, Response response) throws IOException {
//                String serverResponse = response.body().string();
//                if (response.isSuccessful()) {
//
//                    GsonBuilder gsonBuilder = new GsonBuilder();
//                    Gson gson = gsonBuilder.create();
//
//                    //meals = gson.fromJson(serverResponse, new TypeToken<ArrayList<Meal>>(){}.getType());
//                    meals = gson.fromJson(serverResponse, MealsObj.class);
//
//
//                    List<Meal> sevenMeals=new ArrayList<>();
//                    for (int i = 0; i < 7; i++) {
//                        sevenMeals.add(meals.meals.get(i));
//                    }
//
//                    myAdapter=new MyAdapter(sevenMeals,context,MainPage.this);
//                    myAdapters.add(count,myAdapter);
//
//
//                   count++;
//
//                    ((MainActivity) context).runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//
////                            switch (count){
////                                case 0:
////                                    for (Meal meal:meals.meals) {
////                                        meals0.add(meal);
////                                    }
////                                    allmeals_arr.add(meals0);
////                                    break;
////                                case 1:
////                                    for (Meal meal:meals.meals) {
////                                        meals1.add(meal);
////                                    }
////                                    allmeals_arr.add(meals1);
////                                    break;
////                                case 2:
////                                    for (Meal meal:meals.meals) {
////                                        meals2.add(meal);
////                                    }
////                                    allmeals_arr.add(meals2);
////                                    break;
////                            }
////
////                            myAdapter.notifyDataSetChanged();
//
//                        if (count==3){
//                            recyclerView0.setLayoutManager(new LinearLayoutManager(context,RecyclerView.HORIZONTAL,false));
//                            recyclerView0.setAdapter(myAdapters.get(0));
//
//                            recyclerView1.setLayoutManager(new LinearLayoutManager(context,RecyclerView.HORIZONTAL,false));
//                            recyclerView1.setAdapter(myAdapters.get(1));
//
//                            recyclerView2.setLayoutManager(new LinearLayoutManager(context,RecyclerView.HORIZONTAL,false));
//                            recyclerView2.setAdapter(myAdapters.get(2));
//                        };
//
//                        }
//                    });
//
//
//
//                }
//            }
//        });
//
//
//    }

    public void getRandomMeal(final Context context,final View myView){
        String url="https://www.themealdb.com/api/json/v1/1/random.php";
        final OkHttpClient client=new OkHttpClient();
        Request request=new Request.Builder().url(url).build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

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

                            randomMealTV=myView.findViewById(R.id.randomMeal);
                            randomMealTV.setText(randomMeal.strMeal);
                            randomMealImage=myView.findViewById(R.id.randomMealImage);
                            Picasso.with(context).load(randomMeal.strMealThumb).into(randomMealImage);

                        }
                    });

                }
            }
        });

    }
}


