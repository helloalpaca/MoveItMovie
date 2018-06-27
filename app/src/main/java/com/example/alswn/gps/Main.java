package com.example.alswn.gps;


import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Main extends AppCompatActivity implements View.OnClickListener {

    Button btn1, btn2, btn3, btn4;
    FragmentManager fm;
    FragmentTransaction tran;
    Frag1 frag1;
    Frag2 frag2;
    Frag3 frag3;
    Frag4 frag4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        btn1 = (Button) findViewById(R.id.btn1);
        btn2 = (Button) findViewById(R.id.btn2);
        btn3 = (Button) findViewById(R.id.btn3);
        btn4 = (Button) findViewById(R.id.btn4);
        btn1.setOnClickListener(this);
        btn2.setOnClickListener(this);
        btn3.setOnClickListener(this);
        btn4.setOnClickListener(this);
        frag1 = new Frag1();
        frag2 = new Frag2();
        frag3 = new Frag3();
        frag4 = new Frag4();
        setFrag(0);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn1:
                setFrag(0);
                break;
            case R.id.btn2:
                setFrag(1);
                break;
            case R.id.btn3:
                setFrag(2);
                break;
            case R.id.btn4:
                setFrag(3);
                break;
        }
    }

    public void setFrag(int n) {
        fm = getFragmentManager();
        tran = fm.beginTransaction();
        switch (n) {
            case 0:
                tran.replace(R.id.main_frame, frag1);
                tran.commit();
                break;
            case 1:
                tran.replace(R.id.main_frame, frag2);
                tran.commit();
                break;
            case 2:
                tran.replace(R.id.main_frame, frag3);
                tran.commit();
                break;
            case 3:
                tran.replace(R.id.main_frame, frag4);
                tran.commit();
                break;
        }
    }
}

