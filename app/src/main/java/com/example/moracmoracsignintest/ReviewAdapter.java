package com.example.moracmoracsignintest;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder> {

    private List<Review> reviewList;

    public ReviewAdapter() {
        this.reviewList = new ArrayList<>();
    }

    public void setReviews(List<Review> reviewList) {
        this.reviewList = reviewList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_review, parent, false);
        return new ReviewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewViewHolder holder, int position) {
        Review review = reviewList.get(position);
        holder.bind(review);
    }

    @Override
    public int getItemCount() {
        return reviewList.size();
    }

    static class ReviewViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewReviewTitle;
        private TextView textViewReviewContent;
        private RatingBar ratingBarReview;

        ReviewViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewReviewTitle = itemView.findViewById(R.id.textViewReviewTitle);
            textViewReviewContent = itemView.findViewById(R.id.textViewReviewContent);
            ratingBarReview = itemView.findViewById(R.id.ratingBarReview);
        }

        void bind(Review review) {
            textViewReviewTitle.setText(review.getTitle());
            textViewReviewContent.setText(review.getContent());
            ratingBarReview.setRating(review.getRating());
        }
    }
}