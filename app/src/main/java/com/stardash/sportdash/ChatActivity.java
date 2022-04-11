package com.stardash.sportdash;

import static com.stardash.sportdash.ProfileActivity.chatId;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

public class ChatActivity extends AppCompatActivity {

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        TextView textViewTop = findViewById(R.id.textViewTop);
        textViewTop.setText("#" + chatId);

        if (Account.isAmoled()) {
            ConstraintLayout main = findViewById(R.id.main);
            main.setBackgroundDrawable(new ColorDrawable(Color.BLACK));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Window window = getWindow();
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(Color.BLACK);
            }
            TextView textView = findViewById(R.id.textViewHome);
            textView.setTextColor(Color.parseColor("#FFFFFF"));

        }
    }

    public void sendMessage(View view) {
        EditText editTextMessage = findViewById(R.id.editTextTextPersonName);
        if (editTextMessage.getText().toString().length()<1){
            toast("enter a message");
        } else {
            StarsocketConnector.sendMessage("chat " + Account.userid() + " " + chatId + " MESSAGE&" + editTextMessage.getText().toString().replace("#","(hashtag)"));
            editTextMessage.setText("");
            toast("sent message to #" + chatId);
        }
    }

    public void back(View view) {
        Intent i = new Intent(this, FriendsActivity.class);
        startActivity(i);
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
}