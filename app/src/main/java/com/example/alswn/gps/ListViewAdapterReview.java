package com.example.alswn.gps;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class ListViewAdapterReview extends BaseAdapter{
    private ArrayList<ListViewItemReview> listViewItemListReview = new ArrayList<ListViewItemReview>() ;
    @Override
    public int getCount() {
        return listViewItemListReview.size();
    }

    @Override
    public Object getItem(int position) {
        return listViewItemListReview.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void addItem(String ID, String screen, String kind, String popcorn, String clean, String Review){
        ListViewItemReview item = new ListViewItemReview();

        item.setID(ID);
        item.setScreen(screen);
        item.setKind(kind);
        item.setPopCorn(popcorn);
        item.setClean(clean);
        item.setReview(Review);

        listViewItemListReview.add(item);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;
        final Context context = parent.getContext();

        // "listview_item" Layout을 inflate하여 convertView 참조 획득.
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.listview_review, parent, false);
        }

        TextView ID = (TextView) convertView.findViewById(R.id.reviewID);
        TextView Screen = (TextView) convertView.findViewById(R.id.reviewScreen);
        TextView Kind = (TextView) convertView.findViewById(R.id.reviewKind);
        TextView Popcorn = (TextView) convertView.findViewById(R.id.reviewPopcorn);
        TextView Clean = (TextView) convertView.findViewById(R.id.reviewClean);
        TextView Review = (TextView) convertView.findViewById(R.id.reviewReview);

        // Data Set(listViewItemList)에서 position에 위치한 데이터 참조 획득
        ListViewItemReview listViewItemReview = listViewItemListReview.get(position);

        // 아이템 내 각 위젯에 데이터 반영

        ID.setText(listViewItemReview.getID());
        Screen.setText(listViewItemReview.getScreen());
        Kind.setText(listViewItemReview.getKind());
        Popcorn.setText(listViewItemReview.getPopCorn());
        Clean.setText(listViewItemReview.getClean());
        Review.setText(listViewItemReview.getReview());

        return convertView;
    }
}
