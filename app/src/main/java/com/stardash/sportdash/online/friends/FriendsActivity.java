package com.stardash.sportdash.online.friends;


import static com.stardash.sportdash.online.chat.ChatActivity.isInChat;
import static com.stardash.sportdash.plans.run.PlanActivity.isMyPlan;
import static com.stardash.sportdash.online.ProfileActivity.invalidId;
import static com.stardash.sportdash.plans.run.RunPlanActivity.isRandom;
import static com.stardash.sportdash.settings.app.vibrate;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.text.Layout;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.stardash.sportdash.me.leaderboard.LeaderboardAdapter;
import com.stardash.sportdash.me.leaderboard.LeaderboardItem;
import com.stardash.sportdash.me.leaderboard.leaderboard;
import com.stardash.sportdash.online.ProfileActivity;
import com.stardash.sportdash.online.friends.follows.FollowActivity;
import com.stardash.sportdash.plans.run.RunPlanActivity;
import com.stardash.sportdash.settings.Account;
import com.stardash.sportdash.online.chat.ChatActivity;
import com.stardash.sportdash.MainActivity;
import com.stardash.sportdash.R;
import com.stardash.sportdash.network.tcp.StarsocketConnector;
import com.stardash.sportdash.settings.MyApplication;

import java.util.ArrayList;
import java.util.Locale;

public class FriendsActivity extends AppCompatActivity {
    public static Boolean tappedOnSearchItem;
    public static String tappedOnSearchItemId;
    public Boolean isFollowProcess = false;
    public String theId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        setContentView(R.layout.activity_friends);
        ((ProgressBar) findViewById(R.id.progressBar)).setVisibility(View.VISIBLE);
        isMyPlan = false; // so that the discard btn in active plan is gone
        isRandom = false;
        settingShowHideSearch = false;



        TextView textViewUrId = findViewById(R.id.textViewMe);
        textViewUrId.setText("#"+ Account.userid());

        Bundle extras = getIntent().getExtras(); // get link hashtag if opened from link
        if (extras != null) {
            String value = extras.getString("friendHashtag").toString();
            EditText editTextHashtag = findViewById(R.id.editTextTextPersonNameHashtag);
            String id = value.split("#",3)[1];
            editTextHashtag.setText(id);
            vibrate();
            EditText editText = findViewById(R.id.editTextTextPersonNameHashtag);
            if (editText.getText().toString().length()<1){
                toast("enter id");
            } else {
                try {
                    StarsocketConnector.sendMessage("getProfile " + editText.getText().toString());
                    Intent i = new Intent(this, FriendsActivity.class);
                    startActivity(i);
                } catch (Exception e) {
                    toast("no network");
                }
            }
            //The key argument here must match that used in the other activity
        }
        if (Account.isAmoled()) {
            ConstraintLayout main = findViewById(R.id.main);
            main.setBackgroundDrawable(new ColorDrawable(Color.BLACK));
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.BLACK);

            TextView textViewName = findViewById(R.id.textViewUsername);
            TextView textViewAbout = findViewById(R.id.textViewAbout);
            TextView textViewPlans = findViewById(R.id.textViewPlans);
            TextView textViewDate = findViewById(R.id.textViewDate);
            textViewDate.setTextColor(ContextCompat.getColor(this, R.color.darkMode));
            textViewAbout.setTextColor(ContextCompat.getColor(this, R.color.darkMode));
            textViewPlans.setTextColor(ContextCompat.getColor(this, R.color.darkMode));
            textViewName.setTextColor(ContextCompat.getColor(this, R.color.darkMode));
        }


        if (tappedOnSearchItem){
            theId = tappedOnSearchItemId;
            tappedOnSearchItem = false;
        } else {
            theId = Account.userid();
        }

        try {
            StarsocketConnector.sendMessage("getProfile " + theId);
            try {
                final Handler handler = new Handler(Looper.getMainLooper());
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getUserProfile();
                    }
                }, 100);
            } catch (Exception e) {
                toast("no network");
            }
        } catch (Exception e) {
            toast("no network");
        }

    }



    private void getUserProfile() {

            String profile = StarsocketConnector.getMessage();
            String id = profile.split("ROFILE_OF_USER", 15)[0];
            String username = profile.split("ROFILE_OF_USER", 15)[1];
            String xp = profile.split("ROFILE_OF_USER", 15)[2];
            String xpToday = profile.split("ROFILE_OF_USER", 15)[3];
            String xpWeek = profile.split("ROFILE_OF_USER", 15)[4];
            String age = profile.split("ROFILE_OF_USER", 15)[5];
            String weight = profile.split("ROFILE_OF_USER", 15)[6];
            String style = profile.split("ROFILE_OF_USER", 15)[7];
            String accountCreated = profile.split("ROFILE_OF_USER", 15)[8];
            String follows = profile.split("ROFILE_OF_USER", 15)[9];
            String followers = profile.split("ROFILE_OF_USER", 15)[10];
            String bio = profile.split("ROFILE_OF_USER", 15)[11];

            TextView textViewDate = findViewById(R.id.textViewDate);
            textViewDate.setText("StarKing/Queen since "+accountCreated);
            Account.setFriend(0, "#" + id + " " + username);
            TextView textViewUsername = findViewById(R.id.textViewMeUsername);
            textViewUsername.animate().translationXBy(-10f).setDuration(1000);
            TextView textViewUsername1 = findViewById(R.id.textViewUsername);
            TextView textViewUserID = findViewById(R.id.textViewMe);
            TextView textViewLevel = findViewById(R.id.textViewLevel);
            TextView textViewStyle = findViewById(R.id.textViewStyle);
            textViewStyle.animate().translationYBy(40f).setDuration(1000);
            textViewStyle.setText(style);
            TextView textViewXp = findViewById(R.id.textViewProgress);
            TextView textViewXpToday = findViewById(R.id.textViewProgressTd);
            TextView textViewXpWeek = findViewById(R.id.textViewProgressWk);
            TextView textViewFollows = findViewById(R.id.textViewFollowingNr);
            TextView textViewFollowers = findViewById(R.id.textViewFollowersNr);
            TextView textViewBio = findViewById(R.id.textViewBio);

            if(bio.equals("null")) {
                textViewBio.setVisibility(View.GONE);
            } else {
                textViewBio.setText(bio);
            }

            textViewFollowers.setText(follows);
            textViewFollows.setText(followers);

            int level = 100;
            try {
                int number = Integer.parseInt(xp);
                level = number / 10000 + 1;
            } catch (NumberFormatException ex) {
                ex.printStackTrace();
            }
            String usernameLines = username.replace("","\n").toUpperCase(Locale.ROOT);
            textViewUsername.setText(usernameLines);
            textViewUsername1.setText(username);
            textViewUserID.setText("#" + id);
            textViewLevel.setText(String.valueOf(level));
            textViewXp.setText(xp + " xp");
            ProgressBar progressBarToday = findViewById(R.id.progressBarToday);
            ProgressBar progressBarWeek = findViewById(R.id.progressBarWeek);
            try {
                ObjectAnimator.ofInt(progressBarToday, "progress", Integer.valueOf(xpToday))
                        .setDuration(600)
                        .start();
                ObjectAnimator.ofInt(progressBarWeek, "progress", Integer.valueOf(xpWeek))
                        .setDuration(600)
                        .start();
            } catch (Exception e){

            }

            TextView textViewFollow = findViewById(R.id.textViewFollow);
             TextView textViewMessage = findViewById(R.id.textViewMessage);
            String idZ = textViewUserID.getText().toString().replace("#","");
            if(idZ.equals(Account.userid())){
                textViewFollow.setVisibility(View.GONE);
                textViewMessage.setVisibility(View.GONE);
            }

            textViewXpToday.setText(xpToday + "/10.000 xp");
            textViewXpWeek.setText(xpWeek + "/70.000 xp");
            checkFollow(id);
    }

    private void checkFollow(String id) {
        try {
            TextView textViewId = findViewById(R.id.textViewMe);
            TextView textViewFollow = findViewById(R.id.textViewFollow);
            try {

                StarsocketConnector.sendMessage("checkFollow " + Account.userid() +" "+ textViewId.getText().toString().split("#")[1]);
                textViewFollow.setText("loading");

                final Handler handler = new Handler(Looper.getMainLooper());
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        String isFollow = StarsocketConnector.getMessage();
                        if (isFollow.length()>10) {
                            textViewFollow.setText("follow");

                        } else {
                            textViewFollow.setText("unfollow");
                        }
                        if(isFollowProcess){

                        } else {
                            loadPlans(id);
                        }
                    }
                }, 200);

            } catch (Exception e){
            }
        } catch (Exception e){
        }

    }


    public void onResume() {
        super.onResume();
        isInChat = false;
    }
   /* @Override
    public void onBackPressed() {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }*/
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

    public void searchUsers(View view) {
        ((ProgressBar) findViewById(R.id.progressBar)).setVisibility(View.VISIBLE);
        vibrate();
        EditText editTextSearch = findViewById(R.id.editTextSearch);
        getFriends(editTextSearch.getText().toString().replaceAll(" ",""));
    }

    private void getFriends(String search) {
        try {
            StarsocketConnector.sendMessage("searchFriends ="+search);
            final Handler handler = new Handler(Looper.getMainLooper());
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    String received = StarsocketConnector.getMessage().replaceAll("undefined","");

                    try {
                        String rest = received;
                        createFriendsList(rest);
                        buildRecyclerView();

                    } catch (Exception e){
                        toast("no accounts found");
                        ((ProgressBar) findViewById(R.id.progressBar)).setVisibility(View.GONE);

                    }
                }
            }, 200);
        } catch (Exception e){
            toast("no network");
            ((ProgressBar) findViewById(R.id.progressBar)).setVisibility(View.GONE);

        }
    }

    private ArrayList<FriendsItem> mFriendsList;
    private RecyclerView mRecyclerView;
    private FriendsAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    public void createFriendsList(String rest){
        String[] user = rest.split("\n");

        mFriendsList = new ArrayList<>();

        for (String element : user) {
            try {
                mFriendsList.add(new FriendsItem(element.split("@")[0], element.split("@")[1], element.split("@")[2]));
            } catch (Exception e) {

            }
        }
    }

    public void buildRecyclerView(){
        mRecyclerView = findViewById(R.id.search_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(FriendsActivity.this);
        mAdapter = new FriendsAdapter(mFriendsList);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
        ((ProgressBar) findViewById(R.id.progressBar)).setVisibility(View.GONE);

    }

    Boolean settingShowHideSearch;
    public void showSearch(View view) {
        ConstraintLayout searchLayout = findViewById(R.id.searchLayout);
        if (searchLayout.getVisibility() != View.VISIBLE) {
            if (!settingShowHideSearch) {
                settingShowHideSearch = true;
                vibrate();

                searchLayout.setVisibility(View.VISIBLE);
                searchLayout.animate().translationYBy(20f).setDuration(1000);
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        settingShowHideSearch = false;
                    }
                }, 1000);
            }
        }
    }

    public void hideSearch(View view) {
        if (!settingShowHideSearch) {
            vibrate();
            settingShowHideSearch = true;
            ConstraintLayout searchLayout = findViewById(R.id.searchLayout);
            searchLayout.animate().translationYBy(4000f).setDuration(1000);
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    searchLayout.setVisibility(View.GONE);
                    searchLayout.animate().translationYBy(-4000f).setDuration(1);
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            settingShowHideSearch = false;
                        }
                    }, 100);
                }
            }, 1000);
        }
    }
    public static String chatUsername;
    public static String chatId;
    public void openChat(View view) {

        vibrate();
        TextView textViewUsername = findViewById(R.id.textViewUsername);
        TextView textViewUserID = findViewById(R.id.textViewMe);
        chatUsername = textViewUsername.getText().toString();
        chatId = textViewUserID.getText().toString().replace("#","");
        Intent i = new Intent(this, ChatActivity.class);
        startActivity(i);
    }

    private void loadPlans(String id) {
        StarsocketConnector.sendMessage("downloadPlans " + id);
        try {
            final Handler handler = new Handler(Looper.getMainLooper());
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    String received_plans = StarsocketConnector.getMessage().toString();

                    SharedPreferences settings = getSharedPreferences("sport", MODE_PRIVATE);
                    SharedPreferences.Editor editor = settings.edit();
                    try {
                        String plan1 = received_plans.split("##########", 9)[1];
                        String plan2 = received_plans.split("##########", 9)[2];
                        String plan3 = received_plans.split("##########", 9)[3];
                        String plan4 = received_plans.split("##########", 9)[4];
                        String plan5 = received_plans.split("##########", 9)[5];

                        editor.putString("1 planFriend", String.valueOf(plan1)).apply();
                        editor.putString("2 planFriend", String.valueOf(plan2)).apply();
                        editor.putString("3 planFriend", String.valueOf(plan3)).apply();
                        editor.putString("4 planFriend", String.valueOf(plan4)).apply();
                        editor.putString("5 planFriend", String.valueOf(plan5)).apply();

                        setPlanNames();
                    } catch (Exception e){
                        toast("error loading plans");
                    }
                }
            }, 500);
        } catch (Exception e){
            toast("no network");
        }

    }

    private void setPlanNames() {
        TextView textViewNamePlan1 = findViewById(R.id.textViewPlan1);
        TextView textViewNamePlan2 = findViewById(R.id.textViewPlan2);
        TextView textViewNamePlan3 = findViewById(R.id.textViewPlan3);
        TextView textViewNamePlan4 = findViewById(R.id.textViewPlan4);
        TextView textViewNamePlan5 = findViewById(R.id.textViewPlan5);
        String empty = "E M P T Y";
        try {
            textViewNamePlan1.setText(Account.planFriend(1).split("\n", 5)[2]);
        } catch (Exception e){
            textViewNamePlan1.setText(empty);
        }
        try {
            textViewNamePlan2.setText(Account.planFriend(2).split("\n",5)[2]);
        } catch (Exception e){
            textViewNamePlan2.setText(empty);
        }
        try {
            textViewNamePlan3.setText(Account.planFriend(3).split("\n",5)[2]);
        } catch (Exception e){
            textViewNamePlan3.setText(empty);
        }
        try {
            textViewNamePlan4.setText(Account.planFriend(4).split("\n",5)[2]);
        } catch (Exception e){
            textViewNamePlan4.setText(empty);
        }
        try {
            textViewNamePlan5.setText(Account.planFriend(5).split("\n",5)[2]);
        } catch (Exception e){
            textViewNamePlan5.setText(empty);
        }
        ((ProgressBar) findViewById(R.id.progressBar)).setVisibility(View.INVISIBLE);
    }
    public void plan1(View view) {
        vibrate();
        TextView textViewNamePlan = findViewById(R.id.textViewPlan1);
        if (textViewNamePlan.getText().toString().equals("E M P T Y")) {
            toast("empty plan");
        } else {
            openPlan(1);
        }
    }
    public void plan2(View view) {
        vibrate();
        TextView textViewNamePlan = findViewById(R.id.textViewPlan2);
        if (textViewNamePlan.getText().toString().equals("E M P T Y")) {
            toast("empty plan");
        } else {
            openPlan(2);
        }
    }
    public void plan3(View view) {
        vibrate();
        TextView textViewNamePlan = findViewById(R.id.textViewPlan3);
        if (textViewNamePlan.getText().toString().equals("E M P T Y")) {
            toast("empty plan");
        } else {
            openPlan(3);
        }
    }
    public void plan4(View view) {
        vibrate();
        TextView textViewNamePlan = findViewById(R.id.textViewPlan4);
        if (textViewNamePlan.getText().toString().equals("E M P T Y")) {
            toast("empty plan");
        } else {
            openPlan(4);
        }
    }
    public void plan5(View view) {
        vibrate();
        TextView textViewNamePlan = findViewById(R.id.textViewPlan5);
        if (textViewNamePlan.getText().toString().equals("E M P T Y")) {
            toast("empty plan");
        } else {
            openPlan(5);
        }
    }

    private void openPlan(int id) {
        Account.setIsMine(false);
        isMyPlan= false;
        SharedPreferences settings = getSharedPreferences("sport", MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();

        Account.setActiveIterations(0); // set iterations to none done
        editor.putInt("selectedPlan", id).commit(); // for running plan

        Intent i = new Intent(this, RunPlanActivity.class);
        startActivity(i);

    }


    public void doNth(View view) {
    }

    private Boolean rotating = false;
    public void rotate(View view) {
        if(!isFollowProcess) {
            if (!rotating) {
                rotating = true;
                TextView textView = (TextView) findViewById(R.id.textViewStyle);

                int d = 5;

                RotateAnimation rotate = new RotateAnimation(0, d, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                rotate.setDuration(250);
                rotate.setInterpolator(new LinearInterpolator());
                textView.startAnimation(rotate);
                textView.animate().translationYBy(-10f).setDuration(1000);

                final Handler handler = new Handler(Looper.getMainLooper());
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        RotateAnimation rotate = new RotateAnimation(d, -d, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                        rotate.setDuration(250);
                        rotate.setInterpolator(new LinearInterpolator());
                        textView.startAnimation(rotate);
                    }
                }, 250);
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        RotateAnimation rotate = new RotateAnimation(-d, 0, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                        rotate.setDuration(250);
                        rotate.setInterpolator(new LinearInterpolator());
                        textView.startAnimation(rotate);
                        textView.animate().translationYBy(10f).setDuration(500);
                        rotating = false;
                    }
                }, 500);
            }
        }
    }

    public void follow(View view) {
        isFollowProcess = true;
        vibrate();
        try {
            TextView textViewId = findViewById(R.id.textViewMe);
            TextView textViewFollow = findViewById(R.id.textViewFollow);
            TextView textViewUsername = findViewById(R.id.textViewUsername);
            try {
                ((ProgressBar) findViewById(R.id.progressBar)).setVisibility(View.VISIBLE);
                StarsocketConnector.sendMessage("follow " + Account.userid() +" "+ textViewId.getText().toString().split("#")[1]+" "+Account.username()+" "+ textViewUsername.getText().toString());

                final Handler handler = new Handler(Looper.getMainLooper());
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        textViewFollow.setText("following");
                        ((ProgressBar) findViewById(R.id.progressBar)).setVisibility(View.INVISIBLE);
                    }
                }, 100);

            } catch (Exception e){
                toast("no network");
                ((ProgressBar) findViewById(R.id.progressBar)).setVisibility(View.INVISIBLE);
            }
        } catch (Exception e){
            toast("error");
            ((ProgressBar) findViewById(R.id.progressBar)).setVisibility(View.INVISIBLE);
        }
        try {
            StarsocketConnector.sendMessage("getProfile " + theId);
            try {
                final Handler handler = new Handler(Looper.getMainLooper());
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getUserProfile();
                    }
                }, 100);
            } catch (Exception e) {
                toast("no network");
            }
        } catch (Exception e) {
            toast("no network");
        }
    }

    public void openFollowers(View view) {
        followPage(false);
    }

    public void openFollowing(View view) {
        followPage(true);
    }

    public void followPage(Boolean b){
        TextView textViewUserID = findViewById(R.id.textViewMe);
        String id = textViewUserID.getText().toString();


        vibrate();
        if(b){
            StarsocketConnector.sendMessage("follows " + id.replace("#",""));
        } else {
            StarsocketConnector.sendMessage("followers " + id.replace("#",""));
        }
        Intent i = new Intent(this, FollowActivity.class);
        startActivity(i);
    }
}