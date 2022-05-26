package com.stardash.sportdash.online.friends.follows;

import static android.content.Context.MODE_PRIVATE;
import static com.stardash.sportdash.online.friends.FriendsActivity.isStars;
import static com.stardash.sportdash.online.friends.FriendsActivity.tappedOnSearchItem;
import static com.stardash.sportdash.online.friends.FriendsActivity.tappedOnSearchItemId;
import static com.stardash.sportdash.plans.run.PlanActivity.isMyPlan;
import static com.stardash.sportdash.settings.app.vibrate;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.stardash.sportdash.R;
import com.stardash.sportdash.network.tcp.StarsocketConnector;
import com.stardash.sportdash.online.friends.FriendsActivity;
import com.stardash.sportdash.online.friends.FriendsItem;
import com.stardash.sportdash.plans.run.RunPlanActivity;
import com.stardash.sportdash.settings.Account;
import com.stardash.sportdash.settings.MyApplication;

import java.util.ArrayList;

public class FollowAdapter extends RecyclerView.Adapter<FollowAdapter.FollowViewHolder> {

    private ArrayList<FollowItem> mFollowList;

    public static class FollowViewHolder extends RecyclerView.ViewHolder{

        public TextView mTextView1;
        public TextView mTextView2;
        public TextView mTextView3;

        public FollowViewHolder(@NonNull View itemView) {
            super(itemView);
            mTextView1 = itemView.findViewById(R.id.textViewLine1);
            mTextView2 = itemView.findViewById(R.id.textViewLine2);
            mTextView3 = itemView.findViewById(R.id.textViewLine3);

        }
    }

    public FollowAdapter(ArrayList<FollowItem> followList){
        mFollowList = followList;
    }

    @NonNull
    @Override
    public FollowViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.follow_item,parent, false);
        FollowViewHolder evh = new FollowViewHolder(v);
        return evh;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull FollowViewHolder holder, int position) {
        FollowItem currentItem = mFollowList.get(position);

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
                vibrate();
                    String id = holder.mTextView1.getText().toString().split("#")[1];

                    try {
                        if (isStars) {
                            try {
                                StarsocketConnector.sendMessage("downloadPlanById " + id);

                                final Handler handler = new Handler(Looper.getMainLooper());
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {

                                        String received = StarsocketConnector.getMessage().toString();

                                        SharedPreferences settings = MyApplication.getAppContext().getSharedPreferences("sport", MODE_PRIVATE);
                                        SharedPreferences.Editor editor = settings.edit();
                                        String plan1 = received;

                                        if (plan1.equals("err")) {
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
                                }, 1000);
                            } catch (Exception e){

                            }
                        } else {
                            vibrate();
                            tappedOnSearchItem = true;
                            tappedOnSearchItemId = id;
                            Intent i = new Intent(MyApplication.getAppContext(), FriendsActivity.class);
                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            MyApplication.getAppContext().startActivity(i);
                        }
                    } catch (Exception e) {

                    }
            }
        });


    }


    @Override
    public int getItemCount() {
        return mFollowList.size();
    }
}
