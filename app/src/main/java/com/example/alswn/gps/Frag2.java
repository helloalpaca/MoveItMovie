package com.example.alswn.gps;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class Frag2 extends Fragment implements View.OnClickListener {

    View view;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState){
        view = inflater.inflate(R.layout.frag2,container,false);

        Button button = (Button) view.findViewById(R.id.button);
        button.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent(getActivity(),MainActivity.class);
        startActivity(intent);
    }
}
