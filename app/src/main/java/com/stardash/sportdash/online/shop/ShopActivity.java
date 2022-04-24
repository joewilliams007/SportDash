package com.stardash.sportdash.online.shop;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import com.stardash.sportdash.BuildConfig;
import com.stardash.sportdash.MainActivity;
import com.stardash.sportdash.R;
import com.stardash.sportdash.network.tcp.StarsocketConnector;
import com.stardash.sportdash.settings.Account;

public class ShopActivity extends AppCompatActivity {

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        setContentView(R.layout.activity_shop);



        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("uuuu/MM/dd");
        LocalDate localDate = LocalDate.now();
        Account.setShopNew((dtf.format(localDate)));

        if (Account.isAmoled()) {
            ConstraintLayout main = findViewById(R.id.main);
            main.setBackgroundDrawable(new ColorDrawable(Color.BLACK));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Window window = getWindow();
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(Color.BLACK);
            }
            TextView textViewApp = findViewById(R.id.textViewApp);
            textViewApp.setTextColor(Color.parseColor("#323232"));
            TextView textViewItems2 = findViewById(R.id.textViewItems2);
            textViewItems2.setTextColor(Color.parseColor("#323232"));
            TextView textViewRefresh = findViewById(R.id.textViewRefresh);
            textViewRefresh.setTextColor(Color.parseColor("#323232"));


        }
        getShop();


      /*  ImageView image = findViewById(R.id.imageViewBg);
        ObjectAnimator scaleDownX = ObjectAnimator.ofFloat(image, "scaleX", 0.2f);
        ObjectAnimator scaleDownY = ObjectAnimator.ofFloat(image, "scaleY", 0.2f);
        scaleDownX.setDuration(500);
        scaleDownY.setDuration(500);

        ObjectAnimator moveUpY = ObjectAnimator.ofFloat(image, "translationY", -20000);
        moveUpY.setDuration(1500);

        AnimatorSet scaleDown = new AnimatorSet();
        AnimatorSet moveUp = new AnimatorSet();

        scaleDown.play(scaleDownX).with(scaleDownY);
        moveUp.play(moveUpY);

        scaleDown.start();

        final Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                moveUp.start();//Do something after 100ms
            }
        }, 1000);
*/
    }

    static String min = "loading";
    int version = BuildConfig.VERSION_CODE;
    private void getShop() {
        try {
            StarsocketConnector.sendMessage("shop "+String.valueOf(version));
            final Handler handler = new Handler(Looper.getMainLooper());
            TextView textViewItem1 = findViewById(R.id.textViewItem1);
            TextView textViewItem2 = findViewById(R.id.textViewItem2);
            TextView textViewItem3 = findViewById(R.id.textViewItem3);
            TextView textViewItemDesc1 = findViewById(R.id.textViewItemDesc1);
            TextView textViewItemDesc2 = findViewById(R.id.textViewItemDesc2);
            TextView textViewItemDesc3 = findViewById(R.id.textViewItemDesc3);
            TextView textViewItemText1 = findViewById(R.id.textViewItemText1);
            TextView textViewItemText2 = findViewById(R.id.textViewItemText2);
            TextView textViewItemText3 = findViewById(R.id.textViewItemText3);
            TextView textViewItemDescBox1 = findViewById(R.id.textViewItemDescBox1);
            TextView textViewItemDescBox2 = findViewById(R.id.textViewItemDescBox2);
            TextView textViewItemTextBox1 = findViewById(R.id.textViewItemTextBox1);
            TextView textViewItemTextBox2 = findViewById(R.id.textViewItemTextBox2);
            ImageView imageViewBox1 = findViewById(R.id.imageViewBox1);
            ImageView imageViewBox2 = findViewById(R.id.imageViewBox2);
            TextView textViewRefresh = findViewById(R.id.textViewRefresh);
            handler.postDelayed(new Runnable() {
                @SuppressLint("SetTextI18n")
                @Override
                public void run() {
                    String received = StarsocketConnector.getMessage();
                    if (received.contains("outdated-app")){
                        toast("update SportDash app to access shop");
                    } else {

                        try {
                        String info = received.split("\n")[0];
                        String hours = info.split("h")[0];


                        int hoursMin = Integer.parseInt(info.split("h")[1].replace("!-",""))/60;
                        int minMinusHours = Integer.parseInt(info.split("h")[1].replace("!-",""))-hoursMin*60+1;

                        min = "refreshes in "+hours.split("\\.")[0]+" h"+" "+String.valueOf(minMinusHours)+" min";

                        textViewRefresh.setText("r e f r e s h e s  i n  "+hours.split("\\.")[0]+" h"+" "+String.valueOf(minMinusHours)+" min");

                        String item1 = received.split("\n")[1];
                        String item2 = received.split("\n")[2];
                        String item3 = received.split("\n")[3];
                        String item4 = received.split("\n")[4];
                        String item5 = received.split("\n")[5];

                        String itemStyle1 = item1.split("!-")[0];
                        String itemName1 = item1.split("!-")[1];
                        String itemCoins1 = item1.split("!-")[2];
                        String itemId1 = item1.split("!-")[3];

                        String itemStyle2 = item2.split("!-")[0];
                        String itemName2 = item2.split("!-")[1];
                        String itemCoins2 = item2.split("!-")[2];
                        String itemId2 = item2.split("!-")[3];

                        String itemStyle3 = item3.split("!-")[0];
                        String itemName3 = item3.split("!-")[1];
                        String itemCoins3 = item3.split("!-")[2];
                        String itemId3 = item3.split("!-")[3];

                        String itemImage4 = item4.split("!-")[0];
                        String itemName4 = item4.split("!-")[1];
                        String itemCoins4 = item4.split("!-")[2];
                        String itemId4 = item4.split("!-")[3];

                        String itemImage5 = item5.split("!-")[0];
                        String itemName5 = item5.split("!-")[1];
                        String itemCoins5 = item5.split("!-")[2];
                        String itemId5 = item5.split("!-")[3];

                        textViewItem1.setText(itemStyle1);
                        textViewItemDesc1.setText(itemName1);
                        textViewItemText1.setText(itemCoins1+" c o i n s");

                        textViewItem2.setText(itemStyle2);
                        textViewItemDesc2.setText(itemName2);
                        textViewItemText2.setText(itemCoins2+" c o i n s");

                        textViewItem3.setText(itemStyle3);
                        textViewItemDesc3.setText(itemName3);
                        textViewItemText3.setText(itemCoins3+" c o i n s");


                        String uri = "@drawable/box"+itemImage4.replaceAll(" ","");  // where myresource (without the extension) is the file
                        int imageResource = getResources().getIdentifier(uri, null, getPackageName());
                        Drawable res = getResources().getDrawable(imageResource);
                        imageViewBox1.setImageDrawable(res);

                        String uri1 = "@drawable/box"+itemImage5.replaceAll(" ","");  // where myresource (without the extension) is the file
                        int imageResource1 = getResources().getIdentifier(uri1, null, getPackageName());
                        Drawable res1 = getResources().getDrawable(imageResource1);
                        imageViewBox2.setImageDrawable(res1);

                        textViewItemDescBox1.setText(itemName4);
                        textViewItemTextBox1.setText(itemCoins4+" c o i n s");

                        textViewItemDescBox2.setText(itemName5);
                        textViewItemTextBox2.setText(itemCoins5+" c o i n s");

                        } catch (Exception e){
                            toast("unknown error please update app or report error");
                        }

                    }
                }
            }, 100);

        } catch (Exception e){
            toast("no network");
        }
    }

    public void vibrate(){
        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            v.vibrate(VibrationEffect.createOneShot(100, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            v.vibrate(100);
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

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void showMin(View view) {
        vibrate();
        toast(min);
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }


}