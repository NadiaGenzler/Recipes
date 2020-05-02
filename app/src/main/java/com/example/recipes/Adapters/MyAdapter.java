package com.example.recipes.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recipes.Model.Meal;
import com.example.recipes.R;
import com.example.recipes.Utils.DatabaseHelper;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
    private List<Meal> listOfMeals;
    private Context context;
    private OnMealListener onMealListener;
    private int cardLayout;

    public MyAdapter(List<Meal> listOfStrings, Context context,OnMealListener onMealListener,int cardLayout) {
        this.listOfMeals = listOfStrings;
        this.context = context;
        this.onMealListener=onMealListener;
        this.cardLayout=cardLayout;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(context).inflate(cardLayout,parent,false);

        return new MyAdapter.ViewHolder(view,onMealListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        TextView nameTV = holder.nameTV;
        nameTV.setText(listOfMeals.get(position).strMeal);

        if (cardLayout == R.layout.cell_favorites) {
            TextView categoryTV = holder.categoryTV;
            categoryTV.setText("Category: " + listOfMeals.get(position).strCategory);
        }
        else if (cardLayout == R.layout.cell_main_page || cardLayout == R.layout.cell_recipe_category) {
            ImageView fav = holder.addFavsBtn;
            DatabaseHelper db = new DatabaseHelper(context);
            if (db.isFavorite(listOfMeals.get(position).idMeal)) {
                fav.setImageResource(R.drawable.full_heart);
            } else {
                fav.setImageResource(R.drawable.empty_heart);
            }
        }

        ImageView imageView=holder.imageView;
        Picasso.with(context).load(listOfMeals.get(position).strMealThumb).into(imageView);
    }

    @Override
    public int getItemCount() {
        return listOfMeals.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    TextView nameTV;
    TextView categoryTV;
    ImageView imageView;
    OnMealListener onMealListener;
    ImageView removeFavsBtn;
    ImageView addFavsBtn;
        public ViewHolder(@NonNull View itemView,OnMealListener onMealListener) {
            super(itemView);
            this.onMealListener=onMealListener;

            nameTV = itemView.findViewById(R.id.nameTv);
            categoryTV = itemView.findViewById(R.id.categoryTv);
            imageView=itemView.findViewById(R.id.mealImg);
            itemView.setOnClickListener(this);

            if(cardLayout==R.layout.cell_favorites) {
                removeFavsBtn = itemView.findViewById(R.id.removeBtn);
                removeFavsBtn.setOnClickListener(this);
            }else{
                addFavsBtn=itemView.findViewById(R.id.favoriteBtn);
                addFavsBtn.setOnClickListener(this);
            }
        }


        @Override
        public void onClick(View v) {
            if(v instanceof ImageView) {
                onMealListener.onFavoriteClick(v, listOfMeals.get(getAdapterPosition()).idMeal, getAdapterPosition());
            }else{
                onMealListener.onMealClick(v,listOfMeals.get(getAdapterPosition()).idMeal, getAdapterPosition());
            }
        }
    }

    public interface OnMealListener{
        void onMealClick(View v,String mealId,int position);
        void onFavoriteClick(View v, String mealId, int position);
    }
}
