package com.stardash.sportdash.online.chat;

import static com.stardash.sportdash.online.ProfileActivity.invalidId;
import static com.stardash.sportdash.online.friends.FriendsActivity.friendsSearchRequest;
import static com.stardash.sportdash.settings.app.vibrate;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
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

import com.stardash.sportdash.online.feed.FeedActivity;
import com.stardash.sportdash.online.feed.FeedAdapter;
import com.stardash.sportdash.online.feed.FeedItem;
import com.stardash.sportdash.online.friends.FriendsActivity;
import com.stardash.sportdash.settings.Account;
import com.stardash.sportdash.MainActivity;
import com.stardash.sportdash.R;
import com.stardash.sportdash.network.tcp.StarsocketConnector;

import java.util.ArrayList;
import java.util.Locale;

public class InboxActivity extends AppCompatActivity {
    public String currentType;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      //  overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        setContentView(R.layout.activity_inbox);


        currentType = "all";
        getInbox();

        if (Account.isAmoled()) {
            ConstraintLayout main = findViewById(R.id.main);
            main.setBackgroundDrawable(new ColorDrawable(Color.BLACK));
            Window window = getWindow();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                window.setStatusBarColor(Color.BLACK);
            }
        }
    }

    @Override
    public void onResume(){
        super.onResume();
       getInbox();

    }

    public void showFollows(View view) {
        currentType = "follows";
        getInbox();
        vibrate();
    }

    public void showStars(View view) {
        currentType = "stars";
        getInbox();
        vibrate();
    }

    public void showComments(View view) {
        currentType = "comments";
        getInbox();
        vibrate();
    }

    public void showChat(View view) {
        currentType = "chats";
        getInbox();
        vibrate();
    }

    public void showAll(View view) {
        currentType = "all";
        getInbox();
        vibrate();
    }

    private void getInbox() {

        TextView textViewAll = findViewById(R.id.textViewAll);
        TextView textViewStars = findViewById(R.id.textViewStars);
        TextView textViewFollows = findViewById(R.id.textViewFollowing);
        TextView textViewComments = findViewById(R.id.textViewComments);
        TextView textViewChats = findViewById(R.id.textViewChat);

        if (currentType.equals("all")) {
            textViewStars.setTextColor(Color.parseColor("#FFFFFFFF"));
            textViewFollows.setTextColor(Color.parseColor("#FFFFFFFF"));
            textViewComments.setTextColor(Color.parseColor("#FFFFFFFF"));
            textViewChats.setTextColor(Color.parseColor("#FFFFFFFF"));
            textViewAll.setTextColor(Color.parseColor("#14FFEC"));
        } else if (currentType.equals("chats")) {
            textViewStars.setTextColor(Color.parseColor("#FFFFFFFF"));
            textViewFollows.setTextColor(Color.parseColor("#FFFFFFFF"));
            textViewComments.setTextColor(Color.parseColor("#FFFFFFFF"));
            textViewChats.setTextColor(Color.parseColor("#14FFEC"));
            textViewAll.setTextColor(Color.parseColor("#FFFFFFFF"));
        } else if (currentType.equals("stars")) {
            textViewStars.setTextColor(Color.parseColor("#14FFEC"));
            textViewFollows.setTextColor(Color.parseColor("#FFFFFFFF"));
            textViewComments.setTextColor(Color.parseColor("#FFFFFFFF"));
            textViewChats.setTextColor(Color.parseColor("#FFFFFFFF"));
            textViewAll.setTextColor(Color.parseColor("#FFFFFFFF"));
        } else if (currentType.equals("comments")) {
            textViewStars.setTextColor(Color.parseColor("#FFFFFFFF"));
            textViewFollows.setTextColor(Color.parseColor("#FFFFFFFF"));
            textViewComments.setTextColor(Color.parseColor("#14FFEC"));
            textViewChats.setTextColor(Color.parseColor("#FFFFFFFF"));
            textViewAll.setTextColor(Color.parseColor("#FFFFFFFF"));
        } else if (currentType.equals("follows")) {
            textViewStars.setTextColor(Color.parseColor("#FFFFFFFF"));
            textViewFollows.setTextColor(Color.parseColor("#14FFEC"));
            textViewComments.setTextColor(Color.parseColor("#FFFFFFFF"));
            textViewChats.setTextColor(Color.parseColor("#FFFFFFFF"));
            textViewAll.setTextColor(Color.parseColor("#FFFFFFFF"));
        }


        try {
            StarsocketConnector.sendMessage("notifications " + currentType);

            final Handler handler = new Handler(Looper.getMainLooper());
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    try {
                        String inbox = StarsocketConnector.getMessage();
                        createFeedList(inbox.replaceAll("undefined",""));
                    } catch (Exception e){
                        toast("no network");
                    }
                    checkNotifications();
                }
            }, 500);

        } catch (Exception e){
            toast("no network");
        }
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
                    amount = 0;
                }
            }
        }, 400);
    }


    static public void notificationAmount() {
        amount = 0;
        try {
            StarsocketConnector.sendMessage("notifications all");
            final Handler handler = new Handler(Looper.getMainLooper());
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    try {
                        amount = 0;
                        String inbox = StarsocketConnector.getMessage();
                        String rest = inbox.replaceAll("undefined","");
                        String[] user = rest.split("\n");
                        String separator = "NOTIF_DIVIDER";
                        for (String element : user) {
                            try {
                             //   String type = element.split(separator)[0].toUpperCase(Locale.ROOT).replaceAll(" ","");
                                String viewed = element.split(separator)[5];
                                if (viewed.equals("0")) {
                                    amount++;
                                }
                            } catch (Exception ignored) {
                            }
                        }
                    } catch (Exception ignored){
                    }
                }
            }, 300);

        } catch (Exception ignored){
        }
    }


    public static int amount = 0;
    static public int notifications(){
        return amount;
    }

    private ArrayList<InboxItem> mInboxList;
    private RecyclerView mRecyclerView;
    private InboxAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    public void createFeedList(String rest){
        String[] user = rest.split("\n");

        String separator = "NOTIF_DIVIDER";


        mInboxList = new ArrayList<>();
        for (String element : user) {
            try {

                String type = element.split(separator)[0].toUpperCase(Locale.ROOT).replaceAll(" ","");
                String from_id = element.split(separator)[1];
                String from_name = element.split(separator)[2];
                String plan_id = element.split(separator)[3];
                String notification_text = element.split(separator)[4];
                String viewed = element.split(separator)[5];
                String finalTime = element.split(separator)[6];
                String notif_id = element.split(separator)[7];



                if(type.equals("STAR")) {
                    mInboxList.add(new InboxItem("STAR", from_name+" has starred your plan!",from_id+" "+notif_id+" "+viewed+" "+plan_id,finalTime));
                } else if (type.equals("FOLLOW")) {
                    mInboxList.add(new InboxItem("FOLLOW", from_name+" has followed you!",from_id+" "+notif_id+" "+viewed,finalTime));
                } else if (type.equals("COMMENT")){
                    mInboxList.add(new InboxItem("COMMENT", from_name+" has commented on your plan!",from_id+" "+notif_id+" "+viewed+" "+plan_id,finalTime));
                } else if (type.equals("CHAT")){

                } else {

                }


            } catch (Exception ignored) {

            }
        }
        buildRecyclerView();
    }

    public void buildRecyclerView(){
        mRecyclerView = findViewById(R.id.inbox_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(InboxActivity.this);
        mAdapter = new InboxAdapter(mInboxList);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
    }


    @Override
    public void onBackPressed() {
        Intent i = new Intent(this, MainActivity.class);
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