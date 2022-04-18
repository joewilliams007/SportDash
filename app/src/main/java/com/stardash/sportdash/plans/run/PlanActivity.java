package com.stardash.sportdash.plans.run;

import static com.stardash.sportdash.plans.run.RunPlanActivity.isRandom;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.stardash.sportdash.Account;
import com.stardash.sportdash.MainActivity;
import com.stardash.sportdash.R;
import com.stardash.sportdash.StarsocketConnector;
import com.stardash.sportdash.plans.create.CreateStructureActivity;

public class PlanActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan);
        setPlanNames();

        TextView textViewCreate = findViewById(R.id.textViewCreate);
        if (Account.isCreate()){
            textViewCreate.setVisibility(View.GONE);
            toast("select a position for your new plan");
        }

        Account.setActiveIterations(0);
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

    public static Boolean isMyPlan = true;

    private void setPlanNames() {
        TextView textViewNamePlan1 = findViewById(R.id.textViewNamePlan1);
        TextView textViewNamePlan2 = findViewById(R.id.textViewNamePlan2);
        TextView textViewNamePlan3 = findViewById(R.id.textViewNamePlan3);
        TextView textViewNamePlan4 = findViewById(R.id.textViewNamePlan4);
        TextView textViewNamePlan5 = findViewById(R.id.textViewNamePlan5);
        try {
            textViewNamePlan1.setText(Account.plan(1).split("\n", 5)[2]);
        } catch (Exception e){

        }
        try {
        textViewNamePlan2.setText(Account.plan(2).split("\n",5)[2]);
        } catch (Exception e){

        }
        try {
        textViewNamePlan3.setText(Account.plan(3).split("\n",5)[2]);
        } catch (Exception e){

        }
        try {
        textViewNamePlan4.setText(Account.plan(4).split("\n",5)[2]);
        } catch (Exception e){

        }
        try {
        textViewNamePlan5.setText(Account.plan(5).split("\n",5)[2]);
        } catch (Exception e){

        }

    }



    public void createPlan(View view) {
        Intent i = new Intent(this, CreateStructureActivity.class);
        startActivity(i);
    }


    private void runPlan(int plan) {
        isMyPlan = true;
        SharedPreferences settings = getSharedPreferences("sport", MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();

        Account.setActiveIterations(0); // set iterations to none done
        editor.putInt("selectedPlan", plan).commit(); // for running plan
        Account.setIsMine(true);
        if (Account.isCreate()){
            editor.putString(plan + " plan", String.valueOf(Account.committedPlan())).commit();
            uploadPlan(plan);
            Intent i = new Intent(this, PlanActivity.class);
            startActivity(i);

            editor.putBoolean("create", false).commit();
        } else {
            isRandom = false;
            Intent i = new Intent(this, RunPlanActivity.class);
            startActivity(i);
        }



    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }

    public void open1(View view) {
        TextView textViewNamePlan = findViewById(R.id.textViewNamePlan1);
        if (Account.isCreate()){
            runPlan(1);
        } else if (textViewNamePlan.getText().toString().equals("EMPTY PLAN")){
            Intent i = new Intent(this, CreateStructureActivity.class);
            startActivity(i);
        } else {
            runPlan(1);
        }
    }

    public void open2(View view) {
        TextView textViewNamePlan = findViewById(R.id.textViewNamePlan2);
        if (Account.isCreate()){
            runPlan(2);
        } else if (textViewNamePlan.getText().toString().equals("EMPTY PLAN")){
            Intent i = new Intent(this, CreateStructureActivity.class);
            startActivity(i);
        } else {
            runPlan(2);
        }
    }
    public void open3(View view) {
        TextView textViewNamePlan = findViewById(R.id.textViewNamePlan3);
        if (Account.isCreate()){
            runPlan(3);
        } else if (textViewNamePlan.getText().toString().equals("EMPTY PLAN")){
            Intent i = new Intent(this, CreateStructureActivity.class);
            startActivity(i);
        } else {
            runPlan(3);
        }
    }
    public void open4(View view) {
        TextView textViewNamePlan = findViewById(R.id.textViewNamePlan4);
        if (Account.isCreate()){
            runPlan(4);
        } else if (textViewNamePlan.getText().toString().equals("EMPTY PLAN")){
            Intent i = new Intent(this, CreateStructureActivity.class);
            startActivity(i);
        } else {
            runPlan(4);
        }
    }
    public void open5(View view) {
        TextView textViewNamePlan = findViewById(R.id.textViewNamePlan5);
        if (Account.isCreate()){
            runPlan(5);
        } else if (textViewNamePlan.getText().toString().equals("EMPTY PLAN")){
            Intent i = new Intent(this, CreateStructureActivity.class);
            startActivity(i);
        } else {
            runPlan(5);
        }
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

    public void downloadPlans(View view) {
        try {
            toast("starting download .");
            StarsocketConnector.sendMessage("downloadPlans " + Account.userid());
            String received_plans = StarsocketConnector.getMessage().toString();

            SharedPreferences settings = getSharedPreferences("sport", MODE_PRIVATE);
            SharedPreferences.Editor editor = settings.edit();

            String plan1 = received_plans.split("##########", 9)[1];
            String plan2 = received_plans.split("##########", 9)[2];
            String plan3 = received_plans.split("##########", 9)[3];
            String plan4 = received_plans.split("##########", 9)[4];
            String plan5 = received_plans.split("##########", 9)[5];

            editor.putString("1 plan", String.valueOf(plan1)).apply();
            editor.putString("2 plan", String.valueOf(plan2)).apply();
            editor.putString("3 plan", String.valueOf(plan3)).apply();
            editor.putString("4 plan", String.valueOf(plan4)).apply();
            editor.putString("5 plan", String.valueOf(plan5)).apply();


            final Handler handler = new Handler(Looper.getMainLooper());
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    toast("setting training plans ..");
                }
            }, 5000);

            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    toast("success!");
                    setPlanNames();
                }
            }, 10000);
        } catch (Exception e){
            toast("no network");
        }
    }

    public void uploadPlan(int planId) { // upload plan after user created it
        try {
        toast("uploading ...");
        if (online()){
            StarsocketConnector.sendMessage("upload_plans_everyone "+Account.userid()+" "+planId+" "+planId()+" XXXXXXXX"+category()+"XXXXXXXX ##########"+Account.plan(planId));
        } else {
            StarsocketConnector.sendMessage("upload_plans "+Account.userid()+" "+planId+" "+planId()+" ##########"+Account.plan(planId));
        }


            StarsocketConnector.getMessage();
            toast("success uploading plan of id "+String.valueOf(planId));
        } catch (Exception e){
            toast("no network");
        }
    }
    String category() { // the category the user wants to upload the plan to
        SharedPreferences pref = this.getSharedPreferences("sport", 0); // 0 - for private mode
        String c = pref.getString("category", null);
        return c;
    }
    String planId() { // the category the user wants to upload the plan to
        SharedPreferences pref = this.getSharedPreferences("sport", 0); // 0 - for private mode
        String c = pref.getString("planId", null);
        return c;
    }
    Boolean online() { // does the user want the plan to be online for everyone?
        SharedPreferences pref = this.getSharedPreferences("sport", 0); // 0 - for private mode
        Boolean a = pref.getBoolean("online", false);
        return a;
    }

    public void randomPlan(View view) {

        isRandom = true;
        isMyPlan = false;
        Intent i = new Intent(this, RunPlanActivity.class);
        startActivity(i);

    }
}