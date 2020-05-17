package com.example.recipes.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.LinearLayout;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.example.recipes.Adapters.MyAdapter;
import com.example.recipes.Adapters.StaggeredCategoriesAdapter;
import com.example.recipes.R;
import com.example.recipes.Utils.GlobalVariable;
import com.example.recipes.Utils.NetworkConnection;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class AllCategories extends Fragment implements StaggeredCategoriesAdapter.OnCategoryClickListener {

    RecyclerView recyclerView;

    List<String> categoryName;
    List<String> categoryImage;

    private OnFragmentInteractionListener mListener;
    public AllCategories() {
        // Required empty public constructor
    }
    public static AllCategories newInstance(String param1, String param2) {
        AllCategories fragment = new AllCategories();
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

        View myView=inflater.inflate(R.layout.fragment_all_categories, container, false);

        categoryImageAndName();

        recyclerView=myView.findViewById(R.id.staggeredRecycler);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, LinearLayout.VERTICAL));
        recyclerView.setAdapter(new StaggeredCategoriesAdapter(this.getContext(),categoryName,categoryImage,AllCategories.this));

        return myView;
    }


    //Category Name and Images
    public void categoryImageAndName(){
       categoryName= new ArrayList<>();
        categoryName.add("Beef");
        categoryName.add("Miscellaneous");
        categoryName.add("Chicken");
        categoryName.add("Pasta");//4
        categoryName.add("Dessert");//5
        categoryName.add("Lamb");//6
        categoryName.add("Side");//7
        categoryName.add("Seafood");//8
        categoryName.add("Pork");//9
        categoryName.add("Goat");//10
        categoryName.add("Starter");
        categoryName.add("Vegan");
        categoryName.add("Vegetarian");
        categoryName.add("Breakfast");

        categoryImage=new ArrayList<>();
        categoryImage.add("https://images.pexels.com/photos/675951/pexels-photo-675951.jpeg?auto=compress&cs=tinysrgb&dpr=2&w=500");
        categoryImage.add("https://images.pexels.com/photos/3730946/pexels-photo-3730946.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=750&w=1260");
        categoryImage.add("https://images.pexels.com/photos/1624487/pexels-photo-1624487.jpeg?auto=compress&cs=tinysrgb&dpr=2&w=500");
        categoryImage.add("https://images.pexels.com/photos/803963/pexels-photo-803963.jpeg?auto=compress&cs=tinysrgb&dpr=2&w=500");//4
        categoryImage.add("https://images.pexels.com/photos/1099680/pexels-photo-1099680.jpeg?auto=compress&cs=tinysrgb&dpr=2&w=500");//5
        categoryImage.add("https://images.pexels.com/photos/323682/pexels-photo-323682.jpeg?auto=compress&cs=tinysrgb&dpr=2&w=500");//6
        categoryImage.add("https://images.pexels.com/photos/2781540/pexels-photo-2781540.jpeg?auto=compress&cs=tinysrgb&dpr=2&w=500");//7
        categoryImage.add("https://images.pexels.com/photos/566344/pexels-photo-566344.jpeg?auto=compress&cs=tinysrgb&dpr=2&w=500");//8
        categoryImage.add("https://images.pexels.com/photos/416471/pexels-photo-416471.jpeg?auto=compress&cs=tinysrgb&dpr=2&w=500");//9
        categoryImage.add("https://images.pexels.com/photos/3906290/pexels-photo-3906290.jpeg?auto=compress&cs=tinysrgb&dpr=2&w=500");//10
        categoryImage.add("https://images.pexels.com/photos/3669501/pexels-photo-3669501.jpeg?auto=compress&cs=tinysrgb&dpr=2&w=500");
        categoryImage.add("https://images.pexels.com/photos/248509/pexels-photo-248509.jpeg?auto=compress&cs=tinysrgb&dpr=2&w=500");
        categoryImage.add("https://images.pexels.com/photos/1143754/pexels-photo-1143754.jpeg?auto=compress&cs=tinysrgb&dpr=2&w=500");
        categoryImage.add("https://images.pexels.com/photos/2113556/pexels-photo-2113556.jpeg?auto=compress&cs=tinysrgb&dpr=2&w=500");

    }


    @Override
    public void onCategoryClick(View view, String name, int position) {
        NavDirections action=AllCategoriesDirections.actionAllCategoriesToRecipesCategory(name);
        Navigation.findNavController(view).navigate(action);
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
