package com.joshgraef.popmovie.Models;

import java.util.List;

/***************************************************************************************************
 * Author:          jsgraef
 * Date:            28-Sep-2015
 * Description:
 **************************************************************************************************/

public class ReviewResult {
    public int mID;
    public int page;
    public List<Review> results;
    public int total_pages;
    public int total_results;
}
