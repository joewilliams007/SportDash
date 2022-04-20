package com.stardash.sportdash.me.leaderboard;

import static com.stardash.sportdash.settings.app.vibrate;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
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
import android.widget.TextView;

import com.stardash.sportdash.online.ProfileActivity;
import com.stardash.sportdash.R;
import com.stardash.sportdash.network.tcp.StarsocketConnector;
import com.stardash.sportdash.settings.Account;

import java.util.ArrayList;

public class leaderboard extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        setContentView(R.layout.activity_leaderboard);
        getLeaderboard();

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

    static ArrayList<LeaderboardItem> leaderboardList;


    private void getLeaderboard() {
        try {
            StarsocketConnector.sendMessage("leaderboard");
            final Handler handler = new Handler(Looper.getMainLooper());
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    String received = StarsocketConnector.getMessage().replaceAll("\"", "");

                    try {
                        String rest = received;

                        createBoardList(rest);
                        buildRecyclerView();

                    } catch (Exception e){
                            toast("not enough users to determine all");
                    }
                }
            }, 200);
        } catch (Exception e){
            toast("no network");
        }
    }

    private ArrayList<LeaderboardItem> mLeaderboardList;
    private RecyclerView mRecyclerView;
    private LeaderboardAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    public void createBoardList(String rest){
        String[] user = rest.split("\n");

        mLeaderboardList = new ArrayList<>();

        for (String element : user) {
            try {
                mLeaderboardList.add(new LeaderboardItem(element.split("@")[0]+" place", element.split("@")[2]+element.split("@")[3], element.split("@")[1]));
            } catch (Exception e) {

            }
        }
    }

    public void buildRecyclerView(){
        mRecyclerView = findViewById(R.id.leaderboard_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(leaderboard.this);
        mAdapter = new LeaderboardAdapter(mLeaderboardList);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
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




}