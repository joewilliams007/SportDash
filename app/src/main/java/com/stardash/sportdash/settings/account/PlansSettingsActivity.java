package com.stardash.sportdash.settings.account;

import static com.stardash.sportdash.settings.app.vibrate;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.stardash.sportdash.R;

public class PlansSettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plans_settings);
    }

    public void openTtsSettings(View view) {
        vibrate();
        Intent i = new Intent(this, TtsActivity.class);
        startActivity(i);
    }
}