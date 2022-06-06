package com.stardash.sportdash.online.friends;

import static com.stardash.sportdash.online.friends.FriendsActivity.tappedOnSearchItem;
import static com.stardash.sportdash.online.friends.FriendsActivity.tappedOnSearchItemId;
import static com.stardash.sportdash.settings.app.vibrate;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.stardash.sportdash.R;
import com.stardash.sportdash.me.leaderboard.LeaderboardItem;
import com.stardash.sportdash.network.tcp.StarsocketConnector;
import com.stardash.sportdash.online.ProfileActivity;
import com.stardash.sportdash.settings.Account;
import com.stardash.sportdash.settings.MyApplication;

import java.util.ArrayList;

public class FriendsAdapter extends RecyclerView.Adapter<FriendsAdapter.FriendsViewHolder> {

    private ArrayList<FriendsItem> mFriendsList;




    public static class FriendsViewHolder extends RecyclerView.ViewHolder{

        public TextView mTextView1;
        public TextView mTextView2;
        public TextView mTextView3;

        public FriendsViewHolder(@NonNull View itemView) {
            super(itemView);
            mTextView1 = itemView.findViewById(R.id.textViewLine1);
            mTextView2 = itemView.findViewById(R.id.textViewLine2);
            mTextView3 = itemView.findViewById(R.id.textViewLine3);

        }
    }

    public FriendsAdapter(ArrayList<FriendsItem> friendsList){
        mFriendsList = friendsList;
    }

    @NonNull
    @Override
    public FriendsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.friends_item,parent, false);
        FriendsViewHolder evh = new FriendsViewHolder(v);
        return evh;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull FriendsViewHolder holder, int position) {
        FriendsItem currentItem = mFriendsList.get(position);

        if (currentItem.getText1().equals(Account.userid())) {
            holder.mTextView2.setTextColor(Color.parseColor("#14FFEC"));
        }
        holder.mTextView1.setText("#"+currentItem.getText1());
        holder.mTextView2.setText(currentItem.getText2());
        holder.mTextView3.setText(currentItem.getText3());

        holder.mTextView3.animate().translationXBy(-10f).setDuration(1000);
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                holder.mTextView3.animate().translationXBy(10f).setDuration(1000);
            }
        }, 1000);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    String id = holder.mTextView1.getText().toString().split("#")[1];

                    try {
                        vibrate();
                        tappedOnSearchItem = true;
                        tappedOnSearchItemId = id;
                        Intent i = new Intent(MyApplication.getAppContext(), FriendsActivity.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        MyApplication.getAppContext().startActivity(i);
                    } catch (Exception e) {

                    }
            }
        });

        holder.mTextView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String id = holder.mTextView1.getText().toString().split("#")[1];

                try {
                    vibrate();
                    tappedOnSearchItem = true;
                    tappedOnSearchItemId = id;
                    Intent i = new Intent(MyApplication.getAppContext(), FriendsActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    MyApplication.getAppContext().startActivity(i);
                } catch (Exception ignored) {

                }
            }
        });

        holder.mTextView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String id = holder.mTextView1.getText().toString().split("#")[1];

                try {
                    vibrate();
                    tappedOnSearchItem = true;
                    tappedOnSearchItemId = id;
                    Intent i = new Intent(MyApplication.getAppContext(), FriendsActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    MyApplication.getAppContext().startActivity(i);
                } catch (Exception ignored) {

                }
            }
        });

        holder.mTextView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String id = holder.mTextView1.getText().toString().split("#")[1];

                try {
                    vibrate();
                    tappedOnSearchItem = true;
                    tappedOnSearchItemId = id;
                    Intent i = new Intent(MyApplication.getAppContext(), FriendsActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    MyApplication.getAppContext().startActivity(i);
                } catch (Exception ignored) {

                }
            }
        });



    }




    @Override
    public int getItemCount() {
        return mFriendsList.size();
    }
}
