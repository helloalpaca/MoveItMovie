package com.example.alswn.gps;

import android.graphics.drawable.Drawable;
import android.widget.ImageView;

public class ListViewItem {
    private Drawable iconDrawable ;
    private String titleStr ;
    private String openDtStr;
    private String saleshareStr;

    public void setIcon(Drawable icon) {
        iconDrawable = icon ;
    }
    public void setTitle(String title) {
        titleStr = title ;
    }
    public void setOpenDtStr(String openDt){
        openDtStr = openDt;
    }
    public void setSaleshareStr(String saleshare){
        saleshareStr = saleshare;
    }

    public Drawable getIcon() {
        return this.iconDrawable;
    }
    public String getTitle() {
        return this.titleStr ;
    }
    public String getOpenDtStr(){
        return this.openDtStr;
    }
    public String getSaleshareStr(){
        return this.saleshareStr;
    }

}
