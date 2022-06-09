package com.stardash.sportdash.online.chat;

import static android.graphics.BitmapFactory.*;
import static com.stardash.sportdash.online.friends.FriendsActivity.chatId;
import static com.stardash.sportdash.online.friends.FriendsActivity.chatUsername;
import static com.stardash.sportdash.online.friends.FriendsActivity.openedChat;
import static com.stardash.sportdash.online.friends.FriendsActivity.tappedOnSearchItem;
import static com.stardash.sportdash.online.friends.FriendsActivity.tappedOnSearchItemId;
import static com.stardash.sportdash.settings.app.vibrate;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.pm.ShortcutInfoCompat;
import androidx.core.content.pm.ShortcutManagerCompat;
import androidx.core.graphics.drawable.IconCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.view.DragEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.stardash.sportdash.network.tcp.getMessage;
import com.stardash.sportdash.online.ProfileActivity;
import com.stardash.sportdash.online.friends.FriendsActivity;
import com.stardash.sportdash.settings.Account;
import com.stardash.sportdash.MainActivity;
import com.stardash.sportdash.R;
import com.stardash.sportdash.network.tcp.StarsocketConnector;
import com.stardash.sportdash.settings.MyApplication;
import com.stardash.sportdash.settings.chat.ChatBackgroundActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ChatActivity extends AppCompatActivity {

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        updateChat = false;
        editingMessage = false;
        updateChat = true;
        isInChat = true;
        isNew = true;
        isReply = false;
        openedChat = true;
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        setContentView(R.layout.activity_chat);
        TextView textViewTop = findViewById(R.id.textViewTop);


        Bundle extras = getIntent().getExtras(); // get link hashtag if opened from link
        if (extras != null) {
            chatUsername = extras.getString("USERNAME").toString();
            chatId = extras.getString("ID").toString();
        }
        String savedChat = Account.chat(chatId);
        if (savedChat!=null){
            createList(savedChat);
        }
        check();
        textViewTop.setText(chatUsername);
        showSet();
        setBg();

        Window window = getWindow();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

        }

        if (Account.isAmoled()) {
            ConstraintLayout main = findViewById(R.id.main);
            main.setBackgroundDrawable(new ColorDrawable(Color.BLACK));
            TextView textView = findViewById(R.id.textViewHome);
            textView.setTextColor(Color.parseColor("#FFFFFF"));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                window.setStatusBarColor(Color.parseColor("#000000"));
            }
        }
        ((ProgressBar) findViewById(R.id.progressBar)).setVisibility(View.VISIBLE);
       // setRecyclerView();


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

                } else if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {

                } else {

                }
            }
        });
    }

    public void onPause(){
        super.onPause();
        scheduleTaskExecutor.shutdown();
        isInChat = false;
    }
    public void onResume(){
        super.onResume();
        try {
            scheduleTaskExecutor.shutdown();
        } catch (Exception ignored){

        }
        isInChat = true;
        check();
    }
    public void onDestroy(){
        super.onDestroy();
        isInChat = false;
        scheduleTaskExecutor.shutdown();
    }
    public void onStop(){
        super.onStop();
        isInChat = false;
        scheduleTaskExecutor.shutdown();
    }
    public void onBackPressed(){
        super.onBackPressed();
        isInChat = false;
        scheduleTaskExecutor.shutdown();
        finish();
    }
        public ScheduledExecutorService scheduleTaskExecutor;
    public static Boolean isNew;
    public static Boolean isInChat = false;
    public void check(){
        scheduleTaskExecutor = Executors.newScheduledThreadPool(5);

        // This schedule a runnable task every 2 minutes
        scheduleTaskExecutor.scheduleAtFixedRate(new Runnable() {
            public void run() {
                getData();
                if (!isInChat){
                    scheduleTaskExecutor.shutdown();
                }
            }
        }, 0, 2, TimeUnit.SECONDS);
    }

    public void refreshChat(View view) {
    }

    public static Boolean updateChat;
    private void getData() {
        try {
            StarsocketConnector.sendMessage("getChat "+chatId);
            final Handler handler = new Handler(Looper.getMainLooper());
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    try {
                       String received = StarsocketConnector.getMessage().replaceAll("undefined", "");
                        createList(received);
                        isInChat = true;
                    } catch (Exception e) {
                        toast("no network");
                    }
                }
            }, 200);

        } catch (Exception e){
            toast("no network");
            ((ProgressBar) findViewById(R.id.progressBar)).setVisibility(View.INVISIBLE);
        }
    }

    private ArrayList<ChatItem> mChatList;
    private RecyclerView mRecyclerView;
    private ChatAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;

    public String lastMessageId = "0";
    public String newMessageId = "00";
    public int currentChats = 0;
    public void createList(String data){
        String[] user = data.split("NEXTTEXTMESSAGE");

        mChatList = new ArrayList<>();
        String separator = "CHAT_DIVIDER";

        for (String element : user) {
            try {

                String message_id = element.split(separator)[0];
                String text = element.split(separator)[1].replaceAll("-hashtag","#");
                String to_id = element.split(separator)[2];
                String from_id = element.split(separator)[3];
                String viewed = element.split(separator)[4];
                String edited = element.split(separator)[5];
                String deleted = element.split(separator)[6];
                String date = element.split(separator)[7];
                String type = element.split(separator)[8];
                String from_name = element.split(separator)[9];

                if (currentChats == 0){
                    newMessageId = message_id;
                    currentChats++;
                }

                if (deleted.equals("1")) {
                    text = "this message was deleted";
                }

                Calendar calendar = Calendar.getInstance(); isInChat = false;
                calendar.setTimeInMillis(Long.parseLong(date)*1000);
                @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
                String dateString = sdf.format(calendar.getTime());

                if (!text.equals("editedMessage")) {
                    mChatList.add(new ChatItem(from_name,text,dateString,from_id+" "+to_id+" "+message_id+" "+viewed+" "+deleted+" "+edited));
                }


            } catch (Exception ignored) {

            }
        }
        isInChat = true;
        if (lastMessageId.equals(newMessageId)) {

        } else {
            buildRecyclerView();
            Account.setChat(data,chatId);
            lastMessageId = newMessageId;
        }
        currentChats = 0;
    }

    public static Boolean editingMessage = false;
    public void buildRecyclerView(){
        mRecyclerView = findViewById(R.id.chat_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(ChatActivity.this);
        mLayoutManager.setStackFromEnd(true);
        mLayoutManager.setReverseLayout(true);
        mAdapter = new ChatAdapter(mChatList);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
        isInChat = true;
        ((ProgressBar) findViewById(R.id.progressBar)).setVisibility(View.GONE);
        mRecyclerView.scrollToPosition(0);
        mRecyclerView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v,
                                       int left, int top, int right, int bottom,
                                       int oldLeft, int oldTop, int oldRight, int oldBottom) {
                if (bottom < oldBottom) {
                    if (!editingMessage) {
                        mRecyclerView.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                mRecyclerView.smoothScrollToPosition(0);
                            }
                        }, 100);
                    }
                }
            }
        });

    }


    public static Boolean isReply = false;
    public static String replyText = "";
    public void sendMessage(View view) {
        vibrate();
        EditText editTextMessage = findViewById(R.id.editTextTextPersonName);
        if (editTextMessage.getText().toString().length()<1){
            toast("enter a message");
            isInChat = true;
        } else if (editTextMessage.getText().toString().length()>2000){
            toast("message is too long");
            isInChat = true;
        } else if (editTextMessage.getText().toString().replaceAll(" ","").length()<1) {

        } else {
                ((ProgressBar) findViewById(R.id.progressBar)).setVisibility(View.VISIBLE);
                if (isReply) {
                    StarsocketConnector.sendMessage("chat " + chatId + " reply TEXTMESSAGESP:" + editTextMessage.getText().toString().replace("#","-hashtag")+"TEXTMESSAGESP:"+replyText);
                } else {
                    StarsocketConnector.sendMessage("chat " + chatId + " noReply TEXTMESSAGESP:" + editTextMessage.getText().toString().replace("#","-hashtag"));
                }
               editTextMessage.setText("");
            isInChat = true;
          //  setRecyclerView();
        }

    }

    public void home(View view) {
        vibrate();
        Intent i = new Intent(this, MainActivity.class);
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

    public void showOptions(View view) {
       showSet();
       vibrate();
    }

    private void showSet() {
        ConstraintLayout constraintLayoutSet = findViewById(R.id.constraintLayoutSet);
        if ( constraintLayoutSet.getVisibility() == View.VISIBLE){
            constraintLayoutSet.setVisibility(View.GONE);
        } else {
            constraintLayoutSet.setVisibility(View.VISIBLE);
        }
    }


    public void scrollDown(View view) {
        vibrate();
        mRecyclerView.smoothScrollToPosition((mChatList.size() - 1));
    }
    public void scrollUp(View view) {
        vibrate();
        mRecyclerView.smoothScrollToPosition(0);
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
            tappedOnSearchItem = true;
            tappedOnSearchItemId = chatId;
            Intent i = new Intent(MyApplication.getAppContext(), FriendsActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            MyApplication.getAppContext().startActivity(i);
        } catch (Exception e) {
            toast("no network");
        }
    }


    public void goBack(View view) {
        scheduleTaskExecutor.shutdown();
        isInChat = false;
        vibrate();
        finish();
    }

    public void addShortcut(View view) {
        vibrate();
        if (ShortcutManagerCompat.isRequestPinShortcutSupported(getApplicationContext())) {
            ShortcutInfoCompat shortcutInfo = new ShortcutInfoCompat.Builder(getApplicationContext(), chatId)
                    .setIntent(new Intent(getApplicationContext(), ChatActivity.class).setAction(Intent.ACTION_MAIN)
                            .putExtra("ID",chatId)
                            .putExtra("USERNAME",chatUsername)
                    ) // !!! intent's action must be set on oreo
                    .setShortLabel(chatUsername)
                    .setIcon(IconCompat.createWithResource(getApplicationContext(), R.drawable.star1))
                    .build();
            ShortcutManagerCompat.requestPinShortcut(getApplicationContext(), shortcutInfo, null);
        } else {
            Toast.makeText(ChatActivity.this,"launcher does not support short cut icon",Toast.LENGTH_LONG).show();
        }
    }
}