package com.example.alswn.gps;

import android.graphics.drawable.Drawable;

public class ListViewItemReview {
    private String ID ;
    private String Screen ;
    private String Kind;
    private String PopCorn;
    private String Clean;
    private String Review;

    public void setID(String id) { ID = id; }
    public void setScreen(String screen) { Screen = screen; }
    public void setKind(String kind) { Kind = kind; }
    public void setPopCorn(String popcorn) { PopCorn = popcorn; }
    public void setClean(String clean ) { Clean = clean; }
    public void setReview(String review) { Review = review; }

    public String getID() { return this.ID; }
    public String getScreen() { return this.Screen; }
    public String getKind() { return this.Kind; }
    public String getPopCorn() { return this.PopCorn; }
    public String getClean() { return this.Clean; }
    public String getReview() { return this.Review; }
}
