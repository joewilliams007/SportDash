package com.stardash.sportdash.settings.account;

import static com.stardash.sportdash.settings.app.vibrate;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Switch;

import com.stardash.sportdash.MainActivity;
import com.stardash.sportdash.R;
import com.stardash.sportdash.settings.Account;

public class CustomizeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customize);
        checkBoxes();
    }

    private void checkBoxes() {
        Switch checkBox = findViewById(R.id.checkBoxName);
        Switch checkBoxTracking = findViewById(R.id.checkBoxTracking);
        checkBox.setChecked(Account.isHomeName());
        checkBoxTracking.setChecked(Account.isTrackingHome());
    }

    public void enableName(View view) {
        vibrate();
        Account.setHomeName(!Account.isHomeName());
        checkBoxes();
    }

    public void enableTracking(View view) {
        vibrate();
        Account.setTrackingHome(!Account.isTrackingHome());
        checkBoxes();
    }

    public void restartApp(View view) {
        vibrate();
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }
}