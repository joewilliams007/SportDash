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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.stardash.sportdash.Account;
import com.stardash.sportdash.plans.run.PlanActivity;
import com.stardash.sportdash.R;
import com.stardash.sportdash.ResultActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

public class CreatePlanActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_plan);

        if (isEditing()) {

            try {
                TextView textViewCategory = findViewById(R.id.textViewChoose);
                if (activity().length() > 1) {
                    textViewCategory.setText(activity());
                }
            } catch (Exception e){

            }

            SharedPreferences settings = getSharedPreferences("sport", MODE_PRIVATE);
            SharedPreferences.Editor editor = settings.edit();
            editor.putBoolean("editing", false).commit();
        }

        SharedPreferences settings = getSharedPreferences("sport", MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        int number = 0;
        editor.putInt("planDifficulty", number).commit();
        editor.putInt("planDifficulty", number).commit();

        generateResult();
        listenSeekbarDifficulty();
        listenSeekbarPower();
        listenSeekbarIterations();
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

    private void generateResult() {
        TextView textViewDuration = findViewById(R.id.textViewLengthMin);
        TextView textViewSummary = findViewById(R.id.textViewSummary);
        textViewSummary.setText(trainingPlan());
        textViewDuration.setText(String.valueOf(trainingPlanDuration())+ " seconds or "+String.valueOf(trainingPlanDuration()/60)+ " minutes");
    }

    private void listenSeekbarIterations() {
        SeekBar seekBar = (SeekBar)findViewById(R.id.seekBarIterations);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,
                                          boolean fromUser) {
                // TODO Auto-generated method stub
                Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    v.vibrate(VibrationEffect.createOneShot(100, VibrationEffect.DEFAULT_AMPLITUDE));
                } else {
                    //deprecated in API 26
                    v.vibrate(100);
                }
                SharedPreferences settings = getSharedPreferences("sport", MODE_PRIVATE);
                SharedPreferences.Editor editor = settings.edit();
                try{
                    int number = Integer.parseInt(String.valueOf(progress));
                    editor.putInt("planIterations", number).commit();

                    TextView textViewIterationsMin = findViewById(R.id.textViewIterationsMin);
                    int totalTime = trainingPlanDuration()*(number+1)/60;
                    textViewIterationsMin.setText(String.valueOf(number+1)+" * "+String.valueOf(trainingPlanDuration()/60)+" min = "+String.valueOf(totalTime)+" min");

                }
                catch (NumberFormatException ex){
                    ex.printStackTrace();
                }
                generateResult();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }
        });
    }

    private void listenSeekbarPower() {
        SeekBar seekBar = (SeekBar)findViewById(R.id.seekBarPower);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,
                                          boolean fromUser) {
                // TODO Auto-generated method stub
                Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    v.vibrate(VibrationEffect.createOneShot(100, VibrationEffect.DEFAULT_AMPLITUDE));
                } else {
                    //deprecated in API 26
                    v.vibrate(100);
                }
                SharedPreferences settings = getSharedPreferences("sport", MODE_PRIVATE);
                SharedPreferences.Editor editor = settings.edit();
                try{
                    int number = Integer.parseInt(String.valueOf(progress));
                    editor.putInt("planPower", number).commit();

                    TextView textViewPower = findViewById(R.id.textViewGood);
                    textViewPower.setText(String.valueOf(number+1)+"/5");

                }
                catch (NumberFormatException ex){
                    ex.printStackTrace();
                }
                generateResult();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }
        });
    }

    private void listenSeekbarDifficulty() {
        SeekBar seekBar = (SeekBar)findViewById(R.id.seekBarDifficulty);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,
                                          boolean fromUser) {
                // TODO Auto-generated method stub
                Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    v.vibrate(VibrationEffect.createOneShot(100, VibrationEffect.DEFAULT_AMPLITUDE));
                } else {
                    //deprecated in API 26
                    v.vibrate(100);
                }
                SharedPreferences settings = getSharedPreferences("sport", MODE_PRIVATE);
                SharedPreferences.Editor editor = settings.edit();
                try{
                    int number = Integer.parseInt(String.valueOf(progress));
                    editor.putInt("planDifficulty", number).commit();

                    TextView textViewDifficulty = findViewById(R.id.textViewDifficultyLevel);
                    if (number==0){
                        textViewDifficulty.setText("normal");
                    } else if (number==1){
                        textViewDifficulty.setText("hard");
                    }else if (number==2){
                        textViewDifficulty.setText("ultra hard");
                    }else if (number==3){
                        textViewDifficulty.setText("unbeatable hard");
                    }
                }
                catch (NumberFormatException ex){
                    ex.printStackTrace();
                }
                generateResult();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }
        });
    }

    public void openCategories(View view) {
        SharedPreferences settings = getSharedPreferences("sport", MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean("create", true).commit();
        editor.putBoolean("editing", true).commit();

        Intent i = new Intent(this, ResultActivity.class);
        startActivity(i);
    }

    String activity() {
        SharedPreferences pref = this.getSharedPreferences("sport", 0); // 0 - for private mode
        String activity = pref.getString("whatSport", null);
        return activity;
    }

    boolean isEditing() {
        SharedPreferences pref = this.getSharedPreferences("sport", 0); // 0 - for private mode
        boolean create = pref.getBoolean("editing", true);
        return create;
    }



    StringBuilder trainingPlan(){
        EditText editTextName = findViewById(R.id.editTextTextPersonNameElementCustom);
        EditText editTextDescription = findViewById(R.id.editTextTextPersonNameElementCustomDescription);
        TextView textViewCategory = findViewById(R.id.textViewChoose);

        SharedPreferences pref = this.getSharedPreferences("sport", 0); // 0 - for private mode
        StringBuilder trainingPlanNames = new StringBuilder();

        trainingPlanNames.append("trainingsPLan SportDash App"); // 0
        trainingPlanNames.append("\n");
        trainingPlanNames.append(trainingPlanDuration()+" "+difficulty()+" "+power()+" "+iterations()); // 1
        trainingPlanNames.append("\n");
        trainingPlanNames.append(editTextName.getText().toString()); // 2
        trainingPlanNames.append("\n");
        trainingPlanNames.append(editTextDescription.getText().toString()); // 3
        trainingPlanNames.append("\n");
        trainingPlanNames.append(textViewCategory.getText().toString()); // 4
        trainingPlanNames.append("\n");
        trainingPlanNames.append(Account.username()); // 5
        trainingPlanNames.append("\n");
        trainingPlanNames.append(Account.userid()); // 6
        trainingPlanNames.append("\n");

        int randomNumber = new Random().nextInt(900000) + 1000; // get and save random number for upload
        SharedPreferences settings = getSharedPreferences("sport", MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("planId", Account.userid()+"-"+String.valueOf(randomNumber)).apply();

        trainingPlanNames.append(Account.userid()+"-"+String.valueOf(randomNumber)); // 7 --> Every user has their own plan id´s. Example user id = #JSAHFOIAHSFO87? plan id = #JSAHFOIAHSFO87?1
        trainingPlanNames.append("\n");
        trainingPlanNames.append(date()); // 8 get current date and time
        trainingPlanNames.append("\n");
        trainingPlanNames.append(dateInMs()); // 9 get timestamp in millis -> so we can sort them easier later
        trainingPlanNames.append("\n");
        trainingPlanNames.append("#12345G"); // Stars will append users # so you can view profile of starred people
        trainingPlanNames.append("\n");
        trainingPlanNames.append("comments"); // 11 Comments will be shown here
        trainingPlanNames.append("\n");
        trainingPlanNames.append("unused"); // 12 unused. Maybe usage in future who will know
        trainingPlanNames.append("\n");

        for (int i = 0; i < 26; i++) {
            String add = pref.getString(i+" name", "C H O O S E"); // 13
            trainingPlanNames.append("&"+add);
        }
        trainingPlanNames.append("\n");
        for (int i = 0; i < 26; i++) {
            String add = pref.getString(i+" description", "C H O O S E"); // 14
            trainingPlanNames.append("&"+add);
        }
        trainingPlanNames.append("\n");
        for (int i = 0; i < 26; i++) {
            String add = pref.getString(i+" advice", "C H O O S E"); // 15
            trainingPlanNames.append("&"+add);
        }
        trainingPlanNames.append("\n");
        for (int i = 0; i < 26; i++) {
            String add = pref.getString(i+" seconds", "C H O O S E"); // 16
            trainingPlanNames.append("&"+add);
        }
        trainingPlanNames.append("\n");
        for (int i = 0; i < 26; i++) {
            String add = pref.getString(i+" format", "C H O O S E"); // 17
            trainingPlanNames.append("&"+add);
        }

        return trainingPlanNames;
    }

    private String date() {
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        return formatter.format(date);
    }

    private String dateInMs() {
        return String.valueOf(System.currentTimeMillis());
    }

    int trainingPlanDuration(){
        SharedPreferences pref = this.getSharedPreferences("sport", 0); // 0 - for private mode

        int duration = 0;

        for (int i = 0; i < 26; i++) {
            String addString = pref.getString(i+" seconds", "C H O O S E");
            try{
                int number = Integer.parseInt(addString);
                System.out.println(number); // output = 25
                duration += number;
            }
            catch (NumberFormatException ex){
                ex.printStackTrace();
            }


        }

        return duration;
    }

    int difficulty() {
        SharedPreferences pref = this.getSharedPreferences("sport", 0); // 0 - for private mode
        int power = pref.getInt("planDifficulty", 0);

        return power;
    }

    int power() {
        SharedPreferences pref = this.getSharedPreferences("sport", 0); // 0 - for private mode
        int power = pref.getInt("planPower", 0)+1;

        return power;
    }

    int iterations() {
        SharedPreferences pref = this.getSharedPreferences("sport", 0); // 0 - for private mode
        int iterations = pref.getInt("planIterations", 0)+1;
        return iterations;
    }

    public void generate(View view) {
        generateResult();
    }

    public void submit(View view) {
        String s = trainingPlan().toString().toLowerCase(Locale.ROOT);
        // FILTER BAD LINKS/WORDS OUT!
        if (s.contains("sex")|s.contains("fuck")|s.contains("naked")|s.contains("porn")|s.contains("xhamster")|s.contains("cock")|s.contains("dick")|s.contains("pussy")|s.contains("virgina")|s.contains("bit.ly")){
            Toast.makeText(getApplicationContext(),"Bad word/link detected. Please remove first :(",Toast.LENGTH_LONG).show();

            Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                v.vibrate(VibrationEffect.createOneShot(100, VibrationEffect.DEFAULT_AMPLITUDE));
            } else {
                v.vibrate(100);
            }
        } else {
            EditText editTextName = findViewById(R.id.editTextTextPersonNameElementCustom);
            EditText editTextDescription = findViewById(R.id.editTextTextPersonNameElementCustomDescription);
            TextView textViewCategory = findViewById(R.id.textViewChoose);
            TextView textViewMin = findViewById(R.id.textViewLengthMin);
            int duration = Integer.parseInt(textViewMin.getText().toString().split(" ")[0]);

            if (editTextName.getText().toString().length()<1){
                toast("set training plan name first");
            } else if (editTextDescription.getText().toString().length()<1){
                toast("set training plan description first");
            } else if (textViewCategory.getText().toString().equals("C H O O S Ξ C A T Ξ G O R Y")) {
                toast("select a category first");
            } else if (duration<2){
                CheckBox checkBox = findViewById(R.id.checkBox);
                if (checkBox.isChecked()) {
                    toast("the workout is to short to be public");
                } else {
                    completeGeneratePlan();
                }

            } else {
                completeGeneratePlan();
            }

        }
    }

    private void completeGeneratePlan() {
        SharedPreferences settings = getSharedPreferences("sport", MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        CheckBox checkBox = findViewById(R.id.checkBox);
        if (checkBox.isChecked()) {
            editor.putBoolean("online",true).apply();
        } else {
            editor.putBoolean("online",false).apply();
        }

        TextView textViewCategory = findViewById(R.id.textViewChoose);
        editor.putString("category", String.valueOf(textViewCategory.getText().toString())).commit();
        editor.putString("committed plan", String.valueOf(trainingPlan())).commit();
        Intent i = new Intent(this, PlanActivity.class);
        startActivity(i);
        addPlan();
    }

    public void addPlan(){
        SharedPreferences settings = getSharedPreferences("sport", MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt("planTotal", myTotalPlans()+1).commit();
    }

    int myTotalPlans() {
        SharedPreferences pref = this.getSharedPreferences("sport", 0); // 0 - for private mode
        int plans = pref.getInt("planTotal", 0);
        return plans;
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