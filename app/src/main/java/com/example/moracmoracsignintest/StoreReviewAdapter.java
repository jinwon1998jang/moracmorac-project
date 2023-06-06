package com.example.moracmoracsignintest;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class StoreReviewAdapter extends BaseAdapter {
    private List<Review> reviewList;
    private LayoutInflater inflater;

    public StoreReviewAdapter(Context context, List<Review> reviewList) {
        this.reviewList = reviewList;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return reviewList.size();
    }

    @Override
    public Object getItem(int position) {
        return reviewList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = inflater.inflate(R.layout.list_item_review, parent, false);
        }

        TextView titleTextView = view.findViewById(R.id.title_text);
        TextView contentTextView = view.findViewById(R.id.content_text);
        TextView ratingTextView = view.findViewById(R.id.rating_text);

        Review review = reviewList.get(position);
        titleTextView.setText(review.getTitle());
        contentTextView.setText(review.getContent());
        ratingTextView.setText(String.valueOf(review.getRating()));

        return view;
    }
}