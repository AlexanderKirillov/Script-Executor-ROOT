package com.scriptexecutor;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;


public class About extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_aboutprogram);

        getSupportActionBar()
                .setSubtitle(R.string.about);

    }

}