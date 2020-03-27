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

    public EditText email, pwd;

    final static public String MainURL = "http://172.30.1.36:8080/moveitmovie/";
    public String loginURL = MainURL+"query_ID.php";
    public static String userID, userPW;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        email = (EditText) findViewById(R.id.editText_ID);
        pwd = (EditText) findViewById(R.id.editText_pwd);

    }

    public void onClickButtonLogin(View view){

        userID = email.getText().toString().trim();
        userPW = pwd.getText().toString().trim();

        GetData task = new GetData(this);
        task.execute(loginURL, userID, userPW);

    }

    public void onClickButtonSignup(View view){
        Intent intent=new Intent(LogIn.this, SignUp.class);
        startActivity(intent);
    }

}
