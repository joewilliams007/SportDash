package com.stardash.sportdash;

import static android.graphics.BitmapFactory.*;
import static com.stardash.sportdash.ProfileActivity.chatId;
import static com.stardash.sportdash.ProfileActivity.chatUsername;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.BitmapFactory;
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
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

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
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        setContentView(R.layout.activity_chat);
        TextView textViewTop = findViewById(R.id.textViewTop);
        textViewTop.setText(chatUsername);
        showSet();

        ((TextView) findViewById(R.id.textViewOptionRefresh)).setVisibility(View.GONE);
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
        setRecyclerView();
        setBg();

        mRecyclerView = findViewById(R.id.chat_recycler_view);
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0) {
                    ((TextView) findViewById(R.id.textViewOptionRefresh)).setVisibility(View.GONE);
                } else {
                    ((TextView) findViewById(R.id.textViewOptionRefresh)).setVisibility(View.VISIBLE);
                }
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

    public void refresh(View view) {
        ((TextView) findViewById(R.id.textViewOptionRefresh)).setVisibility(View.GONE);
        ((ProgressBar) findViewById(R.id.progressBar)).setVisibility(View.VISIBLE);
        vibrate();
        setRecyclerView();
    }


    public static Boolean updateChat;
    static ArrayList<ChatItem> chatList;
    private void setRecyclerView() {
       try {
        StarsocketConnector.sendMessage("getChat "+Account.userid()+" "+chatId);
        final Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                    try {

                        String s[] = StarsocketConnector.getMessage().split("NEXTMESSAGEIS:;");

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
                        ((TextView) findViewById(R.id.textViewOptionRefresh)).setVisibility(View.VISIBLE);
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
            ((TextView) findViewById(R.id.textViewOptionRefresh)).setVisibility(View.GONE);
                StarsocketConnector.sendMessage("chat " + Account.userid() + " " + chatId + " MESSAGE&" + editTextMessage.getText().toString().replace("#","(hashtag)")+ " MESSAGE&" + Account.username());
                editTextMessage.setText("");
            setRecyclerView();
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

    private void vibrate() {
        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            v.vibrate(VibrationEffect.createOneShot(100, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            v.vibrate(100);
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
}