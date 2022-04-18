package com.stardash.sportdash.plans.create;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.stardash.sportdash.settings.Account;
import com.stardash.sportdash.R;

import java.util.Locale;

public class ElementsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_elements);

        SharedPreferences pref = this.getSharedPreferences("sport", 0); // 0 - for private mode
        String name = pref.getString("selectedName", "LΛST USΞD");
        TextView textViewLastUsed = findViewById(R.id.textViewElementLastUsed);
        textViewLastUsed.setText(name.toUpperCase(Locale.ROOT));

        String id = Integer.toString(chosenId());
        String activeName = pref.getString(id+ " name", "");
        String activeDescription = pref.getString(id+ " description", "");
        String activeAdvice = pref.getString(id+ " advice", "");
        String activeSeconds = pref.getString(id+ " seconds", "");
        String activeFormat = pref.getString(id+ " format", "");
        if (activeFormat.length()<1){
            activeFormat = "seconds";
        }
        if (activeSeconds.length()<1){
            activeSeconds = "30";
        }
        if (!activeName.equals("S Ξ L Ξ C T")){
            setText(activeName, activeDescription, activeAdvice, activeSeconds, activeFormat);
        }

        setSlots();
        if (Account.isAmoled()) {
            ConstraintLayout main = findViewById(R.id.main);
            main.setBackgroundDrawable(new ColorDrawable(Color.BLACK));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Window window = getWindow();
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(Color.BLACK);
            }
        }
    }

    private void setSlots() {
        SharedPreferences pref = this.getSharedPreferences("sport", 0); // 0 - for private mode
        String slotName1 = pref.getString("1 slotName", "FRΞΞ SLOT");
        String slotName2 = pref.getString("2 slotName", "FRΞΞ SLOT");
        String slotName3 = pref.getString("3 slotName", "FRΞΞ SLOT");
        String slotName4 = pref.getString("4 slotName", "FRΞΞ SLOT");
        String slotName5 = pref.getString("5 slotName", "FRΞΞ SLOT");
        String slotName6 = pref.getString("6 slotName", "FRΞΞ SLOT");

        TextView textViewSlot1 = findViewById(R.id.textViewElement1);
        TextView textViewSlot2 = findViewById(R.id.textViewElement2);
        TextView textViewSlot3 = findViewById(R.id.textViewElement3);
        TextView textViewSlot4 = findViewById(R.id.textViewElement4);
        TextView textViewSlot5 = findViewById(R.id.textViewElement5);
        TextView textViewSlot6 = findViewById(R.id.textViewElement6);

        if(slotName1.length()<1){
            textViewSlot1.setText("Ξ M P T Y");
        } else {
            textViewSlot1.setText(slotName1);
        }
        if(slotName2.length()<1){
            textViewSlot2.setText("Ξ M P T Y");
        } else {
            textViewSlot2.setText(slotName2);
        }
        if(slotName3.length()<1){
            textViewSlot3.setText("Ξ M P T Y");
        } else {
            textViewSlot3.setText(slotName3);
        }
        if(slotName4.length()<1){
            textViewSlot4.setText("Ξ M P T Y");
        } else {
            textViewSlot4.setText(slotName4);
        }
        if(slotName5.length()<1){
            textViewSlot5.setText("Ξ M P T Y");
        } else {
            textViewSlot5.setText(slotName5);
        }
        if(slotName6.length()<1){
            textViewSlot6.setText("Ξ M P T Y");
        } else {
            textViewSlot6.setText(slotName6);
        }

    }

    public void addPause(View view) {
        vibrate();
        setText("pause","a pause","https://www.urmc.rochester.edu/news/publications/health-matters/time-out-why-you-need-to-a-break-from-exercise","30","seconds");
    }

    public void addLastUsed(View view) {
        vibrate();
        SharedPreferences pref = this.getSharedPreferences("sport", 0); // 0 - for private mode
        String name = pref.getString("selectedName", "not available");
        String description = pref.getString("selectedDescription", "not available");
        String advice = pref.getString("selectedAdvice", "not available");
        String seconds = pref.getString("selectedSeconds", "not available");
        String format = pref.getString("selectedFormat", "seconds");
        if (format.length()<1){
            format = "seconds";
        }
        if (seconds.length()<1){
            seconds = "30";
        }
        setText(name,description,advice,seconds,format);
    }

    private void setText(String elementName, String description, String image, String seconds, String format) {
        EditText editTextName = findViewById(R.id.editTextTextPersonNameElementCustom);
        EditText editTextDescription = findViewById(R.id.editTextTextPersonNameElementCustomDescription);
        EditText editTextAdvice = findViewById(R.id.editTextTextPersonNameElementCustomLink);
        EditText editTextSeconds = findViewById(R.id.editTextTextPersonNameSeconds);
        TextView textViewFormat = findViewById(R.id.textViewSeconds);

        editTextName.setText(elementName);
        editTextDescription.setText(description);
        editTextAdvice.setText(image);
        editTextSeconds.setText(seconds);
        textViewFormat.setText(format);
    }

    public void submit(View view) {
        vibrate();
        EditText editTextName = findViewById(R.id.editTextTextPersonNameElementCustom);
        EditText editTextDescription = findViewById(R.id.editTextTextPersonNameElementCustomDescription);
        EditText editTextAdvice = findViewById(R.id.editTextTextPersonNameElementCustomLink);
        EditText editTextSeconds = findViewById(R.id.editTextTextPersonNameSeconds);
        TextView textViewFormat = findViewById(R.id.textViewSeconds);

        String name = editTextName.getText().toString();
        String description = editTextDescription.getText().toString();
        String advice = editTextAdvice.getText().toString();
        String seconds = editTextSeconds.getText().toString();
        String format = textViewFormat.getText().toString();

        if (name.length()<1){
            toast("set element name first. E.g. Push Up");
        } else if (description.length()<1){
            toast("set element description first");
        } else if (seconds.length()<1){
            toast("set element duration/iterations first");
        } else {

            SharedPreferences settings = getSharedPreferences("sport", MODE_PRIVATE);
            SharedPreferences.Editor editor = settings.edit();
            editor.putString("selectedName", name).commit();
            editor.putString("selectedDescription", description).commit();
            editor.putString("selectedAdvice", advice).commit();
            editor.putString("selectedSeconds", seconds).commit();
            editor.putString("selectedFormat", format).commit();

            String id = Integer.toString(chosenId());

            editor.putString(id + " name", name.toUpperCase(Locale.ROOT)).commit();
            editor.putString(id + " description", description).commit();
            editor.putString(id + " advice", advice).commit();
            editor.putString(id + " seconds", seconds).commit();
            editor.putString(id + " format", format).commit();

            Intent i = new Intent(this, CreateStructureActivity.class);
            startActivity(i);
        }
    }

    int chosenId() {
        SharedPreferences pref = this.getSharedPreferences("sport", 0); // 0 - for private mode
        int chosen = pref.getInt("chooseId", 0);
        return chosen;
    }

    public void SKIP(View view) {
        vibrate();
        setText("DO NOT USE","Element will NOT be used and ignored totally. Useful when you don't want to use all elements.","","0","seconds");
    }

    public void saveToSlot(View view) {
        vibrate();
        TextView textViewRed = findViewById(R.id.textViewSaving);
        TextView textViewSave = findViewById(R.id.textViewSave);
        textViewRed.setVisibility(View.VISIBLE);
        textViewSave.setText("TΛP SLOT");
    }

    private void setSlot(String slot) {
        vibrate();
        TextView textViewRed = findViewById(R.id.textViewSaving);
        TextView textViewFormat = findViewById(R.id.textViewSeconds);
        EditText editTextName = findViewById(R.id.editTextTextPersonNameElementCustom);
        EditText editTextDescription = findViewById(R.id.editTextTextPersonNameElementCustomDescription);
        EditText editTextAdvice = findViewById(R.id.editTextTextPersonNameElementCustomLink);
        EditText editTextSeconds = findViewById(R.id.editTextTextPersonNameSeconds);

        if (textViewRed.getVisibility() == View.VISIBLE) {
            String name = editTextName.getText().toString();
            String description = editTextDescription.getText().toString();
            String advice = editTextAdvice.getText().toString();
            String seconds = editTextSeconds.getText().toString();
            String format = textViewFormat.getText().toString();

            SharedPreferences settings = getSharedPreferences("sport", MODE_PRIVATE);
            SharedPreferences.Editor editor = settings.edit();
            editor.putString(slot +" slotName", name).commit();
            editor.putString(slot +" slotDescription", description).commit();
            editor.putString(slot +" slotAdvice", advice).commit();
            editor.putString(slot +" slotSeconds", seconds).commit();
            editor.putString(slot +" slotFormat", format).commit();

            textViewRed.setVisibility(View.GONE);
            TextView textViewSave = findViewById(R.id.textViewSave);
            textViewSave.setText("SΛVE TO SLOT");
            setSlots();
        } else {
            SharedPreferences pref = this.getSharedPreferences("sport", 0); // 0 - for private mode
            String name = pref.getString(slot +" slotName", "not available");
            String description = pref.getString(slot +" slotDescription", "not available");
            String advice = pref.getString(slot +" slotAdvice", "not available");
            String seconds = pref.getString(slot +" slotSeconds", "0");
            String format = pref.getString(slot +" slotFormat", "seconds");
            setText(name,description,advice,seconds, format);
        }
    }

    public void slot1(View view) {
        setSlot("1");
    }
    public void slot2(View view) {
        setSlot("2");
    }
    public void slot3(View view) {
        setSlot("3");
    }
    public void slot4(View view) {
        setSlot("4");
    }
    public void slot5(View view) {
        setSlot("5");
    }
    public void slot6(View view) {
        setSlot("6");
    }

    public void changeFormat(View view) {
        vibrate();
        TextView textViewInEditText = findViewById(R.id.textViewSeconds);
        if (textViewInEditText.getText().equals("seconds")) {
            textViewInEditText.setText("iterations");
        } else {
            textViewInEditText.setText("seconds");
        }
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


    public void openHelp(View view) {
        showHelp();
    }

    private void showHelp() {
        TextView textViewHelp = findViewById(R.id.textViewHelpInfo);
        if (textViewHelp.getVisibility() == View.VISIBLE){
            textViewHelp.setVisibility(View.INVISIBLE);
        } else {
            textViewHelp.setVisibility(View.VISIBLE);
        }
    }

    public void hideHelp(View view) {
        vibrate();
        showHelp();
    }

    private void vibrate() {
        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            v.vibrate(VibrationEffect.createOneShot(100, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            v.vibrate(100);
        }
    }

    public void openImages(View view) {
        Intent i = new Intent(this, ImagesActivity.class);
        startActivity(i);
        vibrate();
    }
}