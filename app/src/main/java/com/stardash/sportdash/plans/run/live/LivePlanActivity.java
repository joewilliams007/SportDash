package com.stardash.sportdash.plans.run.live;

import static com.stardash.sportdash.settings.app.vibrate;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import com.stardash.sportdash.R;
import com.stardash.sportdash.network.tcp.StarsocketConnector;
import com.stardash.sportdash.settings.Account;

public class LivePlanActivity extends AppCompatActivity {
    public static String livePlanId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_plan);
        setSwitches();
    }


    @SuppressLint("UseSwitchCompatOrMaterialCode")
    public void enablePassword(View view) {
        vibrate();
        setSwitches();
    }

    private void setSwitches() {
        @SuppressLint("UseSwitchCompatOrMaterialCode") Switch Switch = findViewById(R.id.switchPassword);
        EditText editTextPassword = findViewById(R.id.editTextTextPersonNamePassword);
        if (Switch.isChecked()) {
            editTextPassword.setVisibility(View.VISIBLE);
        } else {
            editTextPassword.setVisibility(View.GONE);
        }
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

    public void livePlanGenerate(View view) {
        vibrate();
        @SuppressLint("UseSwitchCompatOrMaterialCode") Switch SwitchFollow = findViewById(R.id.switchFollowing);
        @SuppressLint("UseSwitchCompatOrMaterialCode") Switch SwitchProfile = findViewById(R.id.switchProfile);
        @SuppressLint("UseSwitchCompatOrMaterialCode") Switch SwitchPassword = findViewById(R.id.switchPassword);
        EditText editTextPassword = findViewById(R.id.editTextTextPersonNamePassword);

        String followEnabled = "no";
        String profileEnabled = "no";
        String passwordEnabled = "no";
        String password = editTextPassword.getText().toString();

        if (SwitchFollow.isChecked()) {
            followEnabled = "yes";
        }
        if (SwitchProfile.isChecked()) {
            profileEnabled = "yes";
        }
        if (SwitchPassword.isChecked()) {
            passwordEnabled = "yes";
        }

        if (SwitchPassword.isChecked()) {
            if (password.length()>10) {
                toast("password must be shorter then 10");
            } else if (password.length()<1) {
                toast("enter password or disable password");
            } else {
                try {
                    StarsocketConnector.sendMessage("start_lobby "+ Account.userid()+" "+livePlanId+" "+followEnabled+" "+profileEnabled+" "+passwordEnabled+" "+password);
                    Intent i = new Intent(this, LivePlanGenerateActivity.class);
                    startActivity(i);
                } catch (Exception e){
                    toast("no network");
                }
            }
        } else {
            try {
                StarsocketConnector.sendMessage("start_lobby "+ Account.userid()+" "+livePlanId+" "+followEnabled+" "+profileEnabled+" "+passwordEnabled+" "+password);
                Intent i = new Intent(this, CreatingLiveActivity.class);
                startActivity(i);
            } catch (Exception e){
                toast("no network");
            }
        }

    }

    @SuppressLint("SetTextI18n")
    public void toast(String message){
        TextView textViewCustomToast = findViewById(R.id.textViewCustomToast);
        textViewCustomToast.setVisibility(View.VISIBLE);
        textViewCustomToast.setText(">_< "+message);
        final Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                textViewCustomToast.setVisibility(View.GONE);
            }
        }, 2000);
    }
}