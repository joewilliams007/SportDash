package com.stardash.sportdash.plans.comments;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.stardash.sportdash.settings.Account;
import com.stardash.sportdash.R;

import java.util.ArrayList;

public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.CommentsViewHolder> {

    private ArrayList<CommentsItem> mCommentsList;
    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }



    public static  class CommentsViewHolder extends RecyclerView.ViewHolder{

        public TextView mTextView1;
        public TextView mTextView2;
        public TextView mTextView3;

        public CommentsViewHolder(@NonNull View itemView) {
            super(itemView);
            mTextView1 = itemView.findViewById(R.id.textViewLine1);
            mTextView2 = itemView.findViewById(R.id.textViewLine2);
            mTextView3 = itemView.findViewById(R.id.textViewLine3);
        }
    }

    public CommentsAdapter(ArrayList<CommentsItem> CommentsList){
        mCommentsList = CommentsList;
    }

    @NonNull
    @Override
    public CommentsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.comments_item,parent, false);
        CommentsViewHolder evh = new CommentsViewHolder(v);
        return evh;
    }

    @Override
    public void onBindViewHolder(@NonNull CommentsViewHolder holder, int position) {
        CommentsItem currentItem = mCommentsList.get(position);

        if (currentItem.getText1().split(" ")[0].equals(Account.username())) {
            holder.mTextView1.setTextColor(Color.parseColor("#14FFEC"));
        }
        holder.mTextView1.setText(currentItem.getText1());
        holder.mTextView2.setText(currentItem.getText2());
        holder.mTextView3.setText(currentItem.getText3());
    }

    @Override
    public int getItemCount() {
        return mCommentsList.size();
    }
}
