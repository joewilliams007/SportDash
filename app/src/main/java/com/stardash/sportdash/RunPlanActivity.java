package com.stardash.sportdash;

import static com.stardash.sportdash.PlanActivity.isMyPlan;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.CheckBox;
import android.widget.TextView;

public class RunPlanActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

            overridePendingTransition(R.anim.fadein, R.anim.fadeout);


        setContentView(R.layout.activity_run_plan);

        try {
            if (isRandom) {
                generatePlan();
            } else {
                setPlan();
            }
        } catch (Exception e){

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
      ismyplan();
    }

    private void ismyplan() {
        try {
            TextView textViewDiscard = findViewById(R.id.textViewTopLeft);
            TextView textViewReport = findViewById(R.id.textViewReport);
            if (!isMyPlan) {
                textViewDiscard.setVisibility(View.GONE);
                textViewReport.setVisibility(View.VISIBLE);
            }
        } catch (Exception e){

        }
    }

    public void start(View view) {
        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            v.vibrate(VibrationEffect.createOneShot(100, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            v.vibrate(100);
        }
        Intent i = new Intent(this, ActivePlanActivity.class);
        startActivity(i);
    }

    @SuppressLint("SetTextI18n")
    private void setPlan() {
        try {
            String name = plan().split("\n",5)[2];
            String category = plan().split("\n",5)[4];
            String description = plan().split("\n",5)[3];
            String firstRow = plan().split("\n",5)[1];
            String difficultyInt = firstRow.split(" ",5)[1];
            String difficulty = "none";
            String energy = firstRow.split(" ",5)[2];
            String duration = "none";
            String totalDuration = "none";
            String iterations = firstRow.split(" ",5)[3];
            String madeUser = plan().split("\n",9)[5];
            String madeId = plan().split("\n",9)[6];
            String planId = plan().split("\n",9)[7];
            int iterationsInt = Integer.parseInt(firstRow.split(" ",5)[3]);

            try{
                int number = Integer.parseInt(firstRow.split(" ",5)[0]);
                totalDuration = String.valueOf(number/60*iterationsInt);
                if (number/60*iterationsInt<4){
                    toast("! this workout is under 4 minutes !");
                }
            } catch (NumberFormatException ex){
            ex.printStackTrace();
             }

            try{
                int number = Integer.parseInt(firstRow.split(" ",5)[0]);
                duration = String.valueOf(number/60);
            }
            catch (NumberFormatException ex){
                ex.printStackTrace();
            }

            if (difficultyInt.equals("0")){
                difficulty = "normal";
            } else if (difficultyInt.equals("1")){
                difficulty = "hard";
            }else if (difficultyInt.equals("2")){
                difficulty = "ultra hard";
            }else if (difficultyInt.equals("3")){
                difficulty = "unbeatable hard";
            }

            TextView textViewPlanSport = findViewById(R.id.textViewPlanSport);
            TextView textViewPlanName = findViewById(R.id.textViewPlanName);
            TextView textViewPlanDifficulty = findViewById(R.id.textViewPlanDifficulty);
            TextView textViewPlanDescription = findViewById(R.id.textViewPlanDescription);
            TextView textViewEnergy = findViewById(R.id.textViewEnergy);
            TextView textViewPlanDuration = findViewById(R.id.textViewPlanDuration);
            TextView textViewIterations = findViewById(R.id.textViewPlanIterations);
            TextView textViewPlanTotalDuration = findViewById(R.id.textViewPlanTotalDuration);

            TextView textViewMadeByUser = findViewById(R.id.textViewMadeByUser);
            TextView textViewPlanId = findViewById(R.id.textViewPlanId);
            TextView textViewMadeById = findViewById(R.id.textViewMadeByUserId);

            textViewMadeById.setText("#"+madeId);
            textViewMadeByUser.setText(madeUser);
            textViewPlanId.setText("#"+planId);

            textViewPlanSport.setText(category.split("\n",5)[0]);
            textViewPlanName.setText(name);
            textViewPlanDifficulty.setText(difficulty);
            textViewPlanDescription.setText(description);
            textViewEnergy.setText(energy);
            textViewPlanDuration.setText(duration+" min");
            textViewIterations.setText(iterations+"x");
            textViewPlanTotalDuration.setText(totalDuration+" min");

            getStars();

        } catch (Exception e){

        }

    }
    static Boolean isRandom;
    String plan() {
        String plan = "err";

        SharedPreferences pref = this.getSharedPreferences("sport", 0); // 0 - for private mode
        int planInt = pref.getInt("selectedPlan", 0);

        if (isRandom) {
            plan = thePlan;
        } else if (Account.isMine()) {
            plan = pref.getString(String.valueOf(planInt) + " plan", null);
        } else {
             plan = pref.getString(planInt+" planFriend", "empty");
        }

        return plan;
    }
    static String thePlan;
    public void openLobby(View view) {
        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            v.vibrate(VibrationEffect.createOneShot(100, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            v.vibrate(100);
        }
        Intent i = new Intent(this, LobbyActivity.class);
        startActivity(i);
    }

    /*@Override
    public void onBackPressed() {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }*/

    public void deletePlan(View view) {
        vibrate();
        SharedPreferences settings = getSharedPreferences("sport", MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();

        SharedPreferences pref = this.getSharedPreferences("sport", 0); // 0 - for private mode
        int planInt = pref.getInt("selectedPlan", 0);

        editor.putString(String.valueOf(planInt)+ " plan", "EMPTY PLAN\nEMPTY PLAN\nEMPTY PLAN\nEMPTY PLAN\nEMPTY PLAN\n").commit();
        Intent i = new Intent(this, PlanActivity.class);
        startActivity(i);
    }

    public void openAccount(View view) {
        vibrate();
        TextView textViewMadeById = findViewById(R.id.textViewMadeByUserId);
        try {
            StarsocketConnector.sendMessage("getProfile " + textViewMadeById.getText().toString().replace("#", "").split(" ")[0]);
            Intent i = new Intent(this, ProfileActivity.class);
            startActivity(i);
        } catch (Exception e) {
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

    public void savePlan(View view) {
        vibrate();
        SharedPreferences settings = getSharedPreferences("sport", MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        CheckBox checkBox = findViewById(R.id.checkBox);
        editor.putBoolean("online",false).apply();

        editor.putString("category", "none").commit();
        editor.putString("committed plan",plan()).commit();

        editor.putBoolean("create", true).commit();

        Intent i = new Intent(this, PlanActivity.class);
        startActivity(i);
    }

    public void generateRandomPlan(View view) {
        generatePlan();
    }

    private void generatePlan() {
        vibrate();
        try {
            StarsocketConnector.sendMessage("randomPlan");
            final Handler handler = new Handler(Looper.getMainLooper());
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    thePlan = StarsocketConnector.getMessage().toString();
                    isRandom = true;
                    setPlan();
                    isMyPlan = false;
                    ismyplan();
                }
            }, 1000);

        } catch (Exception e){
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

    static String reportPlanId;
    static Boolean reportingPlan;
    public void reportPlan(View view) {
        vibrate();
        reportingPlan = true;
        TextView textViewPlanId = findViewById(R.id.textViewPlanId);
        reportPlanId = textViewPlanId.getText().toString();
        Intent i = new Intent(this, FeedbackActivity.class);
        startActivity(i);
    }

    static String commentsPlanId;

    public void openComments(View view) {
        vibrate();
        TextView textViewPlanId = findViewById(R.id.textViewPlanId);
        commentsPlanId = textViewPlanId.getText().toString();
        Intent i = new Intent(this, CommentsActivity.class);
        startActivity(i);
    }

    public void starPlan(View view) {
            try {
                vibrate();
                TextView textViewPlanId = findViewById(R.id.textViewPlanId);
                StarsocketConnector.sendMessage("starPlan "+Account.userid()+" "+textViewPlanId.getText().toString().replace("#",""));
                final Handler handler = new Handler(Looper.getMainLooper());
               // TextView textViewItem1 = findViewById(R.id.textViewItem1);

                handler.postDelayed(new Runnable() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void run() {
                        String received = StarsocketConnector.getMessage();
                        if (received.contains("star-removed")) {
                            toast("star removed");
                        } else if (received.contains("star-added")) {
                            toast("star added");
                        }
                        getStars();
                    }
                }, 500);

            } catch (Exception e){
                toast("no network");
            }
    }
    public void getStars() {
        TextView textViewStarsAmount = findViewById(R.id.textViewStarsAmount);
        try {
            TextView textViewPlanId = findViewById(R.id.textViewPlanId);
            StarsocketConnector.sendMessage("getStars "+textViewPlanId.getText().toString().replace("#",""));
            final Handler handler = new Handler(Looper.getMainLooper());

            handler.postDelayed(new Runnable() {
                @SuppressLint("SetTextI18n")
                @Override
                public void run() {
                    String received = StarsocketConnector.getMessage();
                    textViewStarsAmount.setText(received);
                }
            }, 500);

        } catch (Exception e){
            textViewStarsAmount.setText("not available");
        }
    }
}