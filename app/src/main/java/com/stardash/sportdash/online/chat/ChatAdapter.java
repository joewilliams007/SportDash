package com.stardash.sportdash.online.chat;

import static com.stardash.sportdash.online.chat.ChatActivity.editingMessage;
import static com.stardash.sportdash.online.friends.FriendsActivity.chatId;
import static com.stardash.sportdash.settings.app.vibrate;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.ItemTouchHelper;
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
        public ImageView mImageViewCopy;
        public ImageView mImageViewEdit;
        public TextView mTextViewMetaData;
        public EditText mEditText;
        public ImageView mImageViewTick;
        public ConstraintLayout mConstraintLayout;

        public ChatViewHolder(@NonNull View itemView) {
            super(itemView);
            mTextView1 = itemView.findViewById(R.id.textViewLine1);
            mTextView2 = itemView.findViewById(R.id.textViewLine2);
            mTextView3 = itemView.findViewById(R.id.textViewLine3);
            mImageViewCopy = itemView.findViewById(R.id.imageViewCopy);
            mTextViewMetaData = itemView.findViewById(R.id.textViewLine8);
            mConstraintLayout = itemView.findViewById(R.id.options);
            mImageViewEdit = itemView.findViewById(R.id.imageViewEdit);
            mEditText = itemView.findViewById(R.id.editText);
            mImageViewTick = itemView.findViewById(R.id.imageViewTick);
        }
    }

    public ChatAdapter(ArrayList<ChatItem> chatList){
        mChatList = chatList;
    }

    @NonNull
    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_item,parent, false);
        return new ChatViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatViewHolder holder, int position) {
        ChatItem currentItem = mChatList.get(position);


        holder.mTextView1.setText(currentItem.getText1());
        holder.mTextView2.setText(currentItem.getText2());
        holder.mTextView3.setText(currentItem.getText3());
        holder.mTextViewMetaData.setText(currentItem.getText4());

        if (currentItem.getText4().split(" ")[4].equals("1")) {
            holder.mTextView2.setTextColor(Color.parseColor("#E64868"));
        } else {
            holder.mTextView2.setTextColor(Color.parseColor("#FFFFFFFF"));
        }
        if (currentItem.getText4().split(" ")[5].equals("1")) {
            holder.itemView.findViewById(R.id.imageViewEditMessageStatus).setVisibility(View.VISIBLE);
        } else {
            holder.itemView.findViewById(R.id.imageViewEditMessageStatus).setVisibility(View.GONE);
        }

        if (currentItem.getText4().split(" ")[3].equals("1")) {
           holder.mImageViewTick.setImageResource(R.drawable.doubletick_removebg);
        } else {
            holder.mImageViewTick.setImageResource(R.drawable.tick_removebg);
        }

        if (!currentItem.getText4().split(" ")[0].equals(Account.userid())) {
            holder.mImageViewTick.setVisibility(View.GONE);
            holder.mTextView1.setTextColor(Color.parseColor("#FFFDD835"));
        } else {
            holder.mImageViewTick.setVisibility(View.VISIBLE);
            holder.mTextView1.setTextColor(Color.parseColor("#14FFEC"));
        }



        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vibrate();
                editingMessage = false;

                    if (holder.mConstraintLayout.getVisibility()==View.VISIBLE){
                        holder.mConstraintLayout.setVisibility(View.GONE);
                    } else {
                        holder.mConstraintLayout.setVisibility(View.VISIBLE);
                        if (!currentItem.getText4().split(" ")[0].equals(Account.userid())) {
                            holder.mImageViewEdit.setVisibility(View.GONE);
                            holder.itemView.findViewById(R.id.imageViewDelete).setVisibility(View.GONE);
                        }
                        if (currentItem.getText4().split(" ")[4].equals("1")) {
                            holder.mImageViewCopy.setVisibility(View.GONE);
                            holder.mImageViewEdit.setVisibility(View.GONE);
                            holder.itemView.findViewById(R.id.imageViewDelete).setVisibility(View.GONE);
                        }
                    }

            }
        });

       holder.mImageViewCopy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String id = holder.mTextView2.getText().toString();

                try {
                    vibrate();
                        ClipboardManager clipboard = (ClipboardManager) MyApplication.getAppContext().getSystemService(Context.CLIPBOARD_SERVICE);
                        ClipData clip = ClipData.newPlainText("SportDash", id);
                        clipboard.setPrimaryClip(clip);
                        Toast myToast = Toast.makeText(MyApplication.getAppContext(), "Copied!", Toast.LENGTH_SHORT);
                        myToast.show();
                } catch (Exception e) {
                    Toast myToast = Toast.makeText(MyApplication.getAppContext(), "ERROR", Toast.LENGTH_SHORT);
                    myToast.show();
                }
            }
        });


        holder.itemView.findViewById(R.id.imageViewEdit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vibrate();
                if (holder.mEditText.getVisibility()==View.GONE) {
                    editingMessage = true;
                    holder.mEditText.setVisibility(View.VISIBLE);
                    holder.itemView.findViewById(R.id.imageViewSave).setVisibility(View.VISIBLE);
                    holder.mEditText.setText(holder.mTextView2.getText().toString());
                } else {
                    editingMessage = false;
                    holder.mEditText.setVisibility(View.GONE);
                    holder.itemView.findViewById(R.id.imageViewSave).setVisibility(View.GONE);
                }
            }
        });

        holder.itemView.findViewById(R.id.imageViewDelete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    editingMessage = false;
                    vibrate();
                    try {
                        String id = holder.mTextViewMetaData.getText().toString().split(" ")[2];
                        StarsocketConnector.sendMessage("deleteMessage "+id+" " + chatId + " TEXTMESSAGESP:editedMessage");
                        Toast myToast = Toast.makeText(MyApplication.getAppContext(), "Message deleted." ,Toast.LENGTH_SHORT);
                        myToast.show();
                    } catch (Exception e) {
                        Toast myToast = Toast.makeText(MyApplication.getAppContext(), "Error - no network?" ,Toast.LENGTH_SHORT);
                        myToast.show();
                    }
            }
        });

        holder.itemView.findViewById(R.id.imageViewSave).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vibrate();
                editingMessage = false;
                try {
                    String id = holder.mTextViewMetaData.getText().toString().split(" ")[2];
                    String text = holder.mEditText.getText().toString();
                    if (text.length()<1) {
                        Toast myToast = Toast.makeText(MyApplication.getAppContext(), "Enter a message" ,Toast.LENGTH_SHORT);
                        myToast.show();
                    } else {
                        StarsocketConnector.sendMessage("editMessage "+id+" "+ chatId + " editedMessage"+text+"editedMessage TEXTMESSAGESP:editedMessage");
                        Toast myToast = Toast.makeText(MyApplication.getAppContext(), "Edited message" ,Toast.LENGTH_SHORT);
                        myToast.show();
                        holder.mEditText.setVisibility(View.GONE);
                        holder.itemView.findViewById(R.id.imageViewSave).setVisibility(View.GONE);
                        holder.mConstraintLayout.setVisibility(View.GONE);
                    }

                } catch (Exception e) {
                    Toast myToast = Toast.makeText(MyApplication.getAppContext(), "Error - no network?" ,Toast.LENGTH_SHORT);
                    myToast.show();
                    holder.mEditText.setVisibility(View.GONE);
                    holder.itemView.findViewById(R.id.imageViewSave).setVisibility(View.GONE);
                    holder.mConstraintLayout.setVisibility(View.GONE);
                }


            }
        });


    }

    @Override
    public int getItemCount() {
        return mChatList.size();
    }
}
