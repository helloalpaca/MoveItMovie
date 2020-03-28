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
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import static com.example.alswn.gps.LogIn.MainURL;

public class Frag4 extends Fragment {

    final static private String myReviewURL = MainURL+"myReview.php";
    String mJsonString;

    ListView listview2;
    ListViewAdapterReview adapter;

    String mkind;

    View view;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState){
        view = inflater.inflate(R.layout.frag4,container,false);

        /* listview 생성 및 adapter와 연결 */
        listview2 = (ListView) view.findViewById(R.id.listview2);
        adapter = new ListViewAdapterReview() ;
        listview2.setAdapter(adapter);

        GetData task = new GetData();
        task.execute(LogIn.userID);

        return view;
    }

    private class GetData extends AsyncTask<String,Void,String> {
        ProgressDialog progressDialog;
        String errorString = null;

        @Override
        protected String doInBackground(String... strings) {

            String serverURL = "id="+LogIn.userID;
            if (android.os.Build.VERSION.SDK_INT > 9) {
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);
            }

            try {
                URL url = new URL(myReviewURL);
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

    }

}
