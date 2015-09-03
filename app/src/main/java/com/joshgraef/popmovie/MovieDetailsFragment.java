package com.joshgraef.popmovie;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * A placeholder fragment containing a simple view.
 */
public class MovieDetailsFragment extends Fragment {

    @Bind(R.id.ivPoster) ImageView movieIcon;
    @Bind(R.id.tvTitle) TextView tvTitle;
    @Bind(R.id.tvYear) TextView tvYear;
    @Bind(R.id.tvRating) TextView tvRating;
    @Bind(R.id.tvOverview) TextView tvOverview;

    public MovieDetailsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_movie_details, container, false);
        ButterKnife.bind(this, v);

        Intent intent = getActivity().getIntent();
        Movie movie;

        if (intent != null && intent.hasExtra("MOVIE"))
            movie = intent.getParcelableExtra("MOVIE");
        else
            return v;

        // Load image with picasso and also darken is to the text pops out a bit
        Picasso.with(getActivity()).load(movie.getPosterUrl(true /* big image */)).into(movieIcon);
        movieIcon.setColorFilter(Color.argb(190, 22, 22, 22));

        tvTitle.setText(movie.getmTitle());
        tvYear.setText(ParseYear(movie.getmYear()));
        tvRating.setText("Rating: " + movie.getmRating() + " / 10" );
        tvOverview.setText(movie.getmOverview());

        return v;
    }

    private String ParseYear(String year) {
        return year.substring(0,4);
    }

}
