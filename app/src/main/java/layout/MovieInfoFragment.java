package layout;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.academy.web.myapplication.R;
import com.academy.web.popular_movies.Data.FetchMovieTrailers;
import com.academy.web.popular_movies.MainActivityFragment;
import com.academy.web.popular_movies.Movie;
import com.academy.web.popular_movies.MovieAdapter;
import com.squareup.picasso.Picasso;

import butterknife.Bind;
import butterknife.ButterKnife;


public class MovieInfoFragment extends Fragment {
    @Bind(R.id.detail_fragment_text_movie_title) TextView movieTitle;
    @Bind(R.id.detail_fragment_text_release_date) TextView movieYear;
    @Bind(R.id.detail_fragment_text_rating) TextView movieRating;
    @Bind(R.id.detail_fragment_text_synopsis) TextView movieSynopsis;
    @Bind(R.id.detail_fragment_poster_image) ImageView imageView;

    private String imageURL;
    private Movie movie;

    public MovieInfoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_movie_info, container, false);
        ButterKnife.bind(this, view);
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

    @Override
    public void onResume() {
        super.onResume();
    }

    public void updateDetailFragment(Movie movie){
        movieTitle.setText(movie.movieTitle);
        movieYear.setText(movie.releaseDate.substring(0,4));
        movieRating.setText(String.valueOf(movie.userRating).concat("/10"));
        movieSynopsis.setText(String.valueOf(movie.plotSynopsis));

        imageURL = MovieAdapter.FORECAST_BASE_URL + movie.posterImageLink;
        Picasso.with(getContext()).load(imageURL).
                into(imageView);
    }
}
