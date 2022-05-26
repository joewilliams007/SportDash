package com.stardash.sportdash.settings.account;

import static com.stardash.sportdash.settings.app.vibrate;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.InputType;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.stardash.sportdash.R;
import com.stardash.sportdash.network.tcp.StarsocketConnector;
import com.stardash.sportdash.settings.Account;

public class AccountActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        if (Account.isAmoled()) {
            ConstraintLayout main = findViewById(R.id.main);
            main.setBackgroundDrawable(new ColorDrawable(Color.BLACK));
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.BLACK);

            TextView textViewApp = findViewById(R.id.textViewApp);
            textViewApp.setTextColor(ContextCompat.getColor(this, R.color.darkMode));
        }
    }

    public void saveUsername(View view) {
        EditText editTextUsername = findViewById(R.id.editTextTextPersonNameUsername);
        String username = editTextUsername.getText().toString().replace(" ","");
        try {
            if (username.length()<3) {
                toast("username is too short");
            } else {
                StarsocketConnector.sendMessage("setUsername " + Account.userid() + " " + username);
                toast("updated username!");
                SharedPreferences settings = getSharedPreferences("account", MODE_PRIVATE);
                SharedPreferences.Editor editor = settings.edit();
                editor.putString("username", username).apply();
            }
        } catch (Exception e) {
            toast("network error");
        }
        vibrate();
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

    public void saveBio(View view) {
        EditText editTextBio = findViewById(R.id.editTextTextPersonNameBio);
        vibrate();
        if (editTextBio.getText().toString().length()<1){
            toast("enter a bio");
        } else if (editTextBio.getText().toString().length()>1999) {
            toast("must be under 2000 characters");
        } else {
            try {
                StarsocketConnector.sendMessage("setBio " + Account.userid() + " BIO_IS_THE_FOLLOWING" + editTextBio.getText().toString());
                toast("updated bio!");
            } catch (Exception e) {
                toast("network error");
            }
        }
        editTextBio.setText("");
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

    public void savePassword(View view) {
        EditText editTextPassword = findViewById(R.id.editTextTextPersonNamePassword);
        vibrate();
        if (editTextPassword.getText().toString().replaceAll(" ","").length()<5){
            toast("enter a password with min 5 length");
        } else {
            try {
                if (editTextPassword.getText().toString().contains(" ")){
                    toast("all blank spaces will be removed");
                }
                StarsocketConnector.sendMessage("setPassword " + Account.userid() + " " + editTextPassword.getText().toString().replaceAll(" ",""));
                toast("updated password!");
                Account.setPassword(editTextPassword.getText().toString().replaceAll(" ",""));
                editTextPassword.setText("");
            } catch (Exception e) {
                toast("network error");
            }
        }
    }

    public void showPassword(View view) {
        vibrate();
        EditText editTextPassword = findViewById(R.id.editTextTextPersonNamePassword);
        ImageView imageViewEye = findViewById(R.id.imageViewEye);
        imageViewEye.setVisibility(View.GONE);
        editTextPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PERSON_NAME);
        final Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                editTextPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                imageViewEye.setVisibility(View.VISIBLE);
            }
        }, 3000);
    }
}