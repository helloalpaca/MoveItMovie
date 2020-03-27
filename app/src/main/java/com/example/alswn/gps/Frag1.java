package com.example.alswn.gps;


import android.app.Fragment;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;

public class Frag1 extends Fragment implements AdapterView.OnItemClickListener, View.OnClickListener{

    public final static String BoxOfficeURL = "http://www.kobis.or.kr/kobisopenapi/webservice/rest/boxoffice/searchDailyBoxOfficeList.xml?";
    public final static String KEY = "key=230633a2ac677ec1574e86789aa3b586";
    public final static String NaverAPI = "https://openapi.naver.com/v1/search/movie.json?query=";
    public final static String clientId = "S1N3ovKB8l6gkWERCwFE";
    public final static String clientSecret = "0ZFd4YgOOU";

    ListView listview ;
    ListViewAdapter adapter;
    Button btnMovieType;

    String movieImageURL = null;
    String movieNm = null;
    Bitmap bitmap;
    Drawable imageView;
    BitmapDrawable bitmapDrawable;
    String Genre = "장르";
    boolean flag = true;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState){

        View view = inflater.inflate(R.layout.frag1,container,false);

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        // ListView 생성 및 adapter 연결
        listview = (ListView) view.findViewById(R.id.listview1);
        ListViewAdapter adapter = new ListViewAdapter();
        listview.setAdapter(adapter);
        listview.setOnItemClickListener(this);

        /* Button에 onClickListener 연결 */
        btnMovieType = (Button)view.findViewById(R.id.frag1_btn_type);
        btnMovieType.setOnClickListener(this);

        String commertialURL = BoxOfficeURL+KEY+"&targetDt=20180622";
        GetBoxOfficeData(adapter, commertialURL);

        return view;
    }

    public void GetBoxOfficeData(ListViewAdapter adapter, String movieURL){

        listview.setAdapter(adapter);

        String openDate = null;
        String saleShare = null;
        boolean movie = false;
        boolean openDt = false;
        boolean salesh = false;

        try {
            URL movieUrl = new URL(movieURL);
            InputStream in = movieUrl.openStream();
            XmlPullParserFactory parserCreator = XmlPullParserFactory.newInstance();
            //XmlPullParser parser = getResources().getXml(R.xml.board);
            XmlPullParser parser = parserCreator.newPullParser();

            parser.setInput(in,null);

            //System.out.println("파싱중입니다.......");
            int parserEvent = parser.getEventType();
            //System.out.println("Start to Parse ~ \n");

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
                            GetMoviePosterNaver();

                            /* ㄴㅓ무 오래걸려서 image뷰 일단 막아놓음*/
                            //adapter.addItem(imageView,movieNm,openDate,saleShare);
                            adapter.addItem(null,movieNm,openDate,saleShare);
                        }
                        break;
                }
                parserEvent = parser.next();
            }

        }catch (Exception e){
            Log.e("NET","네트워크 에러가 났습니다....",e);
        }
    }

    public void GetMoviePosterNaver(){
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

    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Toast.makeText(getActivity() ,adapter.getItemTitle(i),Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View view) {

        //adapter.clear();

        if (flag) {
            ListViewAdapter adapter2 = new ListViewAdapter();
            btnMovieType.setText("상업 영화");
            String independentURL = BoxOfficeURL + KEY + "&targetDt=20180622&multiMovieYn=Y";
            GetBoxOfficeData(adapter2, independentURL);
            flag = false;
        } else{
            ListViewAdapter adapter3 = new ListViewAdapter();
            btnMovieType.setText("독립 영화");
            String commertialURL = BoxOfficeURL + KEY + "&targetDt=20180622";
            GetBoxOfficeData(adapter3, commertialURL);
            flag = true;
        }
    }
}


