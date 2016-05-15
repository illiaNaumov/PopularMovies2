package com.academy.web.popular_movies;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.academy.web.myapplication.R;
import com.academy.web.popular_movies.Items.Review;

import java.util.ArrayList;

/**
 * Created by ilyua on 14.05.2016.
 */
public class ReviewsAdapter extends ArrayAdapter<Review>{


    public ReviewsAdapter(Context context, ArrayList<Review> users) {
        super(context, 0, users);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Review review = (Review) getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_movie_review, parent, false);
        }

        TextView author = (TextView) convertView.findViewById(R.id.item_movie_review_reviewerName);
        if (author != null) {
            author.setText("Author: " + review.reviewerName);
            TextView content = (TextView) convertView.findViewById(R.id.item_movie_review_reviewText);
            content.setText(review.reviewText);
        }
        return convertView;
    }

    @Override
    public Review getItem(int position) {
        return super.getItem(position);
    }

}

