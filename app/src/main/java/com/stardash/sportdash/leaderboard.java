package com.stardash.sportdash;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

public class leaderboard extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        setContentView(R.layout.activity_leaderboard);
        getLeaderboard();

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

    private void getLeaderboard() {
        try {
            StarsocketConnector.sendMessage("leaderboard");
            final Handler handler = new Handler(Looper.getMainLooper());
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    String received = StarsocketConnector.getMessage().replaceAll("\"", "");
                    TextView textViewFirstPlace = findViewById(R.id.textViewFirstPlace);
                    TextView textViewSecondPlace = findViewById(R.id.textViewSecondPlace);
                    TextView textViewThirdPlace = findViewById(R.id.textViewThirdPlace);
                    TextView textViewRest = findViewById(R.id.textViewRest);
                    try {
                        textViewFirstPlace.setText(received.split("\n")[0]);
                        textViewSecondPlace.setText(received.split("\n")[1]);
                        textViewThirdPlace.setText(received.split("\n")[2]);

                        textViewRest.setText(received.split("\n", 4)[3]);
                    } catch (Exception e){
                            toast("not enough users to determine all");
                    }
                }
            }, 200);
        } catch (Exception e){
            toast("no network");
        }
    }

    public void toast(String message){
        TextView textViewCustomToast = findViewById(R.id.textViewCustomToast);
        textViewCustomToast.setVisibility(View.VISIBLE);
        textViewCustomToast.setText(Account.errorStyle()+" "+message);
        final Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                textViewCustomToast.setVisibility(View.GONE);
            }
        }, 3000);
    }

    public void open1(View view) {
        vibrate();
        TextView textViewFirstPlace = findViewById(R.id.textViewFirstPlace);
        try {
        String id = textViewFirstPlace.getText().toString().split("#")[1];
            StarsocketConnector.sendMessage("getProfile " + id);
            Intent i = new Intent(this, ProfileActivity.class);
            startActivity(i);
        } catch (Exception e) {
            toast("no network");
        }

    }
    public void open2(View view) {
        vibrate();
        TextView textViewSecondPlace = findViewById(R.id.textViewSecondPlace);
        try {
        String id = textViewSecondPlace.getText().toString().split("#")[1];
            StarsocketConnector.sendMessage("getProfile " + id);
            Intent i = new Intent(this, ProfileActivity.class);
            startActivity(i);
        } catch (Exception e) {
            toast("no network");
        }
    }
    public void open3(View view) {
        vibrate();
        TextView textViewThirdPlace = findViewById(R.id.textViewThirdPlace);
        try {
        String id = textViewThirdPlace.getText().toString().split("#")[1];
            StarsocketConnector.sendMessage("getProfile " + id);
            Intent i = new Intent(this, ProfileActivity.class);
            startActivity(i);
        } catch (Exception e) {
            toast("no network");
        }

    }

    private void vibrate() {
        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            v.vibrate(VibrationEffect.createOneShot(100, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            v.vibrate(100);
        }
    }
}