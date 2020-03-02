package com.example.recipes.Fragments;

import android.content.Context;
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
import android.widget.TextView;

import com.example.recipes.MainActivity;
import com.example.recipes.Model.Meal;
import com.example.recipes.Model.MealsObj;
import com.example.recipes.Adapters.MyAdapter;
import com.example.recipes.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;

import static android.content.ContentValues.TAG;


public class MainPage extends Fragment implements MyAdapter.OnMealListener {
    String [] categories={"Pasta","Breakfast","Dessert"};
    RecyclerView recyclerView0;
    RecyclerView recyclerView1;
    RecyclerView recyclerView2;

    List<MyAdapter> myAdapters;

    MyAdapter myAdapter;
    MealsObj meals;
    TextView categoryNameTV;
    int count;

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
        count=0;
        View myView=inflater.inflate(R.layout.fragment_main_page, container, false);

        instantiateRecyclerView(myView);


        myAdapters=new ArrayList<>();

            for (int i = 0; i < categories.length; i++) {
                //String recyclerViewIdStr="categoryPics"+i;
                String categoryNameIdStr="categoryName"+i;
                String categoryButtonIdStr="categoryButton"+i;

                categoryNameTV=myView.findViewById(getResources().getIdentifier(categoryNameIdStr,"id",this.getContext().getPackageName()));
                categoryNameTV.setText(categories[i]);

                String url = "https://www.themealdb.com/api/json/v1/1/filter.php?c="+categories[i];

                getTheList(this.getContext(),url);
            }










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



    public void getTheList( final Context context , String url){

        meals = new MealsObj();


        final OkHttpClient client = new OkHttpClient();
       // final OkHttpClient client = OkHttpClient.Builder()


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

                    //meals = gson.fromJson(serverResponse, new TypeToken<ArrayList<Meal>>(){}.getType());
                    meals = gson.fromJson(serverResponse, MealsObj.class);

                    List<Meal> sevenMeals=new ArrayList<>();
                    for (int i = 0; i < 7; i++) {
                        sevenMeals.add(meals.meals.get(i));
                    }

                    myAdapter=new MyAdapter(sevenMeals,context,MainPage.this);
                    myAdapters.add(count,myAdapter);

                   count++;

                    ((MainActivity) context).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                        if (count==3){
                            recyclerView0.setLayoutManager(new LinearLayoutManager(context,RecyclerView.HORIZONTAL,false));
                            recyclerView0.setAdapter(myAdapters.get(0));

                            recyclerView1.setLayoutManager(new LinearLayoutManager(context,RecyclerView.HORIZONTAL,false));
                            recyclerView1.setAdapter(myAdapters.get(1));

                            recyclerView2.setLayoutManager(new LinearLayoutManager(context,RecyclerView.HORIZONTAL,false));
                            recyclerView2.setAdapter(myAdapters.get(2));
                        };

                        }
                    });



                }
            }
        });


    }
}


