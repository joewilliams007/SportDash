package com.stardash.sportdash.settings.account;

import static com.stardash.sportdash.settings.app.vibrate;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Switch;

import com.stardash.sportdash.R;
import com.stardash.sportdash.settings.Account;

public class DataSaverActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_saver);
        checkBoxes();
    }

    private void checkBoxes() {
        @SuppressLint("UseSwitchCompatOrMaterialCode") Switch checkBoxMobileDataSaver = findViewById(R.id.checkBoxMobileDataSaver);
        @SuppressLint("UseSwitchCompatOrMaterialCode") Switch checkBoxDataSaver = findViewById(R.id.checkBoxDataSaver);
        checkBoxMobileDataSaver.setChecked(Account.isMobileDataSaver());
        checkBoxDataSaver.setChecked(Account.isDataSaver());
    }

    public void dataSaver(View view) {
        Boolean currentDataSaver = Account.isDataSaver();
        Account.setDataSaver(!currentDataSaver);

        if(currentDataSaver) {
            Account.setMobileDataSaver(false);
        }

        vibrate();
        checkBoxes();
    }

    public void mobileDataSaver(View view) {
        Account.setMobileDataSaver(!Account.isMobileDataSaver());
        Account.setDataSaver(true);

        vibrate();
        checkBoxes();
    }
}