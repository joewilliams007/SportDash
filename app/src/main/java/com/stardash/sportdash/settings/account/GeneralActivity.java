package com.stardash.sportdash.settings.account;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;
import static com.stardash.sportdash.plans.run.RunPlanActivity.reportingPlan;
import static com.stardash.sportdash.settings.account.AppLockSettingsActivity.changeLock;
import static com.stardash.sportdash.settings.app.vibrate;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.Switch;
import android.widget.TextView;

import com.stardash.sportdash.MainActivity;
import com.stardash.sportdash.R;
import com.stardash.sportdash.network.tcp.StarsocketConnector;
import com.stardash.sportdash.settings.AboutActivity;
import com.stardash.sportdash.settings.Account;
import com.stardash.sportdash.settings.FeedbackActivity;
import com.stardash.sportdash.settings.MyApplication;
import com.stardash.sportdash.signIn.LockActivity;
import com.stardash.sportdash.signIn.RegisterActivity;

public class GeneralActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_general);

        if (Account.isAmoled()) {
            ConstraintLayout main = findViewById(R.id.main);
            main.setBackgroundDrawable(new ColorDrawable(Color.BLACK));
            Window window = getWindow();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                window.setStatusBarColor(Color.BLACK);
            }

            TextView textViewApp = findViewById(R.id.textViewApp);
            textViewApp.setTextColor(ContextCompat.getColor(this, R.color.darkMode));
        }

        checkBoxes();
    }

    private void checkBoxes() {
        @SuppressLint("UseSwitchCompatOrMaterialCode") Switch checkBox = findViewById(R.id.checkBoxLocalhost);
        @SuppressLint("UseSwitchCompatOrMaterialCode") Switch checkBoxTheme = findViewById(R.id.checkBoxTheme);
        @SuppressLint("UseSwitchCompatOrMaterialCode") Switch checkBoxVibration = findViewById(R.id.checkBoxVibration);
        checkBoxVibration.setChecked(Account.isVibration());
        checkBox.setChecked(Account.localhost());
        checkBoxTheme.setChecked(Account.isAmoled());
    }

    public void openDeviceLinksSettings(View view) {
        vibrate();
        AlertDialog.Builder builder = new AlertDialog.Builder(GeneralActivity.this);
        builder.setTitle("Default Links");
        builder.setIcon(R.drawable.default_removebg);
        builder.setMessage("/SetAsDefault/SupportedWebAddresses\n\nLaunch Setting to enable?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        vibrate();
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", getPackageName(), null);
                        intent.setData(uri);
                        startActivity(intent);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        vibrate();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    public void enableLocalhost(View view) {
        vibrate();
        Account.setLocalhost(!Account.localhost());
        checkBoxes();
    }

    public void enableAmoled(View view) {
        vibrate();
        Account.setAmoled(!Account.isAmoled());
        if (Account.isAmoled()) {
            setThemeAmoled();
        } else {
            setThemeGrey();
        }
        checkBoxes();
    }

    public void enableVibration(View view) {
        Account.setVibration(!Account.isVibration());
        if (Account.isVibration()) {
            vibrate();
        }
        checkBoxes();
    }

    public void setThemeAmoled() {
            ConstraintLayout main = findViewById(R.id.main);
            //Let's change background's color from blue to red.
            ColorDrawable[] color = {new ColorDrawable(Color.parseColor("#323232")), new ColorDrawable(Color.BLACK)};
            TransitionDrawable trans = new TransitionDrawable(color);
            //This will work also on old devices. The latest API says you have to use setBackground instead.
            main.setBackgroundDrawable(trans);
            trans.startTransition(2000);

            Window window = getWindow();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(Color.BLACK);
        }

        TextView textViewApp = findViewById(R.id.textViewApp);
            textViewApp.setTextColor(ContextCompat.getColor(this, R.color.darkMode));
    }


    public void setThemeGrey() {
            ConstraintLayout main = findViewById(R.id.main);
            //Let's change background's color from blue to red.
            ColorDrawable[] color = {new ColorDrawable(Color.BLACK), new ColorDrawable(Color.parseColor("#323232"))};
            TransitionDrawable trans = new TransitionDrawable(color);
            //This will work also on old devices. The latest API says you have to use setBackground instead.
            main.setBackgroundDrawable(trans);
            trans.startTransition(2000);

            Window window = getWindow();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(Color.parseColor("#323232"));
        }
        TextView textViewApp = findViewById(R.id.textViewApp);
        textViewApp.setTextColor(ContextCompat.getColor(this, R.color.black));
    }

    public void openFeedback(View view) {
        vibrate();
        reportingPlan = false;
        Intent i = new Intent(this, FeedbackActivity.class);
        startActivity(i);
    }

    public void logout(View view) {
        vibrate();
        AlertDialog.Builder builder = new AlertDialog.Builder(GeneralActivity.this);
        builder.setTitle("StarDash");
        builder.setMessage("Are you sure you want to logout?")
                .setCancelable(false)
                .setIcon(R.drawable.logout_removebg)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        vibrate();
                        Account.setLoggedIn(false);
                        Account.setCoins(0);
                        Account.setEnergy(5);
                        Account.setAge(0);
                        Account.setId("0");
                        Account.setWeight(0);
                        Account.setXp(0);
                        SharedPreferences settings = MyApplication.getAppContext().getSharedPreferences("account", Context.MODE_PRIVATE);
                        settings.edit().clear().apply();
                        SharedPreferences settings1 = MyApplication.getAppContext().getSharedPreferences("me", Context.MODE_PRIVATE);
                        settings1.edit().clear().apply();
                        SharedPreferences settings2 = MyApplication.getAppContext().getSharedPreferences("sport", Context.MODE_PRIVATE);
                        settings2.edit().clear().apply();
                        Intent i = new Intent(MyApplication.getAppContext(), RegisterActivity.class);
                        startActivity(i);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        vibrate();
                   //     finish();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();

    }

    public void openAbout(View view) {
        vibrate();
        Intent i = new Intent(this, AboutActivity.class);
        startActivity(i);
    }


    @SuppressLint("SetTextI18n")
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


    public void restartApp(View view) {
        vibrate();
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }

    public void openCustomize(View view) {
        vibrate();
        Intent i = new Intent(this, CustomizeActivity.class);
        startActivity(i);
    }

    public void openDataSaver(View view) {
        vibrate();
        Intent i = new Intent(this, DataSaverActivity.class);
        startActivity(i);
    }
}