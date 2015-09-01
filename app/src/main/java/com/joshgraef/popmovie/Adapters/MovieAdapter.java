package com.joshgraef.popmovie.Adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.joshgraef.popmovie.Movie;
import com.joshgraef.popmovie.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/***************************************************************************************************
 * Author:          jsgraef
 * Date:            20-Aug-2015
 * Description:
 **************************************************************************************************/

public class MovieAdapter extends BaseAdapter {

    private ArrayList<Movie> mMovies;
    private Context mContext;

    //----------------------------------------------------------------------------------------------
    public MovieAdapter(Context c) {
        mContext = c;
        mMovies = new ArrayList<>();
    }

    //----------------------------------------------------------------------------------------------
    public MovieAdapter(Context c, ArrayList<Movie> data) {
        mContext = c;
        mMovies = data;
    }

    //----------------------------------------------------------------------------------------------
    public void clear() {
        mMovies.clear();
        notifyDataSetChanged();
    }

    //----------------------------------------------------------------------------------------------
    public void addMovies(ArrayList<Movie> movies) {
        mMovies.addAll(movies);
        notifyDataSetChanged();
    }

    //----------------------------------------------------------------------------------------------
    public ArrayList<Movie> getMovieList() {
        return mMovies;
    }

    //----------------------------------------------------------------------------------------------
    @Override
    public int getCount() {
        return mMovies.size();
    }

    //----------------------------------------------------------------------------------------------
    public Movie getItem(int position) {
        if(position < 0 || position >= mMovies.size() )
            return null;

        return mMovies.get(position);
    }

    //----------------------------------------------------------------------------------------------
    public long getItemId(int position) {
        return 0;
    }

    //----------------------------------------------------------------------------------------------
    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = ((Activity)mContext).getLayoutInflater();

        // Get the movie
        Movie movie = mMovies.get(position);

        View gv = (convertView != null) ? convertView : inflater.inflate(R.layout.griditem_movie, null);

        ImageView imageView = (ImageView)gv.findViewById(R.id.griditem_movie);
        Picasso.with(mContext).load(movie.getPosterUrl(false /* big image */)).fit().into(imageView);

        return gv;
    }

}