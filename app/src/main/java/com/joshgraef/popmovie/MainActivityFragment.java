package com.joshgraef.popmovie;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.joshgraef.popmovie.Adapters.MovieAdapter;
import com.joshgraef.popmovie.ComplexPreferences.ComplexPreferences;
import com.joshgraef.popmovie.Interfaces.IMovieDB;
import com.joshgraef.popmovie.Models.Movie;
import com.joshgraef.popmovie.Models.MovieResult;

import java.util.ArrayList;

import retrofit.RestAdapter;
import retrofit.RetrofitError;

public class MainActivityFragment extends Fragment {

    final static String MOVIE_LIST = "movielist";
    final static String SORT_PREF = "sortpref";
    private MovieAdapter mMovieAdapter;
    private SharedPreferences mPrefs;
    private Favorites mFavorites;

    public MainActivityFragment() {
    }


    //----------------------------------------------------------------------------------------------
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mPrefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
       // mPrefs.registerOnSharedPreferenceChangeListener(this);

        //PreferenceManager.getDefaultSharedPreferences(getActivity()).registerOnSharedPreferenceChangeListener(this);

        if(mMovieAdapter == null)
            mMovieAdapter = new MovieAdapter( getActivity() );

        ComplexPreferences prefs = ComplexPreferences.getComplexPreferences(getActivity(), "favorites", Context.MODE_PRIVATE);
        mFavorites = prefs.getObject("movies", Favorites.class);

        if(savedInstanceState != null) {
            String sortby = mPrefs.getString(getString(R.string.pref_sort_by), getString(R.string.pref_sort_by_popularity));
            // if there's no discrepancy in sort type, carry on
            if (savedInstanceState.getString(SORT_PREF).compareToIgnoreCase(sortby) == 0) {
                mMovieAdapter.addMovies((ArrayList<Movie>) savedInstanceState.get(MOVIE_LIST));
                return;
            }
        }

        init();
    }

    //----------------------------------------------------------------------------------------------
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putParcelableArrayList(MOVIE_LIST, mMovieAdapter.getMovieList());
        outState.putString(SORT_PREF, mPrefs.getString(getString(R.string.pref_sort_by), getString(R.string.pref_sort_by_popularity)) );
    }

    //----------------------------------------------------------------------------------------------
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_main, container, false);

        Context c = getActivity();
        if(c != null) {
            GridView gridview = (GridView) v.findViewById(R.id.gridPosters);
            gridview.setAdapter(mMovieAdapter);

            // Make a listener to handle touch events on the poster (grid)
            gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Movie m = mMovieAdapter.getItem(position);

                    // Put parcelable intent through to new activity
                    Intent intent = new Intent(getActivity(), MovieDetails.class);
                    intent.putExtra("MOVIE", m);
                    startActivity(intent);
                }
            });
        }

        return v;
    }


    //----------------------------------------------------------------------------------------------
    // Initialization done here
    private void init() {

        // First need to see how we sort
        String sSortBy = mPrefs.getString(getString(R.string.pref_sort_by), getString(R.string.pref_sort_by_popularity));

        // If we want to see our favorites, then we don't need any network calls.
        if( sSortBy.compareToIgnoreCase("favorites") == 0) {
            mMovieAdapter.addMovies(mFavorites.getFavorites());
            return;
        }

        // Get movies from API if we have internet connection
        if(isNetworkAvailable()) {

            // This only works because we're either sorting by popularity or rating.
            // It will need to change if we offer anything more
            boolean bSortByPopularity = (sSortBy.compareToIgnoreCase("popularity") == 0);

            RestAdapter restAdapter = new RestAdapter.Builder()
                    .setEndpoint(IMovieDB.API_BASE_URL)
                    .build();

            IMovieDB movieDB = restAdapter.create(IMovieDB.class);

            movieDB.getMovies(IMovieDB.API_KEY,
                    bSortByPopularity ? IMovieDB.SORT_POP_DESC : IMovieDB.SORT_RATING,
                    new retrofit.Callback<MovieResult>() {
                @Override
                public void success(MovieResult movieResult, retrofit.client.Response response) {
                    ArrayList<Movie> movieList = movieResult.results;
                    mMovieAdapter.addMovies(movieList);
                }

                @Override
                public void failure(RetrofitError error) {
                    Log.e("IMovieDB", error.getMessage());
                    Toast.makeText(getActivity(), "Cannot load movies at this time", Toast.LENGTH_LONG).show();
                }
            });
        }
        else {
            Toast.makeText(getActivity(), "Cannot connect to internet.", Toast.LENGTH_SHORT);

            // Since no network, just show favorites since we have nothing else to show.
            mMovieAdapter.addMovies(mFavorites.getFavorites());
        }
    }

    //----------------------------------------------------------------------------------------------
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

}
