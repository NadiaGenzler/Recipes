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
import com.squareup.picasso.Picasso;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
    private List<Meal> listOfMeals;
    private Context context;
    private OnMealListener onMealListener;

    public MyAdapter(List<Meal> listOfStrings, Context context,OnMealListener onMealListener) {
        this.listOfMeals = listOfStrings;
        this.context = context;
        this.onMealListener=onMealListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(context).inflate(R.layout.image_description_cell,parent,false);

        return new MyAdapter.ViewHolder(view,onMealListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        TextView textView=holder.textView;
        textView.setText(listOfMeals.get(position).strMeal);

        ImageView imageView=holder.imageView;
        Picasso.with(context).load(listOfMeals.get(position).strMealThumb).into(imageView);
    }

    @Override
    public int getItemCount() {
        return listOfMeals.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    TextView textView;
    ImageView imageView;
    OnMealListener onMealListener;
        public ViewHolder(@NonNull View itemView,OnMealListener onMealListener) {
            super(itemView);
            this.onMealListener=onMealListener;

            textView = itemView.findViewById(R.id.myText);
            imageView=itemView.findViewById(R.id.mealImg);
            itemView.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {
            onMealListener.onMealClick(v,listOfMeals.get(getAdapterPosition()).idMeal, getAdapterPosition());

        }
    }

    public interface OnMealListener{
        void onMealClick(View v,String mealId,int position);
    }
}
