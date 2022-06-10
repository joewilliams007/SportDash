package com.stardash.sportdash.online.chat;

import static com.stardash.sportdash.online.friends.FriendsActivity.chatId;
import static com.stardash.sportdash.online.friends.FriendsActivity.chatUsername;
import static com.stardash.sportdash.online.friends.FriendsActivity.tappedOnSearchItem;
import static com.stardash.sportdash.online.friends.FriendsActivity.tappedOnSearchItemId;
import static com.stardash.sportdash.settings.app.vibrate;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.pm.ShortcutInfoCompat;
import androidx.core.content.pm.ShortcutManagerCompat;
import androidx.core.graphics.drawable.IconCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.stardash.sportdash.R;
import com.stardash.sportdash.network.tcp.StarsocketConnector;
import com.stardash.sportdash.online.friends.FriendsActivity;
import com.stardash.sportdash.settings.MyApplication;
import com.stardash.sportdash.settings.account.ChatSettingsActivity;

public class ChatInfoActivity extends AppCompatActivity {

    public TextView textViewTop;
    public TextView textViewMessagesTotal;
    public TextView textViewMessagesMe;
    public TextView textViewMessagesHim;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_info);
        textViewTop = findViewById(R.id.textViewTop);
        textViewMessagesTotal = findViewById(R.id.textViewMessagesNr);
        textViewMessagesMe = findViewById(R.id.textViewMessagesYouNr);
        textViewMessagesHim = findViewById(R.id.textViewMessagesHimNr);
        textViewTop.setText(chatUsername);
        String received = StarsocketConnector.getReplyTo("getChatInfo "+chatId);
        textViewMessagesTotal.setText(received.split(" ")[0]);
        textViewMessagesMe.setText(received.split(" ")[2]);
        textViewMessagesHim.setText(received.split(" ")[1]);
    }

    public void openProfile(View view) {
        try {
            vibrate();
            tappedOnSearchItem = true;
            tappedOnSearchItemId = chatId;
            Intent i = new Intent(MyApplication.getAppContext(), FriendsActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            MyApplication.getAppContext().startActivity(i);
        } catch (Exception ignored) {

        }
    }

    public void openChatSettings(View view) {
        vibrate();
        Intent i = new Intent(MyApplication.getAppContext(), ChatSettingsActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        MyApplication.getAppContext().startActivity(i);
    }

    public void addShortcut(View view) {
        vibrate();
        if (ShortcutManagerCompat.isRequestPinShortcutSupported(getApplicationContext())) {
            ShortcutInfoCompat shortcutInfo = new ShortcutInfoCompat.Builder(getApplicationContext(), chatId)
                    .setIntent(new Intent(getApplicationContext(), ChatActivity.class)
                            .setAction(Intent.ACTION_MAIN)
                            .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                            .putExtra("ID",chatId)
                            .putExtra("USERNAME",chatUsername)
                    ) // !!! intent's action must be set on oreo
                    .setShortLabel(chatUsername)
                    .setIcon(IconCompat.createWithResource(getApplicationContext(), R.drawable.star1))
                    .build();
            ShortcutManagerCompat.requestPinShortcut(getApplicationContext(), shortcutInfo, null);
        } else {
            Toast.makeText(ChatInfoActivity.this,"launcher does not support short cut icon",Toast.LENGTH_LONG).show();
        }
    }
}