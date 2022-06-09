package com.stardash.sportdash.online.chat;

import static android.content.Context.MODE_PRIVATE;
import static com.stardash.sportdash.online.friends.FriendsActivity.chatId;
import static com.stardash.sportdash.online.friends.FriendsActivity.chatUsername;
import static com.stardash.sportdash.online.friends.FriendsActivity.friendsSearchRequest;
import static com.stardash.sportdash.online.friends.FriendsActivity.openedChat;
import static com.stardash.sportdash.online.friends.FriendsActivity.tappedOnSearchItem;
import static com.stardash.sportdash.online.friends.FriendsActivity.tappedOnSearchItemId;
import static com.stardash.sportdash.plans.run.PlanActivity.isMyPlan;
import static com.stardash.sportdash.plans.run.RunPlanActivity.isRandom;
import static com.stardash.sportdash.plans.run.RunPlanActivity.isSpecificPlan;
import static com.stardash.sportdash.plans.run.RunPlanActivity.thePlan;
import static com.stardash.sportdash.settings.account.AppLockSettingsActivity.changeLock;
import static com.stardash.sportdash.settings.app.vibrate;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.stardash.sportdash.R;
import com.stardash.sportdash.network.tcp.StarsocketConnector;
import com.stardash.sportdash.online.ProfileActivity;
import com.stardash.sportdash.online.friends.FriendsActivity;
import com.stardash.sportdash.plans.run.RunPlanActivity;
import com.stardash.sportdash.settings.Account;
import com.stardash.sportdash.settings.MyApplication;
import com.stardash.sportdash.signIn.LockActivity;

import java.util.ArrayList;

public class InboxAdapter extends RecyclerView.Adapter<InboxAdapter.InboxViewHolder> {

    private ArrayList<InboxItem> mInboxList;
    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }



    public static  class InboxViewHolder extends RecyclerView.ViewHolder{

        public TextView mTextView1;
        public TextView mTextView2;
        public TextView mTextView3;
        public TextView mTextView4;
        public ImageView mImageView1;

        public InboxViewHolder(@NonNull View itemView) {
            super(itemView);
            mTextView1 = itemView.findViewById(R.id.textViewLine1);
            mTextView2 = itemView.findViewById(R.id.textViewLine2);
            mTextView3 = itemView.findViewById(R.id.textViewLine3);
            mTextView4 = itemView.findViewById(R.id.textViewLine4);
            mImageView1 = itemView.findViewById(R.id.imageView);
        }
    }

    public InboxAdapter(ArrayList<InboxItem> inboxList){
        mInboxList = inboxList;
    }

    @NonNull
    @Override
    public InboxViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.inbox_item,parent, false);
        return new InboxViewHolder(v);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull InboxViewHolder holder, int position) {
        InboxItem currentItem = mInboxList.get(position);

        if (currentItem.getText1().equals("STAR")) {
            if (currentItem.getText3().split(" ")[2].equals("0")) {
                @SuppressLint("UseCompatLoadingForDrawables") Drawable myDrawable = MyApplication.getAppContext().getResources().getDrawable(R.drawable.star_filled);
                holder.mImageView1.setImageDrawable(myDrawable);
            } else {
                @SuppressLint("UseCompatLoadingForDrawables") Drawable myDrawable = MyApplication.getAppContext().getResources().getDrawable(R.drawable.star_removebg);
                holder.mImageView1.setImageDrawable(myDrawable);
            }
            holder.mTextView3.setVisibility(View.GONE);
        } else if (currentItem.getText1().equals("FOLLOW")) {
            if (currentItem.getText3().split(" ")[2].equals("0")) {
                @SuppressLint("UseCompatLoadingForDrawables") Drawable myDrawable = MyApplication.getAppContext().getResources().getDrawable(R.drawable.follow_filled);
                holder.mImageView1.setImageDrawable(myDrawable);
            } else {
                @SuppressLint("UseCompatLoadingForDrawables") Drawable myDrawable = MyApplication.getAppContext().getResources().getDrawable(R.drawable.follower_removebg);
                holder.mImageView1.setImageDrawable(myDrawable);
            }
            holder.mTextView3.setVisibility(View.GONE);
        } else if (currentItem.getText1().equals("COMMENT")) {
            if (currentItem.getText3().split(" ")[2].equals("0")) {
                @SuppressLint("UseCompatLoadingForDrawables") Drawable myDrawable = MyApplication.getAppContext().getResources().getDrawable(R.drawable.comment_filled);
                holder.mImageView1.setImageDrawable(myDrawable);
            } else {
                @SuppressLint("UseCompatLoadingForDrawables") Drawable myDrawable = MyApplication.getAppContext().getResources().getDrawable(R.drawable.comment_removebg);
                holder.mImageView1.setImageDrawable(myDrawable);
            }
            holder.mTextView3.setVisibility(View.GONE);
        } else if (currentItem.getText1().equals("CHAT")) {
            if (currentItem.getText3().split(" ")[2].equals("0")) {
                @SuppressLint("UseCompatLoadingForDrawables") Drawable myDrawable = MyApplication.getAppContext().getResources().getDrawable(R.drawable.message_filled);
                holder.mImageView1.setImageDrawable(myDrawable);
            } else {
                @SuppressLint("UseCompatLoadingForDrawables") Drawable myDrawable = MyApplication.getAppContext().getResources().getDrawable(R.drawable.message_removebg);
                holder.mImageView1.setImageDrawable(myDrawable);
            }
            holder.mTextView3.setVisibility(View.GONE);
        }

        // holder.mTextView1.setTextColor(Color.parseColor("#14FFEC"));

        holder.mTextView1.setText(currentItem.getText1());
        holder.mTextView2.setText(currentItem.getText2());
        holder.mTextView3.setText(currentItem.getText3());
        holder.mTextView4.setText(currentItem.getText4());




        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    vibrate();
                    String notif_id = holder.mTextView3.getText().toString().split(" ")[1];
                    if (holder.mTextView1.getText().toString().equals("CHAT")) {
                        String from_id = holder.mTextView3.getText().toString().split(" ")[0];
                        try {
                            StarsocketConnector.sendMessage("viewNotificationChat " + from_id);
                        } catch (Exception ignored){

                        }
                    } else {
                        try {
                            StarsocketConnector.sendMessage("viewNotification " + notif_id);
                        } catch (Exception ignored){

                        }
                    }

                    if (holder.mTextView1.getText().toString().equals("STAR") || holder.mTextView1.getText().toString().equals("COMMENT")) {
                        try {
                            String plan_id = holder.mTextView3.getText().toString().split(" ")[3];
                            StarsocketConnector.sendMessage("downloadPlanById " + plan_id);

                            final Handler handler = new Handler(Looper.getMainLooper());
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    String received = null;

                                        received = StarsocketConnector.getMessage();

                                    SharedPreferences settings = MyApplication.getAppContext().getSharedPreferences("sport", MODE_PRIVATE);
                                    SharedPreferences.Editor editor = settings.edit();

                                    isSpecificPlan = true;
                                    thePlan = received;
                                    isRandom = false;

                                    if (received.equals("err")) {
                                        vibrate();
                                    } else {
                                        Account.setIsMine(false);
                                        isMyPlan= false;
                                        Account.setActiveIterations(0); // set iterations to none done
                                        editor.putInt("selectedPlan", 1).apply(); // for running plan
                                        Intent i = new Intent(MyApplication.getAppContext(), RunPlanActivity.class);
                                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        MyApplication.getAppContext().startActivity(i);
                                    }
                                }
                            }, 125);
                        } catch (Exception ignored){

                        }
                    } else if (holder.mTextView1.getText().toString().equals("FOLLOW")) {
                        try {
                            friendsSearchRequest = false;
                            String user_id = holder.mTextView3.getText().toString().split(" ")[0];
                            tappedOnSearchItem = true;
                            tappedOnSearchItemId = user_id;
                            Intent i = new Intent(MyApplication.getAppContext(), FriendsActivity.class);
                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            MyApplication.getAppContext().startActivity(i);
                        } catch (Exception ignored){

                        }

                    } else if (holder.mTextView1.getText().toString().equals("CHAT")) {
                        try {
                            vibrate();
                            String from_id = holder.mTextView3.getText().toString().split(" ")[0];

                            chatUsername = holder.mTextView3.getText().toString().split(" ")[3];
                            chatId = from_id;
                            openedChat = false;

                            changeLock = "chat";
                            if (Account.isChatLock() && !Account.password().equals("none")) {
                                changeLock = "chat";
                                Intent i = new Intent(MyApplication.getAppContext(), LockActivity.class);
                                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                MyApplication.getAppContext().startActivity(i);
                            } else {
                                Intent i = new Intent(MyApplication.getAppContext(), ChatActivity.class);
                                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                MyApplication.getAppContext().startActivity(i);
                            }


                        } catch (Exception ignored){

                        }

                    } else {

                    }
            }
        });

    }

    @Override
    public int getItemCount() {
        return mInboxList.size();
    }
}
