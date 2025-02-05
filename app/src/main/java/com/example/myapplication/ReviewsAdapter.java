package com.example.myapplication;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.ViewHolder> {
    private Context context;
    private List<Rating> reviewList;

    public ReviewsAdapter(Context context, List<Rating> reviewList) {
        this.context = context;
        this.reviewList = reviewList;
    }

    @NonNull
    @Override
    public ReviewsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_review,parent, false);
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ReviewsAdapter.ViewHolder holder,int position) {
        Rating review = reviewList.get(position);
        holder.name.setText(review.getName());
        holder.reviewText.setText(review.getComment());
        holder.ratesBar.setRating(review.getRating());

    }

    @Override
    public int getItemCount() {
        return reviewList.size();
    }
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView name, reviewText;
        RatingBar ratesBar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.username);
            reviewText = itemView.findViewById(R.id.reviewText);
            ratesBar = itemView.findViewById(R.id.ratesbar);
        }
    }
}
