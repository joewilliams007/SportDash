package com.stardash.sportdash.settings.account;

import static com.stardash.sportdash.settings.app.vibrate;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.TextView;

import com.stardash.sportdash.R;
import com.stardash.sportdash.settings.Account;
import com.stardash.sportdash.settings.MyApplication;

public class TtsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tts);

        if (Account.isAmoled()) {
            ConstraintLayout main = findViewById(R.id.main);
            main.setBackgroundDrawable(new ColorDrawable(Color.BLACK));
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.BLACK);

            TextView textViewApp = findViewById(R.id.textViewApp);
            textViewApp.setTextColor(ContextCompat.getColor(this, R.color.darkMode));
        }

        setCheckBoxes();
    }

    private void setCheckBoxes() {
        CheckBox checkBoxNames = findViewById(R.id.checkBoxNames);
        CheckBox checkBoxDescriptions = findViewById(R.id.checkBoxDescriptions);
        checkBoxNames.setChecked(ttsNames());
        checkBoxDescriptions.setChecked(ttsDescriptions());
    }

    public void enableNames(View view) {
        SharedPreferences settings = getSharedPreferences("tts", MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        if (ttsNames()) {
            editor.putBoolean("name", false).apply();
        } else {
            editor.putBoolean("name", true).apply();
        }
        saveSetting();
    }

    public void enableDescriptions(View view) {
        SharedPreferences settings = getSharedPreferences("tts", MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        if (ttsDescriptions()) {
            editor.putBoolean("description", false).apply();
        } else {
            editor.putBoolean("description", true).apply();
        }
        saveSetting();
    }

    private void saveSetting() {
        vibrate();
        setCheckBoxes();
    }

    public void openDeviceTtsSettings(View view) {
        try {
            //Open Android Text-To-Speech Settings
            Intent intent = new Intent();
            intent.setAction("com.android.settings.TTS_SETTINGS");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            this.startActivity(intent);
        } catch (Exception e){
            toast("could not locate device setting TTS");
        }
    }

    public static Boolean ttsNames() {
        SharedPreferences settings = MyApplication.getAppContext().getSharedPreferences("tts", MODE_PRIVATE);
        return settings.getBoolean("name", false);
    }

    public static Boolean ttsDescriptions() {
        SharedPreferences settings = MyApplication.getAppContext().getSharedPreferences("tts", MODE_PRIVATE);
        return settings.getBoolean("description", false);
    }

    @SuppressLint("SetTextI18n")
    public void toast(String message){
        TextView textViewCustomToast = findViewById(R.id.textViewCustomToast);
        textViewCustomToast.setVisibility(View.VISIBLE);
        textViewCustomToast.setText(Account.errorStyle()+" "+message);
        final Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(() -> textViewCustomToast.setVisibility(View.GONE), 3000);
    }
}