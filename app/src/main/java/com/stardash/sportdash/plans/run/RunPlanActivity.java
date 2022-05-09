package com.stardash.sportdash.plans.run;

import static com.stardash.sportdash.online.friends.FriendsActivity.tappedOnSearchItem;
import static com.stardash.sportdash.online.friends.FriendsActivity.tappedOnSearchItemId;
import static com.stardash.sportdash.plans.run.PlanActivity.isMyPlan;
import static com.stardash.sportdash.settings.app.vibrate;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
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
import android.widget.ProgressBar;
import android.widget.TextView;

import com.stardash.sportdash.MainActivity;
import com.stardash.sportdash.online.friends.FriendsActivity;
import com.stardash.sportdash.plans.run.inspect.InspectActivity;
import com.stardash.sportdash.settings.Account;
import com.stardash.sportdash.plans.comments.CommentsActivity;
import com.stardash.sportdash.settings.FeedbackActivity;
import com.stardash.sportdash.online.LobbyActivity;
import com.stardash.sportdash.online.ProfileActivity;
import com.stardash.sportdash.R;
import com.stardash.sportdash.network.tcp.StarsocketConnector;
import com.stardash.sportdash.settings.MyApplication;

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

            TextView textViewTopRight = findViewById(R.id.textViewTopRight); // textview to save plan
            textViewTopRight.setVisibility(View.VISIBLE);
            getStars();

        } catch (Exception e){

        }

    }
    public static Boolean isRandom;
    public static String thePlan() {
        String plan = "err";

        SharedPreferences pref = MyApplication.getAppContext().getSharedPreferences("sport", 0); // 0 - for private mode
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

    @Override
    public void onBackPressed() {
        if (isRandom) {
            Intent i = new Intent(this, MainActivity.class);
            startActivity(i);
        } else {
            this.finish();
            /* AlertDialog.Builder builder = new AlertDialog.Builder(RunPlanActivity.this);
            builder.setTitle(R.string.app_name);
            builder.setIcon(R.mipmap.ic_launcher);
            builder.setMessage("If You Enjoy The App Please Rate us?")
                    .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Intent play =
                                    new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=kd.travellingtips"));
                            startActivity(play);
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            finish();
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show(); */
        }
    }

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
        String id = textViewMadeById.getText().toString().replace("#", "").split(" ")[0];
            try {
                vibrate();
                tappedOnSearchItem = true;
                tappedOnSearchItemId = id;
                Intent i = new Intent(MyApplication.getAppContext(), FriendsActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                MyApplication.getAppContext().startActivity(i);
            } catch (Exception e) {

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
        editor.putString("committed plan",thePlan()).commit();

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
                    try {
                        String received = StarsocketConnector.getMessage();
                        textViewStarsAmount.setText(received);
                    } catch (Exception e){

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
}