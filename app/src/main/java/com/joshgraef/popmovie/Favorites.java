package com.joshgraef.popmovie;

import com.joshgraef.popmovie.Models.Movie;

import java.util.ArrayList;

/***************************************************************************************************
 * Author:          jsgraef
 * Date:            02-Oct-2015
 * Description:
 **************************************************************************************************/
public class Favorites {
    public ArrayList<Movie> favorites = new ArrayList<>();

    public Favorites() {
    }

    public ArrayList<Movie> getFavorites() { return favorites; }

    public void setFavorites(ArrayList<Movie> movies) {
        favorites = movies;
    }

    // Add movie to favorites list
    public void add(Movie m) {
        if(m != null)
            favorites.add(m);
    }

    // Get rid of particular movie
    public void remove(String id) {
        for(Movie movie : favorites ) {
            if (movie.getId().compareToIgnoreCase(id) == 0) {
                favorites.remove(movie);
                return;
            }
        }
    }

    // Check through favorite list for a particular movie id
    public boolean isMovieAFavorite(String id) {
        for(Movie movie : favorites )
            if(movie.getId().compareToIgnoreCase(id) == 0)
                return true;

        return false;
    }
}
