package com.stardash.sportdash.me.leaderboard;

import static com.stardash.sportdash.online.friends.FriendsActivity.tappedOnSearchItem;
import static com.stardash.sportdash.online.friends.FriendsActivity.tappedOnSearchItemId;
import static com.stardash.sportdash.settings.app.vibrate;

import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.stardash.sportdash.R;
import com.stardash.sportdash.network.tcp.StarsocketConnector;
import com.stardash.sportdash.online.ProfileActivity;
import com.stardash.sportdash.online.friends.FriendsActivity;
import com.stardash.sportdash.settings.Account;
import com.stardash.sportdash.settings.MyApplication;

import java.util.ArrayList;

public class LeaderboardAdapter extends RecyclerView.Adapter<LeaderboardAdapter.LeaderboardViewHolder> {

    private ArrayList<LeaderboardItem> mLeaderboardList;




    public static class LeaderboardViewHolder extends RecyclerView.ViewHolder{

        public TextView mTextView1;
        public TextView mTextView2;
        public TextView mTextView3;

        public LeaderboardViewHolder(@NonNull View itemView) {
            super(itemView);
            mTextView1 = itemView.findViewById(R.id.textViewLine1);
            mTextView2 = itemView.findViewById(R.id.textViewLine2);
            mTextView3 = itemView.findViewById(R.id.textViewLine3);

        }
    }

    public LeaderboardAdapter(ArrayList<LeaderboardItem> leaderboardList){
        mLeaderboardList = leaderboardList;
    }

    @NonNull
    @Override
    public LeaderboardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.leaderboard_item,parent, false);
        LeaderboardViewHolder evh = new LeaderboardViewHolder(v);
        return evh;
    }

    @Override
    public void onBindViewHolder(@NonNull LeaderboardViewHolder holder, int position) {
        LeaderboardItem currentItem = mLeaderboardList.get(position);

        if (currentItem.getText2().split("#")[1].equals(Account.userid())) {
            holder.mTextView2.setTextColor(Color.parseColor("#14FFEC"));
        }
        holder.mTextView1.setText(currentItem.getText1());
        holder.mTextView2.setText(currentItem.getText2());
        holder.mTextView3.setText(currentItem.getText3());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    String id = holder.mTextView2.getText().toString().split("#")[1];

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

        if (currentItem.getText1().contains("1. place")||currentItem.getText1().contains("2. place")||currentItem.getText1().contains("3. place")) {
            holder.mTextView1.setTextColor(Color.parseColor("#FFFDD835"));
        }
    }




    @Override
    public int getItemCount() {
        return mLeaderboardList.size();
    }
}
