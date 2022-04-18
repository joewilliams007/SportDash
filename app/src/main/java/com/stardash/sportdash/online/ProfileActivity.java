package com.stardash.sportdash.online;

import static com.stardash.sportdash.plans.run.PlanActivity.isMyPlan;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.animation.ObjectAnimator;
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
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.stardash.sportdash.R;
import com.stardash.sportdash.network.tcp.StarsocketConnector;
import com.stardash.sportdash.online.FriendsActivity;
import com.stardash.sportdash.online.chat.ChatActivity;
import com.stardash.sportdash.plans.run.RunPlanActivity;
import com.stardash.sportdash.settings.Account;

public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        setContentView(R.layout.activity_profile);
        invalidId = false;
        ChatActivity.updateChat = false;
        try {
            final Handler handler = new Handler(Looper.getMainLooper());
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    getUserProfile();
                }
            }, 100);


        } catch (Exception e) {
            Intent i = new Intent(this, FriendsActivity.class);
            startActivity(i);
        }
        if (Account.isAmoled()) {
            ConstraintLayout main = findViewById(R.id.main);
            main.setBackgroundDrawable(new ColorDrawable(Color.BLACK));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Window window = getWindow();
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(Color.BLACK);
            }
            TextView textView = findViewById(R.id.textViewID);
            textView.setTextColor(Color.parseColor("#FFFFFF"));
            TextView textView1 = findViewById(R.id.textViewAdd);
            textView1.setTextColor(Color.parseColor("#FFFFFF"));
            TextView textViewChat = findViewById(R.id.textViewChat);
            textViewChat.setVisibility(View.VISIBLE);
            ImageView imageView = findViewById(R.id.imageViewCompose);
            imageView.setVisibility(View.INVISIBLE);

        }
    }

    private void getUserProfile() {
        try {
            String profile = StarsocketConnector.getMessage();

            String id = profile.split(" ", 15)[0];
            String username = profile.split(" ", 15)[1];
            String xp = profile.split(" ", 15)[2];
            String xpToday = profile.split(" ", 15)[3];
            String xpWeek = profile.split(" ", 15)[4];
            String age = profile.split(" ", 15)[5];
            String weight = profile.split(" ", 15)[6];


            Account.setFriend(0, "#" + id + " " + username);


            TextView textViewUsername = findViewById(R.id.textViewUsername);
            TextView textViewUserID = findViewById(R.id.textViewUserID);
            TextView textViewLevel = findViewById(R.id.textViewLevel);
            TextView textViewXp = findViewById(R.id.textViewXp);
            TextView textViewAge = findViewById(R.id.textViewAge);
            TextView textViewWeight = findViewById(R.id.textViewWeight);
            TextView textViewXpToday = findViewById(R.id.textViewXpToday);
            TextView textViewXpWeek = findViewById(R.id.textViewXpWeek);

            int level = 100;
            try {
                int number = Integer.parseInt(xp);
                level = number / 10000 + 1;
            } catch (NumberFormatException ex) {
                ex.printStackTrace();
            }

            textViewUsername.setText(username);
            textViewUserID.setText("#" + id);

            ImageView imageViewVerified = findViewById(R.id.imageViewVerified);
            if (id.equals("1")){
                imageViewVerified.setVisibility(View.VISIBLE);
            }
            textViewLevel.setText(String.valueOf(level));
            textViewXp.setText(xp + " xp");
            ProgressBar progressBarToday = findViewById(R.id.progressBarToday);
            ProgressBar progressBarWeek = findViewById(R.id.progressBarWeek);
            try {
                ObjectAnimator.ofInt(progressBarToday, "progress", Integer.valueOf(xpToday))
                        .setDuration(600)
                        .start();
                ObjectAnimator.ofInt(progressBarWeek, "progress", Integer.valueOf(xpWeek))
                        .setDuration(600)
                        .start();
            } catch (Exception e){

            }
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    textViewXpToday.setText(xpToday + "/10.000 xp");
                    textViewXpWeek.setText(xpWeek + "/70.000 xp");
                }
            }, 599);


            textViewAge.setText(age);
            textViewWeight.setText(weight + " kg");

            loadPlans(id);

        } catch (Exception e){
            invalidId = true;
            Intent i = new Intent(this, FriendsActivity.class);
            startActivity(i);
        }

    }
    public static Boolean invalidId;
    private void loadPlans(String id) {
        try {
            StarsocketConnector.sendMessage("downloadPlans " + id);
            String received_plans = StarsocketConnector.getMessage().toString();

            SharedPreferences settings = getSharedPreferences("sport", MODE_PRIVATE);
            SharedPreferences.Editor editor = settings.edit();

            String plan1 = received_plans.split("##########", 9)[1];
            String plan2 = received_plans.split("##########", 9)[2];
            String plan3 = received_plans.split("##########", 9)[3];
            String plan4 = received_plans.split("##########", 9)[4];
            String plan5 = received_plans.split("##########", 9)[5];

            editor.putString("1 planFriend", String.valueOf(plan1)).apply();
            editor.putString("2 planFriend", String.valueOf(plan2)).apply();
            editor.putString("3 planFriend", String.valueOf(plan3)).apply();
            editor.putString("4 planFriend", String.valueOf(plan4)).apply();
            editor.putString("5 planFriend", String.valueOf(plan5)).apply();

            setPlanNames();
        } catch (Exception e){
           // toast("no network");
        }
    }

    private void setPlanNames() {
        TextView textViewNamePlan1 = findViewById(R.id.textViewPlan1);
        TextView textViewNamePlan2 = findViewById(R.id.textViewPlan2);
        TextView textViewNamePlan3 = findViewById(R.id.textViewPlan3);
        TextView textViewNamePlan4 = findViewById(R.id.textViewPlan4);
        TextView textViewNamePlan5 = findViewById(R.id.textViewPlan5);
        try {
            textViewNamePlan1.setText(Account.planFriend(1).split("\n", 5)[2]);
        } catch (Exception e){

        }
        try {
            textViewNamePlan2.setText(Account.planFriend(2).split("\n",5)[2]);
        } catch (Exception e){

        }
        try {
            textViewNamePlan3.setText(Account.planFriend(3).split("\n",5)[2]);
        } catch (Exception e){

        }
        try {
            textViewNamePlan4.setText(Account.planFriend(4).split("\n",5)[2]);
        } catch (Exception e){

        }
        try {
            textViewNamePlan5.setText(Account.planFriend(5).split("\n",5)[2]);
        } catch (Exception e){

        }

    }
    public void plan1(View view) {
        vibrate();
        TextView textViewNamePlan = findViewById(R.id.textViewPlan1);
        if (textViewNamePlan.getText().toString().equals("E M P T Y")) {
            toast("empty plan");
        } else {
            openPlan(1);
        }
    }
    public void plan2(View view) {
        vibrate();
        TextView textViewNamePlan = findViewById(R.id.textViewPlan2);
        if (textViewNamePlan.getText().toString().equals("E M P T Y")) {
            toast("empty plan");
        } else {
            openPlan(2);
        }
    }
    public void plan3(View view) {
        vibrate();
        TextView textViewNamePlan = findViewById(R.id.textViewPlan3);
        if (textViewNamePlan.getText().toString().equals("E M P T Y")) {
            toast("empty plan");
        } else {
            openPlan(3);
        }
    }
    public void plan4(View view) {
        vibrate();
        TextView textViewNamePlan = findViewById(R.id.textViewPlan4);
        if (textViewNamePlan.getText().toString().equals("E M P T Y")) {
            toast("empty plan");
        } else {
            openPlan(4);
        }
    }
    public void plan5(View view) {
        vibrate();
        TextView textViewNamePlan = findViewById(R.id.textViewPlan5);
        if (textViewNamePlan.getText().toString().equals("E M P T Y")) {
            toast("empty plan");
        } else {
            openPlan(5);
        }
    }


    private void openPlan(int id) {
        Account.setIsMine(false);
        isMyPlan= false;
        SharedPreferences settings = getSharedPreferences("sport", MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();

        Account.setActiveIterations(0); // set iterations to none done
        editor.putInt("selectedPlan", id).commit(); // for running plan

            Intent i = new Intent(this, RunPlanActivity.class);
            startActivity(i);

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

    public void saveFriend(View view) {
        vibrate();
        TextView textViewUsername = findViewById(R.id.textViewUsername);
        TextView textViewUserID = findViewById(R.id.textViewUserID);
        String text = textViewUserID.getText().toString()+" "+textViewUsername.getText().toString();
        Account.selectFriend(text);
        Account.setAddingFriend(true);

        Intent i = new Intent(this, FriendsActivity.class);
        startActivity(i);
    }

    public static String chatId;
    public static String chatUsername;
    public void openChat(View view) {
        vibrate();
        TextView textViewUsername = findViewById(R.id.textViewUsername);
        TextView textViewUserID = findViewById(R.id.textViewUserID);
        chatUsername = textViewUsername.getText().toString();
        chatId = textViewUserID.getText().toString().replace("#","");
        Intent i = new Intent(this, ChatActivity.class);
        startActivity(i);
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