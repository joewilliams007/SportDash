package com.stardash.sportdash.settings;

import static com.stardash.sportdash.plans.run.RunPlanActivity.reportingPlan;
import static com.stardash.sportdash.settings.app.vibrate;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.TransitionDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.stardash.sportdash.BuildConfig;
import com.stardash.sportdash.MainActivity;
import com.stardash.sportdash.network.api.Methods;
import com.stardash.sportdash.network.api.Model;
import com.stardash.sportdash.R;
import com.stardash.sportdash.settings.account.AccountActivity;
import com.stardash.sportdash.settings.account.GeneralActivity;
import com.stardash.sportdash.settings.account.TtsActivity;
import com.stardash.sportdash.settings.account.UpdateSettingsActivity;
import com.stardash.sportdash.settings.changelog.UpdateActivity;
import com.stardash.sportdash.settings.chat.ChatSettingsActivity;
import com.stardash.sportdash.signIn.RegisterActivity;
import com.stardash.sportdash.network.api.RetrofitClient;
import com.stardash.sportdash.network.tcp.StarsocketConnector;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        setContentView(R.layout.activity_settings);

        if (Account.isAmoled()) {
            ConstraintLayout main = findViewById(R.id.main);
            main.setBackgroundDrawable(new ColorDrawable(Color.BLACK));
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.BLACK);

            TextView textViewAccount = findViewById(R.id.textViewAccount);
            TextView textViewTts = findViewById(R.id.textViewTts);
            TextView textViewUpdates = findViewById(R.id.textViewUpdates);
            TextView textViewGeneral = findViewById(R.id.textViewGeneral);
            TextView textViewChat = findViewById(R.id.textViewChatSettings);
            textViewGeneral.setTextColor(ContextCompat.getColor(this, R.color.darkMode));
            textViewAccount.setTextColor(ContextCompat.getColor(this, R.color.darkMode));
            textViewTts.setTextColor(ContextCompat.getColor(this, R.color.darkMode));
            textViewChat.setTextColor(ContextCompat.getColor(this, R.color.darkMode));
            textViewUpdates.setTextColor(ContextCompat.getColor(this, R.color.darkMode));
        }

    }


    public void copyID(View view) {
        vibrate();
        TextView textViewId = findViewById(R.id.textViewId);
            ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            String id = textViewId.getText().toString();
            String clip0 = "SportDash - User Account\nAdd my account via the link https://www.sportdash.com/user="+id+"\nor enter my id in the app "+id;
            ClipData clip = ClipData.newPlainText("SportDash-ID", clip0);
            clipboard.setPrimaryClip(clip);
            toast("copied!");
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


    public void openChatSettings(View view) {
        vibrate();
        Intent i = new Intent(this, ChatSettingsActivity.class);
        startActivity(i);
    }


    public void openAccountSettings(View view) {
        vibrate();
        Intent i = new Intent(this, AccountActivity.class);
        startActivity(i);
    }

    public void openTtsSettings(View view) {
        vibrate();
        Intent i = new Intent(this, TtsActivity.class);
        startActivity(i);
    }

    public void openUpdates(View view) {
        vibrate();
        Intent i = new Intent(this, UpdateSettingsActivity.class);
        startActivity(i);
    }

    public void openGeneral(View view) {
        vibrate();
        Intent i = new Intent(this, GeneralActivity.class);
        startActivity(i);
    }
}