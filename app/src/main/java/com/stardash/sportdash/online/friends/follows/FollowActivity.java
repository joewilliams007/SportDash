package com.stardash.sportdash.online.friends.follows;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.stardash.sportdash.R;
import com.stardash.sportdash.network.tcp.StarsocketConnector;
import com.stardash.sportdash.settings.Account;
import com.stardash.sportdash.settings.changelog.UpdateActivity;
import com.stardash.sportdash.settings.changelog.UpdateAdapter;
import com.stardash.sportdash.settings.changelog.UpdateItem;

import java.util.ArrayList;

public class FollowActivity extends AppCompatActivity {

    public static String pageStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_follow);
        ((ProgressBar) findViewById(R.id.progressBar)).setVisibility(View.VISIBLE);

        TextView textViewTop = findViewById(R.id.textViewTop);
        textViewTop.setText(pageStatus);

        final Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                getData();
            }
        }, 200);
    }

    private void getData() {
        try {
            String received = StarsocketConnector.getMessage().replaceAll("\"", "");;
            createUpdateList(received);
        } catch (Exception e){
            toast("no network");
            ((ProgressBar) findViewById(R.id.progressBar)).setVisibility(View.INVISIBLE);
        }
    }

    private ArrayList<FollowItem> mFollowList;
    private RecyclerView mRecyclerView;
    private FollowAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    public void createUpdateList(String data){
        String[] user = data.split("\n");

        mFollowList = new ArrayList<>();

        for (String element : user) {
            try {
                if (element.contains("undefined")) {

                } else {
                    mFollowList.add(new FollowItem(element.split("@")[0], element.split("@")[1], element.split("@")[2]));

                }
            } catch (Exception ignored) {

            }
        }
        buildRecyclerView();
    }

    public void buildRecyclerView(){
        mRecyclerView = findViewById(R.id.follow_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(FollowActivity.this);
        mAdapter = new FollowAdapter(mFollowList);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        ((ProgressBar) findViewById(R.id.progressBar)).setVisibility(View.GONE);
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
}