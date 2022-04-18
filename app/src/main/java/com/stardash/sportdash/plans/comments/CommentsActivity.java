package com.stardash.sportdash.plans.comments;

import static com.stardash.sportdash.plans.run.RunPlanActivity.commentsPlanId;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import android.widget.ProgressBar;
import android.widget.TextView;

import com.stardash.sportdash.settings.Account;
import com.stardash.sportdash.R;
import com.stardash.sportdash.network.tcp.StarsocketConnector;

import java.util.ArrayList;

public class CommentsActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private CommentsAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        setContentView(R.layout.activity_comments);
        ((ProgressBar) findViewById(R.id.progressBar)).setVisibility(View.VISIBLE);
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

    static String lastS;
    static ArrayList<CommentsItem> commentsList;

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

                    try {

            String s[] = StarsocketConnector.getMessage().split("NEXTMESSAGEIS:;");
            String newS = s[s.length-1];

                String ans = "";
                for (int i = s.length - 1; i >= 0; i--) {
                    ans += s[i] + "NEXTMESSAGEIS:;";
                }

                String[] chat = ans.split("NEXTMESSAGEIS:;");


                commentsList = new ArrayList<>();

                for (String element : chat) {
                    try {
                        commentsList.add(new CommentsItem(element.split("@")[1], element.split("@")[2], element.split("@")[0]));
                    } catch (Exception e) {

                    }
                }

                lastS = newS;

                // chatList.add(new ChatItem("JoeJoe", "Hi how are you?", "10.44 pm"));

                mRecyclerView = findViewById(R.id.comments_recycler_view);
                mRecyclerView.setHasFixedSize(true);
                mLayoutManager = new LinearLayoutManager(CommentsActivity.this);
                mAdapter = new CommentsAdapter(commentsList);

                mRecyclerView.setLayoutManager(mLayoutManager);
                mRecyclerView.setAdapter(mAdapter);

                //  mRecyclerView.scrollToPosition(chatList.size() - 1);

                        ((ProgressBar) findViewById(R.id.progressBar)).setVisibility(View.GONE);
                } catch (Exception e){
                    toast("no network");
                }


                }
            }, 1000);
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
        ((ProgressBar) findViewById(R.id.progressBar)).setVisibility(View.VISIBLE);
        vibrate();
        EditText editTextMessage = findViewById(R.id.editTextTextPersonName);
        try {
            if (editTextMessage.getText().toString().length() < 1) {
                toast("enter a message");
            } else {
                StarsocketConnector.sendMessage("commentPlan " + commentsPlanId + " " + Account.userid() + " " + Account.username() + " THECOMMENTISSTAR" + editTextMessage.getText().toString());
                toast("sent!");
                editTextMessage.setText("");
                final Handler handler = new Handler(Looper.getMainLooper());
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getComments();
                    }
                }, 500);

            }
        } catch (Exception e){
            toast("no network");
        }
    }


    public void clearComments(View view) {
        ((ProgressBar) findViewById(R.id.progressBar)).setVisibility(View.VISIBLE);
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