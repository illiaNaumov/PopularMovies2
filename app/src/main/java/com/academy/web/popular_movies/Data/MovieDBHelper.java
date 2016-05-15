package com.academy.web.popular_movies.Data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.academy.web.popular_movies.Data.MovieContract.FavoriteMovieEntry;
import com.academy.web.popular_movies.Data.MovieContract.FavoriteMovieTrailers;

/**
 * Created by ilyua on 19.04.2016.
 */
public class MovieDBHelper extends SQLiteOpenHelper{
    // If you change the database schema, you must increment the database version.
    private static final int DATABASE_VERSION = 6;

    static final String DATABASE_NAME = "movies.db";

    public MovieDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String SQL_CREATE_FAVORITE_MOVIE_TABLE = "CREATE TABLE " + FavoriteMovieEntry.MOVIES_TABLE_NAME + " (" +
                FavoriteMovieEntry._ID + " INTEGER PRIMARY KEY, " +
                FavoriteMovieEntry.MOVIE_ID + " INTEGER, " +
                FavoriteMovieEntry.TITLE + " TEXT, " +
                FavoriteMovieEntry.POSTER + " TEXT, " +
                FavoriteMovieEntry.RELEASE_DATE + " REAL, " +
                FavoriteMovieEntry.PLOT_SYNOPSIS + " TEXT, " +
                FavoriteMovieEntry.POPULARITY + " REAL, " +
                FavoriteMovieEntry.VOTE_AVERAGE + " REAL " +
                " );";

        final String SQL_CREATE_FAVORITE_MOVIE_TRAILERS_TABLE = "CREATE TABLE " + FavoriteMovieTrailers.MOVIE_TRAILERS_TABLE_NAME + " (" +
                FavoriteMovieTrailers._ID + " INTEGER PRIMARY KEY, " +
                FavoriteMovieTrailers.MOVIE_ID + " INTEGER, " +
                FavoriteMovieTrailers.TITLE + " TEXT, " +
                FavoriteMovieTrailers.KEY + " TEXT " +
                " );";

        sqLiteDatabase.execSQL(SQL_CREATE_FAVORITE_MOVIE_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_FAVORITE_MOVIE_TRAILERS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + FavoriteMovieEntry.MOVIES_TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + FavoriteMovieTrailers.MOVIE_TRAILERS_TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

}