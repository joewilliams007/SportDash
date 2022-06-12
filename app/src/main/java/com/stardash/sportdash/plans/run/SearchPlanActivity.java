package com.stardash.sportdash.plans.run;

import static com.stardash.sportdash.plans.run.PlanActivity.isMyPlan;
import static com.stardash.sportdash.plans.run.RunPlanActivity.isRandom;
import static com.stardash.sportdash.settings.app.vibrate;

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
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.stardash.sportdash.MainActivity;
import com.stardash.sportdash.R;
import com.stardash.sportdash.network.tcp.StarsocketConnector;
import com.stardash.sportdash.online.ProfileActivity;
import com.stardash.sportdash.settings.Account;

public class SearchPlanActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_plan);

        if (Account.isAmoled()) {
            ConstraintLayout main = findViewById(R.id.main);
            main.setBackgroundDrawable(new ColorDrawable(Color.BLACK));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Window window = getWindow();
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(Color.BLACK);
            }
        }

        Bundle extras = getIntent().getExtras(); // get link hashtag if opened from link
        if (extras != null) {
            try {
            String value = extras.getString("planHashtag").toString();
            String id = value.split("#",3)[1];
            vibrate();

                try {
                    loadPlan(id);
                } catch (Exception e) {
                    toast("no network");
                }
            } catch (Exception e) {
                toast("invalid link");
            }
            //The key argument here must match that used in the other activity
        }
    }

    public void openPlan(View view) {
        vibrate();
        isRandom = false;
        isMyPlan = false;
        EditText editText1 = findViewById(R.id.editTextTextPersonNameHashtag);
        EditText editText2 = findViewById(R.id.editTextTextPersonNameDash);
        String first = editText1.getText().toString();
        String second = editText2.getText().toString();

        if (first.length()<1){
            toast("enter a valid plan id");
        } else {
            if (second.length()<1){
                toast("enter a valid plan id");
            } else {
                ((ProgressBar) findViewById(R.id.progressBar)).setVisibility(View.VISIBLE);
                loadPlan(first+"-"+second);
            }
        }
    }

    private void loadPlan(String id) {
        try {




                        String received = StarsocketConnector.getReplyTo("downloadPlanById " + id);

                        SharedPreferences settings = getSharedPreferences("sport", MODE_PRIVATE);
                        SharedPreferences.Editor editor = settings.edit();

                        String plan1 = received;

                        if (plan1.equals("err")) {
                            toast("invalid plan");
                            ((ProgressBar) findViewById(R.id.progressBar)).setVisibility(View.INVISIBLE);
                        } else {
                            editor.putString("1 planFriend", String.valueOf(plan1)).apply();
                            openPlan();
                        }

        } catch (Exception e){
            toast("no network");
        }
    }

    private void openPlan() {
        ((ProgressBar) findViewById(R.id.progressBar)).setVisibility(View.INVISIBLE);
        Account.setIsMine(false);
        isMyPlan= false;
        SharedPreferences settings = getSharedPreferences("sport", MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();

        Account.setActiveIterations(0); // set iterations to none done
        editor.putInt("selectedPlan", 1).apply(); // for running plan

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

    @Override
    public void onBackPressed() {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }
}