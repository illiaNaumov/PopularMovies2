package com.academy.web.popular_movies;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.academy.web.myapplication.BuildConfig;
import com.academy.web.myapplication.R;
import com.academy.web.popular_movies.Data.FetchMovieTrailers;
import com.academy.web.popular_movies.Data.MovieContract;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubeStandalonePlayer;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import layout.MovieInfoFragment;
import layout.MovieTrailers;
import layout.ReviewsFragment;


/**
 * A placeholder fragment containing a simple view.
 */
public class MovieDetailActivityFragment extends Fragment {
    @Bind(R.id.detail_fragment_favorite_movie_button) FloatingActionButton favoriteMovieButton;

    Context mContext;
    private long movieApiID;
    Movie movie;





    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ViewPagerAdapter viewPagerAdapter;


    private static final  String [] projection = {
            MovieContract.FavoriteMovieEntry.MOVIES_TABLE_NAME + "." + MovieContract.FavoriteMovieEntry._ID,
            MovieContract.FavoriteMovieEntry.MOVIE_ID,
            MovieContract.FavoriteMovieEntry.TITLE,
            MovieContract.FavoriteMovieEntry.POSTER,
            MovieContract.FavoriteMovieEntry.RELEASE_DATE,
            MovieContract.FavoriteMovieEntry.PLOT_SYNOPSIS,
            MovieContract.FavoriteMovieEntry.VOTE_AVERAGE

    };


    public MovieDetailActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.fragment_movie_detail, container, false);


        tabLayout = (TabLayout) view.findViewById(R.id.tab_layout);
        viewPager = (ViewPager) view.findViewById(R.id.pager);

        viewPagerAdapter = new ViewPagerAdapter(getFragmentManager(), movie);
        viewPager.setAdapter(viewPagerAdapter);
        viewPager.setOffscreenPageLimit(viewPagerAdapter.getCount());


        final TabLayout.Tab movieInfoTab = tabLayout.newTab();
        final TabLayout.Tab trailersTab = tabLayout.newTab();
        final TabLayout.Tab reviewsTab = tabLayout.newTab();



        movieInfoTab.setText("Info");
        trailersTab.setText("Trailers");
        reviewsTab.setText("Reviews");

        tabLayout.addTab(movieInfoTab, 0);
        tabLayout.addTab(trailersTab, 1);
        tabLayout.addTab(reviewsTab, 2);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });



        ButterKnife.bind(this, view);
        favoriteMovieButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(movie!= null) {
                    if (isFilmFavorite()) {
                        deleteFilmFromFavorites();
                        favoriteMovieButton.setImageResource(R.mipmap.ic_favorite_white_48dp);
                        Toast.makeText(getContext(), R.string.detail_fragment_film_deleted_from_favorites, Toast.LENGTH_SHORT).show();
                    } else {
                        markFilmAsFavorite();
                        favoriteMovieButton.setImageResource(R.mipmap.ic_favorite_black_48dp);
                        Toast.makeText(getContext(), R.string.detail_fragment_text_marked_as_favorite, Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });



        return view;
    }


    @Override
    public void onStart() {
        Intent intent =  getActivity().getIntent();
        if(intent.hasExtra(MainActivityFragment.EXTRA_MOVIE)){
            movie = intent.getParcelableExtra(MainActivityFragment.EXTRA_MOVIE);
            movieApiID = movie.movieID;
            updateDetailFragment(movie);
        }

        super.onStart();
    }

    @Override
    public void onResume() {
        updateDetailFragment(movie);
        super.onResume();
    }

    public void updateDetailFragment(Movie movie){
        this.movie = movie;

        if(movie != null && isFilmFavorite()){
            favoriteMovieButton.setImageResource(R.mipmap.ic_favorite_black_48dp);
        }else{
            favoriteMovieButton.setImageResource(R.mipmap.ic_favorite_white_48dp);
        }

    }

    public void updateTabbedFragments(Movie movie){
        MovieInfoFragment  movieInfoFragment = (MovieInfoFragment) viewPagerAdapter.getRegisteredFragment(ViewPagerAdapter.MOVIE_INFO_FRAGMENT);
        MovieTrailers movieTrailersFragment = (MovieTrailers) viewPagerAdapter.getRegisteredFragment(ViewPagerAdapter.MOVIE_TRAILERS_FRAGMENT);
        ReviewsFragment movieReviewsFragment = (ReviewsFragment) viewPagerAdapter.getRegisteredFragment(ViewPagerAdapter.MOVIE_REVIEWS_FRAGMENT);
        movieInfoFragment.updateDetailFragment(movie);
        movieTrailersFragment.updateDetailFragment(movie);
        movieReviewsFragment.updateDetailFragment(movie);
    }



    public boolean isFilmFavorite(){
        boolean isFavorite = false;
        Cursor cursor = getContext().getContentResolver().query(MovieContract.FavoriteMovieEntry.buildMovieApiIDUri(movie.movieID),
                projection,
                null,
                null,
                null);
        if(cursor.moveToFirst()){
            isFavorite = true;
        }
        return isFavorite;
    }

    public void markFilmAsFavorite(){
        ContentValues movieValues = new ContentValues();
        movieValues.put(MovieContract.FavoriteMovieEntry.MOVIE_ID, movie.movieID);
        movieValues.put(MovieContract.FavoriteMovieEntry.TITLE, movie.movieTitle);
        movieValues.put(MovieContract.FavoriteMovieEntry.POSTER, movie.posterImageLink);
        movieValues.put(MovieContract.FavoriteMovieEntry.RELEASE_DATE, movie.releaseDate);
        movieValues.put(MovieContract.FavoriteMovieEntry.PLOT_SYNOPSIS, movie.plotSynopsis);
        movieValues.put(MovieContract.FavoriteMovieEntry.VOTE_AVERAGE, movie.userRating);

        getContext().getContentResolver().insert(MovieContract.FavoriteMovieEntry.CONTENT_URI, movieValues);
    }

    public void deleteFilmFromFavorites(){
        String [] selection = {String.valueOf(movieApiID)};
        getContext().getContentResolver().delete(MovieContract.FavoriteMovieEntry.buildMovieApiIDUri(movieApiID), null, selection);
    }


}
