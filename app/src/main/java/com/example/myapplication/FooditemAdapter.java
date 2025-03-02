package com.example.myapplication;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class FooditemAdapter extends RecyclerView.Adapter<FooditemAdapter.FoodViewHolder> {
    private List<catModel> foodlist;

    public FooditemAdapter(List<catModel>foodList) {
        this.foodlist = foodList;

    }

    @NonNull
    @Override
    public FooditemAdapter.FoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_fooditemsrecycle, parent, false);
        return new FoodViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FooditemAdapter.FoodViewHolder holder, int position) {
        catModel foodItem = foodlist.get(position);
        holder.foodName.setText(foodItem.getFoodName());
        holder.foodDesc.setText(foodItem.getDescription());
        holder.foodPrice.setText("Price: ₹" + foodItem.getPrice());

        // Load image using Glide
        Glide.with(holder.itemView.getContext()).load(foodItem.getImageUrl()).into(holder.foodImage);
    }


    @Override
    public int getItemCount() {

        return foodlist.size() ;
    }
    public static class FoodViewHolder extends RecyclerView.ViewHolder{
        TextView foodName, foodDesc, foodPrice;
        ImageView foodImage;
        public FoodViewHolder(@NonNull View itemView) {
            super(itemView);
            foodName = itemView.findViewById(R.id.fooditemnm);
            foodDesc = itemView.findViewById(R.id.Fooddesc);
            foodPrice = itemView.findViewById(R.id.foodItemprice);
            foodImage = itemView.findViewById(R.id.recyclItemsimg);
        }

    }
}
