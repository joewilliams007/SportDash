package com.stardash.sportdash.plans.run.live;

import static com.stardash.sportdash.settings.app.vibrate;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.TextView;

import com.stardash.sportdash.R;
import com.stardash.sportdash.settings.MyApplication;

public class CreatingLiveActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creating_live);
        TextView textViewTop = findViewById(R.id.textViewTop);

        final Handler handler = new Handler(Looper.getMainLooper());

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                textViewTop.setText("j o i n i n g . . .");
                vibrate();
            }
        }, 1000);


        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(MyApplication.getAppContext(), LivePlanGenerateActivity.class);
                startActivity(i);
                vibrate();
            }
        }, 2000);

    }
}