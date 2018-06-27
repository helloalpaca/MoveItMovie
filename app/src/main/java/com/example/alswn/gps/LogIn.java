package com.example.alswn.gps;

        import android.app.ProgressDialog;
        import android.content.Intent;
        import android.os.AsyncTask;
        import android.os.StrictMode;
        import android.support.annotation.NonNull;
        import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
        import android.util.Log;
        import android.view.View;
        import android.view.WindowManager;
        import android.widget.Button;
        import android.widget.EditText;
        import android.widget.Toast;

        import com.google.android.gms.tasks.OnCompleteListener;
        import com.google.android.gms.tasks.Task;
        import com.google.firebase.auth.AuthResult;
        import com.google.firebase.auth.FirebaseAuth;

        import org.json.JSONArray;
        import org.json.JSONException;
        import org.json.JSONObject;

        import java.io.BufferedReader;
        import java.io.InputStream;
        import java.io.InputStreamReader;
        import java.io.OutputStream;
        import java.net.HttpURLConnection;
        import java.net.URL;

public class LogIn extends AppCompatActivity {
    public Button join;
    public Button login;
    public EditText email;
    public EditText pwd;

    final static public String MainURL = "http://192.168.1.22/~Kwon/";
    public String Query_ID_URL = MainURL+"query_ID.php";

    String myId;
    String myPw;
    String mJsonString;

    public static String afterID;
    public static String afterPw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        join = (Button) findViewById(R.id.button_SignUp);
        login = (Button) findViewById(R.id.button_login);
        email = (EditText) findViewById(R.id.editText_ID);
        pwd = (EditText) findViewById(R.id.editText_pwd);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("onclick success");
                myId = email.getText().toString().trim();
                myPw = pwd.getText().toString().trim();
                System.out.print(myId + ", " + myPw);

                GetData task = new GetData();
                task.execute(myId,myPw);
            }
        });

        join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(LogIn.this, SignUp.class);
                startActivity(intent);
            }
        });
    }//Oncreate END


    private class GetData extends AsyncTask<String,Void,String> {
        ProgressDialog progressDialog;
        String errorString = null;

        @Override
        protected String doInBackground(String... strings) {

            String serverURL = "id="+myId+"&pw="+myPw;
            if (android.os.Build.VERSION.SDK_INT > 9) {
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);
            }

            try {
                URL url = new URL(Query_ID_URL);
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

            progressDialog = ProgressDialog.show(LogIn.this, "Please Wait", null, true, true);
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
                System.out.println(afterID + "," + afterPw);
            }
        }

        private void showResult() {

            try {
                JSONObject jsonObject = new JSONObject(mJsonString);
                JSONArray jsonArray = jsonObject.getJSONArray("users");

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject item = jsonArray.getJSONObject(i);
                    String id = item.getString("id");
                    String pw = item.getString("pw");
                    //System.out.println("id = " + id + ", pw = " + pw);
                    afterID = id;
                    afterPw = pw;
                }

                if(myId.equals(afterID) && myPw.equals(afterPw)){
                    System.out.print("success");
                    Intent intent = new Intent(LogIn.this, Main.class);
                    startActivity(intent);
                }

            } catch (JSONException e) {
                Log.d("TAG", "show result : ", e);
            }
        }

    }
}
