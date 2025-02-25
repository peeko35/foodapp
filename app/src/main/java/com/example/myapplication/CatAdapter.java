package com.example.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class CatAdapter extends RecyclerView.Adapter<CatAdapter.ViewHolder>{

    private List<catModel> datalist;

    public CatAdapter( List<catModel> datalist) {

        this.datalist = datalist;

    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_categoryrecyclview,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            catModel stallDetail =datalist.get(position);


        holder.foodName.setText(stallDetail.getFoodName());
        holder.foodPrice.setText("Price: ₹" + stallDetail.getPrice());
        holder.foodDescription.setText(stallDetail.getDescription());
        Glide.with(holder.itemView.getContext()).load(stallDetail.getImageUrl()).into(holder.foodImage);
    }

    @Override
    public int getItemCount() {

        return datalist.size();
    }
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView foodName, foodPrice, foodDescription;
        ImageView foodImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            foodName = itemView.findViewById(R.id.foodnamm);
            foodPrice = itemView.findViewById(R.id.Catprice);
            foodDescription = itemView.findViewById(R.id.Catdesc);
            foodImage = itemView.findViewById(R.id.recyclimage);
        }
    }
}