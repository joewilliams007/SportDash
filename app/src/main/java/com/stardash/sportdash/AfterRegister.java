package com.stardash.sportdash;

import static com.stardash.sportdash.Account.username;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.View;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;

public class AfterRegister extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_after_register);
        listenSeekbarEnergy();
        toastRegistered("successfully registered "+Account.username());
        final Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                toastUrID("your personal id is #"+Account.userid());
            }
        }, 3000);
    }

    public void openMain(View view) {
        EditText editTextNumberWeight = findViewById(R.id.editTextNumberWeight);
        EditText editTextNumberAge = findViewById(R.id.editTextNumberAge);

        SharedPreferences settings = getSharedPreferences("account", MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();

        if (editTextNumberWeight.getText().toString().length()<1){
            toast("enter your weight first");
        } else if (editTextNumberAge.getText().toString().length()<1){
            toast("enter your age first");
        } else {
            Account.setAge(Integer.parseInt(editTextNumberAge.getText().toString()));
            Account.setWeight(Integer.parseInt(editTextNumberWeight.getText().toString()));
            try {
                StarsocketConnector.sendMessage("setWeight "+Account.userid()+" "+editTextNumberWeight.getText().toString());
                StarsocketConnector.sendMessage("setAge "+Account.userid()+" "+editTextNumberAge.getText().toString());
            } catch (Exception e){
                toast("network error");
            }
            Intent i = new Intent(this,MainActivity.class);
            startActivity(i);
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
        }, 2000);
    }

    public void toastRegistered(String message){
        TextView textViewCustomToast = findViewById(R.id.textViewCustomToast);
        textViewCustomToast.setVisibility(View.VISIBLE);
        textViewCustomToast.setText(Account.errorStyle()+" "+message);
        final Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                textViewCustomToast.setVisibility(View.GONE);
            }
        }, 2500);
    }

    public void toastUrID(String message){
        TextView textViewCustomToast = findViewById(R.id.textViewCustomToast);
        textViewCustomToast.setVisibility(View.VISIBLE);
        textViewCustomToast.setText(Account.errorStyle()+" "+message);
        final Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                textViewCustomToast.setVisibility(View.GONE);
            }
        }, 5000);
    }

    private void listenSeekbarEnergy() {
        SeekBar seekBar = (SeekBar)findViewById(R.id.seekBar);
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
                try{
                    int number = Integer.parseInt(String.valueOf(progress));
                    SharedPreferences settings = getSharedPreferences("account", MODE_PRIVATE);
                    SharedPreferences.Editor editor = settings.edit();
                    editor.putInt("energy", number+1);
                    editor.apply();

                    TextView textViewGood = findViewById(R.id.textViewGood);
                    textViewGood.setText(String.valueOf(number+1)+"/5");

                }
                catch (NumberFormatException ex){
                    ex.printStackTrace();
                }
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



    @Override
    public void onBackPressed() {
    }
}