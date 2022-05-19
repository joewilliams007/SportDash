package com.stardash.sportdash.settings.account;

import static com.stardash.sportdash.settings.account.AppLockSettingsActivity.changeLock;
import static com.stardash.sportdash.settings.app.vibrate;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.stardash.sportdash.R;
import com.stardash.sportdash.settings.Account;
import com.stardash.sportdash.settings.chat.ChatBackgroundActivity;
import com.stardash.sportdash.signIn.LockActivity;

public class ChatSettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_settings);


        if (Account.isAmoled()) {
            ConstraintLayout main = findViewById(R.id.main);
            main.setBackgroundDrawable(new ColorDrawable(Color.BLACK));
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.BLACK);

            TextView textViewApp = findViewById(R.id.textViewApp);
            textViewApp.setTextColor(ContextCompat.getColor(this, R.color.darkMode));
        }
    }

    public void openBackground(View view) {
        vibrate();
        Intent i = new Intent(this, ChatBackgroundActivity.class);
        startActivity(i);
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