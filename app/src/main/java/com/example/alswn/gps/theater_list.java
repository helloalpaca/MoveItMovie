package com.example.alswn.gps;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.ArrayList;

public class theater_list extends AppCompatActivity {
    ListView listview;
    ArrayAdapter<String> adapter;

    //ArrayList<restaurant> storage = new ArrayList<>();
    ArrayList<String> title = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTitle("주위 영화관 목록");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_theater_list);
        //ArrayList<User> list = (ArrayList<User>) getIntent().getSerializableExtra("users");
        title=(ArrayList<String>) getIntent().getSerializableExtra("theater") ;
        init();
    }
    void init(){
        listview = (ListView)findViewById(R.id.list_view);
        adapter = new ArrayAdapter<String>(this ,android.R.layout.simple_list_item_1 , title);
        listview.setAdapter(adapter);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(theater_list.this, NewActivity.class);
                intent.putExtra("title", title.get(i));
                startActivity(intent);
            }
        });
    }
}

