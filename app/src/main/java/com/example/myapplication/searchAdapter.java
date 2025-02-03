package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;


import java.util.List;

public class searchAdapter extends RecyclerView.Adapter<MyViewHolder> {
    private Context context;
    private List<searchmainModel> datalist;

    public searchAdapter(Context context, List<searchmainModel> datalist) {
        this.context = context;
        this.datalist = datalist;
    }
        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.search_item_container,parent,false);
            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
            searchmainModel currentItem = datalist.get(position);

            if (currentItem != null) {
                String imageUrl = currentItem.getImageUrl();

                if (imageUrl == null) {
                    Log.e("searchAdapter", "Image URL is null at position: " + position);
                    imageUrl = ""; // Assign empty string to avoid NullPointerException
                }

                String secureUrl = imageUrl.replace("http://", "https://");

                // Load image into ImageView using Glide
                Glide.with(holder.itemView.getContext())
                        .load(secureUrl)
                        .into(holder.recImage);

                // Set stall name and food name
                holder.recstall.setText(currentItem.getStallName());
                holder.recfood.setText(currentItem.getFoodName());

                // Set click listener to open StallDetailActivity
                holder.itemView.setOnClickListener(view -> {
                    Intent intent = new Intent(view.getContext(), StallDetailActivity.class);
                    intent.putExtra("location", currentItem.getLocation());
                    intent.putExtra("imageUrl", currentItem.getImageUrl());


                    view.getContext().startActivity(intent);
                });
            } else {
                Log.e("searchAdapter", "Current item is null at position: " + position);
            }
        }

        @Override
        public int getItemCount() {

            return datalist != null ? datalist.size() : 0;
        }
    }
    class ViewHolder extends RecyclerView.ViewHolder{
        ImageView recImage;
        TextView recstall,recfood,location;
        CardView recCard;



        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            location.findViewById(R.id.searchfoodstall);
            recImage.findViewById(R.id.recImage);
            recstall.findViewById(R.id.stallnm);
            recfood.findViewById(R.id.foodnm);
            recCard.findViewById(R.id.cardrec);




        }

}
