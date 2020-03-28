package com.example.alswn.gps;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import static com.example.alswn.gps.LogIn.MainURL;
import static com.example.alswn.gps.LogIn.userID;

public class Frag3 extends Fragment implements View.OnClickListener{

    View view;

    EditText theater;
    EditText screen;
    EditText kind;
    EditText popcorn;
    EditText clean;
    EditText review;

    final static public String theaterReviewURL = MainURL+"insertTheaterReview.php";


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState){
        view = inflater.inflate(R.layout.frag3,container,false);

        theater = (EditText) view.findViewById(R.id.theater);
        screen = (EditText) view.findViewById(R.id.editText1);
        kind = (EditText) view.findViewById(R.id.editText2);
        popcorn = (EditText) view.findViewById(R.id.editText3);
        clean = (EditText) view.findViewById(R.id.editText4);
        review = (EditText) view.findViewById(R.id.editText5);

        /* 리뷰작성 완료 버튼 */
        Button button = (Button) view.findViewById(R.id.review_complete);
        button.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View view) {
        String sTheater = theater.getText().toString().trim();
        String sScreen = screen.getText().toString();
        String sKind = kind.getText().toString();
        String sPopcorn = popcorn.getText().toString();
        String sClean = clean.getText().toString();
        String sReview = review.getText().toString();

        InsertData task = new InsertData(view.getContext());
        String serverURL = "id="+userID+"&theater="+sTheater+"&screen="+sScreen+"&kind="+sKind
                +"&taste="+sPopcorn+"&clean="+sClean+"&FreeReview="+sReview;
        task.execute(theaterReviewURL, serverURL);

        Toast.makeText(view.getContext(), "리뷰를 작성했습니다!!", Toast.LENGTH_SHORT).show();
    }

}
