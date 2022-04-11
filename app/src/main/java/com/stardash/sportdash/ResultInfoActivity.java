package com.stardash.sportdash;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.SeekBar;
import android.widget.TextView;

public class ResultInfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_info);
        TextView textViewMin = findViewById(R.id.textViewMinAmount);
        textViewMin.setText(setMinutes()/60+" min");

        SeekBar seekBar = (SeekBar)findViewById(R.id.seekBar);
        final TextView seekBarValue = (TextView)findViewById(R.id.textViewGood);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,
                                          boolean fromUser) {
                // TODO Auto-generated method stub
                seekBarValue.setText(String.valueOf(progress)+"/5");
                Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    v.vibrate(VibrationEffect.createOneShot(100, VibrationEffect.DEFAULT_AMPLITUDE));
                } else {
                    //deprecated in API 26
                    v.vibrate(100);
                }
                SharedPreferences settings = getSharedPreferences("sport", MODE_PRIVATE);
                SharedPreferences.Editor editor = settings.edit();
                try{
                    int number = Integer.parseInt(String.valueOf(progress));
                    editor.putInt("powerResult", number).commit();
                }
                catch (NumberFormatException ex){
                    ex.printStackTrace();
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
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

    int setMinutes() {
        SharedPreferences pref = this.getSharedPreferences("sport", 0); // 0 - for private mode
        int duration = pref.getInt("duration", 0);
        return duration;
    }

    int setPowerResult() {
        SharedPreferences pref = this.getSharedPreferences("sport", 0); // 0 - for private mode
        int power = pref.getInt("powerResult", 0);
        return power;
    }

    public void next(View view) {
        saveReward();
        Intent i = new Intent(this, RewardActivity.class);
        startActivity(i);
    }

    int reward() {
        int reward = setMinutes()*2+setPowerResult()*10;
        return reward;
    }

    int rewardCoins() {
        int reward = setMinutes()/2+setPowerResult()+1;
        return reward;
    }

    public void saveReward(){
        SharedPreferences settings = getSharedPreferences("sport", MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt("rewardXp", reward()).commit();
        editor.putInt("rewardCoins", rewardCoins()).commit();
    }
}