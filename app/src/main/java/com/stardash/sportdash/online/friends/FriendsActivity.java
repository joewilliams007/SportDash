package com.stardash.sportdash.online.friends;


import static com.stardash.sportdash.online.chat.ChatActivity.isInChat;
import static com.stardash.sportdash.online.chat.InboxActivity.amount;
import static com.stardash.sportdash.online.chat.InboxActivity.notificationAmount;
import static com.stardash.sportdash.online.chat.InboxActivity.notifications;
import static com.stardash.sportdash.online.friends.follows.FollowActivity.pageStatus;
import static com.stardash.sportdash.plans.run.PlanActivity.isMyPlan;
import static com.stardash.sportdash.online.ProfileActivity.invalidId;
import static com.stardash.sportdash.plans.run.RunPlanActivity.isRandom;
import static com.stardash.sportdash.settings.account.AppLockSettingsActivity.changeLock;
import static com.stardash.sportdash.settings.app.vibrate;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
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
import android.widget.Toast;

import com.stardash.sportdash.me.leaderboard.LeaderboardAdapter;
import com.stardash.sportdash.me.leaderboard.LeaderboardItem;
import com.stardash.sportdash.me.leaderboard.leaderboard;
import com.stardash.sportdash.online.ProfileActivity;
import com.stardash.sportdash.online.chat.InboxActivity;
import com.stardash.sportdash.online.feed.FeedActivity;
import com.stardash.sportdash.online.friends.follows.FollowActivity;
import com.stardash.sportdash.plans.run.RunPlanActivity;
import com.stardash.sportdash.settings.Account;
import com.stardash.sportdash.online.chat.ChatActivity;
import com.stardash.sportdash.MainActivity;
import com.stardash.sportdash.R;
import com.stardash.sportdash.network.tcp.StarsocketConnector;
import com.stardash.sportdash.settings.MyApplication;
import com.stardash.sportdash.settings.SettingsActivity;
import com.stardash.sportdash.settings.account.DataSaverActivity;
import com.stardash.sportdash.settings.account.GeneralActivity;
import com.stardash.sportdash.signIn.LockActivity;

import java.util.ArrayList;
import java.util.Locale;

public class FriendsActivity extends AppCompatActivity {
    public static Boolean tappedOnSearchItem;
    public static String tappedOnSearchItemId;
    public static Boolean friendsSearchRequest = false;
    public Boolean isFollowProcess = false;
    public String theId;
    public Boolean skipDataSaver;
    public static Boolean openedChat;
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        setContentView(R.layout.activity_friends);
        ((ProgressBar) findViewById(R.id.progressBar)).setVisibility(View.VISIBLE);
        isMyPlan = false; // so that the discard btn in active plan is gone
        isRandom = false;
        settingShowHideSearch = false;
        skipDataSaver = false;
        openedChat = false;

        TextView textViewUrId = findViewById(R.id.textViewMe);
        textViewUrId.setText("#"+ Account.userid());

        Bundle extras = getIntent().getExtras(); // get link hashtag if opened from link
        if (extras != null) {
            String value = extras.getString("friendHashtag").toString();
            EditText editText = findViewById(R.id.editTextTextPersonNameHashtag);
            String id = value.split("#",3)[1];
            editText.setText(id);
            vibrate();
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
        checkNotifications();

        if (friendsSearchRequest) {
            friendsSearchRequest = false;
            showSearchFinal();
        }

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

            TextView textViewName = findViewById(R.id.textViewUsername);
            TextView textViewAbout = findViewById(R.id.textViewAbout);
            TextView textViewPlans = findViewById(R.id.textViewPlans);
            TextView textViewDate = findViewById(R.id.textViewDate);
            textViewDate.setTextColor(ContextCompat.getColor(this, R.color.darkMode));
            textViewAbout.setTextColor(ContextCompat.getColor(this, R.color.darkMode));
            textViewPlans.setTextColor(ContextCompat.getColor(this, R.color.darkMode));
            textViewName.setTextColor(ContextCompat.getColor(this, R.color.darkMode));
        }

        if (!Account.isDataSaver()) {
            TextView textViewDaterSaver = findViewById(R.id.textViewDownloadDataSaver);
            TextView textViewDownload = findViewById(R.id.textViewDownload);
            textViewDaterSaver.setVisibility(View.GONE);
            textViewDownload.setVisibility(View.GONE);
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

    @SuppressLint("SetTextI18n")
    public void checkNotifications() {
        TextView textViewNotification = findViewById(R.id.textViewNotification);
        TextView textViewNotification1 = findViewById(R.id.textViewNotification1);
                if (amount>0) {
                    textViewNotification.setVisibility(View.VISIBLE);
                    textViewNotification.setText(" "+ amount +" ");
                    textViewNotification1.setVisibility(View.VISIBLE);
                    textViewNotification1.setText(" "+ amount +" ");
                } else  {
                    textViewNotification.setVisibility(View.INVISIBLE);
                    textViewNotification1.setVisibility(View.INVISIBLE);
                }

    }

    @SuppressLint("SetTextI18n")
    private void getUserProfile() {
        try {
            String separator = "PROFILE_OF_USER";
            String profile = StarsocketConnector.getMessage();
            String id = profile.split(separator, 15)[0];
            String username = profile.split(separator, 15)[1];
            String xp = profile.split(separator, 15)[2];
            String xpToday = profile.split(separator, 15)[3];
            String xpWeek = profile.split(separator, 15)[4];
            String age = profile.split(separator, 15)[5];
            String weight = profile.split(separator, 15)[6];
            String style = profile.split(separator, 15)[7];
            String accountCreated = profile.split(separator, 15)[8];
            String follows = profile.split(separator, 15)[9];
            String followers = profile.split(separator, 15)[10];
            String bio = profile.split(separator, 15)[11];
            String stars = profile.split(separator, 15)[12];

            TextView textViewDate = findViewById(R.id.textViewDate);
            textViewDate.setText("StarKing/Queen since " + accountCreated);
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
            TextView textViewStarsNr = findViewById(R.id.textViewStarsNr);
            TextView textViewBio = findViewById(R.id.textViewBio);
            TextView textViewBioUnder = findViewById(R.id.textViewBioUnder);

            if (bio.equals("null")) {
                textViewBio.setVisibility(View.GONE);
                textViewBioUnder.setVisibility(View.GONE);
            } else if (bio.length() < 1) {
                textViewBio.setVisibility(View.GONE);
                textViewBioUnder.setVisibility(View.GONE);
            } else {
                textViewBio.setText(bio);
            }

            textViewFollowers.setText(follows);
            textViewFollows.setText(followers);
            textViewStarsNr.setText(stars);

            int level = 100;
            try {
                int number = Integer.parseInt(xp);
                level = number / 10000 + 1;
            } catch (NumberFormatException ex) {
                ex.printStackTrace();
            }
            String usernameLines = username.replace("", "\n").toUpperCase(Locale.ROOT);
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
            } catch (Exception ignored) {

            }

            TextView textViewFollow = findViewById(R.id.textViewFollow);
            TextView textViewMessage = findViewById(R.id.textViewMessage);
            TextView textViewEdit = findViewById(R.id.textViewEdit);
            ImageView imageViewMessage = findViewById(R.id.imageViewMessage);
            ImageView imageViewEdit = findViewById(R.id.imageViewEdit);
            ImageView imageViewFollow = findViewById(R.id.imageViewFollow);
            String idZ = textViewUserID.getText().toString().replace("#", "");
            if (idZ.equals(Account.userid())) {
                textViewFollow.setVisibility(View.GONE);
                textViewMessage.setVisibility(View.GONE);
                textViewEdit.setVisibility(View.VISIBLE);
                imageViewEdit.setVisibility(View.VISIBLE);
                imageViewMessage.setVisibility(View.GONE);
                imageViewFollow.setVisibility(View.GONE);
            } else {
                imageViewEdit.setVisibility(View.GONE);
                imageViewMessage.setVisibility(View.VISIBLE);
                imageViewFollow.setVisibility(View.VISIBLE);
            }

            textViewXpToday.setText(xpToday + "/10.000 xp");
            textViewXpWeek.setText(xpWeek + "/70.000 xp");
            checkFollow(id);
        } catch (Exception e){
            toast("no network");
        }
    }

    @SuppressLint("SetTextI18n")
    private void checkFollow(String id) {
        try {
            TextView textViewId = findViewById(R.id.textViewMe);
            TextView textViewFollow = findViewById(R.id.textViewFollow);
            ImageView imageViewFollow = findViewById(R.id.imageViewFollow);
            try {

                StarsocketConnector.sendMessage("checkFollow " + Account.userid() +" "+ textViewId.getText().toString().split("#")[1]);
                textViewFollow.setText("loading");
                imageViewFollow.setImageResource(R.drawable.follow_loading_removebg);


                final Handler handler = new Handler(Looper.getMainLooper());
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        String isFollow = StarsocketConnector.getMessage();
                        if (isFollow.length()>10) {
                            textViewFollow.setText("follow");
                            imageViewFollow.setImageResource(R.drawable.follow_removebg);
                        } else {
                            textViewFollow.setText("unfollow");
                            imageViewFollow.setImageResource(R.drawable.unfollow_removebg);
                        }
                        if(isFollowProcess){

                        } else {
                            loadPlans(id);
                        }
                    }
                }, 200);

            } catch (Exception ignored){
            }
        } catch (Exception ignored){
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
                        createFriendsList(received);
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
            } catch (Exception ignored) {

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
        showSearchFinal();
    }

    private void showSearchFinal() {
        ConstraintLayout searchLayout = findViewById(R.id.searchLayout);
        ConstraintLayout constraintLayoutNavBarSearch = findViewById(R.id.constraintLayoutNavBarSearch);
        ConstraintLayout constraintLayoutNavBar = findViewById(R.id.constraintLayoutNavBar);
        if (searchLayout.getVisibility() != View.VISIBLE) {
            if (!settingShowHideSearch) {
                settingShowHideSearch = true;
                vibrate();
                constraintLayoutNavBarSearch.setVisibility(View.VISIBLE);
                constraintLayoutNavBar.setVisibility(View.INVISIBLE);
                searchLayout.setVisibility(View.VISIBLE);
                searchLayout.animate().translationYBy(20f).setDuration(1000);
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        settingShowHideSearch = false;
                    }
                }, 1100);
            }
        }
    }

    public void hideSearch(View view) {
        if (!settingShowHideSearch) {
            vibrate();
            settingShowHideSearch = true;
            ConstraintLayout constraintLayoutNavBarSearch = findViewById(R.id.constraintLayoutNavBarSearch);
            ConstraintLayout constraintLayoutNavBar = findViewById(R.id.constraintLayoutNavBar);
            constraintLayoutNavBarSearch.setVisibility(View.GONE);
            ConstraintLayout searchLayout = findViewById(R.id.searchLayout);
            constraintLayoutNavBar.setVisibility(View.VISIBLE);
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
        openedChat = false;

        changeLock = "chat";
        if (Account.isChatLock() && !Account.password().equals("none")) {
            changeLock = "chat";
            Intent i = new Intent(this, LockActivity.class);
            startActivity(i);
        } else {
            Intent i = new Intent(this, ChatActivity.class);
            startActivity(i);
        }
    }

    private void loadPlans(String id) {
        if (Account.isDataSaver()) {
                setPlanNames();
                final ConnectivityManager connMgr = (ConnectivityManager)
                        this.getSystemService(Context.CONNECTIVITY_SERVICE);
                final android.net.NetworkInfo wifi = connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
                final android.net.NetworkInfo mobile = connMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
                if (wifi.isConnectedOrConnecting ()) {
                    if (!Account.isMobileDataSaver()) {
                        loadPlansFinal(id);
                        skipDataSaver = true;
                    }
                } else if (mobile.isConnectedOrConnecting ()) {
                    //
                } else {
                    //
                }
        } else {
            loadPlansFinal(id);
        }
    }



    public void loadPlansFinal(String id) {
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
                            String separator = "##########";
                            String plan1 = received_plans.split(separator, 9)[1];
                            String plan2 = received_plans.split(separator, 9)[2];
                            String plan3 = received_plans.split(separator, 9)[3];
                            String plan4 = received_plans.split(separator, 9)[4];
                            String plan5 = received_plans.split(separator, 9)[5];

                            editor.putString("1 planFriend", String.valueOf(plan1)).apply();
                            editor.putString("2 planFriend", String.valueOf(plan2)).apply();
                            editor.putString("3 planFriend", String.valueOf(plan3)).apply();
                            editor.putString("4 planFriend", String.valueOf(plan4)).apply();
                            editor.putString("5 planFriend", String.valueOf(plan5)).apply();

                            setPlanNames();
                        } catch (Exception e) {
                            toast("error loading plans");
                        }
                    }
                }, 500);
            } catch (Exception e) {
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
        if (Account.isDataSaver() && !skipDataSaver) {
            empty = "Download";
        } else if (Account.isDataSaver()) {

        }
        try {
            textViewNamePlan1.setText(Account.planFriend(1).split("\n", 5)[2]);
            if (Account.isDataSaver() && !skipDataSaver) {
                textViewNamePlan1.setText(empty);
            }
        } catch (Exception e){
            textViewNamePlan1.setText(empty);
        }
        try {
            textViewNamePlan2.setText(Account.planFriend(2).split("\n",5)[2]);
            if (Account.isDataSaver() && !skipDataSaver) {
                textViewNamePlan2.setText(empty);
            }
        } catch (Exception e){
            textViewNamePlan2.setText(empty);
        }
        try {
            textViewNamePlan3.setText(Account.planFriend(3).split("\n",5)[2]);
            if (Account.isDataSaver() && !skipDataSaver) {
                textViewNamePlan3.setText(empty);
            }
        } catch (Exception e){
            textViewNamePlan3.setText(empty);
        }
        try {
            textViewNamePlan4.setText(Account.planFriend(4).split("\n",5)[2]);
            if (Account.isDataSaver() && !skipDataSaver) {
                textViewNamePlan4.setText(empty);
            }
        } catch (Exception e){
            textViewNamePlan4.setText(empty);
        }
        try {
            textViewNamePlan5.setText(Account.planFriend(5).split("\n",5)[2]);
            if (Account.isDataSaver() && !skipDataSaver) {
                textViewNamePlan5.setText(empty);
            }
        } catch (Exception e){
            textViewNamePlan5.setText(empty);
        }
        ((ProgressBar) findViewById(R.id.progressBar)).setVisibility(View.INVISIBLE);


    }
    public void plan1(View view) {
        vibrate();
        TextView textViewNamePlan = findViewById(R.id.textViewPlan1);
        String text =  textViewNamePlan.getText().toString();
        openPlan(1, text);
    }
    public void plan2(View view) {
        vibrate();
        TextView textViewNamePlan = findViewById(R.id.textViewPlan2);
        String text =  textViewNamePlan.getText().toString();
        openPlan(2, text);
    }

    public void plan3(View view) {
        vibrate();
        TextView textViewNamePlan = findViewById(R.id.textViewPlan3);
        String text =  textViewNamePlan.getText().toString();
        openPlan(3, text);
    }
    public void plan4(View view) {
        vibrate();
        TextView textViewNamePlan = findViewById(R.id.textViewPlan4);
        String text =  textViewNamePlan.getText().toString();
        openPlan(4, text);
    }
    public void plan5(View view) {
        vibrate();
        TextView textViewNamePlan = findViewById(R.id.textViewPlan5);
        String text =  textViewNamePlan.getText().toString();
        openPlan(5, text);
    }

    private void openPlan(int id, String text) {
        if (text.equals("E M P T Y")) {
            toast("empty plan");
        } else if (text.equals("loading")) {
            toast("empty plan");
        } else if (text.equals("Download")) {
            try {
                toast("downloading");
                TextView textViewId = findViewById(R.id.textViewMe);
                loadPlansFinal(textViewId.getText().toString().split("#")[1]);
                skipDataSaver = true;
            } catch (Exception e){
                toast("no network");
            }
        } else {
            Account.setIsMine(false);
            isMyPlan= false;
            SharedPreferences settings = getSharedPreferences("sport", MODE_PRIVATE);
            SharedPreferences.Editor editor = settings.edit();

            Account.setActiveIterations(0); // set iterations to none done
            editor.putInt("selectedPlan", id).commit(); // for running plan

            Intent i = new Intent(this, RunPlanActivity.class);
            startActivity(i);
        }
    }

    public void downloadPlansBtn(View view) {
        skipDataSaver = true;
        TextView textViewId = findViewById(R.id.textViewMe);
        vibrate();
        try {
            loadPlansFinal(textViewId.getText().toString().split("#")[1]);
        } catch (Exception e){
            toast("no network");
        }
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
        pageStatus = "Followers";
        page(0);
    }

    public void openFollowing(View view) {
        pageStatus = "Following";
        page(1);
    }

    public void openStars(View view) {
        pageStatus = "Stars";
        page(2);
    }
    public static Boolean isStars;
    public void page(int i){
        try {
            isStars = false;
            TextView textViewUserID = findViewById(R.id.textViewMe);
            String id = textViewUserID.getText().toString();
            vibrate();
            if (i == 1) {
                StarsocketConnector.sendMessage("followsPage " + id.replace("#", ""));
            } else if (i == 2) {
                isStars = true;
                StarsocketConnector.sendMessage("starsPage " + id.replace("#", ""));
            } else if (i == 0) {
                StarsocketConnector.sendMessage("followersPage " + id.replace("#", ""));
            }
            Intent it = new Intent(this, FollowActivity.class);
            startActivity(it);
        } catch (Exception e){
            toast("no network");
        }
    }

    public void editProfile(View view) {
        vibrate();
        Intent i = new Intent(this, SettingsActivity.class);
        startActivity(i);
    }

    public void dataSaverSettings(View view) {
        vibrate();
        Intent i = new Intent(this, DataSaverActivity.class);
        startActivity(i);
    }

    // NAVBAR
    public void openFriendsList(View view) {
        vibrate();
        invalidId = false;
        friendsSearchRequest = false;
        ConstraintLayout constraintLayoutNavBarSearch = findViewById(R.id.constraintLayoutNavBarSearch);
        if (constraintLayoutNavBarSearch.getVisibility() == View.VISIBLE) {
            Intent i = new Intent(this, FriendsActivity.class);
            startActivity(i);
        } else {
            vibrate();
        }

    }
    public void openFriendsSearch(View view) {
        invalidId = false;
        friendsSearchRequest = true;
        ConstraintLayout constraintLayoutNavBarSearch = findViewById(R.id.constraintLayoutNavBarSearch);
        if (constraintLayoutNavBarSearch.getVisibility() != View.VISIBLE) {
            Intent i = new Intent(this, FriendsActivity.class);
            startActivity(i);
        } else {
            vibrate();
        }
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