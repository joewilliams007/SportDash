package com.stardash.sportdash.plans.run;

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

import com.stardash.sportdash.Account;
import com.stardash.sportdash.MainActivity;
import com.stardash.sportdash.R;
import com.stardash.sportdash.plans.run.ActivePlanActivity;

public class FinishedWorkoutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        overridePendingTransition(R.anim.fadein, R.anim.fadeout);

        setContentView(R.layout.activity_finished_workout);

        SharedPreferences settings = getSharedPreferences("sport", MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt("activeIterations", activeIterations()+1).commit(); // add 1 iteration done

        activeIterations();
        iterationsFinished();



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
    private void iterationsFinished() {
        if (iterations() > activeIterations()){
            Intent i = new Intent(this, ActivePlanActivity.class);
            startActivity(i);
        } else  {

        }
    }

    public void next(View view) {

        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }

    @Override
    public void onBackPressed() {

    }

    int iterations() {

            String firstRow = plan().split("\n",5)[1];
            int iterationsInt = Integer.parseInt(firstRow.split(" ",5)[3]);

        return iterationsInt;
    }

    int activeIterations() {
        SharedPreferences pref = this.getSharedPreferences("sport", 0); // 0 - for private mode
        int activeIterations = pref.getInt("activeIterations", 0);
        return activeIterations;
    }

    String plan() {
        SharedPreferences pref = this.getSharedPreferences("sport", 0); // 0 - for private mode
        int planInt = pref.getInt("selectedPlan", 0);

        String plan = pref.getString(String.valueOf(planInt)+" plan", null);
        return plan;
    }
}