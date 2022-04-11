package com.stardash.sportdash;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

public class DeveloperLogActivity extends AppCompatActivity {

    static Boolean scan = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        scan = true;
        setContentView(R.layout.activity_developer_log);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        try {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (scan) {
                    ((TextView) findViewById(R.id.textView)).setText(StarsocketConnector.getMessage());
                } else {
                    timer.cancel();
                }
            }
        }, 0, 2000);//wait 0 ms before doing the action and do it evry 1000ms (1second)

        } catch (Exception e){
            Toast.makeText(this, "No Connection", Toast.LENGTH_LONG).show();
        }
        if (Account.isAmoled()) {
            ConstraintLayout main = findViewById(R.id.main);
            main.setBackgroundDrawable(new ColorDrawable(Color.BLACK));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Window window = getWindow();
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(Color.BLACK);
            }
        }
    }

    public void send(View view) {
        try {
            String text = ((EditText) findViewById(R.id.editTextTextPersonName)).getText().toString();
            StarsocketConnector.sendMessage(text);
            ((TextView) findViewById(R.id.textView)).setText(StarsocketConnector.getMessage());
        } catch (Exception e){
            Toast.makeText(this, "No Connection", Toast.LENGTH_LONG).show();
        }
    }


    @Override
    public void onBackPressed() {
        scan = false;
        finish();
        return;
    }

    public void openRegister(View view) {
        Intent i = new Intent(this,RegisterActivity.class);
        startActivity(i);
        scan = false;
    }
}