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
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.stardash.sportdash.settings.Account;
import com.stardash.sportdash.MainActivity;
import com.stardash.sportdash.R;

public class CreateStructureActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_structure);

        setElements();
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

    private void setElements() {
        TextView textViewStructure1 = findViewById(R.id.textViewStructure1);
        TextView textViewStructure2 = findViewById(R.id.textViewStructure2);
        TextView textViewStructure3 = findViewById(R.id.textViewStructure3);
        TextView textViewStructure4 = findViewById(R.id.textViewStructure4);
        TextView textViewStructure5 = findViewById(R.id.textViewStructure5);
        TextView textViewStructure6 = findViewById(R.id.textViewStructure6);
        TextView textViewStructure7 = findViewById(R.id.textViewStructure7);
        TextView textViewStructure8 = findViewById(R.id.textViewStructure8);
        TextView textViewStructure9 = findViewById(R.id.textViewStructure9);
        TextView textViewStructure10 = findViewById(R.id.textViewStructure10);
        TextView textViewStructure11 = findViewById(R.id.textViewStructure11);
        TextView textViewStructure12 = findViewById(R.id.textViewStructure12);
        TextView textViewStructure13 = findViewById(R.id.textViewStructure13);
        TextView textViewStructure14 = findViewById(R.id.textViewStructure14);
        TextView textViewStructure15 = findViewById(R.id.textViewStructure15);
        TextView textViewStructure16 = findViewById(R.id.textViewStructure16);
        TextView textViewStructure17 = findViewById(R.id.textViewStructure17);
        TextView textViewStructure18 = findViewById(R.id.textViewStructure18);
        TextView textViewStructure19 = findViewById(R.id.textViewStructure19);
        TextView textViewStructure20 = findViewById(R.id.textViewStructure20);
        TextView textViewStructure21 = findViewById(R.id.textViewStructure21);
        TextView textViewStructure22 = findViewById(R.id.textViewStructure22);
        TextView textViewStructure23 = findViewById(R.id.textViewStructure23);
        TextView textViewStructure24 = findViewById(R.id.textViewStructure24);
        TextView textViewStructure25 = findViewById(R.id.textViewStructure25);

        SharedPreferences pref = this.getSharedPreferences("sport", 0); // 0 - for private mode

        String structure1 = pref.getString("1 name", "C H O O S E");
        String structure2 = pref.getString("2 name", "C H O O S E");
        String structure3 = pref.getString("3 name", "C H O O S E");
        String structure4 = pref.getString("4 name", "C H O O S E");
        String structure5 = pref.getString("5 name", "C H O O S E");
        String structure6 = pref.getString("6 name", "C H O O S E");
        String structure7 = pref.getString("7 name", "C H O O S E");
        String structure8 = pref.getString("8 name", "C H O O S E");
        String structure9 = pref.getString("9 name", "C H O O S E");
        String structure10 = pref.getString("10 name", "C H O O S E");
        String structure11 = pref.getString("11 name", "C H O O S E");
        String structure12 = pref.getString("12 name", "C H O O S E");
        String structure13 = pref.getString("13 name", "C H O O S E");
        String structure14 = pref.getString("14 name", "C H O O S E");
        String structure15 = pref.getString("15 name", "C H O O S E");
        String structure16 = pref.getString("16 name", "C H O O S E");
        String structure17 = pref.getString("17 name", "C H O O S E");
        String structure18 = pref.getString("18 name", "C H O O S E");
        String structure19 = pref.getString("19 name", "C H O O S E");
        String structure20 = pref.getString("20 name", "C H O O S E");
        String structure21 = pref.getString("21 name", "C H O O S E");
        String structure22 = pref.getString("22 name", "C H O O S E");
        String structure23 = pref.getString("23 name", "C H O O S E");
        String structure24 = pref.getString("24 name", "C H O O S E");
        String structure25 = pref.getString("25 name", "C H O O S E");

        textViewStructure1.setText(structure1);
        textViewStructure2.setText(structure2);
        textViewStructure3.setText(structure3);
        textViewStructure4.setText(structure4);
        textViewStructure5.setText(structure5);
        textViewStructure6.setText(structure6);
        textViewStructure7.setText(structure7);
        textViewStructure8.setText(structure8);
        textViewStructure9.setText(structure9);
        textViewStructure10.setText(structure10);
        textViewStructure11.setText(structure11);
        textViewStructure12.setText(structure12);
        textViewStructure13.setText(structure13);
        textViewStructure14.setText(structure14);
        textViewStructure15.setText(structure15);
        textViewStructure16.setText(structure16);
        textViewStructure17.setText(structure17);
        textViewStructure18.setText(structure18);
        textViewStructure19.setText(structure19);
        textViewStructure20.setText(structure20);
        textViewStructure21.setText(structure21);
        textViewStructure22.setText(structure22);
        textViewStructure23.setText(structure23);
        textViewStructure24.setText(structure24);
        textViewStructure25.setText(structure25);

    }

    private void selectElement(int chosen) {
        vibrate();
        SharedPreferences settings = getSharedPreferences("sport", MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt("chooseId", chosen).commit();

        Intent i = new Intent(this, ElementsActivity.class);
        startActivity(i);
    }

    public void structure1(View view) {
        selectElement(1);
    }
    public void structure2(View view) {
        selectElement(2);
    }
    public void structure3(View view) {
        selectElement(3);
    }
    public void structure4(View view) {
        selectElement(4);
    }
    public void structure5(View view) {
        selectElement(5);
    }
    public void structure6(View view) {
        selectElement(6);
    }
    public void structure7(View view) {
        selectElement(7);
    }
    public void structure8(View view) {
        selectElement(8);
    }
    public void structure9(View view) {
        selectElement(9);
    }
    public void structure10(View view) {
        selectElement(10);
    }
    public void structure11(View view) {
        selectElement(11);
    }
    public void structure12(View view) {
        selectElement(12);
    }
    public void structure13(View view) {
        selectElement(13);
    }
    public void structure14(View view) {
        selectElement(14);
    }
    public void structure15(View view) {
        selectElement(15);
    }
    public void structure16(View view) {
        selectElement(16);
    }
    public void structure17(View view) {
        selectElement(17);
    }
    public void structure18(View view) {
        selectElement(18);
    }
    public void structure19(View view) {
        selectElement(19);
    }
    public void structure20(View view) {
        selectElement(20);
    }
    public void structure21(View view) {
        selectElement(21);
    }
    public void structure22(View view) {
        selectElement(22);
    }
    public void structure23(View view) {
        selectElement(23);
    }
    public void structure24(View view) {
        selectElement(24);
    }
    public void structure25(View view) {
        selectElement(25);
    }

    public void showSportOptions(View view) {
        vibrate();
        showSportOptionsAction();
    }

    private void showSportOptionsAction() {
        TextView textViewDiscard = findViewById(R.id.textViewDiscard);
        TextView textViewFinish = findViewById(R.id.textViewFinish);
        TextView textViewContinueLater = findViewById(R.id.textViewContinue);
        TextView textViewTop = findViewById(R.id.textViewTop);
        TextView textViewInfo = findViewById(R.id.textViewInfo);

        if (textViewDiscard.getVisibility() != View.GONE){
            textViewDiscard.setVisibility(View.GONE);
            textViewFinish.setVisibility(View.GONE);
            textViewContinueLater.setVisibility(View.GONE);
            textViewTop.setVisibility(View.VISIBLE);
            textViewInfo.setVisibility(View.VISIBLE);
        } else {
            textViewDiscard.setVisibility(View.VISIBLE);
            textViewFinish.setVisibility(View.VISIBLE);
            textViewContinueLater.setVisibility(View.VISIBLE);
            textViewTop.setVisibility(View.INVISIBLE);
            textViewInfo.setVisibility(View.GONE);
        }
    }

    public void continueLater(View view) {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
        vibrate();
    }

    public void finish(View view) {
        Intent i = new Intent(this, CreatePlanActivity.class);
        startActivity(i);
        vibrate();
    }

    public void discard(View view) {

        SharedPreferences settings = getSharedPreferences("sport", MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();


        for (int i = 0; i < 65; i++) {
            System.out.println("Yes");

            editor.putString(i+ " name","S Ξ L Ξ C T");
            editor.putString(i+ " description","");
            editor.putString(i+ " advice","");
            editor.putString(i+ " seconds","");
        }

        editor.commit();
        setElements();

        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
        vibrate();
    }

    public void openPlanStructureGuide(View view) {
        vibrate();
        Intent i = new Intent(this, GuideStructurePlanActivity.class);
        startActivity(i);
    }

    @Override
    public void onBackPressed() {
        showSportOptionsAction();
        vibrate();
    }

    private void vibrate() {
        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            v.vibrate(VibrationEffect.createOneShot(100, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            v.vibrate(100);
        }
    }
}