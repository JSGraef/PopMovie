package com.joshgraef.popmovie;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.joshgraef.popmovie.Models.Movie;

public class MainActivity extends Activity implements MainActivityFragment.Callbacks{

    private boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTwoPane = (findViewById(R.id.movie_detail_container) != null);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onMovieSelected(Movie movie) {
        if (mTwoPane) {
            Bundle args = new Bundle();
            args.putParcelable(MovieDetailsFragment.MOVIE_DETAILS, movie);

            MovieDetailsFragment movieDetails = new MovieDetailsFragment();
            movieDetails.setArguments(args);

            // Send to movie details fragment
            getFragmentManager().beginTransaction()
                    .replace(R.id.movie_detail_container, movieDetails)
                    .commit();
        } else {
            Intent intent = new Intent(this, MovieDetails.class).putExtra(MovieDetailsFragment.MOVIE_DETAILS, movie);
            startActivity(intent);
        }
    }
}
