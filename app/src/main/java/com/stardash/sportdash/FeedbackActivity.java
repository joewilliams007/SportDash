package com.stardash.sportdash;

import static com.stardash.sportdash.plans.run.RunPlanActivity.reportPlanId;
import static com.stardash.sportdash.plans.run.RunPlanActivity.reportingPlan;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.format.Formatter;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.text.DecimalFormat;
import java.util.Enumeration;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FeedbackActivity extends AppCompatActivity {

    boolean submitted = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        submitted = false;
        try {
            if (reportingPlan) {
                EditText editTextTextMultiLine = findViewById(R.id.editTextTextMultiLine);
                TextView textViewInfo = findViewById(R.id.textViewFeedbackInfo);
                CheckBox checkBox = findViewById(R.id.checkBox);
                checkBox.setVisibility(View.INVISIBLE);
                textViewInfo.setText("please give a reason for reporting the plan");
                editTextTextMultiLine.setText("report plan " + reportPlanId+"\nREASON: ");
                reportingPlan = false;
            }
        } catch (Exception e){

        }
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

    public void sendFeedback(View view) {
        if (submitted){
            home();
        } else {
            EditText editTextTextMultiLine = findViewById(R.id.editTextTextMultiLine);

            if (editTextTextMultiLine.getText().toString().length() < 1) {
                toast("enter a message");
            } else if (editTextTextMultiLine.getText().toString().length() > 500) {
                toast("message is too long, max are 500 characters");
            } else {
                try {
                    int versionCode = BuildConfig.VERSION_CODE;
                    String versionName = BuildConfig.VERSION_NAME;
                    CheckBox checkBox = findViewById(R.id.checkBox);
                    if (checkBox.isChecked()) {

                        StarsocketConnector.sendMessage(
                                "feedback "+Account.userid()+" \nFEEDBACK BY ID : #" + Account.userid()
                                        + "\nUSERNAME : " + Account.username()
                                        + "\nEMAIL : " + Account.email()
                                        + "\nXP : " + Account.username()
                                        + "\nLEVEL : " + Account.level()
                                        + "\nAGE : " + Account.age()
                                        + "\nWEIGHT : " + Account.weight()
                                        + "\nCOINS : " + Account.coins()
                                        + "\nENERGY : " + Account.energy()
                                        + "\nAMOLED : " + Account.isAmoled()
                                        + "\nIS.CREATE : " + Account.isCreate()
                                        + "\nIS.LOGGED.IN : " + Account.loggedIn()
                                        + "\nDEVICE RAM : " + getTotalRAM()
                                        + "\nANDROID VERSION : " + getAndroidVersion()
                                        + "\nVERSION.RELEASE : " + Build.VERSION.RELEASE
                                        + "\nVERSION.INCREMENTAL : " + Build.VERSION.INCREMENTAL
                                        + "\nVERSION.SDK.NUMBER : " + Build.VERSION.SDK_INT
                                        + "\nBOARD : " + Build.BOARD
                                        + "\nBOOTLOADER : " + Build.BOOTLOADER
                                        + "\nBRAND : " + Build.BRAND
                                        + "\nCPU_ABI : " + Build.CPU_ABI
                                        + "\nCPU_ABI2 : " + Build.CPU_ABI2
                                        + "\nDISPLAY : " + Build.DISPLAY
                                        + "\nFINGERPRINT : " + Build.FINGERPRINT
                                        + "\nHARDWARE : " + Build.HARDWARE
                                        + "\nHOST : " + Build.HOST
                                        + "\nID : " + Build.ID
                                        + "\nMANUFACTURER : " + Build.MANUFACTURER
                                        + "\nMODEL : " + Build.MODEL
                                        + "\nPRODUCT : " + Build.PRODUCT
                                        + "\nSERIAL : " + Build.SERIAL
                                        + "\nTAGS : " + Build.TAGS
                                        + "\nTIME : " + Build.TIME
                                        + "\nTYPE : " + Build.TYPE
                                        + "\nUNKNOWN : " + Build.UNKNOWN
                                        + "\nUSER : " + Build.USER
                                        + "\nIP : " + getLocalIpAddress()
                                        + "\nAPP.VERSION : " + versionCode
                                        + "\nAPP.VERSION.NAME : " + versionName
                                        + "\nMESSAGE : " + editTextTextMultiLine.getText().toString()
                        );
                    } else {
                        StarsocketConnector.sendMessage(
                                "feedback "+Account.userid()+" \nFEEDBACK BY ID : #" + Account.userid()
                                        + "\nUSERNAME : " + Account.username()
                                        + "\nAPP.VERSION : " + versionCode
                                        + "\nAPP.VERSION.NAME : " + versionName
                                        + "\nMESSAGE : " + editTextTextMultiLine.getText().toString()
                        );
                    }
                    toast("feedback submitted!");
                    TextView textViewSubmit = findViewById(R.id.textViewTopRight);
                    textViewSubmit.setText("H O M E");
                    submitted = true;
                    editTextTextMultiLine.setText("");


                } catch (Exception e) {
                    toast(".. no connection, try again later");
                }
            }
        }

    }

    private void home() {
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

    public void goHome(View view) {
        home();
    }

    public String getTotalRAM() {

        RandomAccessFile reader = null;
        String load = null;
        DecimalFormat twoDecimalForm = new DecimalFormat("#.##");
        double totRam = 0;
        String lastValue = "";
        try {
            reader = new RandomAccessFile("/proc/meminfo", "r");
            load = reader.readLine();

            // Get the Number value from the string
            Pattern p = Pattern.compile("(\\d+)");
            Matcher m = p.matcher(load);
            String value = "";
            while (m.find()) {
                value = m.group(1);
                // System.out.println("Ram : " + value);
            }
            reader.close();

            totRam = Double.parseDouble(value);
            // totRam = totRam / 1024;

            double mb = totRam / 1024.0;
            double gb = totRam / 1048576.0;
            double tb = totRam / 1073741824.0;

            if (tb > 1) {
                lastValue = twoDecimalForm.format(tb).concat(" TB");
            } else if (gb > 1) {
                lastValue = twoDecimalForm.format(gb).concat(" GB");
            } else if (mb > 1) {
                lastValue = twoDecimalForm.format(mb).concat(" MB");
            } else {
                lastValue = twoDecimalForm.format(totRam).concat(" KB");
            }



        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            // Streams.close(reader);
        }

        return lastValue;
    }

    public String getAndroidVersion() {
        String release = Build.VERSION.RELEASE;
        int sdkVersion = Build.VERSION.SDK_INT;
        return "Android SDK: " + sdkVersion + " (" + release +")";
    }

    public String getLocalIpAddress() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        String ip = Formatter.formatIpAddress(inetAddress.hashCode());
                        return ip;
                    }
                }
            }
        } catch (SocketException ex) {
        }
        return null;
    }
}