package com.example.myapplication;



import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class searchAdapter extends RecyclerView.Adapter<MViewHolder> implements Filterable {

       private Context context;
       private List<searchmainModel>dataList;
    private List<searchmainModel> filteredList;


    public searchAdapter(Context context, List<searchmainModel> dataList) {
        this.context = context;
        this.dataList = dataList;
        this.filteredList = new ArrayList<>(dataList);
    }

    @NonNull
    @Override
    public MViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.search_item_container,parent,false);
        return new MViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull MViewHolder holder, int position) {
        searchmainModel currentItem = dataList.get(position);


        if (currentItem != null) {
            String imageUrl = currentItem.getImageUrl();
            if (imageUrl != null && !imageUrl.isEmpty()) {
                String secureUrl = imageUrl.replace("http://", "https://");
                Glide.with(holder.itemView.getContext())
                        .load(secureUrl)
                        .into(holder.reccimage);
            } else {

                holder.reccimage.setImageResource(R.drawable.image);
            }
            holder.stalnm.setText(currentItem.getStallName() != null ? currentItem.getStallName() : "Unknown Stall");
            holder.foodnm.setText(currentItem.getFoodName() != null ? currentItem.getFoodName() : "Unknown Food");
            holder.loct.setText(currentItem.getLocation() != null ? currentItem.getLocation() : "Unknown Location");
            Log.d("Location", "Location for position " + position + ": " + currentItem.getLocation());

            holder.itemView.setOnClickListener(view -> {
                if (currentItem.getVendorId() == null) {
                    Log.e("Adapter", "Error: vendorId is NULL at position " + position);
                    Toast.makeText(view.getContext(), "Vendor ID is missing!", Toast.LENGTH_SHORT).show();
                    return; // Prevents opening activity with null vendorId
                }
                Intent intent = new Intent(view.getContext(), StallDetailActivity.class);
                intent.putExtra("vendorId", currentItem.getVendorId());
                intent.putExtra("imageUrl", currentItem.getImageUrl());
                intent.putExtra("description", currentItem.getDescription());
                intent.putExtra("price", currentItem.getPrice());
                Log.d("Adapter", "Passing vendorId: " + currentItem.getVendorId());
                Log.d("searchAdapter", "Passing Image URL: " + currentItem.getImageUrl());
                Log.d("searchAdapter", "Passing Description: " + currentItem.getDescription());
                Log.d("searchAdapter", "Passing Price: " + currentItem.getPrice());

                view.getContext().startActivity(intent);
            });

        }
    }
    @Override
    public int getItemCount() {
        return dataList == null ? 0 : dataList.size();

    }
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                List<searchmainModel> filteredResults = new ArrayList<>();

                if (constraint == null || constraint.length() == 0) {
                    filteredResults.addAll(filteredList);  // Use original list for reset
                } else {
                    String filterPattern = constraint.toString().toLowerCase().trim();
                    for (searchmainModel item : filteredList) {
                        if (item.getStallName().toLowerCase().contains(filterPattern) ||
                                item.getFoodName().toLowerCase().contains(filterPattern) ||
                                item.getLocation().toLowerCase().contains(filterPattern)) {
                            filteredResults.add(item);
                        }
                    }
                }

                FilterResults results = new FilterResults();
                results.values = filteredResults;
                results.count = filteredResults.size();
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                dataList.clear();
                dataList.addAll((List<searchmainModel>) results.values);
                notifyDataSetChanged();
            }
        };
    }
}



class MViewHolder extends RecyclerView.ViewHolder{
    ImageView reccimage;
    TextView stalnm,foodnm,loct;
    CardView cardView;
    ImageView recImage,addwishlist;
    TextView recstall,recfood,ratetext;
    CardView recCard;
    RatingBar ratingBar;

    public MViewHolder(@NonNull View itemView) {
        super(itemView);
        reccimage=itemView.findViewById(R.id.reccimage);
        stalnm=itemView.findViewById(R.id.stallnam);
        foodnm=itemView.findViewById(R.id.foodnam);
        loct=itemView.findViewById(R.id.stallLoc);
        cardView=itemView.findViewById(R.id.cardview);
        recImage =itemView.findViewById(R.id.recImage);
        recstall =itemView.findViewById(R.id.stallnm);
        recfood =itemView.findViewById(R.id.foodnm);
        recCard = itemView.findViewById(R.id.cardrec);
        ratetext = itemView.findViewById(R.id.ratetext);
        ratingBar = itemView.findViewById(R.id.ratingBar);
        addwishlist=itemView.findViewById(R.id.addwishlist);

    }
}

