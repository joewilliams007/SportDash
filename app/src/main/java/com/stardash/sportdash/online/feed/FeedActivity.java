package com.stardash.sportdash.online.feed;

import static com.stardash.sportdash.MainActivity.loggedIn;
import static com.stardash.sportdash.online.ProfileActivity.invalidId;
import static com.stardash.sportdash.online.chat.InboxActivity.amount;
import static com.stardash.sportdash.online.chat.InboxActivity.notificationAmount;
import static com.stardash.sportdash.online.chat.InboxActivity.notifications;
import static com.stardash.sportdash.online.friends.FriendsActivity.friendsSearchRequest;
import static com.stardash.sportdash.settings.account.AppLockSettingsActivity.changeLock;
import static com.stardash.sportdash.settings.app.vibrate;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.stardash.sportdash.MainActivity;
import com.stardash.sportdash.R;
import com.stardash.sportdash.me.leaderboard.LeaderboardAdapter;
import com.stardash.sportdash.me.leaderboard.LeaderboardItem;
import com.stardash.sportdash.me.leaderboard.leaderboard;
import com.stardash.sportdash.network.tcp.StarsocketConnector;
import com.stardash.sportdash.online.chat.InboxActivity;
import com.stardash.sportdash.online.friends.FriendsActivity;
import com.stardash.sportdash.online.upload.UploadActivity;
import com.stardash.sportdash.plans.create.structure.CreateStructureNewActivity;
import com.stardash.sportdash.settings.Account;
import com.stardash.sportdash.settings.SettingsActivity;
import com.stardash.sportdash.signIn.LockActivity;
import com.stardash.sportdash.signIn.RegisterActivity;

import java.util.ArrayList;

public class FeedActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);

        changeLock = "app";
        if (Account.isAppLock() && !loggedIn && !Account.password().equals("none")) {
            Intent i = new Intent(this, LockActivity.class);
            startActivity(i);
        } else {
            if (!Account.loggedIn()) {
                Intent i = new Intent(this, RegisterActivity.class);
                startActivity(i);
            }
        }

        checkNotifications();
        getFeed("all_time");
        ((ProgressBar) findViewById(R.id.progressBar)).setVisibility(View.VISIBLE);
    }

    @SuppressLint("SetTextI18n")
    public void checkNotifications() {
        TextView textViewNotification = findViewById(R.id.textViewNotification);
        notificationAmount();
        final Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                int notif = notifications();
                if (notif>0) {
                    textViewNotification.setVisibility(View.VISIBLE);
                    textViewNotification.setText(" "+ notif +" ");
                } else  {
                    textViewNotification.setVisibility(View.INVISIBLE);
                }
            }
        }, 400);
    }

    private void getFeed(String type) {
        items_count = 0;
        ((ProgressBar) findViewById(R.id.progressBar)).setVisibility(View.VISIBLE);
        StarsocketConnector.sendMessage(type+" "+Account.userid());

        TextView textViewFollowing = findViewById(R.id.textViewFollowing);
        TextView textViewFresh = findViewById(R.id.textViewFresh);
        TextView textViewAll = findViewById(R.id.textViewAll);
        TextView textViewTrending = findViewById(R.id.textViewTrending);

        if (type.equals("all_time")) {
            textViewTrending.setTextColor(Color.parseColor("#FFFFFFFF"));
            textViewFollowing.setTextColor(Color.parseColor("#FFFFFFFF"));
            textViewFresh.setTextColor(Color.parseColor("#FFFFFFFF"));
            textViewAll.setTextColor(Color.parseColor("#14FFEC"));
        } else if (type.equals("feed_fresh")) {
            textViewFollowing.setTextColor(Color.parseColor("#FFFFFFFF"));
            textViewTrending.setTextColor(Color.parseColor("#FFFFFFFF"));
            textViewFresh.setTextColor(Color.parseColor("#14FFEC"));
            textViewAll.setTextColor(Color.parseColor("#FFFFFFFF"));
        } else if (type.equals("feed_following")) {
            textViewFollowing.setTextColor(Color.parseColor("#14FFEC"));
            textViewTrending.setTextColor(Color.parseColor("#FFFFFFFF"));
            textViewFresh.setTextColor(Color.parseColor("#FFFFFFFF"));
            textViewAll.setTextColor(Color.parseColor("#FFFFFFFF"));
        } else if (type.equals("feed_trending")) {
            textViewFollowing.setTextColor(Color.parseColor("#FFFFFFFF"));
            textViewTrending.setTextColor(Color.parseColor("#14FFEC"));
            textViewFresh.setTextColor(Color.parseColor("#FFFFFFFF"));
            textViewAll.setTextColor(Color.parseColor("#FFFFFFFF"));
        }


        final Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                try {
                    items_count = 0;
                    createFeedList(StarsocketConnector.getMessage().replaceAll("undefined",""));
                    buildRecyclerView();
                    ((ProgressBar) findViewById(R.id.progressBar)).setVisibility(View.GONE);
                } catch (Exception e) {
                    toast("no network");
                }
            }
        }, 300);
    }

    private ArrayList<FeedItem> mFeedList;
    private RecyclerView mRecyclerView;
    private FeedAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    public static int items_count;
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
                String username = element.split(separator)[7];
                mFeedList.add(new FeedItem(username,name, desc,tags+" #"+id,date+" "+views+" views "+stars+" stars"));

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

    public void showTrending(View view) {
        vibrate();
        getFeed("feed_trending");
    }

    public void toast(String message){
        TextView textViewCustomToast = findViewById(R.id.textViewCustomToast);
        textViewCustomToast.setVisibility(View.VISIBLE);
        textViewCustomToast.setText(message);
        final Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                textViewCustomToast.setVisibility(View.GONE);
            }
        }, 3000);
    }


    public void openUpload(View view) {
        vibrate();
        Intent i = new Intent(this, UploadActivity.class);
        startActivity(i);
    }

    boolean animatingUpload = false;
    public void showUpload(View view) {
        vibrate();
        ConstraintLayout constraintLayout = findViewById(R.id.upload);
        if(constraintLayout.getVisibility() == View.GONE && !animatingUpload) {
            animatingUpload = true;
            constraintLayout.setVisibility(View.VISIBLE);
            constraintLayout.animate().translationYBy(-20f).setDuration(1000);
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    animatingUpload = false;
                }
            }, 1100);
        } else if(!animatingUpload) {
            constraintLayout.animate().translationYBy(20f).setDuration(0);
            constraintLayout.setVisibility(View.GONE);
            animatingUpload = false;
        }
    }

    public void newPlan(View view) {
        vibrate();
        Intent i = new Intent(this, CreateStructureNewActivity.class);
        startActivity(i);
    }

    // NAVBAR
    public void openFriendsList(View view) {
        vibrate();
        invalidId = false;
        friendsSearchRequest = false;
        Intent i = new Intent(this, FriendsActivity.class);
        startActivity(i);
    }
    public void openFriendsSearch(View view) {
        invalidId = false;
        friendsSearchRequest = true;
        Intent i = new Intent(this, FriendsActivity.class);
        startActivity(i);
    }

    public void openInbox(View view) {
        Intent i = new Intent(this, InboxActivity.class);
        startActivity(i);
        vibrate();
    }
    public void openFeed(View view) {
        vibrate();
        try {
            StarsocketConnector.sendMessage("all_time "+ Account.userid());
            Intent i = new Intent(this, FeedActivity.class);
            startActivity(i);
        } catch (Exception e){
            toast("no network");
        }
    }
    public void openHome(View view) {
        vibrate();
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }
    // END OF NAVBAR
}