package com.academy.web.popular_movies;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.SparseArray;
import android.view.ViewGroup;

import layout.MovieInfoFragment;
import layout.MovieTrailers;
import layout.ReviewsFragment;

/**
 * Created by ilyua on 14.05.2016.
 */
public class ViewPagerAdapter extends FragmentStatePagerAdapter {
    public static final int MOVIE_INFO_FRAGMENT = 0;
    public static final int MOVIE_TRAILERS_FRAGMENT = 1;
    public static final int MOVIE_REVIEWS_FRAGMENT = 2;
    public static final int TAB_COUNT = 3;
    SparseArray<Fragment> registeredFragments = new SparseArray<Fragment>();
    private Movie movie;
    Bundle args;

    public ViewPagerAdapter(FragmentManager fm, Movie movie) {
        super(fm);
        this.movie = movie;
    }

    @Override
    public Fragment getItem(int position) {
        switch(position){
            case MOVIE_INFO_FRAGMENT:
                MovieInfoFragment movieInfoFragment = new MovieInfoFragment();
                args = new Bundle();
                args.putParcelable ("movie",movie);
                movieInfoFragment.setArguments(args);
                registeredFragments.put(position, movieInfoFragment);

                return movieInfoFragment;
            case MOVIE_TRAILERS_FRAGMENT:
                MovieTrailers trailersFragment = new MovieTrailers();
                args = new Bundle();
                args.putParcelable ("movie",movie);
                trailersFragment.setArguments(args);
                registeredFragments.put(position, trailersFragment);
                return trailersFragment;
            case MOVIE_REVIEWS_FRAGMENT:
                ReviewsFragment reviewsFragment = new ReviewsFragment();
                args = new Bundle();
                args.putParcelable ("movie",movie);
                reviewsFragment.setArguments(args);
                registeredFragments.put(position, reviewsFragment);
                return reviewsFragment;
            default:
                return null;
        }
    }


    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Fragment fragment = (Fragment) super.instantiateItem(container, position);
        registeredFragments.put(position, fragment);
        return fragment;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        registeredFragments.remove(position);
        super.destroyItem(container, position, object);
    }

    @Override
    public int getCount() {
        return TAB_COUNT;
    }

    public Fragment getRegisteredFragment(int position) {
        return registeredFragments.get(position);
    }
}
