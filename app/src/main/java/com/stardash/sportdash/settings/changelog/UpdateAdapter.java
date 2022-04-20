package com.stardash.sportdash.settings.changelog;

import static com.stardash.sportdash.settings.app.vibrate;

import android.content.Intent;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.stardash.sportdash.BuildConfig;
import com.stardash.sportdash.R;
import com.stardash.sportdash.network.tcp.StarsocketConnector;
import com.stardash.sportdash.online.ProfileActivity;
import com.stardash.sportdash.plans.comments.CommentsItem;
import com.stardash.sportdash.settings.Account;
import com.stardash.sportdash.settings.MyApplication;

import java.util.ArrayList;

public class UpdateAdapter extends RecyclerView.Adapter<UpdateAdapter.UpdateViewHolder> {

    private ArrayList<UpdateItem> mUpdateList;
    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }



    public static  class UpdateViewHolder extends RecyclerView.ViewHolder{

        public TextView mTextView1;
        public TextView mTextView2;
        public TextView mTextView3;

        public UpdateViewHolder(@NonNull View itemView) {
            super(itemView);
            mTextView1 = itemView.findViewById(R.id.textViewLine1);
            mTextView2 = itemView.findViewById(R.id.textViewLine2);
            mTextView3 = itemView.findViewById(R.id.textViewLine3);
        }
    }

    public UpdateAdapter(ArrayList<UpdateItem> UpdateList){
        mUpdateList = UpdateList;
    }

    @NonNull
    @Override
    public UpdateViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.update_item,parent, false);
        UpdateViewHolder evh = new UpdateViewHolder(v);
        return evh;
    }

    @Override
    public void onBindViewHolder(@NonNull UpdateViewHolder holder, int position) {
        UpdateItem currentItem = mUpdateList.get(position);

        if (currentItem.getText1().split(" ")[0].equals("NEW")) {
            holder.mTextView1.setTextColor(Color.parseColor("#0eff00"));
        } else if (currentItem.getText1().split(" ")[0].equals("FIX")) {
            holder.mTextView1.setTextColor(Color.parseColor("#FFFDD835"));
        } else if (currentItem.getText1().split(" ")[0].equals("REMOVED")) {
            holder.mTextView1.setTextColor(Color.parseColor("#E64868"));
        } else if (currentItem.getText1().split(" ")[0].equals("ADDED")) {
            holder.mTextView1.setTextColor(Color.parseColor("#0eff00"));
        } else if (currentItem.getText1().split(" ")[0].equals("UPGRADED")) {
            holder.mTextView1.setTextColor(Color.parseColor("#0eff00"));
        } else if (currentItem.getText1().split(" ")[0].equals("NEWVER")) {
            holder.mTextView1.setTextColor(Color.parseColor("#14FFEC"));
        }

        if (currentItem.getText1().split(" ")[0].equals("NEWVER")) {
            holder.mTextView1.setText("NEW VERSION "+currentItem.getText3());
            holder.mTextView1.setPadding(0,0,0,20);
            holder.mTextView1.setGravity(Gravity.CENTER);
            holder.mTextView2.setVisibility(View.GONE);
            holder.mTextView3.setVisibility(View.GONE);
        } else {

            String versionName = BuildConfig.VERSION_NAME;
            if (currentItem.getText3().equals(versionName)) {
                holder.mTextView3.setText("(your current version) version " + currentItem.getText3());
            } else {
                holder.mTextView3.setText("version " + currentItem.getText3());
            }

            if (currentItem.getText1().equals(versionName)) {
                holder.mTextView3.setText("(your current version) version " + currentItem.getText3());
            }

            holder.mTextView1.setText(currentItem.getText1());
            holder.mTextView2.setText(currentItem.getText2());

        }

    }


    @Override
    public int getItemCount() {
        return mUpdateList.size();
    }
}
