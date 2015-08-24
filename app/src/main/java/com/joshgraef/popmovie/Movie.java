package com.joshgraef.popmovie;

/***************************************************************************************************
 * Author:          jsgraef
 * Date:            23-Aug-2015
 * Description:
 **************************************************************************************************/
public class Movie {

    private String  mTitle;
    private String  mPoster;
    private String  mOverview;
    private String  mYear;
    private String  mRating;

    public Movie() {
        mTitle      = "";
        mPoster     = "";
        mOverview   = "";
        mYear       = "";
        mRating     = "";
    }

    public Movie(String title, String poster, String overview, String year, String rating) {
        mTitle      = title;
        mPoster     = poster;
        mOverview   = overview;
        mYear       = year;
        mRating     = rating;
    }

    public String getPosterUrl() {
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
