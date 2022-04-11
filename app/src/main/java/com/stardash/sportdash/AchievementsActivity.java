package com.stardash.sportdash;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;

public class AchievementsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        setContentView(R.layout.activity_achievements);
        setProgressBars();
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

    private void setProgressBars() {
        ProgressBar progressBarAccountCreated = findViewById(R.id.progressBarAccountCreated);
        ProgressBar progressBarLogins = findViewById(R.id.progressBarLogins);
        TextView textViewAccountCreatedAmount = findViewById(R.id.textViewAccountCreatedAmount);
        TextView textViewLoginsAmount = findViewById(R.id.textViewLoginsAmount);

        int AccountCreatedProgress = 100;
        int LoginsProgress = 20;

        ObjectAnimator.ofInt(progressBarAccountCreated, "progress", AccountCreatedProgress)
                .setDuration(600)
                .start();

        ObjectAnimator.ofInt(progressBarLogins, "progress", LoginsProgress)
                .setDuration(600)
                .start();

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                textViewAccountCreatedAmount.setText(String.valueOf(AccountCreatedProgress/100)+"/1");
                textViewLoginsAmount.setText(String.valueOf(LoginsProgress/10)+"/10");
            }
        }, 599);

    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }

}