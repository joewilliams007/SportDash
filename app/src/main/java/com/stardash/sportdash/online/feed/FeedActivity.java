package com.stardash.sportdash.online.feed;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.Handler;

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
        getFeed("all");
    }

    private void getFeed(String type) {
        StarsocketConnector.sendMessage("feed" +type+" "+ Account.userid());

        createFeedList("");
        buildRecyclerView();
    }

    private ArrayList<FeedItem> mFeedList;
    private RecyclerView mRecyclerView;
    private FeedAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    public void createFeedList(String rest){
        String[] user = rest.split("\n");

        mFeedList = new ArrayList<>();

        for (String element : user) {
            try {
                mFeedList.add(new FeedItem(element.split("@")[0]+" place", element.split("@")[2]+element.split("@")[3], element.split("@")[1]));
            } catch (Exception e) {

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
}