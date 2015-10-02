package com.joshgraef.popmovie.Models;

import java.util.List;

/***************************************************************************************************
 * Author:          jsgraef
 * Date:            28-Sep-2015
 * Description:
 **************************************************************************************************/
public class VideoResult {
    public int id;
    public int page;
    public List<Video> results;
    public int total_pages;
    public int total_results;
}
