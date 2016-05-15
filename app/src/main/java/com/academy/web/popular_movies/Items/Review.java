package com.academy.web.popular_movies.Items;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by ilyua on 14.05.2016.
 */
public class Review implements Parcelable{
    public String reviewerName;
    public String reviewText;

    public Review(){}

    protected Review(Parcel in) {
        reviewerName = in.readString();
        reviewText = in.readString();
    }

    public static final Creator<Review> CREATOR = new Creator<Review>() {
        @Override
        public Review createFromParcel(Parcel in) {
            return new Review(in);
        }

        @Override
        public Review[] newArray(int size) {
            return new Review[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(reviewerName);
        dest.writeString(reviewText);
    }
}
