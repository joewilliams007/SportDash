package com.stardash.sportdash.settings.account;

import static com.stardash.sportdash.settings.app.vibrate;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.stardash.sportdash.BuildConfig;
import com.stardash.sportdash.R;
import com.stardash.sportdash.network.api.Methods;
import com.stardash.sportdash.network.api.Model;
import com.stardash.sportdash.network.api.RetrofitClient;
import com.stardash.sportdash.settings.Account;
import com.stardash.sportdash.settings.MyApplication;
import com.stardash.sportdash.settings.changelog.UpdateActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdateSettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_settings);

        String versionName = BuildConfig.VERSION_NAME;
        TextView textViewMyVersion = findViewById(R.id.textViewMyVersion);
        textViewMyVersion.setText(versionName);

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
    }

    public void checkUpdate(View view) {
        vibrate();
        Methods methods = RetrofitClient.getRetrofitInstance().create(Methods.class);
        String clickName = "https://joewilliams007.github.io/jsonapi/adress.json";
        Call<Model> call = methods.getAllData(clickName);
        call.enqueue(new Callback<Model>() {
            @Override
            public void onResponse(@NonNull Call<Model> call, @NonNull Response<Model> response) {

                assert response.body() != null;
                String build = response.body().getBuild();
                String version = response.body().getVersion();
                String updateInfo = response.body().getUpdateInfo();
                String ip =  response.body().getBuild();

                if (version == null) {
                    toast("no network");
                } else {
                    int versionCode = BuildConfig.VERSION_CODE;
                    String versionName = BuildConfig.VERSION_NAME;

                    if (versionCode<Integer.parseInt(build)) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(UpdateSettingsActivity.this);
                        builder.setTitle("Updates");
                        builder.setIcon(R.drawable.update_removebg);
                        builder.setMessage("An update is available! ( VER "+version+" )\n\nWhats new?\n"+updateInfo+"\n\nLaunch app store?")
                                .setCancelable(false)
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        vibrate();
                                        Intent play =
                                                new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.stardash.sportdash"));
                                        startActivity(play);
                                    }
                                })
                                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                      vibrate();
                                    }
                                });
                        AlertDialog alert = builder.create();
                        alert.show();

                    } else {

                        AlertDialog.Builder builder = new AlertDialog.Builder(UpdateSettingsActivity.this);
                        builder.setTitle("Updates");
                        builder.setIcon(R.drawable.update_removebg);
                        builder.setMessage("You have the latest version!")
                                .setCancelable(false)
                                .setPositiveButton("close", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        vibrate();
                                    }
                                });
                        AlertDialog alert = builder.create();
                        alert.show();
                    }

                }
            }

            @Override
            public void onFailure(@NonNull Call<Model> call, @NonNull Throwable t) {
                toast("no network");
            }
        });
    }

    public void openChangelog(View view) {
        vibrate();
        Intent i = new Intent(this, UpdateActivity.class);
        startActivity(i);
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