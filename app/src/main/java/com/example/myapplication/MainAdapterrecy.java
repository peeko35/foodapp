package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.List;
import com.bumptech.glide.Glide;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;



public class MainAdapterrecy extends RecyclerView.Adapter<MyViewHolder>{
    private Context context;
   private List<MainModelrecy>datalist;


    public MainAdapterrecy(Context context, List<MainModelrecy> datalist) {
        this.context = context;
        this.datalist = datalist;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_container_foodstall,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        MainModelrecy currentItem = datalist.get(position);
        String secureUrl = currentItem.getImageUrl().replace("http://", "https://");
        if (currentItem != null) {
            // Load image into ImageView
            Glide.with(holder.itemView.getContext())
                    .load(secureUrl)
                    .into(holder.recImage);

            // Set stall name and food name
            holder.recstall.setText(currentItem.getStallName());
            holder.recfood.setText(currentItem.getFoodName());

            // Set click listener to open StallDetailActivity
            holder.itemView.setOnClickListener(view -> {
                Intent intent = new Intent(view.getContext(), StallDetailActivity.class);
                intent.putExtra("vendorId", currentItem.getVendorId());
                intent.putExtra("imageUrl", currentItem.getImageUrl());
                intent.putExtra("description", currentItem.getDescription());
                intent.putExtra("price", currentItem.getPrice());
                Log.d("Adapter", "Passing vendorId: " + currentItem.getVendorId());
                Log.d("MainAdapterrecy", "Passing Image URL: " + currentItem.getImageUrl());
                Log.d("MainAdapterrecy", "Passing Description: " + currentItem.getDescription());
                Log.d("MainAdapterrecy", "Passing Price: " + currentItem.getPrice());

                view.getContext().startActivity(intent);
            });
        } else {
            Log.e("MainAdapterrecy", "Current item is null at position: " + position);
        }
    }

    @Override
    public int getItemCount() {

        return datalist != null ? datalist.size() : 0;
    }
}
class MyViewHolder extends RecyclerView.ViewHolder{
    ImageView recImage;
    TextView recstall,recfood;
    CardView recCard;



    public MyViewHolder(@NonNull View itemView) {
        super(itemView);
        recImage =itemView.findViewById(R.id.recImage);
        recstall =itemView.findViewById(R.id.stallnm);
        recfood =itemView.findViewById(R.id.foodnm);
        recCard = itemView.findViewById(R.id.cardrec);





    }
}
