package com.joshgraef.popmovie;

import android.os.Parcel;
import android.os.Parcelable;

/***************************************************************************************************
 * Author:          jsgraef
 * Date:            23-Aug-2015
 * Description:
 **************************************************************************************************/
public class Movie implements Parcelable {

    private String  mTitle;
    private String  mPoster;
    private String  mOverview;
    private String  mYear;
    private String  mRating;
    private String  mMovieID;

    public Movie() {
        mTitle      = "";
        mPoster     = "";
        mOverview   = "";
        mYear       = "";
        mRating     = "";
        mMovieID    = "";
    }

    public Movie(String title, String poster, String overview, String year, String rating, String id) {
        mTitle      = title;
        mPoster     = poster;
        mOverview   = overview;
        mYear       = year;
        mRating     = rating;
        mMovieID    = id;
    }

    public Movie(Parcel parcel) {
        mTitle      = parcel.readString();
        mPoster     = parcel.readString();
        mOverview   = parcel.readString();
        mYear       = parcel.readString();
        mRating     = parcel.readString();
        mMovieID    = parcel.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {

        parcel.writeString(mTitle);
        parcel.writeString(mPoster);
        parcel.writeString(mOverview);
        parcel.writeString(mYear);
        parcel.writeString(mRating);
        parcel.writeString(mMovieID);
    }

    public static final Parcelable.Creator<Movie> CREATOR = new Parcelable.Creator<Movie>() {
        public Movie createFromParcel(Parcel parcel) {
            return new Movie(parcel);
        }

        public Movie[] newArray(int i) {
            return new Movie[i];
        }
    };

    public String getPosterUrl(boolean bBigImage) {
        if(bBigImage)
            // I would do "original" but a lot of the posters are missing...
            return "http://image.tmdb.org/t/p/w780" + getmPoster();
        else
            return "http://image.tmdb.org/t/p/w185" + getmPoster();
    }

    public String getmTitle() {
        return mTitle;
    }

    public void setmTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public String getmPoster() {
        return mPoster;
    }

    public void setmPoster(String mPoster) {
        this.mPoster = mPoster;
    }

    public String getmOverview() {
        return mOverview;
    }

    public void setmOverview(String mOverview) {
        this.mOverview = mOverview;
    }

    public String getmYear() {
        return mYear;
    }

    public void setmYear(String mYear) {
        this.mYear = mYear;
    }

    public String getmRating() {
        return mRating;
    }

    public void setmRating(String mRating) {
        this.mRating = mRating;
    }
}
