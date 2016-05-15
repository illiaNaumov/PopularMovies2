package layout;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.academy.web.myapplication.R;
import com.academy.web.popular_movies.Data.FetchMovieReviews;
import com.academy.web.popular_movies.Data.FetchMovieTrailers;
import com.academy.web.popular_movies.Items.Review;
import com.academy.web.popular_movies.MainActivityFragment;
import com.academy.web.popular_movies.Movie;
import com.academy.web.popular_movies.MovieTrailerAdapter;
import com.academy.web.popular_movies.ReviewsAdapter;
import com.academy.web.popular_movies.Trailer;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class ReviewsFragment extends Fragment {
    public ReviewsAdapter reviewAdapter;
    private long movieApiID;
    Movie movie;

    public ReviewsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view  = inflater.inflate(R.layout.fragment_reviews, container, false);

        reviewAdapter = new ReviewsAdapter(getActivity(), new ArrayList<Review>());
        final ListView reviewListView = (ListView) view.findViewById(R.id.fragment_movie_detail_reviews_list_view);
        reviewListView.setAdapter(reviewAdapter);
        return view;
    }

    @Override
    public void onStart() {
        Intent intent =  getActivity().getIntent();
        if(intent.hasExtra(MainActivityFragment.EXTRA_MOVIE)){
            Movie movie = intent.getParcelableExtra(MainActivityFragment.EXTRA_MOVIE);
            updateDetailFragment(movie);
        } else {
            Bundle b = getArguments();
            movie = b.getParcelable("movie");
            if (movie != null) {
                updateDetailFragment(movie);
            }
        }

        super.onStart();
    }

    public void updateDetailFragment(Movie movie){
        movieApiID = movie.movieID;

        FetchMovieReviews fetchMovieReviews = new FetchMovieReviews(reviewAdapter, getContext());
        fetchMovieReviews.execute(movie.movieID);

    }

}
