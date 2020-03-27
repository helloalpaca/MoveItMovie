package com.example.alswn.gps;

        import android.content.Intent;
        import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
        import android.view.View;
        import android.widget.EditText;

public class LogIn extends AppCompatActivity {

    public EditText email, pwd;

    final static public String MainURL = "http://172.30.1.36:8080/moveitmovie/";
    final static public String loginURL = MainURL+"login.php";
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

        GetLogInData task = new GetLogInData(this);
        task.execute(loginURL, userID, userPW);

    }

    public void onClickButtonSignup(View view){

        Intent intent=new Intent(LogIn.this, SignUp.class);
        startActivity(intent);

    }

}
