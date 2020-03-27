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

public class Frag3 extends Fragment {

    View view;

    EditText theater;
    EditText screen;
    EditText kind;
    EditText popcorn;
    EditText clean;
    EditText review;

    String theater_name;
    String screen_num;
    String kind_num;
    String popcorn_num;
    String clean_num;
    String review_free;

    String theaterReviewURL = MainURL+"insert_theaterReview.php";


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

        //리뷰작성 완료 버튼
        Button button = (Button) view.findViewById(R.id.review_complete);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                theater_name = theater.getText().toString().trim();
                screen_num = screen.getText().toString();
                kind_num = kind.getText().toString();
                popcorn_num = popcorn.getText().toString();
                clean_num = clean.getText().toString();
                review_free = review.getText().toString();
                System.out.println("review_free = "+review_free);
                System.out.println("Free Review = "+review_free);

                GetData task = new GetData();
                task.execute(LogIn.userID,theater_name,screen_num,kind_num,popcorn_num,clean_num,review_free);
            }
        });

        return view;
    }

/*------------GetLogInData---------------*/
private class GetData extends AsyncTask<String,Void,String> {
    ProgressDialog progressDialog;
    String errorString = null;

    @Override
    protected String doInBackground(String... strings) {

        String serverURL ="id="+LogIn.userID+"&theater="+theater_name+"&screen="+screen_num+"&kind="+kind_num+"&taste="+popcorn_num+"&clean="+clean_num+"&FreeReview="+review_free;
        System.out.println("serverURL = "+serverURL);
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        try {
            URL url = new URL(theaterReviewURL);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

            httpURLConnection.setReadTimeout(5000);
            httpURLConnection.setConnectTimeout(1000);
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoInput(true);
            httpURLConnection.connect();

            OutputStream outputStream = httpURLConnection.getOutputStream();
            outputStream.write(serverURL.getBytes("UTF-8"));
            outputStream.flush();
            outputStream.close();

            int responseStatusCode = httpURLConnection.getResponseCode();
            //Log.d(TAG,"response - "+responseStatusCode);

            InputStream inputStream;
            if (responseStatusCode == HttpURLConnection.HTTP_OK) {
                inputStream = httpURLConnection.getInputStream();
            } else {
                inputStream = httpURLConnection.getErrorStream();
            }

            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            StringBuilder sb = new StringBuilder();
            String line;

            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line);
                System.out.println(line);
            }

            bufferedReader.close();

            return sb.toString().trim();

        } catch (Exception e) {
            Log.d("TAG", "InsertData : Error", e);
            errorString = e.toString();

            return null;
        }
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        //progressDialog = ProgressDialog.show(Frag3.this, "Please Wait", null, true, true);
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);

        //progressDialog.dismiss();
        Toast.makeText(getActivity(), "리뷰를 작성했습니다!!", Toast.LENGTH_SHORT).show();
    }
}

}
