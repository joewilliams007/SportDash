package com.stardash.sportdash.plans.run;

import static com.stardash.sportdash.MainActivity.runningPlan;
import static com.stardash.sportdash.online.friends.FriendsActivity.isStars;
import static com.stardash.sportdash.online.friends.FriendsActivity.tappedOnSearchItem;
import static com.stardash.sportdash.online.friends.FriendsActivity.tappedOnSearchItemId;
import static com.stardash.sportdash.online.friends.follows.FollowActivity.pageStatus;
import static com.stardash.sportdash.plans.run.PlanActivity.isMyPlan;
import static com.stardash.sportdash.plans.run.live.LivePlanActivity.livePlanId;
import static com.stardash.sportdash.settings.app.vibrate;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
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
import android.provider.MediaStore;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.stardash.sportdash.MainActivity;
import com.stardash.sportdash.online.friends.FriendsActivity;
import com.stardash.sportdash.online.friends.follows.FollowActivity;
import com.stardash.sportdash.plans.run.inspect.InspectActivity;
import com.stardash.sportdash.plans.run.live.LivePlanActivity;
import com.stardash.sportdash.settings.Account;
import com.stardash.sportdash.plans.comments.CommentsActivity;
import com.stardash.sportdash.settings.FeedbackActivity;
import com.stardash.sportdash.R;
import com.stardash.sportdash.network.tcp.StarsocketConnector;
import com.stardash.sportdash.settings.MyApplication;
import com.stardash.sportdash.settings.account.TtsActivity;

public class RunPlanActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        setContentView(R.layout.activity_run_plan);
        runningPlan = true;

        try {
            if (isRandom) {
                generatePlan();
            } else {
                setPlan();
            }
        } catch (Exception ignored){

        }
        if (Account.isAmoled()) {
            ConstraintLayout main = findViewById(R.id.main);
            main.setBackgroundDrawable(new ColorDrawable(Color.BLACK));
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                window.setStatusBarColor(Color.BLACK);
            }
        }
      isMyPlan();

    }

    private void isMyPlan() {
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
            String name = thePlan().split("\n",5)[2];
            String category = thePlan().split("\n",5)[4];
            String description = thePlan().split("\n",5)[3];
            String firstRow = thePlan().split("\n",5)[1];
            String difficultyInt = firstRow.split(" ",5)[1];
            String difficulty = "none";
            String energy = firstRow.split(" ",5)[2];
            String duration = "none";
            String totalDuration = "none";
            String iterations = firstRow.split(" ",5)[3];
            String madeUser = thePlan().split("\n",9)[5];
            String madeId = thePlan().split("\n",9)[6];
            String planId = thePlan().split("\n",9)[7];
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

            switch (difficultyInt) {
                case "0":
                    difficulty = "normal";
                    break;
                case "1":
                    difficulty = "hard";
                    break;
                case "2":
                    difficulty = "ultra hard";
                    break;
                case "3":
                    difficulty = "unbeatable hard";
                    break;
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

            TextView textViewTopRight = findViewById(R.id.textViewTopRight); // textview to save plan
            textViewTopRight.setVisibility(View.VISIBLE);
            getStars();

        } catch (Exception ignored){

        }

    }
    public static String thePlan;
    public static Boolean isRandom;
    public static Boolean isSpecificPlan;
    public static String thePlan() {
        String plan = "err";

        SharedPreferences pref = MyApplication.getAppContext().getSharedPreferences("sport", 0); // 0 - for private mode
        int planInt = pref.getInt("selectedPlan", 0);

        if (isRandom) {
            plan = thePlan;
        } else if (Account.isMine()) {
            plan = pref.getString(String.valueOf(planInt) + " plan", null);
        } else if (isSpecificPlan) {
          plan = thePlan;
        } else{
             plan = pref.getString(planInt+" planFriend", "empty");
        }

        return plan;
    }


    @Override
    public void onBackPressed() {
        if (isRandom) {
            Intent i = new Intent(this, MainActivity.class);
            startActivity(i);
        } else {
            this.finish();

        }
    }

    public void deletePlan(View view) {
        vibrate();
        SharedPreferences settings = getSharedPreferences("sport", MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();

        SharedPreferences pref = this.getSharedPreferences("sport", 0); // 0 - for private mode
        int planInt = pref.getInt("selectedPlan", 0);

        editor.putString(String.valueOf(planInt)+ " plan", "EMPTY PLAN\nEMPTY PLAN\nEMPTY PLAN\nEMPTY PLAN\nEMPTY PLAN\n").apply();
        Intent i = new Intent(this, PlanActivity.class);
        startActivity(i);
    }

    public void openAccount(View view) {
        vibrate();
        TextView textViewMadeById = findViewById(R.id.textViewMadeByUserId);
        String id = textViewMadeById.getText().toString().replace("#", "").split(" ")[0];
            try {
                vibrate();
                tappedOnSearchItem = true;
                tappedOnSearchItemId = id;
                Intent i = new Intent(MyApplication.getAppContext(), FriendsActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                MyApplication.getAppContext().startActivity(i);
            } catch (Exception ignored) {

            }
    }

    @SuppressLint("SetTextI18n")
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
        editor.putString("category", "none").apply();
        editor.putString("committed plan",thePlan()).apply();
        editor.putBoolean("create", true).apply();

        Intent i = new Intent(this, PlanActivity.class);
        startActivity(i);
    }

    public void generateRandomPlan(View view) {
        isSpecificPlan = false;
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
                    try {
                        thePlan = StarsocketConnector.getMessage().toString();
                        isRandom = true;
                        setPlan();
                        isMyPlan = false;
                        isMyPlan();
                    } catch (Exception e){
                        toast("no network");
                    }
                }
            }, 1000);
        } catch (Exception e){
            toast("no network");
        }
    }

    public static String reportPlanId;
    public static Boolean reportingPlan;
    public void reportPlan(View view) {
        vibrate();
        reportingPlan = true;
        TextView textViewPlanId = findViewById(R.id.textViewPlanId);
        reportPlanId = textViewPlanId.getText().toString();
        Intent i = new Intent(this, FeedbackActivity.class);
        startActivity(i);
    }

    public static String commentsPlanId;

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
                TextView textViewPlanName = findViewById(R.id.textViewPlanName);
                String desc = " %%%"+textViewPlanName.getText().toString();
                StarsocketConnector.sendMessage("starPlan "+Account.userid()+" "+textViewPlanId.getText().toString().replace("#","")+desc);
                final Handler handler = new Handler(Looper.getMainLooper());
               // TextView textViewItem1 = findViewById(R.id.textViewItem1);

                handler.postDelayed(new Runnable() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void run() {
                        try {
                            String received = StarsocketConnector.getMessage();
                            if (received.contains("star-removed")) {
                                toast("star removed");
                            } else if (received.contains("star-added")) {
                                toast("star added");
                            }
                            viewPlan();
                            getStars();
                        } catch (Exception e){
                            toast("no network");
                        }
                    }
                }, 500);

            } catch (Exception e){
                toast("no network");
            }
    }

    public void viewPlan() {
        try {
            vibrate();
            TextView textViewPlanId = findViewById(R.id.textViewPlanId);
            StarsocketConnector.sendMessage("viewPlan "+Account.userid()+" "+textViewPlanId.getText().toString().replace("#",""));
        } catch (Exception ignored){

        }
    }

    @SuppressLint("SetTextI18n")
    public void getStars() { // and views
        TextView textViewStarsAmount = findViewById(R.id.textViewStarsAmount);
        TextView textViewViewsAmount = findViewById(R.id.textViewViewsAmount);
        TextView textViewStarPlan = findViewById(R.id.textViewStarPlan);
        try {
            TextView textViewPlanId = findViewById(R.id.textViewPlanId);
            StarsocketConnector.sendMessage("getStars "+textViewPlanId.getText().toString().replace("#",""));
            final Handler handler = new Handler(Looper.getMainLooper());

            handler.postDelayed(new Runnable() {
                @SuppressLint("SetTextI18n")
                @Override
                public void run() {
                    try {
                        String received = StarsocketConnector.getMessage();

                        ImageView imageViewVerified = findViewById(R.id.imageViewVerified);

                        if(Integer.parseInt(received.split("#")[0]) > 4) {
                            imageViewVerified.setVisibility(View.VISIBLE);
                        } else {
                            imageViewVerified.setVisibility(View.GONE);
                        }

                        if (received.contains("&")) {
                            textViewStarsAmount.setText("not available");
                            textViewViewsAmount.setText("not available");
                        } else {
                            textViewStarsAmount.setText(received.split("#")[0]);
                            textViewViewsAmount.setText(received.split("#")[1]);
                        }

                        String star_status = received.split("#")[2];
                        if (star_status.equals("1")) {
                            textViewStarPlan.setText("UN-STAR");
                        } else {
                            textViewStarPlan.setText("STAR");
                        }
                    } catch (Exception ignored){

                    }
                }
            }, 500);

        } catch (Exception e){
            textViewStarsAmount.setText("not available");
        }
    }

    public void copyPlanId(View view) {
        vibrate();
        TextView textViewPlanId = findViewById(R.id.textViewPlanId);
        String toCopy = textViewPlanId.getText().toString();
        ClipboardManager clipboard = (ClipboardManager) MyApplication.getAppContext().getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("SportDash-PLAN-ID", toCopy);
        clipboard.setPrimaryClip(clip);
        toast("copied plan id");
    }

    public void doNothing1(View view) {
        vibrate();
        ConstraintLayout searchLayout = findViewById(R.id.shareLayout);
        searchLayout.animate().alpha(0).setDuration(1000);
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                searchLayout.setVisibility(View.GONE);
            }
        }, 1000);
    }

    public void sharePlan(View view) {
        vibrate();
        ConstraintLayout searchLayout = findViewById(R.id.shareLayout);
        searchLayout.setVisibility(View.VISIBLE);
        searchLayout.animate().alpha(1).setDuration(1000);

    }

    public void copyPlanIdLink(View view) {
        vibrate();
        TextView textViewPlanId = findViewById(R.id.textViewPlanId);
        String toCopy = textViewPlanId.getText().toString();
        String clip0 = "SportDash - Training Plan\nAdd my account via the link https://www.sportdash.com/plan="+toCopy+"\nor enter the plan id "+toCopy;
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, clip0);
        sendIntent.setType("text/plain");
        startActivity(sendIntent);
    }

    public void copyTextPlanIdLink(View view) {
        vibrate();
        TextView textViewPlanId = findViewById(R.id.textViewPlanId);
        String toCopy = textViewPlanId.getText().toString();
        String clip0 = "SportDash - Training Plan\nAdd my account via the link https://www.sportdash.com/plan="+toCopy+"\nor enter the plan id "+toCopy;

        ClipboardManager clipboard = (ClipboardManager) MyApplication.getAppContext().getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("SportDash-PLAN-ID", clip0);
        clipboard.setPrimaryClip(clip);
        toast("copied plan id");
    }

    public void inspectPlan(View view) {
        vibrate();
        Intent i = new Intent(this, InspectActivity.class);
        startActivity(i);
    }

    public void ttsSettings(View view) {
        vibrate();
        Intent i = new Intent(this, TtsActivity.class);
        startActivity(i);
    }

    public void livePlan(View view) {
        vibrate();
        TextView textViewPlanId = findViewById(R.id.textViewPlanId);
        livePlanId = textViewPlanId.getText().toString().split("#")[1];
        Intent i = new Intent(this, LivePlanActivity.class);
        startActivity(i);
    }

    public void pageStars(View view) {
        try {
            pageStatus = "Stars";
            isStars = false;
            TextView textViewPlanId = findViewById(R.id.textViewPlanId);
            String id = textViewPlanId.getText().toString();
            vibrate();
            StarsocketConnector.sendMessage("plansStarsPage " + id.replace("#", ""));
            Intent it = new Intent(this, FollowActivity.class);
            startActivity(it);
        } catch (Exception e){
            toast("no network");
        }
    }

    public void openMusicApp(View view) {
        vibrate();
        try {
            Intent intent = new Intent(MediaStore.INTENT_ACTION_MUSIC_PLAYER);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } catch (Exception e){
            toast("no music player found");
        }
    }
}