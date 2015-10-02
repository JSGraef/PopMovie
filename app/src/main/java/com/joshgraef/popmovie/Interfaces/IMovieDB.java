package com.joshgraef.popmovie.Interfaces;

import com.joshgraef.popmovie.Models.MovieResult;
import com.joshgraef.popmovie.Models.ReviewResult;
import com.joshgraef.popmovie.Models.VideoResult;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;

/***************************************************************************************************
 * Author:          jsgraef
 * Date:            28-Sep-2015
 * Description:
 **************************************************************************************************/
public interface IMovieDB {

    public static final String API_BASE_URL = "http://api.themoviedb.org/3";
    public static final String API_KEY = "";
    public static final String SORT_POP_DESC = "popularity.desc";
    public static final String SORT_RATING = "vote_average.desc";

    @GET("/discover/movie")
    public void getMovies(
            @Query("api_key") String apiKey,
            @Query("sort_by") String sortBy,
            Callback<MovieResult> result);

    @GET("/movie/{id}/videos")
    public void getMovieVideos(
            @Path("id") int movieId,
            @Query("api_key") String apiKey,
            Callback<VideoResult> result);

    @GET("/movie/{id}/reviews")
    public void getMovieReviews(
            @Path("id") int movieId,
            @Query("api_key") String apiKey,
            Callback<ReviewResult> result);

}
