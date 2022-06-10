package com.stardash.sportdash.online;

import static com.stardash.sportdash.online.ProfileActivity.invalidId;
import static com.stardash.sportdash.online.friends.FriendsActivity.friendsSearchRequest;
import static com.stardash.sportdash.settings.app.vibrate;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.stardash.sportdash.MainActivity;
import com.stardash.sportdash.R;
import com.stardash.sportdash.network.tcp.StarsocketConnector;
import com.stardash.sportdash.online.feed.FeedActivity;
import com.stardash.sportdash.online.friends.FriendsActivity;
import com.stardash.sportdash.online.friends.FriendsAdapter;
import com.stardash.sportdash.online.friends.FriendsItem;
import com.stardash.sportdash.online.notifications.InboxActivity;
import com.stardash.sportdash.settings.Account;

import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

    }

    public void searchUsers(View view) {
        vibrate();
        EditText editTextSearch = findViewById(R.id.editTextSearch);
        getFriends(editTextSearch.getText().toString().replaceAll(" ",""));
    }

    private void getFriends(String search) {
        try {
            String received = StarsocketConnector.getReplyTo("searchFriends ="+search).replaceAll("undefined","");
            try {
                createFriendsList(received);
                buildRecyclerView();
            } catch (Exception ignored){

            }
        } catch (Exception e){
            toast("no network");
        }
    }

    private ArrayList<FriendsItem> mFriendsList;
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
        RecyclerView mRecyclerView = findViewById(R.id.search_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(SearchActivity.this);
        FriendsAdapter mAdapter = new FriendsAdapter(mFriendsList);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
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

    // NAVBAR
    public void openFriendsList(View view) {
        vibrate();
        Intent i = new Intent(this, FriendsActivity.class);
        startActivity(i);
    }
    public void openFriendsSearch(View view) {
        vibrate();
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