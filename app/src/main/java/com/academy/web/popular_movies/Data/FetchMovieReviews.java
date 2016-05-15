package com.academy.web.popular_movies.Data;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;

import com.academy.web.myapplication.BuildConfig;
import com.academy.web.popular_movies.Items.Review;
import com.academy.web.popular_movies.MovieTrailerAdapter;
import com.academy.web.popular_movies.ReviewsAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by ilyua on 14.05.2016.
 */
public class FetchMovieReviews extends AsyncTask<Long, Void, Review[]> {
    public ReviewsAdapter moviewReviewAdapter;
    public Context context;
    final String BASE_REVIEW_URL = "http://api.themoviedb.org/3/movie/";

    public FetchMovieReviews(ReviewsAdapter moviewReviewAdapter, Context context) {
        this.moviewReviewAdapter = moviewReviewAdapter;
        this.context = context;
    }

    @Override
    protected Review[] doInBackground(Long... params) {
        long movieID = params[0];
        Review [] reviews = null;

        try{
            String reviewsJSONString = getReviews(movieID);
            reviews = getReviewsFromJSON(reviewsJSONString);


        }catch(Exception e){
            e.printStackTrace();
        }

        return reviews;
    }

    private String getReviews(long movieID){
        String reviewsJSONString = null;
        URL url = null;
        HttpURLConnection httpURLConnection = null;
        BufferedReader bufferedReader = null;

        try{
            Uri buildUri = Uri.parse(BASE_REVIEW_URL).buildUpon().appendPath(String.valueOf(movieID))
                    .appendPath("reviews")
                    .appendQueryParameter("api_key", BuildConfig.POPULAR_MOVIE_API_KEY)
                    .build();

            url = new URL(buildUri.toString());

            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.connect();

            InputStream inputStream = httpURLConnection.getInputStream();
            StringBuffer stringBuffer = new StringBuffer();
            if(inputStream == null){
                return null;
            }
            bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while((line = bufferedReader.readLine()) != null){
                stringBuffer.append(line + "\n");
            }

            if(stringBuffer.length() == 0){
                reviewsJSONString = null;
            }

            reviewsJSONString = stringBuffer.toString();

        }catch(Exception e){
            e.printStackTrace();
        }finally{

        }
        return reviewsJSONString;
    }

    private Review [] getReviewsFromJSON(String trailersJSONString) throws JSONException {
        final String RESULTS = "results";
        final String AUTHOR = "author";
        final String CONTENT = "content";

        JSONObject jsonObject = new JSONObject(trailersJSONString);
        JSONArray jsonArray = jsonObject.getJSONArray(RESULTS);
        Review [] reviews = new Review[jsonArray.length()];

        for(int i = 0; i < reviews.length; i++){
            JSONObject jsonTrailerObject = jsonArray.getJSONObject(i);
            Review review = new Review();
            review.reviewerName = jsonTrailerObject.getString(AUTHOR);
            review.reviewText = jsonTrailerObject.getString(CONTENT);
            reviews[i] = review;
        }

        return reviews;


    }

    @Override
    protected void onPostExecute(Review[] reviews) {
        if(moviewReviewAdapter != null && reviews != null){
            moviewReviewAdapter.clear();
            for(Review review: reviews){
                moviewReviewAdapter.add(review);
            }
        }
    }
}
