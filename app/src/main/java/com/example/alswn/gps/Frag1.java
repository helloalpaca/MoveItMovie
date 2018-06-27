
package com.example.alswn.gps;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;

public class Frag1 extends Fragment {

    View view;
    TextView textview;
    public final static String BoxOfficeURL = "http://www.kobis.or.kr/kobisopenapi/webservice/rest/boxoffice/searchDailyBoxOfficeList.xml?";
    public final static String KEY = "key=230633a2ac677ec1574e86789aa3b586";
    public static String movieNm = null;

    public final static String NaverAPI = "https://openapi.naver.com/v1/search/movie.json?query=";
    public final static String clientId = "rMALO7IaSUmtn4P7M8rb";//애플리케이션 클라이언트 아이디값";
    public final static String clientSecret = "kmTSmLnZWT";//애플리케이션 클라이언트 시크릿값";

    String movieImageURL;
    Bitmap bitmap;
    Drawable imageView;
    BitmapDrawable bitmapDrawable;
    String Genre = "장르";

    boolean flag = true;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState){
        view = inflater.inflate(R.layout.frag1,container,false);

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }


        final ListView listview ;
        final ListViewAdapter adapter;

        // Adapter 생성
        adapter = new ListViewAdapter() ;

        // 리스트뷰 참조 및 Adapter달기
        listview = (ListView) view.findViewById(R.id.listview1);
        listview.setAdapter(adapter);

        //Drawable drawable = getResources().getDrawable(R.drawable.choppa);
        String openDate = null;
        String saleShare = null;
        boolean movie = false;
        boolean openDt = false;
        boolean salesh = false;

        long now = System.currentTimeMillis();
        Date date = new Date(now);
        //System.out.println(date);

        try {
            URL movieUrl = new URL(BoxOfficeURL+KEY+"&targetDt=20180622");
            InputStream in = movieUrl.openStream();
            XmlPullParserFactory parserCreator = XmlPullParserFactory.newInstance();
            //XmlPullParser parser = getResources().getXml(R.xml.board);
            XmlPullParser parser = parserCreator.newPullParser();

            parser.setInput(in,null);

            System.out.println("파싱중입니다.......");
            int parserEvent = parser.getEventType();
            System.out.println("Start to Parse ~ \n");

            while(parserEvent != XmlPullParser.END_DOCUMENT){
                switch (parserEvent){
                    case XmlPullParser.START_TAG:
                        if(parser.getName().equals("movieNm")) {
                            movie = true;
                        }
                        if(parser.getName().equals("openDt")){
                            openDt = true;
                        }
                        if(parser.getName().equals("salesShare")){
                            salesh = true;
                        }
                        break;

                    case XmlPullParser.TEXT:
                        if(movie){
                            movieNm = parser.getText();
                            movie = false;
                        }
                        if(openDt){
                            openDate = parser.getText();
                            openDt = false;
                        }
                        if(salesh){
                            saleShare = parser.getText();
                            salesh = false;
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        if(parser.getName().equals("dailyBoxOffice")){


                            try{
                                String apiURL = NaverAPI + movieNm;
                                URL ImgUrl = new URL(apiURL);
                                HttpURLConnection connection = (HttpURLConnection)ImgUrl.openConnection();
                                connection.setRequestMethod("GET");
                                connection.setRequestProperty("X-Naver-Client-Id",clientId);
                                connection.setRequestProperty("X-Naver-Client-Secret",clientSecret);

                                int responseCode = connection.getResponseCode();
                                BufferedReader bufferedReader;
                                if(responseCode ==200){
                                    bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                                }
                                else{
                                    bufferedReader = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
                                }

                                StringBuilder sb = new StringBuilder();
                                String line;
                                while((line = bufferedReader.readLine())!=null){
                                    sb.append(line);
                                }
                                bufferedReader.close();

                                JSONObject jsonObject = new JSONObject(sb.toString());
                                JSONArray jsonArray = jsonObject.getJSONArray("items");

                                for(int i=0;i<jsonArray.length();i++){
                                    JSONObject item = jsonArray.getJSONObject(i);
                                    movieImageURL = item.getString("image");
                                }
                                connection.disconnect();
                            }catch (Exception e){
                                e.printStackTrace();
                            }

                            Thread mThread = new Thread(){
                                @Override
                                public void run(){
                                    try{
                                        URL myURL = new URL(movieImageURL);
                                        HttpURLConnection con = (HttpURLConnection)myURL.openConnection();
                                        con.setDoInput(true);
                                        con.connect();

                                        InputStream is = con.getInputStream();
                                        bitmap = BitmapFactory.decodeStream(is);
                                    } catch (MalformedURLException e){
                                        e.printStackTrace();
                                    } catch (IOException e){
                                        e.printStackTrace();
                                    }
                                }
                            };

                            mThread.start();

                            try{
                                mThread.join();
                                imageView = new BitmapDrawable(bitmap);


                            }catch (InterruptedException e){
                                e.printStackTrace();
                            }

                            /* ㄴㅓ무 오래걸려서 image뷰 일단 막아놓음*/
                            adapter.addItem(imageView,movieNm,openDate,saleShare);
                            //adapter.addItem(null,movieNm,openDate,saleShare);
                        }
                        break;
                }
                parserEvent = parser.next();
            }

        }catch (Exception e){
            Log.e("NET","네트워크 에러가 났습니다....",e);
        }

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getActivity() ,adapter.getItemTitle(position),Toast.LENGTH_LONG).show();
            }
        });

        final Button btn = (Button) view.findViewById(R.id.movie_btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn.setText("상업 영화");

                final ListViewAdapter adapter2;
                adapter2 = new ListViewAdapter() ;

                listview.setAdapter(adapter2);

                String openDate = null;
                String saleShare = null;
                boolean movie = false;
                boolean openDt = false;
                boolean salesh = false;

                try {
                    URL movieUrl = new URL(BoxOfficeURL+KEY+"&targetDt=20180622&multiMovieYn=Y");
                    InputStream in = movieUrl.openStream();
                    XmlPullParserFactory parserCreator = XmlPullParserFactory.newInstance();
                    //XmlPullParser parser = getResources().getXml(R.xml.board);
                    XmlPullParser parser = parserCreator.newPullParser();

                    parser.setInput(in,null);

                    System.out.println("파싱중입니다.......");
                    int parserEvent = parser.getEventType();
                    System.out.println("Start to Parse ~ \n");

                    while(parserEvent != XmlPullParser.END_DOCUMENT){
                        switch (parserEvent){
                            case XmlPullParser.START_TAG:
                                if(parser.getName().equals("movieNm")) {
                                    movie = true;
                                }
                                if(parser.getName().equals("openDt")){
                                    openDt = true;
                                }
                                if(parser.getName().equals("salesShare")){
                                    salesh = true;
                                }
                                break;

                            case XmlPullParser.TEXT:
                                if(movie){
                                    movieNm = parser.getText();
                                    movie = false;
                                }
                                if(openDt){
                                    openDate = parser.getText();
                                    openDt = false;
                                }
                                if(salesh){
                                    saleShare = parser.getText();
                                    salesh = false;
                                }
                                break;
                            case XmlPullParser.END_TAG:
                                if(parser.getName().equals("dailyBoxOffice")){


                                    try{
                                        String apiURL = NaverAPI + movieNm;
                                        URL ImgUrl = new URL(apiURL);
                                        HttpURLConnection connection = (HttpURLConnection)ImgUrl.openConnection();
                                        connection.setRequestMethod("GET");
                                        connection.setRequestProperty("X-Naver-Client-Id",clientId);
                                        connection.setRequestProperty("X-Naver-Client-Secret",clientSecret);

                                        int responseCode = connection.getResponseCode();
                                        BufferedReader bufferedReader;
                                        if(responseCode ==200){
                                            bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                                        }
                                        else{
                                            bufferedReader = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
                                        }

                                        StringBuilder sb = new StringBuilder();
                                        String line;
                                        while((line = bufferedReader.readLine())!=null){
                                            sb.append(line);
                                        }
                                        bufferedReader.close();

                                        JSONObject jsonObject = new JSONObject(sb.toString());
                                        JSONArray jsonArray = jsonObject.getJSONArray("items");

                                        for(int i=0;i<jsonArray.length();i++){
                                            JSONObject item = jsonArray.getJSONObject(i);
                                            movieImageURL = item.getString("image");
                                        }
                                        connection.disconnect();
                                    }catch (Exception e){
                                        e.printStackTrace();
                                    }

                                    Thread mThread = new Thread(){
                                        @Override
                                        public void run(){
                                            try{
                                                URL myURL = new URL(movieImageURL);
                                                HttpURLConnection con = (HttpURLConnection)myURL.openConnection();
                                                con.setDoInput(true);
                                                con.connect();

                                                InputStream is = con.getInputStream();
                                                bitmap = BitmapFactory.decodeStream(is);
                                            } catch (MalformedURLException e){
                                                e.printStackTrace();
                                            } catch (IOException e){
                                                e.printStackTrace();
                                            }
                                        }
                                    };

                                    mThread.start();

                                    try{
                                        mThread.join();
                                        imageView = new BitmapDrawable(bitmap);


                                    }catch (InterruptedException e){
                                        e.printStackTrace();
                                    }

                                    /* ㄴㅓ무 오래걸려서 image뷰 일단 막아놓음*/
                                    adapter2.addItem(imageView,movieNm,openDate,saleShare);
                                    //adapter.addItem(null,movieNm,openDate,saleShare);
                                }
                                break;
                        }
                        parserEvent = parser.next();
                    }

                }catch (Exception e){
                    Log.e("NET","네트워크 에러가 났습니다....",e);
                }
            }
        });
        return view;
    }





/*



    private class GetData extends AsyncTask<String,Void,String> {
        ProgressDialog progressDialog;
        String errorString = null;

        @Override
        protected String doInBackground(String... strings) {

            String serverURL = "id="+LogIn.afterID;
            if (android.os.Build.VERSION.SDK_INT > 9) {
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);
            }

            try {
                URL url = new URL(Id_Review_URL);
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

            progressDialog = ProgressDialog.show(getActivity(), "Please Wait", null, true, true);
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
                    mkind = "Kind Score : " +item.getString("kind");
                    String Popcorn = "Popcorn Score : "+item.getString("taste");
                    String Clean = "Clean Score : "+item.getString("clean");
                    String Review = "My Review : "+item.getString("FreeReview");

                    adapter.addItem(id,Screen,mkind,Popcorn,Clean,Review);
                    System.out.print("theater="+id+"screen="+Screen);

                }
                System.out.println("adapter Count = "+adapter.getCount());
                listview2.setAdapter(adapter);

            } catch (JSONException e) {
                Log.d("TAG", "show result : ", e);
            }
        }

    }*/
}

