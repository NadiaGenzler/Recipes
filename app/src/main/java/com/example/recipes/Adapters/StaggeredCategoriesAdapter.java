package com.example.recipes.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.PointerIcon;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recipes.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class StaggeredCategoriesAdapter extends RecyclerView.Adapter<StaggeredCategoriesAdapter.ViewHolder> {


    List<String> mImage;
    List<String> mName;
    Context context;
    OnCategoryClickListener onCategoryClickListener;

    public StaggeredCategoriesAdapter( Context context,List<String> mName,List<String> mImage,OnCategoryClickListener onCategoryClickListener) {
        this.mImage = mImage;
        this.mName = mName;
        this.context = context;
        this.onCategoryClickListener=onCategoryClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.cell_all_categories,parent,false);

        return new StaggeredCategoriesAdapter.ViewHolder(view,onCategoryClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        ImageView image=holder.imageView;
        TextView textView=holder.textView;

        textView.setText(mName.get(position));
        Picasso.with(context).load(mImage.get(position)).into(image);

    }

    @Override
    public int getItemCount() {
        return mName.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        ImageView imageView;
        TextView textView;
        OnCategoryClickListener onCategoryClickListener;
        public ViewHolder(@NonNull View itemView, OnCategoryClickListener onCategoryClickListener) {
            super(itemView);
            this.onCategoryClickListener= onCategoryClickListener;
            itemView.setOnClickListener(this);

            this.imageView=itemView.findViewById(R.id.categoryImageview);
            this.textView=itemView.findViewById(R.id.categoryTextview);

        }

        @Override
        public void onClick(View v) {
            onCategoryClickListener.onCategoryClick(v,mName.get(getAdapterPosition()),getAdapterPosition());
        }
    }

    public interface OnCategoryClickListener{

        void onCategoryClick(View view,String name,int position);
    }
}

