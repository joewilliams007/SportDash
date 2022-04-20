package com.stardash.sportdash.online;


import static com.stardash.sportdash.online.chat.ChatActivity.isInChat;
import static com.stardash.sportdash.plans.run.PlanActivity.isMyPlan;
import static com.stardash.sportdash.online.ProfileActivity.chatId;
import static com.stardash.sportdash.online.ProfileActivity.chatUsername;
import static com.stardash.sportdash.online.ProfileActivity.invalidId;
import static com.stardash.sportdash.plans.run.RunPlanActivity.isRandom;
import static com.stardash.sportdash.settings.app.vibrate;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

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
import android.widget.EditText;
import android.widget.TextView;

import com.stardash.sportdash.settings.Account;
import com.stardash.sportdash.online.chat.ChatActivity;
import com.stardash.sportdash.MainActivity;
import com.stardash.sportdash.R;
import com.stardash.sportdash.network.tcp.StarsocketConnector;
import com.stardash.sportdash.settings.MyApplication;

public class FriendsActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        setContentView(R.layout.activity_friends);

        isMyPlan = false; // so that the discard btn in active plan is gone
        isRandom = false;

        TextView textViewUrId = findViewById(R.id.textViewUrId);
        textViewUrId.setText("your id is #"+ Account.userid());

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
                    Intent i = new Intent(this, ProfileActivity.class);
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
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Window window = getWindow();
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(Color.BLACK);
            }
        }
        try {
            if (invalidId) {
                toast("invalid user id");
                vibrate();
            }
        } catch (Exception e){

        }
        hideSelect();

        setFriendNames();
    }

    public void onResume() {
        super.onResume();
        isInChat = false;
    }
    @Override
    public void onBackPressed() {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }
    private void setFriendNames() {
        TextView textViewFriend0 = findViewById(R.id.textViewFriend);
        TextView textViewFriend1 = findViewById(R.id.textViewFriend1);
        TextView textViewFriend2 = findViewById(R.id.textViewFriend2);
        TextView textViewFriend3 = findViewById(R.id.textViewFriend3);
        TextView textViewFriend4 = findViewById(R.id.textViewFriend4);
        TextView textViewFriend5 = findViewById(R.id.textViewFriend5);
        TextView textViewChat1 = findViewById(R.id.textViewChat1);
        TextView textViewChat2 = findViewById(R.id.textViewChat2);
        TextView textViewChat3 = findViewById(R.id.textViewChat3);
        TextView textViewChat4 = findViewById(R.id.textViewChat4);

        try {
            textViewFriend0.setText(Account.friend(0));
        } catch (Exception e){

        }
        try {
            textViewFriend1.setText(Account.friend(1));
        } catch (Exception e){

        }
        try {
            textViewFriend2.setText(Account.friend(2));
        } catch (Exception e){

        }
        try {
            textViewFriend3.setText(Account.friend(3));
        } catch (Exception e){

        }
        try {
            textViewFriend4.setText(Account.friend(4));
        } catch (Exception e){

        }
        try {
            textViewFriend5.setText(Account.friend(5));
        } catch (Exception e){

        }
        try {
            textViewChat1.setText(Account.friend(6));
        } catch (Exception e){

        }
        try {
            textViewChat2.setText(Account.friend(7));
        } catch (Exception e){

        }
        try {
            textViewChat3.setText(Account.friend(8));
        } catch (Exception e){

        }
        try {
            textViewChat4.setText(Account.friend(9));
        } catch (Exception e){

        }

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

    public void openProfile(View view) {
        vibrate();
        EditText editText = findViewById(R.id.editTextTextPersonNameHashtag);
        if (editText.getText().toString().length()<1){
            toast("enter id");
        } else {
            try {
                StarsocketConnector.sendMessage("getProfile " + editText.getText().toString());
                Intent i = new Intent(this, ProfileActivity.class);
                startActivity(i);
            } catch (Exception e) {
                toast("no network");
            }
        }
    }

    public void friend0(View view) {
        useFriend(0);
    }
    public void friend1(View view) {
        useFriend(1);
    }
    public void friend2(View view) {
        useFriend(2);
    }
    public void friend3(View view) {
        useFriend(3);
    }
    public void friend4(View view) {
        useFriend(4);
    }
    public void friend5(View view) {
        useFriend(5);
    }
    public void friend6(View view) {
        useFriend(6);
    }
    public void friend7(View view) {
        useFriend(7);
    }
    public void friend8(View view) {
        useFriend(8);
    }
    public void friend9(View view) {
        useFriend(9);
    }


    static int friend;
    private void useFriend(int friend1) {
        ChatActivity.updateChat = false;
        friend = friend1;
        vibrate();
        if (Account.isAddingFriend()) {
            String adding = Account.selectedFriend();
            Account.setFriend(friend,adding);
            setFriendNames();
            toast("adding to friends");
        } else {
            if (Account.friend(friend).length()<1){
                toast("empty place for a friend");
            } else {
                TextView textViewSelect = findViewById(R.id.textViewSelect);
                TextView textViewProfile = findViewById(R.id.textViewProfile);
                TextView textViewChat = findViewById(R.id.textViewChat);
                TextView textViewClose = findViewById(R.id.textViewClose);
                TextView textViewProfileId = findViewById(R.id.textViewProfileId);

                textViewProfileId.setVisibility(View.VISIBLE);
                textViewClose.setVisibility(View.VISIBLE);
                textViewSelect.setVisibility(View.VISIBLE);
                textViewProfile.setVisibility(View.VISIBLE);
                textViewChat.setVisibility(View.VISIBLE);

                textViewProfileId.setText(Account.friend(friend));

            }
        }
        Account.setAddingFriend(false);
    }
    public void openProfileId(View view) {
        vibrate();
        hideSelect();
        if (Account.friend(friend).replace("#","").split(" ")[0].length()<1){
            toast("empty place for a friend!");
        } else {
            try {
                StarsocketConnector.sendMessage("getProfile " + Account.friend(friend).replace("#", "").split(" ")[0]);
                Intent i = new Intent(this, ProfileActivity.class);
                startActivity(i);
            } catch (Exception e) {
                toast("no network");
            }
        }
    }



    public void openMyProfile(View view) {
        vibrate();
        try {
            StarsocketConnector.sendMessage("getProfile " + Account.userid());
            Intent i = new Intent(this, ProfileActivity.class);
            startActivity(i);
        } catch (Exception e) {
            toast("no network");
        }
    }




    public void hideSelect() {
        TextView textViewSelect = findViewById(R.id.textViewSelect);
        TextView textViewProfile = findViewById(R.id.textViewProfile);
        TextView textViewChat = findViewById(R.id.textViewChat);
        TextView textViewClose = findViewById(R.id.textViewClose);
        TextView textViewProfileId = findViewById(R.id.textViewProfileId);

        textViewClose.setVisibility(View.GONE);
        textViewSelect.setVisibility(View.GONE);
        textViewProfile.setVisibility(View.GONE);
        textViewChat.setVisibility(View.GONE);
        textViewProfileId.setVisibility(View.GONE);
    }

    public void doNth(View view) {
    }

    public void openChat(View view) {
        vibrate();
        hideSelect();

        TextView textViewProfileId = findViewById(R.id.textViewProfileId);
        chatUsername = textViewProfileId.getText().toString().split(" ")[1];
        chatId = textViewProfileId.getText().toString().split(" ")[0].replace("#","");

            Intent i = new Intent(this, ChatActivity.class);
            startActivity(i);

    }

    public void hideSelectStart(View view) {
        vibrate();
        hideSelect();
    }


}