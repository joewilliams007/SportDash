package com.stardash.sportdash;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.stardash.sportdash.plans.create.CreatePlanActivity;
import com.stardash.sportdash.plans.run.TrackActivity;

import java.util.Locale;

public class ResultActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);

        setContentView(R.layout.activity_result);

        TextView textViewCustom = findViewById(R.id.textView9);
        SharedPreferences pref = this.getSharedPreferences("sport", 0); // 0 - for private mode
        String customSport = pref.getString("customSport", "");
        if (customSport.length()<1) {
            textViewCustom.setText("S P O R T D Λ S H");
        } else {
            textViewCustom.setText(customSport);
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

    public void nextRunning(View view) {
        startActivityGo("R U N N I N G");
    }
    public void nextSwimming(View view) {
        startActivityGo("S W I M M I N G");
    }
    public void nextWalking(View view) {
        startActivityGo("W Λ L K I N G");
    }
    public void nextBike(View view) {
        startActivityGo("B I K I N G");
    }
    public void nextBall(View view) {
        startActivityGo("B Λ L L S P O R T S");
    }
    public void nextClimbing(View view) {
        startActivityGo("C L I M B I N G");
    }
    public void nextSki(View view) {
        startActivityGo("S K I I N G");
    }
    public void nextWorkout(View view) {
        startActivityGo("W O R K O U T");
    }
    public void nextCustom(View view) {
        SharedPreferences pref = this.getSharedPreferences("sport", 0); // 0 - for private mode
        String customSport = pref.getString("customSport", "");
        if (customSport.length()<1) {
            startActivityGo("S P O R T D Λ S H");
        } else {
            startActivityGo(customSport);
        }
    }

    private void startActivityGo(String sport) {
        SharedPreferences settings = getSharedPreferences("sport", MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("whatSport", sport).commit();

        if (isCreate()) {
            Intent i = new Intent(this, CreatePlanActivity.class);
            startActivity(i);
        } else {
            Intent i = new Intent(this, TrackActivity.class);
            startActivity(i);
        }
    }

    boolean isCreate() {
        SharedPreferences pref = this.getSharedPreferences("sport", 0); // 0 - for private mode
        boolean create = pref.getBoolean("create", true);
        return create;
    }

    public void setCustom(View view) {
        EditText editTextSport = findViewById(R.id.editTextTextPersonNameElementCustom);
        SharedPreferences settings = getSharedPreferences("sport", MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("customSport", editTextSport.getText().toString().toUpperCase(Locale.ROOT)).commit();
        String sport = editTextSport.getText().toString().toUpperCase(Locale.ROOT);
        startActivityGo(sport);
    }


}