package com.stardash.sportdash.online.feed;

import static com.stardash.sportdash.settings.app.vibrate;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.stardash.sportdash.R;
import com.stardash.sportdash.me.leaderboard.LeaderboardAdapter;
import com.stardash.sportdash.me.leaderboard.LeaderboardItem;
import com.stardash.sportdash.me.leaderboard.leaderboard;
import com.stardash.sportdash.network.tcp.StarsocketConnector;
import com.stardash.sportdash.settings.Account;

import java.util.ArrayList;

public class FeedActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);
        ((ProgressBar) findViewById(R.id.progressBar)).setVisibility(View.VISIBLE);
        final Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                createFeedList(StarsocketConnector.getMessage().replaceAll("undefined",""));
                buildRecyclerView();
                ((ProgressBar) findViewById(R.id.progressBar)).setVisibility(View.GONE);
            }
        }, 100);
    }

    private void getFeed(String type) {
        ((ProgressBar) findViewById(R.id.progressBar)).setVisibility(View.VISIBLE);
        StarsocketConnector.sendMessage(type+" "+Account.userid());

        TextView textViewFollowing = findViewById(R.id.textViewFollowing);
        TextView textViewFresh = findViewById(R.id.textViewFresh);
        TextView textViewAll = findViewById(R.id.textViewAll);

        if (type.equals("all_time")) {
            textViewFollowing.setTextColor(Color.parseColor("#FFFFFFFF"));
            textViewFresh.setTextColor(Color.parseColor("#FFFFFFFF"));
            textViewAll.setTextColor(Color.parseColor("#14FFEC"));
        } else if (type.equals("feed_fresh")) {
            textViewFollowing.setTextColor(Color.parseColor("#FFFFFFFF"));
            textViewFresh.setTextColor(Color.parseColor("#14FFEC"));
            textViewAll.setTextColor(Color.parseColor("#FFFFFFFF"));
        } else if (type.equals("feed_following")) {
            textViewFollowing.setTextColor(Color.parseColor("#14FFEC"));
            textViewFresh.setTextColor(Color.parseColor("#FFFFFFFF"));
            textViewAll.setTextColor(Color.parseColor("#FFFFFFFF"));
        }


        final Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                createFeedList(StarsocketConnector.getMessage().replaceAll("undefined",""));
                buildRecyclerView();
                ((ProgressBar) findViewById(R.id.progressBar)).setVisibility(View.GONE);
            }
        }, 300);
    }

    private ArrayList<FeedItem> mFeedList;
    private RecyclerView mRecyclerView;
    private FeedAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    public static Boolean text_item = false;
    public void createFeedList(String rest){
        String[] user = rest.split("\n");

        mFeedList = new ArrayList<>();
        String separator = "PLAN_DIVIDER";

        for (String element : user) {
            try {
                String name = element.split(separator)[0];
                String desc = element.split(separator)[1];
                String tags = element.split(separator)[2].replaceAll("#","");
                String views = element.split(separator)[3];
                String stars = element.split(separator)[4];
                String id = element.split(separator)[5];
                String date = element.split(separator)[6].split("\\.")[0]+element.split(separator)[6].split("\\.")[2];
                mFeedList.add(new FeedItem(name, desc,tags+" #"+id,date+" "+views+" views "+stars+" stars"));
            } catch (Exception ignored) {

            }
        }
    }

    public void buildRecyclerView(){
        mRecyclerView = findViewById(R.id.feed_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(FeedActivity.this);
        mAdapter = new FeedAdapter(mFeedList);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
    }

    public void showFollowing(View view) {
        vibrate();
        getFeed("feed_following");
    }

    public void showAllTime(View view) {
        vibrate();
        getFeed("all_time");
    }

    public void showFresh(View view) {
        vibrate();
        getFeed("feed_fresh");
    }


}