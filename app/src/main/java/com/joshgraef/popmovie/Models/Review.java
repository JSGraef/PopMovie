package com.joshgraef.popmovie.Models;

import android.os.Parcel;
import android.os.Parcelable;

/***************************************************************************************************
 * Author:          jsgraef
 * Date:            28-Sep-2015
 * Description:
 **************************************************************************************************/
public class Review implements Parcelable{

    public String id;
    public String author;
    public String content;
    public String url;

    public Review() {
        super();
    }

    public Review(Parcel src) {
        id = src.readString();
        author = src.readString();
        content = src.readString();
        url = src.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int i) {
        dest.writeString(id);
        dest.writeString(author);
        dest.writeString(content);
        dest.writeString(url);
    }

    @Override
    public String toString() {
        return "Review: [" + id + "] " + url;
    }

    public static final Creator<Review> CREATOR = new Creator<Review>() {
        @Override
        public Review createFromParcel(Parcel source) {
            return new Review(source);
        }

        public Review[] newArray(int size) {
            return new Review[size];
        }
    };

}
