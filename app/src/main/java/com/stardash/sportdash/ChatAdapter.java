package com.stardash.sportdash;

import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatViewHolder> {

    private ArrayList<ChatItem> mChatList;
    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }



    public static  class ChatViewHolder extends RecyclerView.ViewHolder{

        public TextView mTextView1;
        public TextView mTextView2;
        public TextView mTextView3;

        public ChatViewHolder(@NonNull View itemView) {
            super(itemView);
            mTextView1 = itemView.findViewById(R.id.textViewLine1);
            mTextView2 = itemView.findViewById(R.id.textViewLine2);
            mTextView3 = itemView.findViewById(R.id.textViewLine3);
        }
    }

    public ChatAdapter(ArrayList<ChatItem> chatList){
        mChatList = chatList;
    }

    @NonNull
    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_item,parent, false);
        ChatViewHolder evh = new ChatViewHolder(v);
        return evh;
    }

    @Override
    public void onBindViewHolder(@NonNull ChatViewHolder holder, int position) {
        ChatItem currentItem = mChatList.get(position);

        if (currentItem.getText1().equals(Account.username())) {
            holder.mTextView1.setTextColor(Color.parseColor("#14FFEC"));
        }
        holder.mTextView1.setText(currentItem.getText1());
        holder.mTextView2.setText(currentItem.getText2());
        holder.mTextView3.setText(currentItem.getText3());
    }

    @Override
    public int getItemCount() {
        return mChatList.size();
    }
}
