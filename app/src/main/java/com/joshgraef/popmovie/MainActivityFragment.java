package com.joshgraef.popmovie;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.joshgraef.popmovie.Adapters.MovieAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    private MovieAdapter mMovieAdapter;

    public MainActivityFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        init();
    }

    //----------------------------------------------------------------------------------------------
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_main, container, false);

        Context c = getActivity();
        if(c != null) {
            GridView gridview = (GridView) v.findViewById(R.id.gridPosters);

            if(mMovieAdapter == null)
                mMovieAdapter = new MovieAdapter(c);

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
        // Get movies from API
        FetchMovieTask fetchMovieTask = new FetchMovieTask();
        fetchMovieTask.execute();
    }

    //----------------------------------------------------------------------------------------------
    private ArrayList<Movie> getMoviesFromJSON(String sMovieList)
        throws JSONException {

        if(sMovieList == null)
            return null;

        ArrayList<Movie> movieList = new ArrayList<>();

        // Defining constants in case they change...
        final String ML_RESULTS     = "results";
        final String ML_POSTER      = "poster_path";
        final String ML_OVERVIEW    = "overview";
        final String ML_DATE        = "release_date";
        final String ML_TITLE       = "title";
        final String ML_RATING      = "vote_average";

        JSONObject movies = new JSONObject(sMovieList);
        JSONArray resultsArr = movies.getJSONArray(ML_RESULTS);

        // Traverse JSON result array and add movies as we get them
        int length = resultsArr.length();
        for(int i=0; i<length; i++) {
            JSONObject obj = resultsArr.getJSONObject(i);

            String sPosterPath  = obj.getString(ML_POSTER);
            String sOverview    = obj.getString(ML_OVERVIEW);
            String sDate        = obj.getString(ML_DATE);
            String sTitle       = obj.getString(ML_TITLE);
            String sRating      = obj.getString(ML_RATING);

            // Add this movie to the movie manager list
            Movie movie = new Movie(sTitle, sPosterPath, sOverview, sDate, sRating);
            movieList.add(movie);
        }

        return movieList;
    }

    //----------------------------------------------------------------------------------------------
    private class FetchMovieTask extends AsyncTask<Movie, Void, ArrayList<Movie>> {

        private final String LOG_TAG = FetchMovieTask.class.getSimpleName();

        @Override
        protected ArrayList<Movie> doInBackground(Movie... params) {
            // These two need to be declared outside the try/catch
            // so that they can be closed in the finally block.
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            // Will contain the raw JSON response as a string.
            String movieJSONString = null;

            try {
                // Keeping to show what URL looks like
                // URL url = new URL("http://api.themoviedb.org/3/discover/movie?sort_by=popularity.desc&api_key=[API KEY]");

                final String MOVIEDB_BASEURL = "http://api.themoviedb.org/3/discover/movie";
                final String API_KEY = ""; // TODO ADD API KEY HERE
                final String SORT_POP_DESC = "popularity.desc";
                final String SORT_POP_ASC = "popularity.asc";

                Uri uri = Uri.parse(MOVIEDB_BASEURL)
                        .buildUpon()
                        .appendQueryParameter("sort_by", SORT_POP_DESC) // setting.sortbyasc ? SORT_POP_ASC : SORT_POP_DESC
                        .appendQueryParameter("api_key", API_KEY)
                        .build();

                URL url = new URL(uri.toString());


                // Create the request to Movie Database, and open the connection
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null)
                    return null;

                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null)
                    buffer.append(line + "\n");

                if (buffer.length() == 0)
                    return null;

                movieJSONString = buffer.toString();

            } catch (IOException e) {
                Log.e(LOG_TAG, "Error ", e);
                return null;
            } finally {
                if (urlConnection != null)
                    urlConnection.disconnect();

                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e(LOG_TAG, "Error closing stream", e);
                    }
                }
            }

            // This will try to parse the json we received and kickstart the poster viewing process
            try
                { return getMoviesFromJSON(movieJSONString); }
            catch (JSONException e) { Log.e("JSON ERROR: ", "error", e); }

            Log.d(LOG_TAG, "JSONSTRING: " + movieJSONString, null);
            return null;
        }

        @Override
        protected void onPostExecute(ArrayList<Movie> movieList) {
            super.onPostExecute(movieList);

            // If list is empty, just get out
            if(movieList == null)
                return;

            // Update adapter
            mMovieAdapter.clear();
            mMovieAdapter.addMovies(movieList);

        }
    }
}
