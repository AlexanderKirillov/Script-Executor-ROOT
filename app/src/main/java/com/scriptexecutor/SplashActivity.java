package com.scriptexecutor;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.widget.ProgressBar;

import com.stericson.RootTools.RootTools;

public class SplashActivity extends ActionBarActivity {
    public static final int ALERT_DIALOG = 0;
    private static int SPLASH_TIME_OUT = 2500;

    @Override
    protected Dialog onCreateDialog(int id) {

        Dialog dialog;
        AlertDialog.Builder builder;
        switch (id) {
            case ALERT_DIALOG:
                builder = new AlertDialog.Builder(SplashActivity.this);
                builder.setMessage(R.string.root)
                        .setCancelable(false)
                        .setNegativeButton(R.string.exit, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Intent intent = new Intent(Intent.ACTION_MAIN);
                                intent.addCategory(Intent.CATEGORY_HOME);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                                SplashActivity.this.finish();
                                System.exit(0);
                            }
                        });

                dialog = builder.create();
                break;

            default:
                dialog = null;
        }
        return dialog;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.splash_activity);
        ((ProgressBar) findViewById(R.id.progressBar)).getIndeterminateDrawable()
                .setColorFilter(Color.parseColor("#FFFFFF"), android.graphics.PorterDuff.Mode.SRC_ATOP);


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(SplashActivity.this, ScriptActivity.class);
                if (RootTools.isAccessGiven()) {
                    startActivity(i);
                    finish();
                } else {
                    showDialog(ALERT_DIALOG);
                }

            }
        }, SPLASH_TIME_OUT);
    }


    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }
}