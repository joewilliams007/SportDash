package com.stardash.sportdash.plans.run;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.os.Handler;

import java.util.Locale;

import com.stardash.sportdash.settings.Account;
import com.stardash.sportdash.MainActivity;
import com.stardash.sportdash.R;

public class TrackActivity extends AppCompatActivity {

    private boolean running;
    private boolean allowEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track);
        setTextView();
        runTimer();
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

    private void setTextView() {
        TextView textViewActivity = findViewById(R.id.textViewActivity);
        textViewActivity.setText(getString(R.string.set_activity)+activity());
    }

    public void finish(View view) {
        if (allowEdit) {
            SharedPreferences settings = getSharedPreferences("sport", MODE_PRIVATE);
            SharedPreferences.Editor editor = settings.edit();
            editor.putInt("duration", seconds).commit();
            Intent i = new Intent(this, ResultInfoActivity.class);
            startActivity(i);
        } else {
            activateSecure();
        }
    }

    String activity() {
        SharedPreferences pref = this.getSharedPreferences("sport", 0); // 0 - for private mode
        String activity = pref.getString("whatSport", null);
        return activity;
    }

    private int seconds = 0;
    private void runTimer()
    {
        TextView textViewTrack = findViewById(R.id.textViewTrack);
        final Handler handler
                = new Handler();

        handler.post(new Runnable() {
            @Override
            public void run()
            {
                int hours = seconds / 3600;
                int minutes = (seconds % 3600) / 60;
                int secs = seconds % 60;

                String time
                        = String
                        .format(Locale.getDefault(),
                                "%d:%02d:%02d", hours,
                                minutes, secs);

                textViewTrack.setText(time);

                // If running is true, increment the
                // seconds variable.
                if (running) {
                    seconds++;
                }

                // Post the code again
                // with a delay of 1 second.
                handler.postDelayed(this, 1000);
            }
        });
    }

    public void onClickStart(View view)
    {
        running = true;
        TextView textViewStart = findViewById(R.id.textViewStart);
        textViewStart.setVisibility(View.GONE);
    }

    public void onClickStop(View view)
    {
        if (allowEdit) {
            allowEdit = false;
            if (running) {
                running = false;
                TextView textViewCancel = findViewById(R.id.textViewCancel);
                textViewCancel.setText("R Ξ S U M Ξ");
                textViewCancel.setTextColor(Color.parseColor("#FFFDD835"));
            } else {
                running = true;
                TextView textViewCancel = findViewById(R.id.textViewCancel);
                textViewCancel.setText("P Λ U S Ξ");
                textViewCancel.setTextColor(Color.parseColor("#E64868"));
            }
        } else {
            activateSecure();
        }
    }

    private void activateSecure() {
        vibrate();
        toast("activate lock first");
        try {
            final Handler handler = new Handler(Looper.getMainLooper());
            TextView textViewAllow = findViewById(R.id.textViewSecure);
            TextView textViewError = findViewById(R.id.textViewError);
            textViewError.setVisibility(View.VISIBLE);
            textViewAllow.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.track_red_background));
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    setBackgroundNormal();
                }
            }, 100);
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    setError();
                }
            }, 1000);
        } catch (Exception e) {

        }
    }

    private void setError() {
        TextView textViewError = findViewById(R.id.textViewError);
        textViewError.setVisibility(View.INVISIBLE);
    }

    private void setBackgroundNormal() {
        TextView textViewAllow = findViewById(R.id.textViewSecure);
        textViewAllow.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.main_text_background) );
    }

    public void allowPauseFinish(View view) {
        allowEdit = true;
        toast("activated editing!");
    }

    public void vibrate(){
        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            v.vibrate(VibrationEffect.createOneShot(100, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            //deprecated in API 26
            v.vibrate(100);
        }
    }

    @Override
    public void onBackPressed() {
        showOptionsAction();
    }

    private void showOptionsAction() {
        TextView textViewDiscard = findViewById(R.id.textViewTopDiscard);
        if (textViewDiscard.getVisibility() == View.VISIBLE){
            textViewDiscard.setVisibility(View.GONE);
        } else  {
            textViewDiscard.setVisibility(View.VISIBLE);
        }

    }

    public void discard(View view) {
        running = false;
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }

    public void toast(String message){
        TextView textViewCustomToast = findViewById(R.id.textViewCustomToast);
        textViewCustomToast.setVisibility(View.VISIBLE);
        textViewCustomToast.setText(message);
        final Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                textViewCustomToast.setVisibility(View.GONE);
            }
        }, 3000);
    }
}
