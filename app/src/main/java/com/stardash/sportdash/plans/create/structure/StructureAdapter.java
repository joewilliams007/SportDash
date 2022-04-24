package com.stardash.sportdash.plans.create.structure;

import static com.stardash.sportdash.online.ProfileActivity.chatId;
import static com.stardash.sportdash.settings.app.vibrate;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.stardash.sportdash.R;
import com.stardash.sportdash.network.tcp.StarsocketConnector;
import com.stardash.sportdash.online.ProfileActivity;
import com.stardash.sportdash.online.chat.ChatItem;
import com.stardash.sportdash.settings.Account;
import com.stardash.sportdash.settings.MyApplication;

import java.util.ArrayList;

public class StructureAdapter extends RecyclerView.Adapter<StructureAdapter.StructureViewHolder> {

    private ArrayList<StructureItem> mStructureList;
    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }



    public static  class StructureViewHolder extends RecyclerView.ViewHolder{

        public TextView mTextView1;
        public TextView mTextView2;
        public TextView mTextView3;

        public StructureViewHolder(@NonNull View itemView) {
            super(itemView);
            mTextView1 = itemView.findViewById(R.id.textViewLine1);
            mTextView2 = itemView.findViewById(R.id.textViewLine2);
            mTextView3 = itemView.findViewById(R.id.textViewLine3);
        }
    }

    public StructureAdapter(ArrayList<StructureItem> structureList){
        mStructureList = structureList;
    }

    @NonNull
    @Override
    public StructureViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.element_item,parent, false);
        StructureViewHolder evh = new StructureViewHolder(v);
        return evh;
    }

    @Override
    public void onBindViewHolder(@NonNull StructureViewHolder holder, int position) {
        StructureItem currentItem = mStructureList.get(position);

        if (currentItem.getText1().equals(Account.username())) {
            holder.mTextView1.setTextColor(Color.parseColor("#14FFEC"));
        }
        if (currentItem.getText2().split(" ")[0].contains("http")) {
            holder.mTextView2.setTextColor(Color.parseColor("#FFFDD835"));
        }
        holder.mTextView1.setText(currentItem.getText1());
        holder.mTextView2.setText(currentItem.getText2());
        holder.mTextView3.setText(currentItem.getText3());

    }

    @Override
    public int getItemCount() {
        return mStructureList.size();
    }
}
