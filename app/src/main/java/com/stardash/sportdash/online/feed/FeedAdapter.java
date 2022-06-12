package com.stardash.sportdash.online.feed;

import static android.content.Context.MODE_PRIVATE;
import static com.stardash.sportdash.online.feed.FeedActivity.items_count;
import static com.stardash.sportdash.online.feed.FeedActivity.text_item;
import static com.stardash.sportdash.online.friends.FriendsActivity.isStars;
import static com.stardash.sportdash.online.friends.FriendsActivity.tappedOnSearchItem;
import static com.stardash.sportdash.online.friends.FriendsActivity.tappedOnSearchItemId;
import static com.stardash.sportdash.plans.run.PlanActivity.isMyPlan;
import static com.stardash.sportdash.plans.run.RunPlanActivity.commentsPlanId;
import static com.stardash.sportdash.plans.run.RunPlanActivity.isRandom;
import static com.stardash.sportdash.plans.run.RunPlanActivity.isSpecificPlan;
import static com.stardash.sportdash.plans.run.RunPlanActivity.thePlan;
import static com.stardash.sportdash.settings.app.vibrate;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Handler;
import android.os.Looper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.stardash.sportdash.BuildConfig;
import com.stardash.sportdash.R;
import com.stardash.sportdash.network.tcp.StarsocketConnector;
import com.stardash.sportdash.online.friends.FriendsActivity;
import com.stardash.sportdash.plans.comments.CommentsActivity;
import com.stardash.sportdash.plans.run.RunPlanActivity;
import com.stardash.sportdash.settings.Account;
import com.stardash.sportdash.settings.MyApplication;
import com.stardash.sportdash.settings.changelog.UpdateItem;

import java.util.ArrayList;
import java.util.Locale;

public class FeedAdapter extends RecyclerView.Adapter<FeedAdapter.FeedViewHolder> {

    private ArrayList<FeedItem> mFeedList;
    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }



    public static  class FeedViewHolder extends RecyclerView.ViewHolder{
        public TextView mTextView0;
        public TextView mTextView1;
        public TextView mTextView2;
        public TextView mTextView3;
        public TextView mTextView4;
        public TextView mTextViewStars;
        public ImageView mImageViewStar;
        public ImageView mImageViewComments;

        public FeedViewHolder(@NonNull View itemView) {
            super(itemView);
            mTextView0 = itemView.findViewById(R.id.textViewLine0);
            mTextView1 = itemView.findViewById(R.id.textViewLine1);
            mTextView2 = itemView.findViewById(R.id.textViewLine2);
            mTextView3 = itemView.findViewById(R.id.textViewLine3);
            mTextView4 = itemView.findViewById(R.id.textViewLine4);
            mImageViewStar = itemView.findViewById(R.id.imageViewStar);
            mTextViewStars = itemView.findViewById(R.id.textViewStars);
            mImageViewComments = itemView.findViewById(R.id.imageViewComment);
        }
    }

    public FeedAdapter(ArrayList<FeedItem> FeedList){
        mFeedList = FeedList;
    }

    @NonNull
    @Override
    public FeedViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v;
         // if (text_item) {
           // v = LayoutInflater.from(parent.getContext()).inflate(R.layout.black_feed_item,parent, false);
            text_item = false;
       // } else {
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.feed_item,parent, false);
           // text_item = true;
       // }

      //  v = LayoutInflater.from(parent.getContext()).inflate(R.layout.feed_item,parent, false);

        return new FeedViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull FeedViewHolder holder, int position) {
        FeedItem currentItem = mFeedList.get(position);

            holder.mTextView0.setText(currentItem.getText0().toUpperCase(Locale.ROOT));
            holder.mTextView1.setText(currentItem.getText1());
            holder.mTextView2.setText(currentItem.getText2());
            holder.mTextView3.setText(currentItem.getText3());
            holder.mTextView4.setText(currentItem.getText4());
        items_count++;

        holder.mTextView0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String id = holder.mTextView3.getText().toString().split("#")[1].split("-")[0];

                vibrate();
                tappedOnSearchItem = true;
                tappedOnSearchItemId = id;
                Intent i = new Intent(MyApplication.getAppContext(), FriendsActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                MyApplication.getAppContext().startActivity(i);
            }
        });

        holder.mTextView4.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                try {
                    String id = holder.mTextView3.getText().toString().split("#")[1];
                    vibrate();
                    ImageView img = holder.mImageViewStar;
                    if (img.getVisibility() == View.VISIBLE) {
                        img.setVisibility(View.GONE);
                        holder.mImageViewComments.setVisibility(View.GONE);
                    } else {
                        img.setVisibility(View.VISIBLE);
                        holder.mImageViewComments.setVisibility(View.VISIBLE);

                                try {
                                    String received = StarsocketConnector.getReplyTo("getStars " + id);
                                    String star_status = received.split("#")[2];
                                    if (star_status.equals("1")) {
                                        holder.mImageViewStar.setImageResource(R.drawable.star_filled);
                                        holder.mTextViewStars.setText("true");
                                    } else {
                                        holder.mImageViewStar.setImageResource(R.drawable.star_removebg);
                                        holder.mTextViewStars.setText("false");
                                    }
                                }catch (Exception e) {
                                    holder.mTextView4.setText("there was an error.");
                                }



                    }
                }catch (Exception e) {
                    holder.mTextView4.setText("there was an error.");
                }
            }
        });

        holder.mImageViewComments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vibrate();
                    commentsPlanId = holder.mTextView3.getText().toString().split("#")[1];
                    Intent i = new Intent(MyApplication.getAppContext(), CommentsActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    MyApplication.getAppContext().startActivity(i);
            }
        });

        holder.mImageViewStar.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                vibrate();
                try {
                    String id = holder.mTextView3.getText().toString().split("#")[1];

                    String desc = " %%%" + holder.mTextView1.getText().toString();
                    StarsocketConnector.sendMessage("starPlan " + Account.userid() + " " + id + desc);


                    if (holder.mTextViewStars.getText().equals("false")) {
                        holder.mImageViewStar.setImageResource(R.drawable.star_filled);
                        holder.mTextViewStars.setText("true");

                        String Line4 = holder.mTextView4.getText().toString();
                        String endLine4 = Line4.split("views ")[1];
                        String stars = endLine4.split(" stars")[0];
                        int starsInt = Integer.parseInt(stars);
                        holder.mTextView4.setText(Line4.replace(endLine4, String.valueOf(starsInt + 1) + " stars"));
                    } else {
                        holder.mImageViewStar.setImageResource(R.drawable.star_removebg);
                        holder.mTextViewStars.setText("false");
                        String Line4 = holder.mTextView4.getText().toString();
                        String endLine4 = Line4.split("views ")[1];
                        String stars = endLine4.split(" stars")[0];
                        int starsInt = Integer.parseInt(stars);
                        holder.mTextView4.setText(Line4.replace(endLine4, String.valueOf(starsInt - 1) + " stars"));
                    }
                }catch (Exception e) {
                    holder.mTextView4.setText("there was an error.");
                }
            }
        });


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String id = holder.mTextView3.getText().toString().split("#")[1];
                vibrate();
                holder.mImageViewStar.setVisibility(View.GONE);
                holder.mImageViewComments.setVisibility(View.GONE);
                try {
                        try {




                                    String received = StarsocketConnector.getReplyTo("downloadPlanById " + id).toString();

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



                        } catch (Exception ignored){

                        }
                } catch (Exception ignored) {

                }
            }
        });
    }


    @Override
    public int getItemCount() {
        return mFeedList.size();
    }
}
