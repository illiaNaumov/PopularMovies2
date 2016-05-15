package layout;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.academy.web.myapplication.BuildConfig;
import com.academy.web.myapplication.R;
import com.academy.web.popular_movies.Data.FetchMovieTrailers;
import com.academy.web.popular_movies.MainActivityFragment;
import com.academy.web.popular_movies.Movie;
import com.academy.web.popular_movies.MovieTrailerAdapter;
import com.academy.web.popular_movies.Trailer;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubeStandalonePlayer;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class MovieTrailers extends Fragment {
    private static final int REQ_START_STANDALONE_PLAYER = 1;
    private static final int REQ_RESOLVE_SERVICE_MISSING = 2;
    public MovieTrailerAdapter movieTrailerAdapter;
    private long movieApiID;
    private Movie movie;


    public MovieTrailers() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view  = inflater.inflate(R.layout.fragment_movie_trailers, container, false);

        movieTrailerAdapter = new MovieTrailerAdapter(getActivity(), new ArrayList<Trailer>());
        final ListView trailersListView = (ListView) view.findViewById(R.id.fragment_movie_detail_trailers_list_view);
        trailersListView.setAdapter(movieTrailerAdapter);
        trailersListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Trailer trailer = (Trailer) adapterView.getItemAtPosition(i);

                Intent intent = null;
                intent = YouTubeStandalonePlayer.createVideoIntent(
                        getActivity(), BuildConfig.YOUTUBE_API_KEY, trailer.key, 0, false, false);


                if (intent != null) {
                    startActivityForResult(intent, REQ_START_STANDALONE_PLAYER);
                } else {
                    // Could not resolve the intent - must need to install or update the YouTube API service.
                    YouTubeInitializationResult.SERVICE_MISSING
                            .getErrorDialog(getActivity(), REQ_RESOLVE_SERVICE_MISSING).show();
                }
            }
        });

        return view;
    }

    @Override
    public void onStart() {
        Intent intent =  getActivity().getIntent();
        if(intent.hasExtra(MainActivityFragment.EXTRA_MOVIE)){
            Movie movie = intent.getParcelableExtra(MainActivityFragment.EXTRA_MOVIE);
            updateDetailFragment(movie);
        }else {

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
        FetchMovieTrailers fetchMovieTrailers = new FetchMovieTrailers(movieTrailerAdapter, getContext());
        fetchMovieTrailers.execute(movie.movieID);
    }

}
