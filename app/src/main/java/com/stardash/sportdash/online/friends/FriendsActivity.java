package com.stardash.sportdash.online.friends;


import static com.stardash.sportdash.online.chat.ChatActivity.isInChat;
import static com.stardash.sportdash.online.notifications.InboxActivity.amount;
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
import android.os.Handler;
import android.os.Looper;
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

import com.stardash.sportdash.online.SearchActivity;
import com.stardash.sportdash.online.notifications.InboxActivity;
import com.stardash.sportdash.online.feed.FeedActivity;
import com.stardash.sportdash.online.friends.follows.FollowActivity;
import com.stardash.sportdash.plans.run.RunPlanActivity;
import com.stardash.sportdash.settings.Account;
import com.stardash.sportdash.online.chat.ChatActivity;
import com.stardash.sportdash.MainActivity;
import com.stardash.sportdash.R;
import com.stardash.sportdash.network.tcp.StarsocketConnector;
import com.stardash.sportdash.settings.SettingsActivity;
import com.stardash.sportdash.settings.account.DataSaverActivity;
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
        setContentView(R.layout.activity_friends);

        isMyPlan = false;
        isRandom = false;
        skipDataSaver = false;
        openedChat = false;

        TextView textViewUrId = findViewById(R.id.textViewMe);
        textViewUrId.setText("#"+ Account.userid());

        Bundle extras = getIntent().getExtras();
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
        }

        if (tappedOnSearchItem){
            theId = tappedOnSearchItemId;
            tappedOnSearchItem = false;
        } else {
            theId = Account.userid();
        }
        getUserProfile();
    }

    @SuppressLint("SetTextI18n")
    public void checkNotifications() {
        TextView textViewNotification = findViewById(R.id.textViewNotification);
                if (amount>0) {
                    textViewNotification.setVisibility(View.VISIBLE);
                    textViewNotification.setText(" "+ amount +" ");
                } else  {
                    textViewNotification.setVisibility(View.INVISIBLE);
                }
    }

    @SuppressLint("SetTextI18n")
    private void getUserProfile() {
        try {
            String separator = "PROFILE_OF_USER";
            String profile = StarsocketConnector.getReplyTo("getProfile " + theId);
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
            String plans = profile.split(separator, 15)[13];

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
            TextView textViewPlansNr = findViewById(R.id.textViewPlansNr);
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
            textViewPlansNr.setText(plans);
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
                ObjectAnimator.ofInt(progressBarToday, "progress", Integer.parseInt(xpToday))
                        .setDuration(600)
                        .start();
                ObjectAnimator.ofInt(progressBarWeek, "progress", Integer.parseInt(xpWeek))
                        .setDuration(600)
                        .start();
            } catch (Exception ignored) {

            }

            ImageView imageViewMessage = findViewById(R.id.imageViewMessage);
            ImageView imageViewEdit = findViewById(R.id.imageViewEdit);
            ImageView imageViewFollow = findViewById(R.id.imageViewFollow);
            String idZ = textViewUserID.getText().toString().replace("#", "");
            if (idZ.equals(Account.userid())) {
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
            ImageView imageViewFollow = findViewById(R.id.imageViewFollow);
            try {

                imageViewFollow.setImageResource(R.drawable.follow_loading_removebg);

                        String isFollow = null;
                        try {
                            isFollow = StarsocketConnector.getReplyTo("checkFollow " + Account.userid() +" "+ textViewId.getText().toString().split("#")[1]);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        if (isFollow.length()>10) {
                            imageViewFollow.setImageResource(R.drawable.follow_removebg);
                        } else {
                            imageViewFollow.setImageResource(R.drawable.unfollow_removebg);
                        }
            } catch (Exception ignored){
            }
        } catch (Exception ignored){
        }

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
            TextView textViewUsername = findViewById(R.id.textViewUsername);
            try {
                ((ProgressBar) findViewById(R.id.progressBar)).setVisibility(View.VISIBLE);
                StarsocketConnector.sendMessage("follow " + Account.userid() +" "+ textViewId.getText().toString().split("#")[1]+" "+Account.username()+" "+ textViewUsername.getText().toString());

                final Handler handler = new Handler(Looper.getMainLooper());
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
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
    public void openPlans(View view) {
        pageStatus = "Plans";
        page(3);
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
            } else if (i == 3) {
                isStars = true;
                StarsocketConnector.sendMessage("plansPage " + id.replace("#", ""));
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
    }
    public void openFriendsSearch(View view) {
        vibrate();
        Intent i = new Intent(this, SearchActivity.class);
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