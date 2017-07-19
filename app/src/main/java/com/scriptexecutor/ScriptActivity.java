package com.scriptexecutor;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AlertDialog;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.github.mrengineer13.snackbar.SnackBar;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.concurrent.CountDownLatch;

public class ScriptActivity extends ActionBarActivity {

    public static final int ALERT_DIALOG2 = 2;
    boolean doubleBackToExitPressedOnce = false;
    private boolean isClicked;

    @Override
    protected Dialog onCreateDialog(int dialogId) {
        ProgressDialog progress = null;
        switch (dialogId) {
            case ALERT_DIALOG2:
                progress = new ProgressDialog(this);
                progress.setCancelable(false);
                progress.setMessage(getResources().getString(R.string.pleasewait));
                break;
        }
        return progress;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_user:
                new AlertDialog.Builder(this)
                        .setTitle(R.string.exit)
                        .setMessage(R.string.exittext)
                        .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(Intent.ACTION_MAIN);
                                intent.addCategory(Intent.CATEGORY_HOME);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                                finish();
                                System.exit(0);
                            }
                        })
                        .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .setIcon(R.drawable.exit)
                        .show();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_co_w);
        copyAssets();

        File file2 = new File("/data/data/com.scriptexecutor/files/script.sh");
        file2.setReadable(true, false);
        file2.setExecutable(true, false);
        file2.setWritable(true, false);


        Button about = (Button) findViewById(R.id.about);
        about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isClicked) {
                    return;
                }
                isClicked = true;
                v.postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        isClicked = false;
                    }
                }, 1000);

                Intent i = new Intent(ScriptActivity.this, About.class);
                startActivity(i);

            }

        });

        Button license = (Button) findViewById(R.id.license);
        license.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isClicked) {
                    return;
                }
                isClicked = true;
                v.postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        isClicked = false;
                    }
                }, 1000);

                Intent i = new Intent(ScriptActivity.this, License.class);
                startActivity(i);

            }

        });


        ImageView root = (ImageView) findViewById(R.id.root);
        root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isClicked) {
                    return;
                }
                isClicked = true;
                v.postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        isClicked = false;
                    }
                }, 1000);


                start myTask = new start(getApplication().getApplicationContext(), getWindow().getDecorView().findViewById(android.R.id.content));
                myTask.execute();

            }
        });
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce || getSupportFragmentManager().getBackStackEntryCount() != 0) {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
            System.exit(0);
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, R.string.back, Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }

    private void copyAssets() {
        AssetManager assetManager = this.getAssets();
        String[] files = null;
        try {
            files = assetManager.list("");
        } catch (IOException e) {
        }
        if (files != null) for (String filename : files) {
            InputStream in = null;
            OutputStream out = null;
            try {
                in = assetManager.open(filename);
                File folder = new File("/data/data/com.scriptexecutor/files/");
                if (!folder.exists()) {
                    folder.mkdir();
                }
                File outFile = new File(folder, filename);
                out = new FileOutputStream(outFile);
                copyFile(in, out);


            } catch (IOException e) {
            } finally {
                if (in != null) {
                    try {
                        in.close();
                    } catch (IOException e) {
                    }
                }
                if (out != null) {
                    try {
                        out.close();
                    } catch (IOException e) {
                    }
                }
            }
        }

    }

    private void copyFile(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[1024];
        int read;
        while ((read = in.read(buffer)) != -1) {
            out.write(buffer, 0, read);
        }
    }

    public class Wrapper {
        public String info;
    }

    public class start extends AsyncTask<String, Void, Wrapper> {

        private Context mContext;
        private View rootView;

        public start(Context context, View rootView) {
            this.mContext = context;
            this.rootView = rootView;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
            showDialog(ALERT_DIALOG2);
        }

        @Override
        public Wrapper doInBackground(String... args) {
            final Wrapper w1 = new Wrapper();

            publishProgress(new Void[]{});

            final CountDownLatch latch3 = new CountDownLatch(1);

            try {
                Process su = Runtime.getRuntime().exec("su");
                DataOutputStream outputStream = new DataOutputStream(su.getOutputStream());
                outputStream.writeBytes("/data/data/com.scriptexecutor/files/script.sh\n");
                outputStream.writeBytes("exit\n");
                outputStream.flush();
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(su.getInputStream()));
                int read;
                char[] buffer = new char[4096];
                StringBuffer output = new StringBuffer();
                while ((read = reader.read(buffer)) > 0) {
                    output.append(buffer, 0, read);
                }
                reader.close();

                w1.info = output.toString().trim();

                su.waitFor();

                latch3.countDown();

            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            return w1;
        }


        @Override
        public void onPostExecute(final Wrapper w1) {
            dismissDialog(ALERT_DIALOG2);

            runOnUiThread(new Runnable() {
                public void run() {
                    AlertDialog alertDialog = new AlertDialog.Builder(ScriptActivity.this, R.style.Theme_AppCompat_Dialog_Alert).create();
                    alertDialog.setMessage(getResources().getString(R.string.result) + w1.info);
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    new SnackBar.Builder(ScriptActivity.this)
                                            .withMessage(getResources().getString(R.string.ok))
                                            .withBackgroundColorId(R.color.sucess)
                                            .show();
                                }
                            });
                    alertDialog.show();
                }
            });

        }


    }
}
