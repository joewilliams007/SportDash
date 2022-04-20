package com.stardash.sportdash.online.chat;

import static android.graphics.BitmapFactory.*;
import static com.stardash.sportdash.online.ProfileActivity.chatId;
import static com.stardash.sportdash.online.ProfileActivity.chatUsername;
import static com.stardash.sportdash.settings.app.vibrate;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.stardash.sportdash.online.ProfileActivity;
import com.stardash.sportdash.settings.Account;
import com.stardash.sportdash.MainActivity;
import com.stardash.sportdash.R;
import com.stardash.sportdash.network.tcp.StarsocketConnector;
import com.stardash.sportdash.settings.MyApplication;

import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ChatActivity extends AppCompatActivity {

    private ArrayList<ChatItem> mChatList;

    private RecyclerView mRecyclerView;
    private ChatAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        updateChat = false;
        updateChat = true;
        isInChat = true;
        isNew = true;
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        setContentView(R.layout.activity_chat);
        TextView textViewTop = findViewById(R.id.textViewTop);

        check();

        textViewTop.setText(chatUsername);
        showSet();


        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(Color.parseColor("#212121"));

        if (Account.isAmoled()) {
            ConstraintLayout main = findViewById(R.id.main);
            main.setBackgroundDrawable(new ColorDrawable(Color.BLACK));
            TextView textView = findViewById(R.id.textViewHome);
            textView.setTextColor(Color.parseColor("#FFFFFF"));
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.parseColor("#000000"));
            textViewTop.setBackgroundColor(Color.parseColor("#000000"));
            TextView textViewSet = findViewById(R.id.textViewSet);
            textViewSet.setBackgroundColor(Color.parseColor("#000000"));
        }
        ((ProgressBar) findViewById(R.id.progressBar)).setVisibility(View.VISIBLE);
       // setRecyclerView();
        setBg();

        mRecyclerView = findViewById(R.id.chat_recycler_view);
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_FLING) {
                    // Do something
                } else if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                    // Do something
                } else {
                    // Do something
                }
            }
        });
    }
    public static Boolean isNew;
    public static Boolean isInChat;
    public void check(){
        ScheduledExecutorService scheduleTaskExecutor = Executors.newScheduledThreadPool(5);

        // This schedule a runnable task every 2 minutes
        scheduleTaskExecutor.scheduleAtFixedRate(new Runnable() {
            public void run() {
                setRecyclerView();
                if (!isInChat){
                    scheduleTaskExecutor.shutdown();
                }
            }
        }, 0, 2, TimeUnit.SECONDS);
    }


    public static Boolean updateChat;
    static ArrayList<ChatItem> chatList;
    static String olds;
    private void setRecyclerView() {
       try {
        StarsocketConnector.sendMessage("getChat "+Account.userid()+" "+chatId);
        final Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                    try {

                        String s[] = StarsocketConnector.getMessage().split("NEXTMESSAGEIS:;");


                        if (s[s.length-1].equals(olds)&&!isNew) {

                        } else {
                            olds = s[s.length-1];
                            isNew = false;

                            String ans = "";
                            for (int i = s.length - 1; i >= 0; i--) {
                                ans += s[i] + "NEXTMESSAGEIS:;";
                            }

                            String[] chat = ans.split("NEXTMESSAGEIS:;");


                            chatList = new ArrayList<>();

                            for (String element : chat) {
                                try {
                                    chatList.add(new ChatItem(element.split("@")[1], element.split("@")[2], element.split("@")[0]));
                                } catch (Exception e) {

                                }
                            }


                            // chatList.add(new ChatItem("JoeJoe", "Hi how are you?", "10.44 pm"));

                            mRecyclerView = findViewById(R.id.chat_recycler_view);
                            mRecyclerView.setHasFixedSize(true);
                            mLayoutManager = new LinearLayoutManager(ChatActivity.this);
                            mAdapter = new ChatAdapter(chatList);

                            mRecyclerView.setLayoutManager(mLayoutManager);
                            mRecyclerView.setAdapter(mAdapter);

                            //  mRecyclerView.scrollToPosition(chatList.size() - 1);

                            ((ProgressBar) findViewById(R.id.progressBar)).setVisibility(View.GONE);
                         }
                        } catch(Exception e){
                            toast("no network");
                            ((ProgressBar) findViewById(R.id.progressBar)).setVisibility(View.VISIBLE);
                    }

            }
        }, 2000);

       } catch (Exception e){
           toast("no network");
       }
    }



    public void sendMessage(View view) {
        vibrate();
        EditText editTextMessage = findViewById(R.id.editTextTextPersonName);
        if (editTextMessage.getText().toString().length()<1){
            toast("enter a message");
        } else if (editTextMessage.getText().toString().length()>150){
            toast("message is too long");
        } else {
            ((ProgressBar) findViewById(R.id.progressBar)).setVisibility(View.VISIBLE);
                StarsocketConnector.sendMessage("chat " + Account.userid() + " " + chatId + " MESSAGE&" + editTextMessage.getText().toString().replace("#","(hashtag)")+ " MESSAGE&" + Account.username());
                editTextMessage.setText("");
          //  setRecyclerView();
        }

    }

    public void home(View view) {
        vibrate();
        Intent i = new Intent(this, MainActivity.class);
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

    public void showOptions(View view) {
       showSet();
       vibrate();
    }

    private void showSet() {
        TextView textViewSet = findViewById(R.id.textViewSet);
        TextView textViewOptionBlock = findViewById(R.id.textViewOptionBlock);
        TextView textViewOptionClear = findViewById(R.id.textViewOptionClear);
        TextView textViewOptionUp = findViewById(R.id.textViewOptionUp);
        TextView textViewOptionDown = findViewById(R.id.textViewOptionBottom);
        TextView textViewOptionBg = findViewById(R.id.textViewOptionBg);
        TextView textViewOptionNoBg = findViewById(R.id.textViewOptionNone);

        if (textViewSet.getVisibility() == View.VISIBLE){
            textViewOptionBlock.setVisibility(View.GONE);
            textViewOptionUp.setVisibility(View.GONE);
            textViewSet.setVisibility(View.GONE);
            textViewOptionClear.setVisibility(View.GONE);
            textViewOptionDown.setVisibility(View.GONE);
            textViewOptionBg.setVisibility(View.GONE);
            textViewOptionNoBg.setVisibility(View.GONE);
        } else {
           // textViewOptionBlock.setVisibility(View.VISIBLE);
            textViewOptionUp.setVisibility(View.VISIBLE);
            textViewSet.setVisibility(View.VISIBLE);
            textViewOptionClear.setVisibility(View.VISIBLE);
            textViewOptionDown.setVisibility(View.VISIBLE);
            textViewOptionBg.setVisibility(View.VISIBLE);
            textViewOptionNoBg.setVisibility(View.VISIBLE);
        }
    }


    public void scrollDown(View view) {
        vibrate();
        mRecyclerView.scrollToPosition(chatList.size() - 1);
    }
    public void scrollUp(View view) {
        vibrate();
        mRecyclerView.scrollToPosition(0);
    }


    public void clearChat(View view) {
        vibrate();
        StarsocketConnector.sendMessage("clearChat " + Account.userid() + " " + chatId);
        setRecyclerView();
    }

    public void setBackground(View view) {
        vibrate();
        saveImage();
    }

    boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(ChatActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    void requestPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(ChatActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            toast("... but ... i need this permission ...");
        } else
            ActivityCompat.requestPermissions(ChatActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 123);

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
        setBg();
    }

    private void setBg() {
            String picturePath = PreferenceManager.getDefaultSharedPreferences(this).getString("picturePathBg", "");
            ImageView imageView = (ImageView) findViewById(R.id.imageViewBg);
            if(!picturePath.equals(""))
            {
                imageView.setImageBitmap(decodeFile(picturePath));
                imageView.setVisibility(View.VISIBLE);
            }
            else
            {
                imageView.setVisibility(View.INVISIBLE);
            }
    }

    @Override
    public void onRequestPermissionsResult(int PICK_IMAGE, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(PICK_IMAGE, permissions, grantResults);

        if (PICK_IMAGE == 100 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            selectImage();
        } else {
          //  toast("tap select!");
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
            PreferenceManager.getDefaultSharedPreferences(this).edit().putString("picturePathBg", picturePath).apply();
            cursor.close();
            setBg();

        }
    }

    public void noBg(View view) {
        vibrate();
        PreferenceManager.getDefaultSharedPreferences(this).edit().putString("picturePathBg", null).apply();
        setBg();
    }


    public void openProfile(View view) {
        try {
            vibrate();
            StarsocketConnector.sendMessage("getProfile " + chatId);
            Intent i = new Intent(MyApplication.getAppContext(), ProfileActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            MyApplication.getAppContext().startActivity(i);
        } catch (Exception e) {

        }
    }
}