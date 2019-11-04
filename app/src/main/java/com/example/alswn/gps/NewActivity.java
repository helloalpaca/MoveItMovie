package com.example.alswn.gps;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import static com.example.alswn.gps.LogIn.MainURL;
import static java.sql.DriverManager.println;

public class NewActivity extends AppCompatActivity {
    String title = "";
    String address = "";

    String theaterReview_URL = MainURL+"show_theaterReview.php";
    TextView screen;
    TextView kind;
    TextView popcorn;
    TextView clean;
    String mJsonString;
    TextView numOfReview;

    String screen_total;
    String kind_total;
    String popcorn_total;
    String clean_total;
    String num_of_review;

    ListView listview3;
    ListViewAdapterReview adapter;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new);

        Bundle extras = getIntent().getExtras(); //영화관 여러개 받아옴
        if (extras == null) {
            title = "error"; //영화관 이름
        }
        else {
            title = extras.getString("title");
            address = extras.getString("address");
        }

        adapter = new ListViewAdapterReview() ;
        // 리스트뷰 참조 및 Adapter달기
        listview3 = (ListView) findViewById(R.id.listview3);

        screen = (TextView)findViewById(R.id.newActivity_screen);
        kind = (TextView)findViewById(R.id.newActivity_kind);
        popcorn = (TextView)findViewById(R.id.newActivity_popcorn);
        clean = (TextView)findViewById(R.id.newActivity_clean);
        numOfReview = (TextView)findViewById(R.id.newActivity_num_of_Review) ;

        GetData task = new GetData();
        task.execute(title);

        GetData2 task2 = new GetData2();
        task2.execute(title);
    }

    private class GetData extends AsyncTask<String,Void,String> {
        ProgressDialog progressDialog;
        String errorString = null;

        @Override
        protected String doInBackground(String... strings) {

            String serverURL = "theater="+title;
            if (android.os.Build.VERSION.SDK_INT > 9) {
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);
            }

            try {
                URL url = new URL(theaterReview_URL);
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
                    //System.out.println(line);
                }

                bufferedReader.close();

                return sb.toString().trim();

            } catch (Exception e) {
                Log.d("TAG", "Show Data : Error", e);
                errorString = e.toString();

                return null;
            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = ProgressDialog.show(NewActivity.this, "Please Wait", null, true, true);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            progressDialog.dismiss();

            if (result == null) {
                screen.setText(errorString);
            }
            else {
                mJsonString = result;
                showResult();
                System.out.print(result);

                int numberOfReview = Integer.parseInt(num_of_review);

                float screen_score =  (float)Integer.parseInt(screen_total)/(float)numberOfReview;
                float kind_score = (float)Integer.parseInt(kind_total)/(float)numberOfReview;
                float popcorn_score = (float)Integer.parseInt(popcorn_total)/(float)numberOfReview;
                float clean_score = (float)Integer.parseInt(clean_total)/(float)numberOfReview;

                System.out.print("screen score = "+screen_score);
                screen.setText("screen 점수는"+screen_score+"점 입니다");
                kind.setText("친절도 점수는 " + kind_score +"점 입니다");
                popcorn.setText("매점 음식 점수는 " + popcorn_score +"점 입니다");
                clean.setText("청결도 점수는 " + clean_score +"점 입니다");
                numOfReview.setText("총 리뷰 수는" + num_of_review +"개 입니다");

            }
        }

        private void showResult() {

            try {
                JSONObject jsonObject = new JSONObject(mJsonString);
                JSONArray jsonArray = jsonObject.getJSONArray("theaters");

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject item = jsonArray.getJSONObject(i);
                    screen_total = item.getString("screen");
                    kind_total = item.getString("kind");
                    popcorn_total = item.getString("taste");
                    clean_total = item.getString("clean");
                    num_of_review = item.getString("totalReview");

                }

            } catch (JSONException e) {
                Log.d("TAG", "show result : ", e);
            }
        }

    }





    private class GetData2 extends AsyncTask<String,Void,String> {
        ProgressDialog progressDialog;
        String errorString = null;

        @Override
        protected String doInBackground(String... strings) {

            String serverURL = "title="+title;
            if (android.os.Build.VERSION.SDK_INT > 9) {
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);
            }

            try {
                URL url = new URL(MainURL+"show_id_theaterReview.php");
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
                    //System.out.println(line);
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

            progressDialog = ProgressDialog.show(NewActivity.this, "Please Wait", null, true, true);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            progressDialog.dismiss();

            if (result == null) {
                //mTextViewResult.setText(errorString);
            }
            else {
                mJsonString = result;
                showResult();
                System.out.println(mJsonString);
                listview3.setAdapter(adapter);
            }
        }

        private void showResult() {

            try {
                System.out.print("Start to Json Parsing~~~~~");
                JSONObject jsonObject = new JSONObject(mJsonString);
                JSONArray jsonArray = jsonObject.getJSONArray("theaters");

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject item = jsonArray.getJSONObject(i);
                    String id = "theater Name : "+item.getString("theater");
                    String Screen = "Screen Score : "+item.getString("screen");
                    String mkind = "Kind Score : " +item.getString("kind");
                    String Popcorn = "Popcorn Score : "+item.getString("taste");
                    String Clean = "Clean Score : "+item.getString("clean");
                    String Review = "My Review : "+item.getString("FreeReview");

                    adapter.addItem(id,Screen,mkind,Popcorn,Clean,Review);

                }
                System.out.println("adapter Count = "+adapter.getCount());

            } catch (JSONException e) {
                Log.d("TAG", "show result : ", e);
            }
        }

    }
}