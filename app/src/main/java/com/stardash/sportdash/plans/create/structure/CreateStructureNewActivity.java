package com.stardash.sportdash.plans.create.structure;

import static com.stardash.sportdash.plans.run.inspect.InspectActivity.inspectingPlan;
import static com.stardash.sportdash.settings.app.vibrate;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Layout;
import android.transition.Fade;
import android.transition.Transition;
import android.transition.TransitionManager;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.stardash.sportdash.MainActivity;
import com.stardash.sportdash.R;
import com.stardash.sportdash.me.leaderboard.LeaderboardAdapter;
import com.stardash.sportdash.me.leaderboard.LeaderboardItem;
import com.stardash.sportdash.me.leaderboard.leaderboard;
import com.stardash.sportdash.network.tcp.StarsocketConnector;
import com.stardash.sportdash.plans.create.CreatePlanActivity;
import com.stardash.sportdash.plans.run.ResultActivity;
import com.stardash.sportdash.settings.Account;
import com.stardash.sportdash.settings.SettingsActivity;

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
    static Boolean tappedItem = false;
    int elementsInPlan = 0;
    static public Boolean editItem;
    static public Boolean duplicateElement;
    String zeFormat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        inspectingPlan = false;
        if (tappedItem){
            overridePendingTransition(R.anim.fadein_fast, R.anim.fadeout_fast);
        }
        setContentView(R.layout.activity_create_structure_new);

        if (Account.isAmoled()) {
            ConstraintLayout main = findViewById(R.id.main);
            main.setBackgroundDrawable(new ColorDrawable(Color.BLACK));
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                window.setStatusBarColor(Color.BLACK);
            }
        }

        elementUseSeconds();
        buildPage();

        if (tappedItem){
            View elementView = findViewById(R.id.elementLayout);
            elementView.setVisibility(View.VISIBLE);
            tappedItem = false;

            EditText editTextName = findViewById(R.id.editTextName);
            EditText editTextDescription = findViewById(R.id.editTextDescription);
            EditText editTextTime = findViewById(R.id.editTextTime);

                SharedPreferences pref = this.getSharedPreferences("sport", 0); // 0 - for private mode
                String add = pref.getString(chosenId()+" name", "nOnE");
                String description = pref.getString(chosenId()+" description", "description");
                String seconds = pref.getString(chosenId()+" seconds", "0");
                editTextName.setText(add);
                editTextDescription.setText(description);
                editTextTime.setText(seconds);


                if(duplicateElement){
                    submitFinalElement();
                }

        }
        EditText editTextName = findViewById(R.id.editTextName);
        EditText editTextDescription = findViewById(R.id.editTextDescription);
        EditText editTextTime = findViewById(R.id.editTextTime);

        if (set_e){
            set_e = false;
            editTextName.setText(e_name);
            editTextDescription.setText(e_desc);
            editTextTime.setText(e_time);
            View elementView = findViewById(R.id.elementLayout);
            elementView.setVisibility(View.VISIBLE);
        }


    }

    private void buildPage() {
        SharedPreferences pref = this.getSharedPreferences("sport", 0); // 0 - for private mode


        String items = "";
        for (int i=1; i < 100; i++) {

            String add = pref.getString(i+" name", "nOnE");
            String format = pref.getString(i+" format", "seconds");
            String seconds = pref.getString(i+" seconds", "0");

            if (add.equals("nOnE")) {

            } else if (add.equals("S Ξ L Ξ C T")) {

            } else {
                    items+=i+".@"+add+"@"+seconds+" "+format+"\n";
                    elementsInPlan++;
            }

        }

        createList(items);


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
                mStructureList.add(new StructureItem(element.split("@")[0], element.split("@")[1], element.split("@")[2]));
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
        if (duplicateElement) {
            toast("cancelled duplicating");
        }
        duplicateElement = false;
        TextView textViewUpload = findViewById(R.id.upload);
        vibrate();
        if (elementsInPlan>99 ) {
            toast("100 elements is the maximum !");
        } else if(duplicateElement) {
            textViewUpload.setVisibility(View.VISIBLE);
            duplicateElement = false;
            zeFormat = "seconds";
            View elementView = findViewById(R.id.elementLayout);
            elementView.setVisibility(View.VISIBLE);
            editItem = false;

            EditText editTextName = findViewById(R.id.editTextName);
            EditText editTextDescription = findViewById(R.id.editTextDescription);
            EditText editTextTime = findViewById(R.id.editTextTime);

            editTextName.setText(e_name);
            editTextDescription.setText(e_desc);
            editTextTime.setText(e_time);
        } else {
            textViewUpload.setVisibility(View.VISIBLE);
            zeFormat = "seconds";
            View elementView = findViewById(R.id.elementLayout);
            elementView.setVisibility(View.VISIBLE);
            editItem = false;

            EditText editTextName = findViewById(R.id.editTextName);
            EditText editTextDescription = findViewById(R.id.editTextDescription);
            EditText editTextTime = findViewById(R.id.editTextTime);

            editTextName.setText("");
            editTextDescription.setText("");
            editTextTime.setText("");
        }
    }

    public void submitElement(View view) {
        vibrate();
       submitFinalElement();
    }

    public void cancelElement(View view) {
        vibrate();
        View elementView = findViewById(R.id.elementLayout);
        elementView.setVisibility(View.GONE);
        duplicateElement = false;
    }

    private void submitFinalElement() {
        EditText editTextName = findViewById(R.id.editTextName);
        EditText editTextDescription = findViewById(R.id.editTextDescription);
        EditText editTextSeconds = findViewById(R.id.editTextTime);
        TextView textViewFormat = findViewById(R.id.textViewSeconds);

        String name = editTextName.getText().toString();
        String description = editTextDescription.getText().toString();
        String seconds = editTextSeconds.getText().toString();

        if (name.length()<1){
            toast("set element name first. E.g. Push Up");
        } else if (description.length()<1){
            toast("set element description first");
        } else if (seconds.length()<1){
            toast("set element duration/iterations first");
        } else {

            SharedPreferences settings = getSharedPreferences("sport", MODE_PRIVATE);
            SharedPreferences.Editor editor = settings.edit();

            String id;
            if (duplicateElement){
                id = String.valueOf(elementsInPlan+1);
                duplicateElement = false;
            } else if (editItem) {
                id = Integer.toString(chosenId());
            } else {
                id = String.valueOf(elementsInPlan+1);
            }

            editor.putString(id + " name", name.toUpperCase(Locale.ROOT)).apply();
            editor.putString(id + " description", description).apply();
            editor.putString(id + " seconds", seconds).apply();
            editor.putString(id + " format", zeFormat).apply();

            ConstraintLayout constraintLayout = findViewById(R.id.elementLayout);
            constraintLayout.setVisibility(View.GONE);
            buildPage();
            mRecyclerView.scrollToPosition(mStructureList.size() - 1);
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
        zeFormat = "iterations";
        vibrate();
        TextView textViewIterations = findViewById(R.id.textViewIterations);
        TextView textViewSeconds = findViewById(R.id.textViewSeconds);
        textViewSeconds.setTextColor(Color.parseColor("#14FFEC"));
        textViewIterations.setTextColor(Color.parseColor("#E64868"));
    }

    public void useSeconds(View view) {
        vibrate();
        elementUseSeconds();
    }

    private void elementUseSeconds() {
        type = "seconds";
        zeFormat = "seconds";
        TextView textViewIterations = findViewById(R.id.textViewIterations);
        TextView textViewSeconds = findViewById(R.id.textViewSeconds);
        textViewSeconds.setTextColor(Color.parseColor("#E64868"));
        textViewIterations.setTextColor(Color.parseColor("#14FFEC"));
    }

    @Override
    public void onBackPressed() {
        vibrate();
        View elementView = findViewById(R.id.elementLayout);
        if (elementView.getVisibility() == View.VISIBLE) {
            elementView.setVisibility(View.GONE);
        } else {
            Intent i = new Intent(this, MainActivity.class);
            startActivity(i);
        }
    }

    public void searchElements(View view) {
        vibrate();
        EditText editTextSearch = findViewById(R.id.editTextSearch);
        String search = editTextSearch.getText().toString().toLowerCase(Locale.ROOT);;
        if (search.length()>200){
            toast("enter element name");
        } else {
            try {
                StarsocketConnector.sendMessage("searchElement ="+search);
                ((ProgressBar) findViewById(R.id.progressBar)).setVisibility(View.VISIBLE);
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
        }, 250);
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
        toast("uploading to server");
        TextView textViewUpload = findViewById(R.id.upload);

        EditText editTextName = findViewById(R.id.editTextName);
        EditText editTextDescription = findViewById(R.id.editTextDescription);
        EditText editTextTime = findViewById(R.id.editTextTime);
        String name = editTextName.getText().toString().replaceAll("@","").toLowerCase(Locale.ROOT);
        String desc = editTextDescription.getText().toString().replaceAll("@","");
        String time = editTextTime.getText().toString().replaceAll("@","");
        String id = Account.userid().replaceAll("@","");
        String username = Account.username().replaceAll("@","");
        String all = name+desc;

        if (name.length()<1){
            toast("add name*");
        } else if (desc.length()<1){
            toast("add desc*");
        } else if (time.length()<1) {
            toast("add time*");
        } else if (all.contains("http")||all.contains("bit.ly")||all.contains("www.")||all.contains("com")) {
            toast("links cant be uploaded");
        } else if (all.contains("sex")||all.contains("fuck")||all.contains("porn")||all.contains("moan")||all.contains("make love")||all.contains("dick")||all.contains("pussy")||all.contains("cock")||all.contains("jerk")||all.contains("horny")) {
            toast("kindly uninstall this app :((");
        } else {
            try {
                textViewUpload.setVisibility(View.GONE);
                ((ProgressBar) findViewById(R.id.progressBar)).setVisibility(View.VISIBLE);
                StarsocketConnector.sendMessage("addElement @" + name + "@" + desc + "@" + time + "@" + type + "@" + username + "@" + id + "@");
                final Handler handler = new Handler(Looper.getMainLooper());
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ((ProgressBar) findViewById(R.id.progressBar)).setVisibility(View.GONE);
                        toast("success");
                    }
                }, 1000);
            } catch (Exception e) {
                toast("no network");
                ((ProgressBar) findViewById(R.id.progressBar)).setVisibility(View.GONE);
            }
        }
    }

    public void finish(View view) {
        ConstraintLayout elementLayout = findViewById(R.id.elementLayout);
        if (elementLayout.getVisibility() == View.VISIBLE) return;
        if (elementsInPlan<5){
            toast("required elements: 5");
        } else {
            SharedPreferences settings = getSharedPreferences("sport", MODE_PRIVATE);
            SharedPreferences.Editor editor = settings.edit();

            editor.putBoolean("create", true).apply();
            editor.putBoolean("editing", true).apply();

            Intent i = new Intent(this, ResultActivity.class);
            startActivity(i);
        }
        vibrate();
    }

    public void discard(View view) {

        SharedPreferences settings = getSharedPreferences("sport", MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();

        for (int i = 0; i < 100; i++) {
            System.out.println("Yes");

            editor.putString(i+ " name","S Ξ L Ξ C T");
            editor.putString(i+ " description","");
            editor.putString(i+ " advice","");
            editor.putString(i+ " seconds","");
        }

        editor.apply();

        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
        vibrate();
    }

    public void duplicateItem(View view) {
        vibrate();
        toast("tap element to duplicate");
        duplicateElement = true;
    }

    public void scrollDown(View view) {
        vibrate();
        mRecyclerView.scrollToPosition(mStructureList.size() - 1);
    }
    public void scrollUp(View view) {
        vibrate();
        mRecyclerView.scrollToPosition(0);
    }

    public void doNothing(View view) {
    }
}