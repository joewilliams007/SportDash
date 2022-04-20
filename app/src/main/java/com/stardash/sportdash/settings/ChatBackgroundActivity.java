package com.stardash.sportdash.settings;

import static android.graphics.BitmapFactory.decodeFile;

import static com.stardash.sportdash.settings.app.vibrate;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.stardash.sportdash.R;
import com.stardash.sportdash.online.chat.ChatActivity;

public class ChatBackgroundActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_background);
        setBg();

        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(Color.parseColor("#212121"));
        if (Account.isAmoled()) {
            ConstraintLayout main = findViewById(R.id.main);
            main.setBackgroundDrawable(new ColorDrawable(Color.BLACK));
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.parseColor("#000000"));
        }
    }

    public void setBg() {
        String picturePath = PreferenceManager.getDefaultSharedPreferences(this).getString("picturePathBg", "");
        ImageView imageView = (ImageView) findViewById(R.id.imageView);
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

    public void setBackground(View view) {
        vibrate();
        saveImage();
    }

    boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(ChatBackgroundActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    void requestPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(ChatBackgroundActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            toast("... but ... i need this permission ...");
        } else
            ActivityCompat.requestPermissions(ChatBackgroundActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 123);

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