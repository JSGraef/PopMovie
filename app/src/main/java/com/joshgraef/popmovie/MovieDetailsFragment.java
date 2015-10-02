package com.joshgraef.popmovie;

import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.joshgraef.popmovie.Interfaces.IMovieDB;
import com.joshgraef.popmovie.Models.Movie;
import com.joshgraef.popmovie.Models.Review;
import com.joshgraef.popmovie.Models.ReviewResult;
import com.joshgraef.popmovie.Models.Video;
import com.joshgraef.popmovie.Models.VideoResult;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * A placeholder fragment containing a simple view.
 */
public class MovieDetailsFragment extends Fragment {

    @Bind(R.id.ivPoster)        ImageView movieIcon;
    @Bind(R.id.tvTitle)         TextView tvTitle;
    @Bind(R.id.tvYear)          TextView tvYear;
    @Bind(R.id.tvRating)        TextView tvRating;
    @Bind(R.id.tvOverview)      TextView tvOverview;
    @Bind(R.id.tvTrailers)      TextView tvTrailers;
    @Bind(R.id.tvReviews)       TextView tvReviews;
    @Bind(R.id.lvTrailers)      LinearLayout lvTrailers;
    @Bind(R.id.lvReviews)       LinearLayout lvReviews;

    final static String SAVED_MOVIE = "savedmovie";
    final static String SAVED_TRAILERS = "savedtrailers";
    final static String SAVED_REVIEWS = "savedreviews";

    private Movie mMovie;
    private List<Video> mVideos;
    private List<Review> mReviews;

    public MovieDetailsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_movie_details, container, false);
        ButterKnife.bind(this, v);

        Intent intent = getActivity().getIntent();

        Bundle args = getArguments();
        if(args != null && savedInstanceState != null) {
            mMovie = args.getParcelable("MOVIE");
            getTrailersAndReviews(inflater);
        }
        else if(savedInstanceState != null){
            mMovie = savedInstanceState.getParcelable(SAVED_MOVIE);
            mVideos = savedInstanceState.getParcelableArrayList(SAVED_TRAILERS);
            mReviews = savedInstanceState.getParcelableArrayList(SAVED_REVIEWS);

            for (Video video : mVideos)
                lvTrailers.addView( getVideoView(video, inflater) );

            for (Review review : mReviews)
                lvReviews.addView( getReviewView(review, inflater) );
        }
        else if (intent != null && intent.hasExtra("MOVIE")) {
            mMovie = intent.getParcelableExtra("MOVIE");
            getTrailersAndReviews(inflater);
        }
        else
            return v;


        // Populate our view with our data... if we can
        if(mMovie != null)  {

            // Load image with picasso and also darken is to the text pops out a bit
            Picasso.with(getActivity()).load(mMovie.getPosterUrl(false /* big image */)).into(movieIcon);
            //movieIcon.setColorFilter(Color.argb(190, 22, 22, 22)); // Puts a dark overlay over image

            String sTitle = mMovie.getTitle();
            tvTitle.setText(sTitle!=null? sTitle : "No Title Given");

            String sYear = mMovie.getRelease_date();
            tvYear.setText(sYear!=null? ParseYear(mMovie.getRelease_date()) : "Unknown");

            tvRating.setText("Rating: " + mMovie.getPopularity() + " / 10");

            // See if we have a mMovie description, otherwise say why.
            String sDescription = mMovie.getOverview();
            if (sDescription == null || sDescription.isEmpty())
                sDescription = "No movie description provided.";
            tvOverview.setText(sDescription);
        }


        return v;
    }

    //----------------------------------------------------------------------------------------------
    // Connect to the MovieDB API and get reviews and trailers
    private void getTrailersAndReviews(final LayoutInflater inflater) {
        RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint(IMovieDB.API_BASE_URL).build();
        IMovieDB movieDB = restAdapter.create(IMovieDB.class);

        // Need to get the movie trailers or say that we can't get them
        movieDB.getMovieVideos(Integer.parseInt(mMovie.getId()), IMovieDB.API_KEY, new retrofit.Callback<VideoResult>() {
            @Override
            public void success(VideoResult r, Response response) {
                mVideos = r.results;
                if(mVideos != null && mVideos.size() > 0)
                    for (Video v : mVideos)
                        lvTrailers.addView(getVideoView(v, inflater));
                else
                    tvTrailers.setText("No trailers available.");

            }

            @Override
            public void failure(RetrofitError error) {
                Log.e("IMovieDB", error.getMessage());
                Toast.makeText(getActivity(), "Cannot load videos...", Toast.LENGTH_LONG).show();
            }
        });

        // Now to get the reviews if there are any
        movieDB.getMovieReviews(Integer.parseInt(mMovie.getId()), IMovieDB.API_KEY, new retrofit.Callback<ReviewResult>() {
            @Override
            public void success(ReviewResult r, Response response) {
                mReviews = r.results;
                if(mReviews != null && mReviews.size() > 0)
                    for (Review rev : mReviews)
                        lvReviews.addView(getReviewView(rev, inflater));
                else
                    tvReviews.setText("No reviews yet.");

            }

            @Override
            public void failure(RetrofitError error) {
                Log.e("IMovieDB", error.getMessage());
                Toast.makeText(getActivity(), "Cannot load reviews...", Toast.LENGTH_LONG).show();
            }
        });
    }

    //----------------------------------------------------------------------------------------------
    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelable(SAVED_MOVIE, mMovie);
        outState.putParcelableArrayList(SAVED_TRAILERS, (ArrayList) mVideos);
        outState.putParcelableArrayList(SAVED_REVIEWS, (ArrayList) mReviews);
        super.onSaveInstanceState(outState);
    }

    //----------------------------------------------------------------------------------------------
    private String ParseYear(String year) {
        return year.substring(0,4);
    }

    //----------------------------------------------------------------------------------------------
    public View getVideoView(Video video, LayoutInflater inflater) {
        View view = inflater.inflate(R.layout.list_item_video_detail, null);

        TextView nameView = (TextView) view.findViewById(R.id.tvTrailerName);
        nameView.setText(video.name);

        view.setTag(R.string.tag_video, video);

        view.setOnClickListener(new AdapterView.OnClickListener() {
            @Override
            public void onClick(View view) {
                Video v = (Video) view.getTag(R.string.tag_video);
                Intent youtubeIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(v.getVideoURL()));
                startActivity(youtubeIntent);
            }
        });

        return view;
    }

    //----------------------------------------------------------------------------------------------
    public View getReviewView(Review review, LayoutInflater inflater) {
        View view = inflater.inflate(R.layout.list_item_review_detail, null);

        TextView authorView = (TextView) view.findViewById(R.id.txtAuthor);
        authorView.setText(review.author);

        TextView contentView = (TextView) view.findViewById(R.id.txtReview);
        contentView.setText(review.content);

        return view;
    }

}
