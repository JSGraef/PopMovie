package com.joshgraef.popmovie.Models;

import android.os.Parcel;
import android.os.Parcelable;

/***************************************************************************************************
 * Author:          jsgraef
 * Date:            23-Aug-2015
 * Description:
 **************************************************************************************************/
public class Movie implements Parcelable {

    // Must match json for a clean conversion
    private String title;
    private String poster_path;
    private String overview;
    private String release_date;
    private String popularity;
    private String id;

    public Movie() {
        title = "";
        poster_path = "";
        overview = "";
        release_date = "";
        popularity = "";
        id = "";
    }

    public Movie(String title, String poster, String overview, String year, String rating, String id) {
        this.title      = title;
        poster_path     = poster;
        this.overview   = overview;
        release_date    = year;
        popularity      = rating;
        this.id         = id;
    }

    public Movie(Parcel parcel) {
        title           = parcel.readString();
        poster_path     = parcel.readString();
        overview        = parcel.readString();
        release_date    = parcel.readString();
        popularity      = parcel.readString();
        id              = parcel.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {

        parcel.writeString(title);
        parcel.writeString(poster_path);
        parcel.writeString(overview);
        parcel.writeString(release_date);
        parcel.writeString(popularity);
        parcel.writeString(id);
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
            return "http://image.tmdb.org/t/p/w780" + getPoster_path();
        else
            return "http://image.tmdb.org/t/p/w185" + getPoster_path();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPoster_path() {
        return poster_path;
    }

    public void setPoster_path(String poster_path) {
        this.poster_path = poster_path;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getId() {
        return id;
    }

    public String getRelease_date() {
        return release_date;
    }

    public void setRelease_date(String release_date) {
        this.release_date = release_date;
    }

    public String getPopularity() {
        return popularity;
    }

    public void setPopularity(String popularity) {
        this.popularity = popularity;
    }
}
