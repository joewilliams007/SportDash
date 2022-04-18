package com.stardash.sportdash.me.leaderboard;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.stardash.sportdash.R;
import com.stardash.sportdash.online.chat.ChatItem;
import com.stardash.sportdash.settings.Account;

import java.util.ArrayList;

public class LeaderboardAdapter extends RecyclerView.Adapter<LeaderboardAdapter.LeaderboardViewHolder> {

    private ArrayList<LeaderboardItem> mLeaderboardList;
    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }



    public static  class LeaderboardViewHolder extends RecyclerView.ViewHolder{

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

        if (currentItem.getText1().equals(Account.username())) {
            holder.mTextView1.setTextColor(Color.parseColor("#14FFEC"));
        }
        holder.mTextView1.setText(currentItem.getText1());
        holder.mTextView2.setText(currentItem.getText2());
        holder.mTextView3.setText(currentItem.getText3());
    }

    @Override
    public int getItemCount() {
        return mLeaderboardList.size();
    }
}
