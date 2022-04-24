package com.stardash.sportdash.plans.create.structure;

import static com.stardash.sportdash.settings.app.vibrate;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Layout;
import android.transition.Fade;
import android.transition.Transition;
import android.transition.TransitionManager;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.stardash.sportdash.R;
import com.stardash.sportdash.me.leaderboard.LeaderboardAdapter;
import com.stardash.sportdash.me.leaderboard.LeaderboardItem;
import com.stardash.sportdash.me.leaderboard.leaderboard;
import com.stardash.sportdash.network.tcp.StarsocketConnector;
import com.stardash.sportdash.settings.Account;

import java.util.ArrayList;
import java.util.Locale;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class CreateStructureNewActivity extends AppCompatActivity {

    static String e_name = "";
    static String e_desc = "";
    static String e_time = "";
    static String e_type = "";
    static Boolean set_e = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_structure_new);
        elementUseSeconds();

        Runnable helloRunnable = new Runnable() {
            public void run() {
                EditText editTextName = findViewById(R.id.editTextName);
                EditText editTextDescription = findViewById(R.id.editTextDescription);
                EditText editTextTime = findViewById(R.id.editTextTime);

                if (set_e){
                    set_e = false;
                    editTextName.setText(e_name);
                    editTextDescription.setText(e_desc);
                    editTextTime.setText(e_time);
                   /* if (e_type.contains("itera")){
                        elementUseIterations();
                    } else {
                        elementUseSeconds();
                    }*/
                }
            }
        };

        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
        executor.scheduleAtFixedRate(helloRunnable, 0, 2, TimeUnit.SECONDS);
    }

    private ArrayList<StructureItem> mStructureList;
    private RecyclerView mRecyclerView;
    private StructureAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    public void createList(String list){
        String[] item = list.split("\n");

        mStructureList = new ArrayList<>();

        for (String element : item) {
            try {
                mStructureList.add(new StructureItem(element.split("@")[0], element.split("@")[2]+element.split("@")[3], element.split("@")[1]));
            } catch (Exception e) {

            }
        }

        buildRecyclerView();
    }

    public void buildRecyclerView(){
        mRecyclerView = findViewById(R.id.structure_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(CreateStructureNewActivity.this);
        mAdapter = new StructureAdapter(mStructureList);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
    }

    public void addElement(View view) {
        vibrate();

        View elementView = findViewById(R.id.elementLayout);
        elementView.setVisibility(View.VISIBLE);
    }

    public void submitElement(View view) {
        vibrate();
        View elementView = findViewById(R.id.elementLayout);
        elementView.setVisibility(View.GONE);


        EditText editTextName = findViewById(R.id.editTextName);
        EditText editTextDescription = findViewById(R.id.editTextDescription);
        EditText editTextSeconds = findViewById(R.id.editTextTime);
        TextView textViewFormat = findViewById(R.id.textViewSeconds);

        String name = editTextName.getText().toString();
        String description = editTextDescription.getText().toString();
        String seconds = editTextSeconds.getText().toString();
        String format = textViewFormat.getText().toString();

        if (name.length()<1){
            toast("set element name first. E.g. Push Up");
        } else if (description.length()<1){
            toast("set element description first");
        } else if (seconds.length()<1){
            toast("set element duration/iterations first");
        } else {

            SharedPreferences settings = getSharedPreferences("sport", MODE_PRIVATE);
            SharedPreferences.Editor editor = settings.edit();

            String id = Integer.toString(chosenId());

            editor.putString(id + " name", name.toUpperCase(Locale.ROOT)).apply();
            editor.putString(id + " description", description).apply();
            editor.putString(id + " seconds", seconds).apply();
            editor.putString(id + " format", format).apply();
        }
    }

    int chosenId() {
        SharedPreferences pref = this.getSharedPreferences("sport", 0); // 0 - for private mode
        int chosen = pref.getInt("chooseId", 0);
        return chosen;
    }

    static String type;
    public void useIterations(View view) {
        elementUseIterations();
    }

    private void elementUseIterations() {
        type = "iterations";
        vibrate();
        TextView textViewIterations = findViewById(R.id.textViewIterations);
        TextView textViewSeconds = findViewById(R.id.textViewSeconds);
        textViewIterations.setBackgroundResource(R.drawable.main_text_background_white);
        textViewIterations.setPadding(10,10,10,10);
        textViewSeconds.setBackgroundColor(Color.parseColor("#0eff00"));
    }

    public void useSeconds(View view) {
        vibrate();
        elementUseSeconds();
    }

    private void elementUseSeconds() {
        type = "seconds";
        TextView textViewIterations = findViewById(R.id.textViewIterations);
        TextView textViewSeconds = findViewById(R.id.textViewSeconds);
        textViewSeconds.setBackgroundResource(R.drawable.main_text_background_white);
        textViewSeconds.setPadding(10,10,10,10);
        textViewIterations.setBackgroundColor(Color.parseColor("#0eff00"));
    }

    public void searchElements(View view) {
        ((ProgressBar) findViewById(R.id.progressBar)).setVisibility(View.VISIBLE);
        vibrate();
        EditText editTextSearch = findViewById(R.id.editTextSearch);
        String search = editTextSearch.getText().toString();
        if (search.length()<1){
            toast("enter element name");
        } else {
            try {
                StarsocketConnector.sendMessage("searchElement ="+search);
                getSearchResult();
            } catch (Exception e){
                toast("no network");
            }

        }
    }

    private void getSearchResult() {
        final Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                String search_result = StarsocketConnector.getMessage().replaceAll("undefined","");
                processSearchResult(search_result);
            }

            private void processSearchResult(String res) {

                if (res.equals("no results")){
                    toast("no results");
                    ((ProgressBar) findViewById(R.id.progressBar)).setVisibility(View.GONE);
                } else if (res.length()<1) {
                    toast("no results");
                    ((ProgressBar) findViewById(R.id.progressBar)).setVisibility(View.GONE);
                } else {
                    createListSearch(res);
                }
            }
        }, 1000);
    }

    private ArrayList<ElementSearchItem> mElementSearchList;
    private RecyclerView mRecyclerViewElementSearch;
    private ElementSearchAdapter mAdapterElementSearch;
    private RecyclerView.LayoutManager mLayoutManagerElementSearch;

    public void createListSearch(String list){
        String[] item = list.split("\n");

        mElementSearchList = new ArrayList<>();

        for (String element : item) {
            String name = element.split("@")[0];
            String desc = element.split("@")[1];
            String duration = element.split("@")[2];
            String type = element.split("@")[3];
            String usage = element.split("@")[4];
            String reports = element.split("@")[5];
            String username = element.split("@")[6];
            String id = element.split("@")[7];
            String elementId = element.split("@")[8];
            try {
                mElementSearchList.add(new ElementSearchItem(name+", "+duration+" "+type,desc,"by "+username+" #"+id+"#"+elementId));
            } catch (Exception e) {

            }
        }

        buildRecyclerViewElementSearch();
    }

    public void buildRecyclerViewElementSearch(){
        mRecyclerViewElementSearch = findViewById(R.id.search_recycler_view);
        mRecyclerViewElementSearch.setHasFixedSize(true);
        mLayoutManagerElementSearch = new LinearLayoutManager(CreateStructureNewActivity.this);
        mAdapterElementSearch = new ElementSearchAdapter(mElementSearchList);
        mRecyclerViewElementSearch.setLayoutManager(mLayoutManagerElementSearch);
        mRecyclerViewElementSearch.setAdapter(mAdapterElementSearch);
        ((ProgressBar) findViewById(R.id.progressBar)).setVisibility(View.GONE);
    }

    public void toast(String message){
        TextView textViewCustomToast = findViewById(R.id.textViewCustomToast);
        textViewCustomToast.setVisibility(View.VISIBLE);
        textViewCustomToast.setText(Account.errorStyle()+" "+message);
        final Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                textViewCustomToast.setVisibility(View.GONE);
            }
        }, 3000);
    }

    public void uploadElement(View view) {
        vibrate();
        toast("uploading");
        TextView textViewUpload = findViewById(R.id.upload);
        textViewUpload.setVisibility(View.GONE);
        EditText editTextName = findViewById(R.id.editTextName);
        EditText editTextDescription = findViewById(R.id.editTextDescription);
        EditText editTextTime = findViewById(R.id.editTextTime);
        String name = editTextName.getText().toString();
        String desc = editTextDescription.getText().toString();
        String time = editTextTime.getText().toString();
        String id = Account.userid();
        String username = Account.username();

        if (name.length()<1){
            toast("add name*");
        } else if (desc.length()<1){
            toast("add desc*");
        } else if (time.length()<1){
            toast("add time*");
        } else {
            try {
                ((ProgressBar) findViewById(R.id.progressBar)).setVisibility(View.VISIBLE);
                StarsocketConnector.sendMessage("addElement @" + name + "@" + desc + "@" + time + "@" + type + "@" + username + "@" + id + "@");
                final Handler handler = new Handler(Looper.getMainLooper());
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ((ProgressBar) findViewById(R.id.progressBar)).setVisibility(View.GONE);
                        toast("success");
                    }
                }, 3000);
            } catch (Exception e) {
                toast("no network");
                ((ProgressBar) findViewById(R.id.progressBar)).setVisibility(View.GONE);
            }
        }
    }


}