package com.example.alswn.gps;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import static com.example.alswn.gps.LogIn.MainURL;

/**
 * Created by alswn on 2018-06-07.
 */

public class SignUp extends AppCompatActivity {
    EditText editID, editPW;

    private String myId;
    private String myPw;
    final static private String SignUpURL = MainURL+"joinMember.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        editID = (EditText) findViewById(R.id.editText_id);
        editPW =(EditText)findViewById(R.id.editText_pwd);
    }

    public void onClickButtonJoin(View view){

        myId = editID.getText().toString().trim();
        myPw = editPW.getText().toString().trim();

        InsertData task = new InsertData(this);
        String serverURL = "id="+myId+"&pw="+myPw;
        task.execute(SignUpURL, serverURL);

        Toast.makeText(this,"아이디를 만들었습니다!!",Toast.LENGTH_SHORT).show();
    }
}
