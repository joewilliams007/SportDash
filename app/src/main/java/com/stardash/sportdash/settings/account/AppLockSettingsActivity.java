package com.stardash.sportdash.settings.account;

import static com.stardash.sportdash.online.friends.FriendsActivity.openedChat;
import static com.stardash.sportdash.settings.app.vibrate;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Switch;
import android.widget.TextView;

import com.stardash.sportdash.MainActivity;
import com.stardash.sportdash.R;
import com.stardash.sportdash.settings.Account;
import com.stardash.sportdash.signIn.LockActivity;

public class AppLockSettingsActivity extends AppCompatActivity {
    public static String changeLock;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_lock_settings);
        checkBoxes();
    }

    private void checkBoxes() {
        Switch checkBoxApp = findViewById(R.id.checkBoxApp);
        Switch checkBoxChats = findViewById(R.id.checkBoxChats);
        checkBoxApp.setChecked(Account.isAppLock());
        checkBoxChats.setChecked(Account.isChatLock());
    }

    public void enableAppLock(View view) {
        vibrate();
        if (Account.Lock().equals("none")) {
            setCode();
        } else if (Account.isAppLock()){
            Account.setAppLock(false);
            checkBoxes();
        } else {
            Account.setAppLock(true);
            checkBoxes();
        }
    }

    @Override
    public void onBackPressed(){
        super.onResume();
        // put your code here...
        Intent i;
            i = new Intent(this, MainActivity.class);
            startActivity(i);
    }

    public void enableChatLock(View view) {
        vibrate();
        if (Account.Lock().equals("none")) {
            setCode();
        } else if (Account.isChatLock()){
            Account.setChatLock(false);
            checkBoxes();
        } else {
            Account.setChatLock(true);
            checkBoxes();
        }
    }

    public void setCodeBtn(View view) {
        vibrate();
        setCode();
    }

    private void setCode() {
            changeLock = "setCode";
            Intent i = new Intent(this, LockActivity.class);
            startActivity(i);
    }

    public void removeCodeBtn(View view) {
        vibrate();
        if (Account.Lock().equals("none")) {
            toast("none is set");
        } else {
            changeLock = "removeCode";
            Intent i = new Intent(this, LockActivity.class);
            startActivity(i);
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

}