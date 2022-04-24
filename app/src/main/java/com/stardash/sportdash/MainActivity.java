package com.stardash.sportdash;

import static com.stardash.sportdash.online.ProfileActivity.invalidId;
import static com.stardash.sportdash.online.chat.ChatActivity.isInChat;
import static com.stardash.sportdash.online.friends.FriendsActivity.tappedOnSearchItem;
import static com.stardash.sportdash.plans.run.RunPlanActivity.isRandom;
import static com.stardash.sportdash.settings.app.vibrate;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.StrictMode;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.stardash.sportdash.network.api.Methods;
import com.stardash.sportdash.network.api.Model;
import com.stardash.sportdash.network.api.RetrofitClient;
import com.stardash.sportdash.network.tcp.StarsocketConnector;
import com.stardash.sportdash.online.friends.FriendsActivity;
import com.stardash.sportdash.online.chat.ChatActivity;
import com.stardash.sportdash.online.chat.InboxActivity;
import com.stardash.sportdash.me.leaderboard.leaderboard;
import com.stardash.sportdash.online.shop.ShopActivity;
import com.stardash.sportdash.plans.run.PlanActivity;
import com.stardash.sportdash.plans.run.ResultActivity;
import com.stardash.sportdash.plans.run.RunPlanActivity;
import com.stardash.sportdash.plans.run.SearchPlanActivity;
import com.stardash.sportdash.settings.AboutActivity;
import com.stardash.sportdash.settings.Account;
import com.stardash.sportdash.settings.SettingsActivity;
import com.stardash.sportdash.signIn.LoginActivity;
import com.stardash.sportdash.signIn.RegisterActivity;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ChatActivity.updateChat = false;
        isInChat = false;
        tappedOnSearchItem = false;

        try {
            StarsocketConnector.sendMessage("boost");
        } catch (Exception e){

        }
        setProfilePicture(); // set picture
        Account.setAddingFriend(false); // so that if you canceled adding friends it knows but you have no friends [...] :)
        checkDeepLink(); // check if app was opened with a link
        checkServerStatus(); // is server online make github request
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        if (Account.loggedIn()){
            try {
                setUserStats(); // set user progress
                Account.getXp(1);
            } catch (Exception e){

            }

            try {
                StarsocketConnector.sendMessage("connecting "+" #"+Account.userid());
            } catch (Exception e){
                toast("no network");
            }
        } else {
            Intent i = new Intent(this, RegisterActivity.class);
            startActivity(i);
        }

        if (Account.isAmoled()) {
            ConstraintLayout main = findViewById(R.id.main);
            main.setBackgroundDrawable(new ColorDrawable(Color.BLACK));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Window window = getWindow();
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(Color.BLACK);

                TextView textViewId = findViewById(R.id.textViewId);
                TextView textViewLevel = findViewById(R.id.textViewLevel);
                textViewId.setTextColor(Color.parseColor("#000000"));
                textViewLevel.setTextColor(Color.parseColor("#000000"));
            }
        }

        getInbox();
        isNewLogin();
        isShopNew();

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void isShopNew() {
        TextView textViewNew = findViewById(R.id.textViewNew);
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("uuuu/MM/dd");
        LocalDate localDate = LocalDate.now();

        if (dtf.format(localDate).equals(Account.isShopNew())) {
            textViewNew.setVisibility(View.GONE);
        } else {
            textViewNew.setVisibility(View.VISIBLE);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void isNewLogin() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("uuuu/MM/dd");
        LocalDate localDate = LocalDate.now();
        if (dtf.format(localDate).equals(Account.rewardDay())){

        } else {
            TextView textViewPopup = findViewById(R.id.textViewPopUp);
            TextView textViewClose = findViewById(R.id.textViewX);
            textViewPopup.setVisibility(View.VISIBLE);
            textViewClose.setVisibility(View.VISIBLE);

            Account.setRewardDay(dtf.format(localDate));
        }
    }

    private void getInbox() {
        TextView textView = findViewById(R.id.textViewInbox);
        try {
            StarsocketConnector.sendMessage("mychatinbox " + Account.userid());

            final Handler handler = new Handler(Looper.getMainLooper());
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    String inbox = StarsocketConnector.getMessage();
                    if (inbox.equals("invalid_ip")){
                        getInbox();
                    } else if (inbox.length()>12){
                        textView.setVisibility(View.VISIBLE);

                        char someChar = '#';
                        int count = 0;

                        for (int i = 0; i < inbox.length(); i++) {
                            if (inbox.charAt(i) == someChar) {
                                count++;
                            }
                        }
                        textView.setText(" "+String.valueOf(count)+" ");
                    }
                }
            }, 500);

        } catch (Exception e){

        }
    }
    public void checkServerStatus() {
        try {
            Methods methods = RetrofitClient.getRetrofitInstance().create(Methods.class);
            String clickName = "https://joewilliams007.github.io/jsonapi/adress.json";
            Call<Model> call = methods.getAllData(clickName);
            call.enqueue(new Callback<Model>() {
                @Override
                public void onResponse(Call<Model> call, Response<Model> response) {

                    String build = response.body().getBuild();
                    String version = response.body().getVersion();
                    String updateInfo = response.body().getUpdateInfo();
                    String ip = response.body().getBuild();
                    String status = response.body().getStatus();

                    if (status.equals("online")) {

                    } else {
                        toast("server is under maintenance");
                    }
                }

                @Override
                public void onFailure(Call<Model> call, Throwable t) {

                }
            });
        } catch (Exception e){

        }
    }

    @SuppressLint("SetTextI18n")
    private void setUserStats() {
        ProgressBar progressBarToday = findViewById(R.id.progressBar);
        ProgressBar progressBarWeek = findViewById(R.id.progressBarWeek);
        TextView textViewTodayAmount = findViewById(R.id.textViewTodayAmount);
        TextView textViewWeekAmount = findViewById(R.id.textViewWeekAmount);
        TextView textViewLevel = findViewById(R.id.textViewLevel);
        TextView textViewId = findViewById(R.id.textViewId);
        TextView textViewUsername = findViewById(R.id.textViewDetail);
        ImageView imageViewEnergy1 = findViewById(R.id.imageViewEnergy1);
        ImageView imageViewEnergy2 = findViewById(R.id.imageViewEnergy2);
        ImageView imageViewEnergy3 = findViewById(R.id.imageViewEnergy3);
        ImageView imageViewEnergy4 = findViewById(R.id.imageViewEnergy4);
        ImageView imageViewEnergy5 = findViewById(R.id.imageViewEnergy5);

        if (Account.energy() == 1) {
            imageViewEnergy1.setVisibility(View.VISIBLE);
            imageViewEnergy2.setVisibility(View.INVISIBLE);
            imageViewEnergy3.setVisibility(View.INVISIBLE);
            imageViewEnergy4.setVisibility(View.INVISIBLE);
            imageViewEnergy5.setVisibility(View.INVISIBLE);
        } else if (Account.energy() == 2) {
            imageViewEnergy1.setVisibility(View.VISIBLE);
            imageViewEnergy2.setVisibility(View.VISIBLE);
            imageViewEnergy3.setVisibility(View.INVISIBLE);
            imageViewEnergy4.setVisibility(View.INVISIBLE);
            imageViewEnergy5.setVisibility(View.INVISIBLE);
        } else if (Account.energy() == 3) {
            imageViewEnergy1.setVisibility(View.VISIBLE);
            imageViewEnergy2.setVisibility(View.VISIBLE);
            imageViewEnergy3.setVisibility(View.VISIBLE);
            imageViewEnergy4.setVisibility(View.INVISIBLE);
            imageViewEnergy5.setVisibility(View.INVISIBLE);
        } else if (Account.energy() == 4) {
            imageViewEnergy1.setVisibility(View.VISIBLE);
            imageViewEnergy2.setVisibility(View.VISIBLE);
            imageViewEnergy3.setVisibility(View.VISIBLE);
            imageViewEnergy4.setVisibility(View.VISIBLE);
            imageViewEnergy5.setVisibility(View.INVISIBLE);
        } else if (Account.energy() == 5) {
            imageViewEnergy1.setVisibility(View.VISIBLE);
            imageViewEnergy2.setVisibility(View.VISIBLE);
            imageViewEnergy3.setVisibility(View.VISIBLE);
            imageViewEnergy4.setVisibility(View.VISIBLE);
            imageViewEnergy5.setVisibility(View.VISIBLE);
        }

        textViewLevel.setText("level "+String.valueOf(Account.level())+"\ntotal xp "+Account.xp());
        textViewId.setText("#"+Account.userid());
        textViewUsername.setText(Account.username().toUpperCase(Locale.ROOT).replace(""," "));

        int todayProgress = Account.TodayProgress();
        int weekProgress = Account.WeekProgress();

        ObjectAnimator.ofInt(progressBarToday, "progress", todayProgress)
                .setDuration(600)
                .start();
        ObjectAnimator.ofInt(progressBarWeek, "progress", weekProgress)
                .setDuration(600)
                .start();
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                textViewTodayAmount.setText(String.valueOf(todayProgress)+"/10.000");
                textViewWeekAmount.setText(String.valueOf(weekProgress)+"/70.000");
            }
        }, 599);



    }

    public void onResume() {
        super.onResume();
        checkDeepLink();
    }



    private void checkDeepLink(){
        Intent intent = getIntent();
        String action = intent.getAction();
        Uri data = intent.getData();

        if (data == null){

        } else {
            if (data.toString().contains("user=")) {
                Intent i = new Intent(this, FriendsActivity.class);
                i.putExtra("friendHashtag", data.toString());
                startActivity(i);
            } else if (data.toString().contains("plan=")) {
                Intent i = new Intent(this, SearchPlanActivity.class);
                i.putExtra("planHashtag", data.toString());
                startActivity(i);
            }
        }
    }

    private void setProfilePicture() {
        String picturePath = PreferenceManager.getDefaultSharedPreferences(this).getString("picturePath", "");
        if(!picturePath.equals(""))
        {
            ImageView imageView = (ImageView) findViewById(R.id.imageViewSmallIcon);
            imageView.setImageBitmap(BitmapFactory.decodeFile(picturePath));
        }
    }

    public void openSettings(View view) {
        vibrate();

        Intent i = new Intent(this, SettingsActivity.class);
        startActivity(i);
    }

    public void openFriends(View view) {
        Account.setAddingFriend(false);
        SharedPreferences account = getSharedPreferences("account", MODE_PRIVATE);
        boolean isRegister = account.getBoolean("registration", true);
        if (isRegister) {
            Intent i = new Intent(this, LoginActivity.class);
            startActivity(i);
        }
    }

    public void addResult(View view) {
        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            v.vibrate(VibrationEffect.createOneShot(100, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            v.vibrate(100);
        }

        SharedPreferences settings = getSharedPreferences("sport", MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean("create", false).commit();

        Intent i = new Intent(this, ResultActivity.class);
        startActivity(i);
    }

    public void openShop(View view) {
        // toast("shop will be opened soon");
        Intent i = new Intent(this, ShopActivity.class);
        startActivity(i);
        vibrate();
    }

    public void openInfo(View view) {
        Intent i = new Intent(this, AboutActivity.class);
        startActivity(i);
    }

    public void openMyPlans(View view) {
        vibrate();
        SharedPreferences settings = getSharedPreferences("sport", MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean("create", false).commit();
        Intent i = new Intent(this, PlanActivity.class);
        startActivity(i);
    }

    public void closeApp(View view) {
        moveTaskToBack(true);
    }
    @Override
    public void onBackPressed() {
        showOptionsAction();
    }

    private void showOptionsAction() {
        TextView textViewSportDash = findViewById(R.id.textViewTop);
        TextView textViewClose = findViewById(R.id.textViewTopClose);

        if (textViewSportDash.getVisibility() == View.VISIBLE){
            textViewSportDash.setVisibility(View.INVISIBLE);
            textViewClose.setVisibility(View.VISIBLE);
        } else {
            textViewSportDash.setVisibility(View.VISIBLE);
            textViewClose.setVisibility(View.GONE);
        }
    }

    public void openFriendsList(View view) {
        vibrate();
        invalidId = false;
        Intent i = new Intent(this, FriendsActivity.class);
        startActivity(i);
    }

    public void openAchievements(View view) {
        vibrate();
        toast("available soon");
        //Intent i = new Intent(this, AchievementsActivity.class);
        //startActivity(i);
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


    public void openClub(View view) {
        toast("clubs will be available soon");
        vibrate();
    }

    public void openInbox(View view) {
        Intent i = new Intent(this, InboxActivity.class);
        startActivity(i);
        vibrate();
    }

    public void openLog(View view) {
        vibrate();
        toast("log will be available soon");
    }

    public void openLeaderboard(View view) {
        Intent i = new Intent(this, leaderboard.class);
        startActivity(i);
        vibrate();
    }

    public void randomPlan(View view) {
        isRandom = true;
        PlanActivity.isMyPlan = false;
        Intent i = new Intent(this, RunPlanActivity.class);
        startActivity(i);
    }

    public void closePopUp(View view) {
        TextView textViewPopup = findViewById(R.id.textViewPopUp);
        TextView textViewClose = findViewById(R.id.textViewX);
        textViewPopup.setVisibility(View.GONE);
        textViewClose.setVisibility(View.GONE);
        vibrate();
    }

    public void doNothing(View view) {
    }
}