package com.stardash.sportdash.settings;

import static com.stardash.sportdash.plans.run.RunPlanActivity.reportingPlan;
import static com.stardash.sportdash.settings.app.vibrate;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.TransitionDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.provider.Settings;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.stardash.sportdash.BuildConfig;
import com.stardash.sportdash.MainActivity;
import com.stardash.sportdash.network.api.Methods;
import com.stardash.sportdash.network.api.Model;
import com.stardash.sportdash.R;
import com.stardash.sportdash.settings.changelog.UpdateActivity;
import com.stardash.sportdash.signIn.RegisterActivity;
import com.stardash.sportdash.network.api.RetrofitClient;
import com.stardash.sportdash.network.tcp.StarsocketConnector;

import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        setContentView(R.layout.activity_settings);
        TextView textViewId = findViewById(R.id.textViewId);
        textViewId.setText("#"+ Account.userid());
        setProfilePicture();
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        setProfilePicture();
                    }
                });
            }
        }, 0, 3000);//wait 0 ms before doing the action and do it evry 1000ms (1second)

        int versionCode = BuildConfig.VERSION_CODE;
        String versionName = BuildConfig.VERSION_NAME;
        TextView textViewMyVersion = findViewById(R.id.textViewMyVersion);
        textViewMyVersion.setText(versionName);


        if (Account.isAmoled()) {
            ConstraintLayout main = findViewById(R.id.main);
            main.setBackgroundDrawable(new ColorDrawable(Color.BLACK));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Window window = getWindow();
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(Color.BLACK);
            }
        }
        CheckBox checkBox = findViewById(R.id.checkBox);
        if (Account.localhost()){

            checkBox.setChecked(true);
        } else {
            checkBox.setChecked(false);
        }
    }

    private void setProfilePicture() {
        String picturePath = PreferenceManager.getDefaultSharedPreferences(this).getString("picturePath", "");
        if(!picturePath.equals(""))
        {
            ImageView imageView = (ImageView) findViewById(R.id.imageViewSmallIcon);
            imageView.setImageBitmap(BitmapFactory.decodeFile(picturePath));
        }
    }

    public void exit(View view) {
        vibrate();
        CheckBox checkBox = findViewById(R.id.checkBox);
        if (checkBox.isChecked()) {
            Account.setLocalhost(true);
        } else {
            Account.setLocalhost(false);
        }
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }

    public void  setPicture(View view) {
        saveImage();
    }

    boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(SettingsActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    void requestPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(SettingsActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            toast("... but ... i need this permission ...");
        } else
            ActivityCompat.requestPermissions(SettingsActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 123);

    }

    public void saveImage() {
        if (checkPermission() == false) {
            requestPermission();
            return;
        } else {
            selectImage();
        }
    }

    private static final int PICK_IMAGE = 100;

    private void selectImage() {
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        gallery.setType("image/*.jpeg");
        startActivityForResult(gallery, PICK_IMAGE);
        setProfilePicture();
    }

    @Override
    public void onRequestPermissionsResult(int PICK_IMAGE, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(PICK_IMAGE, permissions, grantResults);

        if (PICK_IMAGE == 100 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            selectImage();
        } else {
            selectImage();
            // requestPermission();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 100 && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};

            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            PreferenceManager.getDefaultSharedPreferences(this).edit().putString("picturePath", picturePath).commit();
            cursor.close();


        }
    }

    public void saveUsername(View view) {
        EditText editTextUsername = findViewById(R.id.editTextTextPersonNameUsername);
        String username = editTextUsername.getText().toString().replace(" ","");

        try {
            StarsocketConnector.sendMessage("setUsername " + Account.userid() + " " + username);
            toast("updated username!");
            SharedPreferences settings = getSharedPreferences("account", MODE_PRIVATE);
            SharedPreferences.Editor editor = settings.edit();
            editor.putString("username", username).commit();
        } catch (Exception e) {
            toast("network error");
        }

        vibrate();

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

    public void openTTSsettings(View view) {
        //Open Android Text-To-Speech Settings
        if (Build.VERSION.SDK_INT >= 14){
            Intent intent = new Intent();
            intent.setAction("com.android.settings.TTS_SETTINGS");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }else {
            Intent intent = new Intent();
            intent.addCategory(Intent.CATEGORY_LAUNCHER);
            intent.setComponent(new ComponentName("com.android.settings", "com.android.settings.TextToSpeechSettings"));
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
    }

    public void enableTTSName(View view) {
        vibrate();
        SharedPreferences settings = getSharedPreferences("tts", MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean("name", true).commit();
        toast("enabled TTS for element names");
    }

    public void disableTTSName(View view) {
        vibrate();
        SharedPreferences settings = getSharedPreferences("tts", MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean("name", false).commit();
        toast("disabled TTS for element names");
    }

    public void enableTTSDescription(View view) {
        vibrate();
        SharedPreferences settings = getSharedPreferences("tts", MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean("description", true).commit();
        toast("enabled TTS for element descriptions");
    }

    public void disableTTSDescription(View view) {
        vibrate();
        SharedPreferences settings = getSharedPreferences("tts", MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean("description", false).commit();
        toast("disabled TTS for element descriptions");
    }

    public void setAsDefault(){
        vibrate();
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);
        startActivity(intent);
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
    @Override
    public void onBackPressed() {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }

    public void openDefaultSetting(View view) {
        setAsDefault();
    }



    public void logout(View view) {
        vibrate();
        Account.setLoggedIn(false);
        Account.setCoins(0);
        Account.setEnergy(5);
        Account.setAge(0);
        Account.setId("0");
        Account.setWeight(0);
        Account.setXp(0);
        SharedPreferences settings = this.getSharedPreferences("account", Context.MODE_PRIVATE);
        settings.edit().clear().apply();
        SharedPreferences settings1 = this.getSharedPreferences("me", Context.MODE_PRIVATE);
        settings1.edit().clear().apply();
        SharedPreferences settings2 = this.getSharedPreferences("sport", Context.MODE_PRIVATE);
        settings2.edit().clear().apply();

        Intent i = new Intent(this, RegisterActivity.class);
        startActivity(i);
    }

    public void setThemeAmoled(View view) {
        vibrate();
        Account.setAmoled(true);
        if (Account.isAmoled()) {
            ConstraintLayout main = findViewById(R.id.main);
            //Let's change background's color from blue to red.
            ColorDrawable[] color = {new ColorDrawable(Color.parseColor("#323232")), new ColorDrawable(Color.BLACK)};
            TransitionDrawable trans = new TransitionDrawable(color);
            //This will work also on old devices. The latest API says you have to use setBackground instead.
            main.setBackgroundDrawable(trans);
            trans.startTransition(2000);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Window window = getWindow();
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(Color.BLACK);
            }
        }
    }


    public void setThemeGrey(View view) {
        vibrate();
        Account.setAmoled(false);
        if (!Account.isAmoled()) {
            ConstraintLayout main = findViewById(R.id.main);
            //Let's change background's color from blue to red.
            ColorDrawable[] color = {new ColorDrawable(Color.BLACK), new ColorDrawable(Color.parseColor("#323232"))};
            TransitionDrawable trans = new TransitionDrawable(color);
            //This will work also on old devices. The latest API says you have to use setBackground instead.
            main.setBackgroundDrawable(trans);
            trans.startTransition(2000);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Window window = getWindow();
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(Color.parseColor("#323232"));
            }
        }
    }

    public void openFeedback(View view) {
        vibrate();
        reportingPlan = false;
        Intent i = new Intent(this, FeedbackActivity.class);
        startActivity(i);
    }

    public void saveAge(View view) {
        EditText editTextAge = findViewById(R.id.editTextTextPersonNameAge);
        vibrate();
        if (editTextAge.getText().toString().length()<1){
            toast("enter your age first");
        } else {
            Account.setAge(Integer.parseInt(editTextAge.getText().toString()));

            try {
                StarsocketConnector.sendMessage("setAge " + Account.userid() + " " + editTextAge.getText().toString());
                toast("updated age!");
            } catch (Exception e) {
                toast("network error");
            }
        }
        editTextAge.setText("");
    }

    public void saveWeight(View view) {
        EditText editTextWeight = findViewById(R.id.editTextTextPersonNameWeight);
        vibrate();
        if (editTextWeight.getText().toString().length()<1){
            toast("enter your weight first");
        } else {
            Account.setAge(Integer.parseInt(editTextWeight.getText().toString()));

            try {
                StarsocketConnector.sendMessage("setWeight " + Account.userid() + " " + editTextWeight.getText().toString());
                toast("updated weight!");
            } catch (Exception e) {
                toast("network error");
            }
        }
        editTextWeight.setText("");
    }


    public void checkUpdate(View view) {
        vibrate();
            Methods methods = RetrofitClient.getRetrofitInstance().create(Methods.class);
            String clickName = "https://joewilliams007.github.io/jsonapi/adress.json";
            Call<Model> call = methods.getAllData(clickName);
            call.enqueue(new Callback<Model>() {
                @Override
                public void onResponse(Call<Model> call, Response<Model> response) {

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
                            toast("new version is available!\nnew version "+version);
                        } else {
                            toast("you have the latest version!");
                        }

                    }
                }

                @Override
                public void onFailure(Call<Model> call, Throwable t) {
                    toast("no network");
                }
            });


    }

    public void openAbout(View view) {
        vibrate();
        Intent i = new Intent(this, AboutActivity.class);
        startActivity(i);
    }

    public void openUpdate(View view) {
        vibrate();
        Intent i = new Intent(this, UpdateActivity.class);
        startActivity(i);
    }

    public void openChatSettings(View view) {
        vibrate();
        Intent i = new Intent(this, ChatSettingsActivity.class);
        startActivity(i);
    }
}