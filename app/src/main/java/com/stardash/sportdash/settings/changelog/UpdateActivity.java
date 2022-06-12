package com.stardash.sportdash.settings.changelog;

import static com.stardash.sportdash.settings.app.vibrate;

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
import android.widget.ProgressBar;
import android.widget.TextView;

import com.stardash.sportdash.BuildConfig;
import com.stardash.sportdash.R;
import com.stardash.sportdash.me.leaderboard.LeaderboardAdapter;
import com.stardash.sportdash.me.leaderboard.LeaderboardItem;
import com.stardash.sportdash.me.leaderboard.leaderboard;
import com.stardash.sportdash.network.tcp.StarsocketConnector;
import com.stardash.sportdash.settings.Account;

import java.util.ArrayList;

public class UpdateActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        setContentView(R.layout.activity_update);
        ((ProgressBar) findViewById(R.id.progressBar)).setVisibility(View.VISIBLE);
        ((TextView) findViewById(R.id.textViewLoad)).setVisibility(View.GONE);
        if (Account.isAmoled()) {
            ConstraintLayout main = findViewById(R.id.main);
            main.setBackgroundDrawable(new ColorDrawable(Color.BLACK));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Window window = getWindow();
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(Color.BLACK);
            }
        }
        getChangelog("changelog");
    }
    String versionName = BuildConfig.VERSION_NAME;
    private void getChangelog(String type) {
        try {
            StarsocketConnector.sendMessage(type);
            final Handler handler = new Handler(Looper.getMainLooper());
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    String received = StarsocketConnector.getTMessage();
                    createUpdateList(received);
                }
            }, 1000);

        } catch (Exception e){
            toast("no network");
        }
    }


    private ArrayList<UpdateItem> mUpdateList;
    private RecyclerView mRecyclerView;
    private UpdateAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    public void createUpdateList(String update){
        String[] user = update.split("#\n");

        mUpdateList = new ArrayList<>();

        for (String element : user) {
            try {
                mUpdateList.add(new UpdateItem(element.split("@")[0], element.split("@")[1], element.split("@")[2]));
            } catch (Exception e) {

            }
        }
        buildRecyclerView();
    }

    public void buildRecyclerView(){
        mRecyclerView = findViewById(R.id.update_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(UpdateActivity.this);
        mAdapter = new UpdateAdapter(mUpdateList);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
        ((ProgressBar) findViewById(R.id.progressBar)).setVisibility(View.GONE);
        ((TextView) findViewById(R.id.textViewLoad)).setVisibility(View.VISIBLE);
        ((TextView) findViewById(R.id.textViewFuture)).setVisibility(View.VISIBLE);
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

    public void myVersion(View view) {
        vibrate();
        toast("your version is "+versionName);
    }

    public void reload(View view) {
        vibrate();
        ((ProgressBar) findViewById(R.id.progressBar)).setVisibility(View.VISIBLE);
        ((TextView) findViewById(R.id.textViewLoad)).setVisibility(View.GONE);
        getChangelog("changelog");

    }

    public void soon(View view) {
        vibrate();
        ((ProgressBar) findViewById(R.id.progressBar)).setVisibility(View.VISIBLE);
        ((TextView) findViewById(R.id.textViewFuture)).setVisibility(View.GONE);
        getChangelog("futureLog");
    }
}