package com.stardash.sportdash.plans.create.structure;

import static android.content.Context.MODE_PRIVATE;
import static com.stardash.sportdash.online.friends.FriendsActivity.tappedOnSearchItem;
import static com.stardash.sportdash.plans.create.structure.CreateStructureNewActivity.duplicateElement;
import static com.stardash.sportdash.plans.create.structure.CreateStructureNewActivity.e_desc;
import static com.stardash.sportdash.plans.create.structure.CreateStructureNewActivity.e_name;
import static com.stardash.sportdash.plans.create.structure.CreateStructureNewActivity.e_time;
import static com.stardash.sportdash.plans.create.structure.CreateStructureNewActivity.editItem;
import static com.stardash.sportdash.plans.create.structure.CreateStructureNewActivity.tappedItem;
import static com.stardash.sportdash.plans.run.inspect.InspectActivity.inspectingPlan;
import static com.stardash.sportdash.settings.app.vibrate;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.stardash.sportdash.online.friends.FriendsActivity;
import com.stardash.sportdash.settings.Account;
import com.stardash.sportdash.settings.MyApplication;
import com.stardash.sportdash.settings.account.AccountActivity;

import java.util.ArrayList;
import java.util.Locale;

public class StructureAdapter extends RecyclerView.Adapter<StructureAdapter.StructureViewHolder> {

    private ArrayList<StructureItem> mStructureList;
    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }



    public static  class StructureViewHolder extends RecyclerView.ViewHolder{

        public View mView1;
        public TextView mTextView1;
        public TextView mTextView2;
        public TextView mTextView3;
        public TextView mTextView4;
        public TextView mTextView5;
        public TextView mTextView6;

        public StructureViewHolder(@NonNull View itemView) {
            super(itemView);
            mTextView1 = itemView.findViewById(R.id.textViewLine1);
            mTextView2 = itemView.findViewById(R.id.textViewLine2);
            mTextView3 = itemView.findViewById(R.id.textViewLine3);
            mTextView4 = itemView.findViewById(R.id.textViewLine4);
            mTextView5 = itemView.findViewById(R.id.textViewLine5);
            mTextView6 = itemView.findViewById(R.id.textViewLine6);
            mView1 = itemView.findViewById(R.id.view);
        }
    }

    public StructureAdapter(ArrayList<StructureItem> structureList){
        mStructureList = structureList;
    }

    @NonNull
    @Override
    public StructureViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.element_item,parent, false);
        return new StructureViewHolder(v);
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

        holder.mTextView4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences settings = MyApplication.getAppContext().getSharedPreferences("sport", MODE_PRIVATE);
                SharedPreferences.Editor editor = settings.edit();
                editor.putInt("chooseId", Integer.parseInt(holder.mTextView1.getText().toString().replace(".", ""))).apply();
                tappedItem = true;
                editItem = true;
                try {
                    vibrate();
                    Intent i = new Intent(MyApplication.getAppContext(), CreateStructureNewActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    MyApplication.getAppContext().startActivity(i);
                } catch (Exception ignored) {

                }
            }
        });
        holder.mTextView5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                duplicateElement = true;
                vibrate();
                SharedPreferences settings = MyApplication.getAppContext().getSharedPreferences("sport", MODE_PRIVATE);
                SharedPreferences.Editor editor = settings.edit();
                int chosenID = Integer.parseInt(holder.mTextView1.getText().toString().replace(".", ""));
                editor.putInt("chooseId", chosenID).apply();
                tappedItem = true;
                editItem = true;

                String name = settings.getString(chosenID+" name", "");
                String description = settings.getString(chosenID+" description", "");
                String seconds = settings.getString(chosenID+" seconds", "0");

                e_desc = description;
                e_name = name;
                e_time = seconds;

                try {
                    Intent i = new Intent(MyApplication.getAppContext(), CreateStructureNewActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    MyApplication.getAppContext().startActivity(i);
                } catch (Exception ignored) {

                }
            }
        });


        holder.mTextView6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vibrate();
                holder.mTextView4.setVisibility(View.GONE);
                holder.mTextView5.setVisibility(View.GONE);
                holder.mTextView6.setVisibility(View.GONE);
                holder.mView1.setVisibility(View.GONE);
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vibrate();
                if (!inspectingPlan) {
                    if (holder.mTextView4.getVisibility() == View.GONE) {
                        holder.mTextView4.setVisibility(View.VISIBLE);
                        holder.mTextView5.setVisibility(View.VISIBLE);
                        holder.mTextView6.setVisibility(View.VISIBLE);
                        holder.mView1.setVisibility(View.VISIBLE);
                    } else {
                        holder.mTextView4.setVisibility(View.GONE);
                        holder.mTextView5.setVisibility(View.GONE);
                        holder.mTextView6.setVisibility(View.GONE);
                        holder.mView1.setVisibility(View.GONE);
                    }
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return mStructureList.size();
    }
}
