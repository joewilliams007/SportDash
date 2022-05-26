package com.stardash.sportdash.plans.run.live;

import static com.stardash.sportdash.settings.app.vibrate;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import com.stardash.sportdash.R;

public class LivePlanActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_plan);
        setSwitches();
    }

    public void livePlanGenerate(View view) {
        vibrate();
        Intent i = new Intent(this, LivePlanGenerateActivity.class);
        startActivity(i);
    }

    @SuppressLint("UseSwitchCompatOrMaterialCode")
    public void enablePassword(View view) {
        vibrate();
        Switch Switch = findViewById(R.id.switchPassword);
        EditText editTextPassword = findViewById(R.id.editTextTextPersonNamePassword);
        if (Switch.isChecked()) {
            editTextPassword.setVisibility(View.VISIBLE);
        } else {
            editTextPassword.setVisibility(View.GONE);
        }
        setSwitches();
    }

    private void setSwitches() {

    }

    public void enableProfile(View view) {
        TextView textViewProfile = findViewById(R.id.textViewProfile);
        @SuppressLint("UseSwitchCompatOrMaterialCode") Switch Switch = findViewById(R.id.switchProfile);
        if (Switch.isChecked()) {
            textViewProfile.setVisibility(View.VISIBLE);
        } else {
            textViewProfile.setVisibility(View.GONE);
        }
        setSwitches();
    }
}