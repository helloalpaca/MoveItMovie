package com.example.alswn.gps;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import static java.sql.DriverManager.println;

/**
 * Created by alswn on 2018-05-25.
 */

public class database extends AppCompatActivity{
    SQLiteDatabase database;
    EditText screen;
    EditText kind;
    EditText popcorn;
    EditText clean;
    EditText review;
    public void onCreate(Bundle savedInstanceState) {
        setTitle("영화관 리뷰 적기");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_database);
        screen = (EditText) findViewById(R.id.editText1);
        kind = (EditText) findViewById(R.id.editText2);
        popcorn = (EditText) findViewById(R.id.editText3);
        clean = (EditText) findViewById(R.id.editText4);
        review = (EditText) findViewById(R.id.editText5);
        final Bundle extras = getIntent().getExtras();
        String theather = ""; //영화관의 이름을 입력받는다.
        if (extras == null) {
            theather = "error";
        } else {
            theather = extras.getString("title").trim();
        }
        theather=theather.replaceAll("\\p{Z}", "");
        String name="영화관";
        openDatabase(name);
        createTable(theather);

        //리뷰작성 완료 버튼
        Button button = (Button) findViewById(R.id.review_complete);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int screen_num = Integer.parseInt(screen.getText().toString());
                int kind_num = Integer.parseInt(kind.getText().toString());
                int popcorn_num = Integer.parseInt(popcorn.getText().toString());
                int clean_num = Integer.parseInt(clean.getText().toString());
                String review_free = review.getText().toString();
                String temp_table_name = extras.getString("title").trim().replaceAll("\\p{Z}", "");
                insertdata(screen_num, kind_num, popcorn_num, clean_num, review_free,temp_table_name);
                Toast.makeText(database.this,"점수가 삽입되었습니다",Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void openDatabase(String databaseName){
        database = openOrCreateDatabase(databaseName,MODE_PRIVATE,null);
        if(database==null){
            finish();
        }
    }
    public void insertdata(int Dsize,int Dkind,int Dpopcorn,int Dclean
        ,String Dreview,String tableName){
            String sql = "insert into "+tableName + " (size, kind, popcorn, clean , review) values(?, ?, ?, ?, ?)";
            Object[] params = {Dsize, Dkind, Dpopcorn, Dclean, Dreview};
            database.execSQL(sql,params);
            finish();
        }
    public void createTable(String tableName){
            if(database!=null){
                String sql= "create table if not exists " + tableName + " (_id integer PRIMARY KEY autoincrement, size integer, kind integer, popcorn integer, clean integer, review text);";
                database.execSQL(sql);
            }
        }

}

