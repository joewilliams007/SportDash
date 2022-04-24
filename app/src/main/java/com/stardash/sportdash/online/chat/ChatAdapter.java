package com.stardash.sportdash.online.chat;

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

import com.stardash.sportdash.network.tcp.StarsocketConnector;
import com.stardash.sportdash.online.ProfileActivity;
import com.stardash.sportdash.settings.Account;
import com.stardash.sportdash.R;
import com.stardash.sportdash.settings.MyApplication;

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
        if (currentItem.getText2().split(" ")[0].contains("http")) {
            holder.mTextView2.setTextColor(Color.parseColor("#FFFDD835"));
        }
        holder.mTextView1.setText(currentItem.getText1());
        holder.mTextView2.setText(currentItem.getText2());
        holder.mTextView3.setText(currentItem.getText3());

        holder.mTextView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String id = holder.mTextView2.getText().toString();

                try {
                    vibrate();
                    if (id.split(" ")[0].contains("http")) {
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(id.split(" ")[0]));
                        browserIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        MyApplication.getAppContext().startActivity(browserIntent);
                    } else {
                        ClipboardManager clipboard = (ClipboardManager) MyApplication.getAppContext().getSystemService(Context.CLIPBOARD_SERVICE);
                        ClipData clip = ClipData.newPlainText("SportDash-ID", id);
                        clipboard.setPrimaryClip(clip);
                    }
                } catch (Exception e) {
                    vibrate();
                    vibrate();
                }
            }
        });
        holder.mTextView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    vibrate();
                    StarsocketConnector.sendMessage("getProfile " + chatId);
                    Intent i = new Intent(MyApplication.getAppContext(), ProfileActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    MyApplication.getAppContext().startActivity(i);
                } catch (Exception e) {

                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mChatList.size();
    }
}
