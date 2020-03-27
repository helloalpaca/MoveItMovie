package com.example.alswn.gps;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import static android.content.ContentValues.TAG;
import static com.example.alswn.gps.LogIn.userID;
import static com.example.alswn.gps.LogIn.userPW;

public class GetData extends AsyncTask<String,Void,String> {

    private Context context;

    ProgressDialog progressDialog;
    String errorString = null;
    String mJsonString;

    public GetData(Context context){
        this.context = context;
    }

    @Override
    protected String doInBackground(String... strings) {

        String serverURL = "id="+strings[1]+"&pw="+strings[2];

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        String responseBody = get(strings[0],serverURL);
        return responseBody;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        progressDialog = ProgressDialog.show(context, "Please Wait", null, true, true);
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
        }
    }

    private static String get(String apiUrl, String query) {
        String responseBody = null;
        try {
            URL url = new URL(apiUrl);
            HttpURLConnection httpURLConnection = null;
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setReadTimeout(5000);
            httpURLConnection.setConnectTimeout(1000);
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoInput(true);
            httpURLConnection.connect();

            OutputStream outputStream = httpURLConnection.getOutputStream();
            outputStream.write(query.getBytes("UTF-8"));
            outputStream.flush();
            outputStream.close();

            int responseStatusCode = httpURLConnection.getResponseCode();
            Log.d(TAG,"response - "+responseStatusCode);

            InputStream inputStream;
            if (responseStatusCode == HttpURLConnection.HTTP_OK) {
                inputStream = httpURLConnection.getInputStream();
            } else {
                inputStream = httpURLConnection.getErrorStream();
            }

            responseBody = readBody(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return  responseBody;
    }

    private static String readBody(InputStream body){
        InputStreamReader streamReader = new InputStreamReader(body);

        try {
            BufferedReader lineReader = new BufferedReader(streamReader);
            StringBuilder responseBody = new StringBuilder();

            String line;
            while ((line = lineReader.readLine()) != null) {
                responseBody.append(line);
            }
            return responseBody.toString();
        } catch (IOException e) {
            throw new RuntimeException("API 응답을 읽는데 실패했습니다.", e);
        }
    }

    private void showResult() {

        try {
            JSONObject jsonObject = new JSONObject(mJsonString);
            JSONArray jsonArray = jsonObject.getJSONArray("users");

            String itemID = null, itemPW = null;

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject item = jsonArray.getJSONObject(i);
                String id = item.getString("id");
                String pw = item.getString("pw");
                //System.out.println("id = " + id + ", pw = " + pw);
                itemID = id;
                itemPW = pw;
            }

            if(itemID.equals(userID) && itemPW.equals(userPW)){
                System.out.print("success");
                Intent intent = new Intent(context,Main.class);
                context.startActivity(intent);
            }

        } catch (JSONException e) {
            Log.d("TAG", "show result : ", e);
        }

    }
}
