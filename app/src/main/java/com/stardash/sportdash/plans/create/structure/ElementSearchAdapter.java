package com.stardash.sportdash.plans.create.structure;

import static com.stardash.sportdash.plans.create.structure.CreateStructureNewActivity.e_desc;
import static com.stardash.sportdash.plans.create.structure.CreateStructureNewActivity.e_name;
import static com.stardash.sportdash.plans.create.structure.CreateStructureNewActivity.e_time;
import static com.stardash.sportdash.plans.create.structure.CreateStructureNewActivity.e_type;
import static com.stardash.sportdash.plans.create.structure.CreateStructureNewActivity.editItem;
import static com.stardash.sportdash.plans.create.structure.CreateStructureNewActivity.set_e;
import static com.stardash.sportdash.plans.create.structure.CreateStructureNewActivity.tappedItem;
import static com.stardash.sportdash.settings.app.vibrate;

import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.stardash.sportdash.R;
import com.stardash.sportdash.network.tcp.StarsocketConnector;
import com.stardash.sportdash.online.ProfileActivity;
import com.stardash.sportdash.settings.Account;
import com.stardash.sportdash.settings.MyApplication;

import java.util.ArrayList;

public class ElementSearchAdapter extends RecyclerView.Adapter<ElementSearchAdapter.ElementSearchViewHolder> {

    private ArrayList<ElementSearchItem> mElementSearchList;
    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }



    public static  class ElementSearchViewHolder extends RecyclerView.ViewHolder{

        public TextView mTextView1;
        public TextView mTextView2;
        public TextView mTextView3;

        public ElementSearchViewHolder(@NonNull View itemView) {
            super(itemView);
            mTextView1 = itemView.findViewById(R.id.textViewLine1);
            mTextView2 = itemView.findViewById(R.id.textViewLine2);
            mTextView3 = itemView.findViewById(R.id.textViewLine3);
        }
    }

    public ElementSearchAdapter(ArrayList<ElementSearchItem> elementSearchList){
        mElementSearchList = elementSearchList;
    }

    @NonNull
    @Override
    public ElementSearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.element_search_item,parent, false);
        ElementSearchViewHolder evh = new ElementSearchViewHolder(v);
        return evh;
    }

    @Override
    public void onBindViewHolder(@NonNull ElementSearchViewHolder holder, int position) {
        ElementSearchItem currentItem = mElementSearchList.get(position);

        holder.mTextView1.setText(currentItem.getText1());
        holder.mTextView2.setText(currentItem.getText2());
        holder.mTextView3.setText(currentItem.getText3());

        String id = holder.mTextView3.getText().toString().split("#")[1];
        String element_id = holder.mTextView3.getText().toString().split("#")[2];

        if (id.equals(Account.userid())){
            holder.mTextView3.setText("you made this element!\ntap to delete from server #"+element_id);
            holder.mTextView3.setTextColor(Color.parseColor("#E64868"));
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    vibrate();
                    e_name = currentItem.getText1().split(",")[0];
                    e_desc = currentItem.getText2();
                    e_time = currentItem.getText1().split(",")[1].split(" ")[1].replaceAll(" ","");
                    e_type = currentItem.getText1().split(",")[1];
                    set_e = true;


                        vibrate();
                        Intent i = new Intent(MyApplication.getAppContext(), CreateStructureNewActivity.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        MyApplication.getAppContext().startActivity(i);

                } catch (Exception e) {

                }
            }
        });


        holder.mTextView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vibrate();
                if (id.equals(Account.userid())){
                    try {
                      StarsocketConnector.sendMessage("deleteElement " + element_id);
                        vibrate();
                        String deleted = "deleted element";
                        holder.mTextView1.setText(deleted);
                        holder.mTextView2.setText(deleted);
                        holder.mTextView3.setText(deleted);
                    } catch (Exception e) {
                        String deleted = "error deleting";
                        holder.mTextView1.setText(deleted);
                        holder.mTextView2.setText(deleted);
                        holder.mTextView3.setText(deleted);
                    }
                } else {
                    try {
                        String id = holder.mTextView3.getText().toString().split("#")[1];
                        StarsocketConnector.sendMessage("getProfile " + id);
                        Intent i = new Intent(MyApplication.getAppContext(), ProfileActivity.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        MyApplication.getAppContext().startActivity(i);
                    } catch (Exception e) {

                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mElementSearchList.size();
    }
}
