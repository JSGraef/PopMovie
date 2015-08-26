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

/**
 * A placeholder fragment containing a simple view.
 */
public class MovieDetailsFragment extends Fragment {

    public MovieDetailsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_movie_details, container, false);

        Intent intent = getActivity().getIntent();
        Movie movie;

        if (intent != null && intent.hasExtra("MOVIE"))
            movie = (Movie)intent.getParcelableExtra("MOVIE");
        else
            return v;

        ImageView movieIcon = (ImageView)v.findViewById(R.id.ivPoster);
        Picasso.with(getActivity()).load(movie.getPosterUrl(true /* big image */)).into(movieIcon);
        movieIcon.setColorFilter(Color.argb(190, 22, 22, 22));

        TextView tvTitle = (TextView)v.findViewById(R.id.tvTitle);
        tvTitle.setText(movie.getmTitle());

        TextView movieYear = (TextView)v.findViewById(R.id.tvYear);
        movieYear.setText(ParseYear(movie.getmYear()));

        TextView movieVote = (TextView)v.findViewById(R.id.tvRating);
        movieVote.setText("Rating: " + movie.getmRating() + " / 10" );

        TextView movieDesc = (TextView)v.findViewById(R.id.tvOverview);
        movieDesc.setText(movie.getmOverview());

        return v;
    }

    private String ParseYear(String year) {
        return year.substring(0,4);
    }

}
