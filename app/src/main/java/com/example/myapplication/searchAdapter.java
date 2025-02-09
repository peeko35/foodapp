package com.example.myapplication;



import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class searchAdapter extends RecyclerView.Adapter<MViewHolder>{

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

        }
    }
    @Override
    public int getItemCount() {
        return dataList.size();

    }
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                List<searchmainModel> filteredResults = new ArrayList<>();

                if (constraint == null || constraint.length() == 0) {
                    filteredResults.addAll(dataList);
                } else {
                    String filterPattern = constraint.toString().toLowerCase().trim();

                    for (searchmainModel item : dataList) {
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
                filteredList.clear();
                filteredList.addAll((List<searchmainModel>) results.values);
                notifyDataSetChanged();
            }
        };
    }
}



class MViewHolder extends RecyclerView.ViewHolder{
    ImageView reccimage;
    TextView stalnm,foodnm,loct;
    CardView cardView;

    public MViewHolder(@NonNull View itemView) {
        super(itemView);
        reccimage=itemView.findViewById(R.id.reccimage);
        stalnm=itemView.findViewById(R.id.stallnam);
        foodnm=itemView.findViewById(R.id.foodnam);
        loct=itemView.findViewById(R.id.stallLoc);
        cardView=itemView.findViewById(R.id.cardview);




    }
}

