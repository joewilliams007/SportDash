package com.stardash.sportdash.signIn;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.stardash.sportdash.settings.Account;
import com.stardash.sportdash.R;
import com.stardash.sportdash.network.tcp.StarsocketConnector;
import com.stardash.sportdash.settings.TermsOfServiceActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
    }

    public void openLogin(View view) {
        vibrate();
        Intent i = new Intent(this, LoginActivity.class);
        startActivity(i);
    }

    public void next(View view) {
        ((ProgressBar) findViewById(R.id.progressBar)).setVisibility(View.VISIBLE);
        vibrate();
        sendRegistrationToServer();
    }

    private void sendRegistrationToServer() {
        try {
        EditText editTextUsername = findViewById(R.id.editTextTextPersonNameUsername);
        EditText editTextEmail = findViewById(R.id.editTextTextPersonNameEmail);
        EditText editTextPassword = findViewById(R.id.editTextTextPersonNamePassword);
        String username = editTextUsername.getText().toString();
        String email = editTextEmail.getText().toString();
        String password = editTextPassword.getText().toString();

        ((ProgressBar) findViewById(R.id.progressBar)).setVisibility(View.GONE);

            if (username.length()<3) {
            toast("username must be at least 3 characters");
        } else if (password.length()<5){
            toast("password must be at least 5 characters long");
        } else if (email.length()<4){
            toast("invalid email");
        } else if (!email.contains("@")) {
            toast("invalid email");
        } else if (!email.contains(".")) {
            toast("invalid email");
        } else {
                ((ProgressBar) findViewById(R.id.progressBar)).setVisibility(View.VISIBLE);
                try {
                TextView textViewNext = findViewById(R.id.textViewNext);
                textViewNext.setVisibility(View.GONE);
                final Handler handler = new Handler(Looper.getMainLooper());
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        StarsocketConnector.sendMessage("register " + username + " " + password + " " + email);
                        getIdFromServer();
                        continueIt();
                        ((ProgressBar) findViewById(R.id.progressBar)).setVisibility(View.GONE);

                    }
                }, 3000);

            } catch (Exception e){
                toast("network error");
                TextView textViewNext = findViewById(R.id.textViewNext);
                textViewNext.setVisibility(View.VISIBLE);
            }
        }
    } catch (Exception e){
        toast("unknown error");
    }
    }

    private void continueIt() {
        Intent i = new Intent(this, AfterRegister.class);
        startActivity(i);
    }

    private void getIdFromServer() {
        EditText editTextUsername = findViewById(R.id.editTextTextPersonNameUsername);
        String username = editTextUsername.getText().toString().replace(" ","");

        String received = StarsocketConnector.getMessage();
        String received_username = received.split(" ",2)[0];
        String received_id = received.split(" ",2)[1];

        if(received.contains("invalid ip")){
            getIdFromServer();
            toast("got an invalid ip, retrying");
        } else if (received_username.equals(username)) {
            createPreferences(received_id);
        } else {
            toast("unknown error");
            getIdFromServer();
        }
    }

    public void createPreferences(String id){

        EditText editTextUsername = findViewById(R.id.editTextTextPersonNameUsername);
        EditText editTextEmail = findViewById(R.id.editTextTextPersonNameEmail);
        EditText editTextPassword = findViewById(R.id.editTextTextPersonNamePassword);
        String username = editTextUsername.getText().toString().replace(" ","");
        String email = editTextEmail.getText().toString().replace(" ","");
        String password = editTextPassword.getText().toString().replace(" ","");

        Account.setUsername(username);
        Account.setId(id);
        Account.setAge(0);
        Account.setWeight(0);
        Account.setEmail(email);
        Account.setCoins(10);
        Account.setEnergy(5);
        Account.setXp(0);
        Account.setPassword(password);
        Account.setTodayProgress(0);
        Account.setWeekProgress(0);
        Account.setErrorStyle(">_<");
        Account.setLoggedIn(true);

        Date cDate = new Date();
        String fDate = new SimpleDateFormat("yyyy-MM-dd").format(cDate);
        Account.setDay(fDate);

        Date d1 = new Date();
        Calendar cl = Calendar.getInstance();
        cl.setTime(d1);
        Account.setWeek(String.valueOf(cl.WEEK_OF_YEAR));
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

    @Override
    public void onBackPressed() {
        toast("exit ?");
        vibrate();
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

    private void vibrate() {
        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            v.vibrate(VibrationEffect.createOneShot(100, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            v.vibrate(100);
        }
    }
}