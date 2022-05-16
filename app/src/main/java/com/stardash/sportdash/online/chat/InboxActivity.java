package com.stardash.sportdash.online.chat;

import static com.stardash.sportdash.settings.app.vibrate;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.stardash.sportdash.settings.Account;
import com.stardash.sportdash.MainActivity;
import com.stardash.sportdash.R;
import com.stardash.sportdash.network.tcp.StarsocketConnector;

import java.util.ArrayList;

public class InboxActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        setContentView(R.layout.activity_inbox);

        getInbox();

        if (Account.isAmoled()) {
            ConstraintLayout main = findViewById(R.id.main);
            main.setBackgroundDrawable(new ColorDrawable(Color.BLACK));
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.BLACK);
        }
    }

    private void getInbox() {
        try {
            StarsocketConnector.sendMessage("mychatinbox " + Account.userid());

            final Handler handler = new Handler(Looper.getMainLooper());
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    String inbox = StarsocketConnector.getMessage();
                    createFeedList(inbox);
                }
            }, 500);

        } catch (Exception e){
            toast("no network");
        }
    }

    private ArrayList<InboxItem> mInboxList;
    private RecyclerView mRecyclerView;
    private InboxAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    public void createFeedList(String rest){
        String[] user = rest.split("NEXTMESSAGEIS:;");

        mInboxList = new ArrayList<>();

        for (String element : user) {
            try {
                mInboxList.add(new InboxItem(element.split("@")[1], element.split("@")[2], element.split("@")[0]));
            } catch (Exception e) {

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


    public void clearInbox(View view) {
        vibrate();
        toast("deleting inbox . . .");
        StarsocketConnector.sendMessage("clearinbox " + Account.userid());
        final Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
               getInbox();
            }
        }, 2000);
    }

    public void refreshInbox(View view) {
        vibrate();
        toast("refreshed!");
        getInbox();
    }
}