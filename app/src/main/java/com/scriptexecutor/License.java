package com.scriptexecutor;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.CheckBox;


public class License extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_cauton);

        getSupportActionBar()
                .setSubtitle(R.string.lic);

        CheckBox cau = (CheckBox) findViewById(R.id.cau);
        if (cau.isChecked()) {
            cau.setEnabled(false);
        }
    }

}