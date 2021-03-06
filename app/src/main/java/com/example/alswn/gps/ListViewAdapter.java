package com.example.alswn.gps;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class ListViewAdapter extends BaseAdapter{
    private ArrayList<ListViewItem> listViewItemList = new ArrayList<ListViewItem>() ;

    @Override
    public int getCount() {
        return listViewItemList.size();
    }

    @Override
    public Object getItem(int position) {
        return listViewItemList.get(position);
    }

    public String getItemTitle(int position) {
        return listViewItemList.get(position).getTitle();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void addItem(Drawable icon, String title,String openDt, String saleshare){
        ListViewItem item = new ListViewItem();

        item.setIcon(icon);
        item.setTitle(title);
        item.setOpenDtStr(openDt);
        item.setSaleshareStr(saleshare);

        listViewItemList.add(item);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;
        final Context context = parent.getContext();

        // "listview_item" Layout을 inflate하여 convertView 참조 획득.
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.listview_item, parent, false);
        }

        // 화면에 표시될 View(Layout이 inflate된)으로부터 위젯에 대한 참조 획득
        ImageView iconImageView = (ImageView) convertView.findViewById(R.id.imageView1) ;
        TextView titleTextView = (TextView) convertView.findViewById(R.id.textview1) ;
        TextView openDtTextView = (TextView) convertView.findViewById(R.id.textview3) ;
        TextView saleshareTextView = (TextView) convertView.findViewById(R.id.textview4) ;

        // Data Set(listViewItemList)에서 position에 위치한 데이터 참조 획득
        ListViewItem listViewItem = listViewItemList.get(position);

        // 아이템 내 각 위젯에 데이터 반영
        iconImageView.setImageDrawable(listViewItem.getIcon());
        titleTextView.setText(listViewItem.getTitle());
        openDtTextView.setText(listViewItem.getOpenDtStr());
        saleshareTextView.setText(listViewItem.getSaleshareStr());


        return convertView;
    }

    public void clear(){
        listViewItemList.clear();
    }

}
