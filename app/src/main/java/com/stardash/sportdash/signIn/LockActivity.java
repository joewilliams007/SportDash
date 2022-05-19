package com.stardash.sportdash.signIn;

import static com.stardash.sportdash.MainActivity.loggedIn;
import static com.stardash.sportdash.online.chat.ChatActivity.isInChat;
import static com.stardash.sportdash.online.friends.FriendsActivity.openedChat;
import static com.stardash.sportdash.settings.account.AppLockSettingsActivity.changeLock;
import static com.stardash.sportdash.settings.app.vibrate;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.TextView;

import com.stardash.sportdash.MainActivity;
import com.stardash.sportdash.R;
import com.stardash.sportdash.online.chat.ChatActivity;
import com.stardash.sportdash.online.friends.FriendsActivity;
import com.stardash.sportdash.settings.Account;
import com.stardash.sportdash.settings.account.AppLockSettingsActivity;

public class LockActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lock);
        isInChat = false;

        if ("chat".equals(changeLock)) {
           // toast("enter code");
        } else if ("app".equals(changeLock)) {
          //  toast("enter code");
        } else if ("appLock".equals(changeLock)) {
            toast("enter code");
        } else if (changeLock.equals("removeCode")) {
            toast("enter code to remove security");
        } else if (changeLock.equals("setCode")) {
            toast("enter a new code");
        } else {

        }
    }

    @SuppressLint("SetTextI18n")
    private void code(int code) {
        vibrate();
        TextView textViewCode = findViewById(R.id.textViewCode);
        String Code = String.valueOf(code);
        String digits = textViewCode.getText().toString();
        if (digits.length() < 5) {
            textViewCode.setText(digits + Code);
        }
        String finalCode = textViewCode.getText().toString();
        if (finalCode.length() > 3) {
                enterCode(finalCode);
                if (finalCode.length() == 4) {
                    final Handler handler = new Handler(Looper.getMainLooper());
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            textViewCode.setText("");
                            vibrate();
                        }
                    }, 600);
                } else {
                    textViewCode.setText("");
                }
            }
    }

    private void enterCode(String code) {
        if (changeLock.equals("removeCode")&&code.equals(Account.Lock())) {
            Account.setLock("none");
            Account.setAppLock(false);
            Account.setChatLock(false);
            Intent i = new Intent(this, MainActivity.class);
            startActivity(i);
        } else if (changeLock.equals("setCode")) {
            Account.setLock(code);
            Intent i = new Intent(this, AppLockSettingsActivity.class);
            startActivity(i);
        } else if (changeLock.equals("app")) {
            if (Account.Lock().equals(code)) {
                Intent i = new Intent(this, MainActivity.class);
                startActivity(i);
                loggedIn = true;
            } else {
                toast("wrong code");
            }
        } else if (changeLock.equals("chat")) {
            if (Account.Lock().equals(code)) {
                Intent i = new Intent(this, ChatActivity.class);
                startActivity(i);
            } else {
                toast("wrong code");
            }
        } else if (changeLock.equals("appLock")) {
            if (Account.Lock().equals(code)) {
                Intent i = new Intent(this, AppLockSettingsActivity.class);
                startActivity(i);
            } else {
                toast("wrong code");
            }
        } else {
            toast("wrong code");
        }

    }


    @Override
    public void onResume(){
        super.onResume();
        // put your code here...
        Intent i;
        if ("chat".equals(changeLock)&& openedChat) {
            i = new Intent(this, MainActivity.class);
            startActivity(i);
        } else if ("chat".equals(changeLock)) {

        } else if ("app".equals(changeLock)) {

        } else if ("appLock".equals(changeLock)) {

        } else if (changeLock.equals("removeCode")) {

        } else if (changeLock.equals("setCode")) {

        } else {
            i = new Intent(this, MainActivity.class);
            startActivity(i);
        }
    }

    @Override
    public void onBackPressed(){
        super.onResume();
        // put your code here...
        Intent i;
        if ("chat".equals(changeLock)&& openedChat) {
            i = new Intent(this, MainActivity.class);
            startActivity(i);
        } else if ("chat".equals(changeLock)) {

        } else if ("app".equals(changeLock)) {

        } else if (changeLock.equals("removeCode")) {

        } else if (changeLock.equals("setCode")) {

        } else if (changeLock.equals("appLock")) {

        } else {
            i = new Intent(this, MainActivity.class);
            startActivity(i);
        }
    }

    public void code0(View view) {
        code(0);
    }

    public void code1(View view) {
        code(1);
    }
    public void code2(View view) {
        code(2);
    }
    public void code3(View view) {
        code(3);
    }
    public void code4(View view) {
        code(4);
    }
    public void code5(View view) {
        code(5);
    }
    public void code6(View view) {
        code(6);
    }
    public void code7(View view) {
        code(7);
    }
    public void code8(View view) {
        code(8);
    }
    public void code9(View view) {
        code(9);
    }

    public void deleteDigit(View view) {
        vibrate();
        try {
            TextView textViewCode = findViewById(R.id.textViewCode);
            String digits = textViewCode.getText().toString();
            String newDigits = digits.substring(1, digits.length());
            textViewCode.setText(newDigits);
        }catch (Exception e){

        }
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

    public void logout(View view) {
        vibrate();
        Account.setLoggedIn(false);
        Account.setCoins(0);
        Account.setEnergy(5);
        Account.setAge(0);
        Account.setId("0");
        Account.setWeight(0);
        Account.setXp(0);
        SharedPreferences settings = this.getSharedPreferences("account", Context.MODE_PRIVATE);
        settings.edit().clear().apply();
        SharedPreferences settings1 = this.getSharedPreferences("me", Context.MODE_PRIVATE);
        settings1.edit().clear().apply();
        SharedPreferences settings2 = this.getSharedPreferences("sport", Context.MODE_PRIVATE);
        settings2.edit().clear().apply();
        Intent i = new Intent(this, RegisterActivity.class);
        startActivity(i);
    }
}