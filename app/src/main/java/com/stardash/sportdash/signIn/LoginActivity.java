package com.stardash.sportdash.signIn;

import static com.stardash.sportdash.settings.app.vibrate;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.StrictMode;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.stardash.sportdash.settings.Account;
import com.stardash.sportdash.MainActivity;
import com.stardash.sportdash.R;
import com.stardash.sportdash.network.tcp.StarsocketConnector;
import com.stardash.sportdash.settings.TermsOfServiceActivity;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
    }

    public void openRegister(View view) {
        vibrate();
        Intent i = new Intent(this, RegisterActivity.class);
        startActivity(i);
    }

    public void next(View view) {

        vibrate();
        EditText editTextId = findViewById(R.id.editTextTextPersonNameId);
        EditText editTextPassword = findViewById(R.id.editTextTextPersonNamePassword);
        String id = editTextId.getText().toString().replace(" ","");
        String password = editTextPassword.getText().toString().replace(" ","");

        if (id.length()<1) {
            toast("invalid user id");
        } else if (password.length()<5){
            toast("your password must been at least 5 characters long");
        } else {
            try {
                ((ProgressBar) findViewById(R.id.progressBar)).setVisibility(View.VISIBLE);
                TextView textViewNext = findViewById(R.id.textViewNext);
                textViewNext.setVisibility(View.GONE);
                StarsocketConnector.sendMessage("login " + id + " " + password);
                final Handler handler = new Handler(Looper.getMainLooper());
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getLoginsFromServer();
                    }
                }, 3000);

                Account.setLoggedIn(true);
            } catch (Exception e){
                toast("network error");
            }
        }

    }

    private void getLoginsFromServer() {
        EditText editTextId = findViewById(R.id.editTextTextPersonNameId);
        EditText editTextPassword = findViewById(R.id.editTextTextPersonNamePassword);
        String id = editTextId.getText().toString().replace(" ","");
        String password = editTextPassword.getText().toString().replace(" ","");

        String received = StarsocketConnector.getMessage().split("\n",100)[0];

        if(received.equals("WRONG")) {
            toast("wrong password or id");
        } else {

            String received_id = received.split("%SPORTDASH%", 13)[1];
            String received_password = received.split("%SPORTDASH%", 13)[2];

            String received_username = received.split("%SPORTDASH%", 13)[3];

            String received_xp = received.split("%SPORTDASH%", 13)[4];
            if (received_xp.equals("null")) {
                received_xp = "0";
            }
            String received_age = received.split("%SPORTDASH%", 13)[5];
            if (received_age.equals("null")) {
                received_age = "0";
            }
            String received_weight = received.split("%SPORTDASH%", 13)[6];
            if (received_weight.equals("null")) {
                received_weight = "0";
            }
            String received_coins = received.split("%SPORTDASH%", 13)[7];
            if (received_coins.equals("null")) {
                received_coins = "0";
            }


            if (received_password.equals(password) && received_id.equals(id)) {

                Account.setUsername(received_username);

                Account.setId(received_id);
                Account.setXp(Integer.parseInt(received_xp));
                Account.setAge(Integer.parseInt(received_age));
                Account.setWeight(Integer.parseInt(received_weight));
                Account.setCoins(Integer.parseInt(received_coins));

                Account.setTodayProgress(0);
                Account.setWeekProgress(0);

                Date cDate = new Date();
                String fDate = new SimpleDateFormat("yyyy-MM-dd").format(cDate);
                Account.setDay(fDate);

                Date d1 = new Date();
                Calendar cl = Calendar.getInstance();
                cl.setTime(d1);
                Account.setWeek(String.valueOf(cl.WEEK_OF_YEAR));

                downloadPlans(received_id);
                Intent i = new Intent(this, MainActivity.class);
                startActivity(i);
            } else {
                toast("unknown error");
                getLoginsFromServer();
            }
            // toast(received);
        }
        TextView textViewNext = findViewById(R.id.textViewNext);
        textViewNext.setVisibility(View.VISIBLE);
        ((ProgressBar) findViewById(R.id.progressBar)).setVisibility(View.GONE);
    }

    public void downloadPlans(String id) {
        vibrate();
        try {
            toast("downloading plans ...");
            StarsocketConnector.sendMessage("downloadPlans " + id);
            String received_plans = StarsocketConnector.getMessage().toString();

            SharedPreferences settings = getSharedPreferences("sport", MODE_PRIVATE);
            SharedPreferences.Editor editor = settings.edit();

            String plan1 = received_plans.split("##########", 6)[1];
            String plan2 = received_plans.split("##########", 6)[2];
            String plan3 = received_plans.split("##########", 6)[3];
            String plan4 = received_plans.split("##########", 6)[4];
            String plan5 = received_plans.split("##########", 6)[5];

            editor.putString("1 plan", String.valueOf(plan1)).apply();
            editor.putString("2 plan", String.valueOf(plan2)).apply();
            editor.putString("3 plan", String.valueOf(plan3)).apply();
            editor.putString("4 plan", String.valueOf(plan4)).apply();
            editor.putString("5 plan", String.valueOf(plan5)).apply();
        } catch (Exception e){
            toast("could not download plans");
        }
    }


    public void toast(String message){
        TextView textViewCustomToast = findViewById(R.id.textViewCustomToast);
        textViewCustomToast.setVisibility(View.VISIBLE);
        textViewCustomToast.setText(">_< "+message);
        final Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                textViewCustomToast.setVisibility(View.GONE);
            }
        }, 3000);
    }

    public void skip(View view) {
        vibrate();
        Account.setLoggedIn(true);
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }

    public void openTermsOfService(View view) {
        vibrate();
        Intent i = new Intent(this, TermsOfServiceActivity.class);
        startActivity(i);
    }

    public void showPassword(View view) {
        vibrate();
        EditText editTextPassword = findViewById(R.id.editTextTextPersonNamePassword);
        ImageView imageViewEye = findViewById(R.id.imageViewEye);
        imageViewEye.setVisibility(View.GONE);
        editTextPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PERSON_NAME);
        final Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                editTextPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                imageViewEye.setVisibility(View.VISIBLE);
            }
        }, 3000);
    }


}