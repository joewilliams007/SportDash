package com.stardash.sportdash.settings.account.security;

import static android.content.Context.MODE_PRIVATE;
import static com.stardash.sportdash.online.feed.FeedActivity.items_count;
import static com.stardash.sportdash.online.feed.FeedActivity.text_item;
import static com.stardash.sportdash.online.friends.FriendsActivity.tappedOnSearchItem;
import static com.stardash.sportdash.online.friends.FriendsActivity.tappedOnSearchItemId;
import static com.stardash.sportdash.plans.run.PlanActivity.isMyPlan;
import static com.stardash.sportdash.plans.run.RunPlanActivity.isRandom;
import static com.stardash.sportdash.plans.run.RunPlanActivity.isSpecificPlan;
import static com.stardash.sportdash.plans.run.RunPlanActivity.thePlan;
import static com.stardash.sportdash.settings.app.vibrate;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.stardash.sportdash.R;
import com.stardash.sportdash.network.tcp.StarsocketConnector;
import com.stardash.sportdash.online.feed.FeedItem;
import com.stardash.sportdash.online.friends.FriendsActivity;
import com.stardash.sportdash.plans.run.RunPlanActivity;
import com.stardash.sportdash.settings.Account;
import com.stardash.sportdash.settings.MyApplication;
import com.stardash.sportdash.settings.account.AccountActivity;

import java.util.ArrayList;
import java.util.Locale;

public class SecurityAdapter extends RecyclerView.Adapter<SecurityAdapter.SecurityViewHolder> {

    private ArrayList<SecurityItem> mSecurityList;
    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }



    public static  class SecurityViewHolder extends RecyclerView.ViewHolder{
        public TextView mTextView0;
        public TextView mTextView1;
        public TextView mTextView2;
        public TextView mTextView3;
        public TextView mTextView4;
        public TextView mTextView5;

        public SecurityViewHolder(@NonNull View itemView) {
            super(itemView);
            mTextView0 = itemView.findViewById(R.id.textViewLine0);
            mTextView1 = itemView.findViewById(R.id.textViewLine1);
            mTextView2 = itemView.findViewById(R.id.textViewLine2);
            mTextView3 = itemView.findViewById(R.id.textViewLine3);
            mTextView4 = itemView.findViewById(R.id.textViewLine4);
            mTextView5 = itemView.findViewById(R.id.textViewLine5);
        }
    }

    public SecurityAdapter(ArrayList<SecurityItem> SecurityList){
        mSecurityList = SecurityList;
    }

    @NonNull
    @Override
    public SecurityViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v;
        v = LayoutInflater.from(parent.getContext()).inflate(R.layout.security_item,parent, false);


        return new SecurityViewHolder(v);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull SecurityViewHolder holder, int position) {
        SecurityItem currentItem = mSecurityList.get(position);

            holder.mTextView0.setText(currentItem.getText0().toUpperCase(Locale.ROOT));
            holder.mTextView1.setText(currentItem.getText1());

            if (currentItem.getText2().contains("From null")) {
                holder.mTextView2.setText("location could not be identified");
            } else {
                holder.mTextView2.setText(currentItem.getText2());
            }

            holder.mTextView3.setText(currentItem.getText3());
            holder.mTextView4.setText(currentItem.getText4());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vibrate();
                if (holder.mTextView5.getVisibility() == View.GONE) {
                    holder.mTextView5.setVisibility(View.VISIBLE);
                } else {
                    holder.mTextView5.setVisibility(View.GONE);
                }
            }
        });

        holder.mTextView5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vibrate();
                Intent i = new Intent(MyApplication.getAppContext(), AccountActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                MyApplication.getAppContext().startActivity(i);
            }
        });
    }


    @Override
    public int getItemCount() {
        return mSecurityList.size();
    }
}
