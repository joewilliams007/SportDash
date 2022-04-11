package com.stardash.sportdash;

import static com.stardash.sportdash.RunPlanActivity.commentsPlanId;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
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
import android.widget.EditText;
import android.widget.TextView;

public class CommentsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        setContentView(R.layout.activity_comments);
        getComments();
        TextView textViewClear = findViewById(R.id.textViewClear);
        try {
            String id = commentsPlanId.split("-")[0].replace("#", "");

            if (id.equals(Account.userid())) {
                textViewClear.setVisibility(View.VISIBLE);
            } else {
                textViewClear.setVisibility(View.GONE);
            }
        } catch (Exception e){
            textViewClear.setVisibility(View.GONE);
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

    private void getComments() {
        String plan_id = commentsPlanId;
        TextView textViewPlanId = findViewById(R.id.textViewPlanId);
        textViewPlanId.setText(plan_id);
        try {
            StarsocketConnector.sendMessage("getComments " + plan_id);
            final Handler handler = new Handler(Looper.getMainLooper());
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    TextView textViewComments = findViewById(R.id.textViewComments);
                    String received = StarsocketConnector.getMessage();

                    if (received.contains("errasdasdasdasdasdasdasdasd")) {
                        toast("no comments yet");
                    } else {
                        textViewComments.setText(received);
                    }
                }
            }, 500);
        } catch (Exception e){
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

    public void sendComment(View view) {
        vibrate();
        EditText editTextMessage = findViewById(R.id.editTextTextPersonName);
        try {
            if (editTextMessage.getText().toString().length() < 1) {
                toast("enter a message");
            } else {
                StarsocketConnector.sendMessage("commentPlan " + commentsPlanId + " " + Account.userid() + " " + Account.username() + " THECOMMENTISSTAR" + editTextMessage.getText().toString());
                toast("sent!");
                editTextMessage.setText("");
                getComments();
            }
        } catch (Exception e){
            toast("no network");
        }
    }


    public void clearComments(View view) {
        vibrate();
        try {
                StarsocketConnector.sendMessage("clearComments " + commentsPlanId.replace("#",""));
                toast("deleting comments . . .");
                getComments();
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
}