package com.stardash.sportdash.settings.account.security;

import static com.stardash.sportdash.settings.account.AppLockSettingsActivity.changeLock;
import static com.stardash.sportdash.settings.app.vibrate;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.stardash.sportdash.R;
import com.stardash.sportdash.network.tcp.StarsocketConnector;
import com.stardash.sportdash.settings.Account;
import com.stardash.sportdash.settings.account.AppLockSettingsActivity;
import com.stardash.sportdash.signIn.LockActivity;

public class SecureActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_secure);
    }

    public void loginHistory(View view) {
        vibrate();
        try {
            StarsocketConnector.sendMessage("accountActivity " + Account.userid());
            Intent i = new Intent(this, SecurityActivity.class);
            startActivity(i);
        } catch (Exception ignored){

        }
    }

    public void openAppLock(View view) {
        vibrate();
        if (Account.Lock().equals("none")) {
            Intent i = new Intent(this, AppLockSettingsActivity.class);
            startActivity(i);
        } else {
            changeLock = "appLock";
            Intent i = new Intent(this, LockActivity.class);
            startActivity(i);
        }
    }
}