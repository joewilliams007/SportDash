package com.stardash.sportdash.settings.account.security;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.stardash.sportdash.R;
import com.stardash.sportdash.network.tcp.StarsocketConnector;
import com.stardash.sportdash.online.feed.FeedActivity;
import com.stardash.sportdash.online.feed.FeedAdapter;
import com.stardash.sportdash.online.feed.FeedItem;

import java.util.ArrayList;

public class SecurityActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_security);

        ((ProgressBar) findViewById(R.id.progressBar)).setVisibility(View.VISIBLE);
        final Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                    createSecurityList(StarsocketConnector.getMessage().replaceAll("undefined",""));
                    buildRecyclerView();
                    ((ProgressBar) findViewById(R.id.progressBar)).setVisibility(View.GONE);

            }
        }, 100);
    }

    private ArrayList<SecurityItem> mSecurityList;
    private RecyclerView mRecyclerView;
    private SecurityAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    public void createSecurityList(String rest){
        String[] user = rest.split("\n");

        mSecurityList = new ArrayList<>();
        String separator = "LOGIN_DIVIDER";

        for (String element : user) {

                String ip = element.split(separator)[0];
                String location = element.split(separator)[1];
                String type = "Login detected";

                    if(element.split(separator)[3].equals("1")){
                        type = "Signup detected";
                    }

                String date = element.split(separator)[2].split("\\.")[0]+element.split(separator)[2].split("\\.")[2];
            mSecurityList.add(new SecurityItem("SECURITY ALERT",type, location,"IP "+ip,date));

        }
    }

    public void buildRecyclerView(){
        mRecyclerView = findViewById(R.id.security_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(SecurityActivity.this);
        mAdapter = new SecurityAdapter(mSecurityList);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
    }

}