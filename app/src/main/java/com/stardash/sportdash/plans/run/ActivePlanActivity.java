package com.stardash.sportdash.plans.run;

import static com.stardash.sportdash.plans.run.RunPlanActivity.thePlan;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.stardash.sportdash.settings.Account;
import com.stardash.sportdash.MainActivity;
import com.stardash.sportdash.R;

import java.util.Locale;

public class ActivePlanActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        setContentView(R.layout.activity_active_plan);
        setCurrentElement();

        TextView textViewInfo = findViewById(R.id.textViewLeft);

        if (!advice(activeElement).contains("http")) {
            textViewInfo.setVisibility(View.GONE);
        }


            toast("now doing iteration "+String.valueOf(activeIterations()+1)+ " of "+String.valueOf(iterationsOfPlan()));

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

    int iterationsOfPlan() {
        String firstRow = plan().split("\n",5)[1];
        int iterationsInt = Integer.parseInt(firstRow.split(" ",5)[3]);

        return iterationsInt;
    }

    int activeIterations() {
        SharedPreferences pref = this.getSharedPreferences("sport", 0); // 0 - for private mode
        int activeIterations = pref.getInt("activeIterations", 0);
        return activeIterations;
    }

    public void onClickStart(View view) {
        TextView textViewStartPause = findViewById(R.id.textViewStart);

        if (textViewStartPause.getText().equals("P Λ U S Ξ")) {
            pauseTimer();
            textViewStartPause.setText("R Ξ S U M Ξ");
        } else if (textViewStartPause.getText().equals("R Ξ S U M Ξ")) {
            textViewStartPause.setText("P Λ U S Ξ");
            continueTimer();
        } else {
            TextView textViewCountdown = findViewById(R.id.textViewTrack);
            String setTime = textViewCountdown.getText().toString();
            startTimer(Integer.parseInt(setTime));
            textViewStartPause.setText("P Λ U S Ξ");
        }
    }


        //Declare timer
        CountDownTimer cTimer = null;
        //Set element
        int activeElement = 2;
        int activeIterations = 0;
        //start timer function
        void startTimer(int i) {
            TextView textViewElement = findViewById(R.id.textViewFormat);
            TextView textViewTrack = findViewById(R.id.textViewTrack);
            TextView textViewDone = findViewById(R.id.textViewDone);
            TextView textViewStart = findViewById(R.id.textViewStart);


            if (!textViewElement.getText().equals("seconds")) {
                String elementNames = plan().split("\n",20)[16];
                String selectedElement = elementNames.split("&",25)[activeElement];
                textViewTrack.setText(selectedElement+"x");
                textViewDone.setVisibility(View.VISIBLE);
                textViewStart.setVisibility(View.GONE);
            } else {
                textViewStart.setVisibility(View.VISIBLE);
                textViewDone.setVisibility(View.GONE);
            textViewElement.setText("seconds");

            TextView textViewCountdown = findViewById(R.id.textViewTrack);
            TextView textViewInfo = findViewById(R.id.textViewLeft);

            if (!advice(activeElement).contains("http")) {
                textViewInfo.setVisibility(View.GONE);
            }


            cTimer = new CountDownTimer(i* 1000L, 1000) {
                public void onTick(long millisUntilFinished) {
                    textViewCountdown.setText(String.valueOf(millisUntilFinished/1000));
                }
                public void onFinish() {
                    activeElement++;
                    setCurrentElement();
                    String setTime = textViewCountdown.getText().toString();
                    if (setTime.length()<1) {
                        activeIterations++;
                        if (activeIterations > iterations()){
                            openFinishedWorkout();
                        } else {
                            activeElement=2;
                            startTimer(Integer.parseInt(setTime));
                        }
                    } else {
                        startTimer(Integer.parseInt(setTime));
                    }

                }
            };
            cTimer.start();

            }
        }

    public void doneElement(View view) {
            nextElement();
    }

    private void nextElement() {
        TextView textViewCountdown = findViewById(R.id.textViewTrack);
        TextView textViewElement = findViewById(R.id.textViewElement);
        activeElement++;
        setCurrentElement();
        String setTime = textViewCountdown.getText().toString();
        if (setTime.length()<1) {
            openFinishedWorkout();
        } else if (textViewElement.getText().toString().equals("C H O O S E")) {
            openFinishedWorkout();
        } else if (textViewElement.getText().toString().equals("S Ξ L Ξ C T")) {
            openFinishedWorkout();
        } else {
            startTimer(Integer.parseInt(setTime));
        }

    }

    private void openFinishedWorkout() {
        cancelTimer();
        Intent i = new Intent(this, FinishedWorkoutActivity.class);
        startActivity(i);
    }

    //cancel timer
        void cancelTimer() {
            if(cTimer!=null)
                cTimer.cancel();
        }

    void pauseTimer() {
        if(cTimer!=null) {
            TextView textViewCountdown = findViewById(R.id.textViewTrack);
            String pausedTime = textViewCountdown.getText().toString();
            cTimer.cancel();
            textViewCountdown.setText(pausedTime);
        }
    }
    void continueTimer() {
        if(cTimer!=null) {
            TextView textViewCountdown = findViewById(R.id.textViewTrack);
            String pausedTime = textViewCountdown.getText().toString();
            try{
                int number = Integer.parseInt(pausedTime);
                startTimer(number);
            }
            catch (NumberFormatException ex){
                ex.printStackTrace();
            }
        }
    }

    public void showOptions(View view) {
            showOptionsAction();
    }
    @Override
    public void onBackPressed() {
            showOptionsAction();
    }

    private void showOptionsAction() {
        TextView textViewEnd = findViewById(R.id.textViewLeft1);
        TextView textViewSkipp = findViewById(R.id.textViewRight);
        TextView textViewTop = findViewById(R.id.textViewTop);

        if (textViewTop.getVisibility() == View.VISIBLE){
            textViewEnd.setVisibility(View.VISIBLE);
            textViewSkipp.setVisibility(View.VISIBLE);
            textViewTop.setVisibility(View.INVISIBLE);
        } else {
            textViewEnd.setVisibility(View.GONE);
            textViewSkipp.setVisibility(View.GONE);
            textViewTop.setVisibility(View.VISIBLE);
        }
    }

    private void planInformation() {
        try {
            String name = plan().split("\n",5)[2];
            String category = plan().split("\n",5)[4];
            String description = plan().split("\n",5)[3];
            String firstRow = plan().split("\n",5)[1];
            String difficultyInt = firstRow.split(" ",5)[1];
            String difficulty = "none";
            String energy = firstRow.split(" ",5)[2];
            String duration = "none";

            try{
                int number = Integer.parseInt(firstRow.split(" ",5)[0]);
                duration = String.valueOf(number/60);
            }
            catch (NumberFormatException ex){
                ex.printStackTrace();
            }

            if (difficultyInt.equals("0")){
                difficulty = "normal";
            } else if (difficultyInt.equals("1")){
                difficulty = "hard";
            }else if (difficultyInt.equals("2")){
                difficulty = "ultra hard";
            }else if (difficultyInt.equals("3")){
                difficulty = "unbeatable hard";
            }

        } catch (Exception e){
        }
    }

    TextToSpeech textToSpeech;

    public void setCurrentElement(){
        int currentElement = activeElement;
        planNames(currentElement);
        //  planAdvice(currentElement);
        planDescription(currentElement);
        planSeconds(currentElement);
        planNamesNext(currentElement);
        planFormat(currentElement);
        speakElement();
    }

    private void speakElement() {

        String elementNames = plan().split("\n",20)[16];
        String selectedElement = elementNames.split("&",25)[activeElement];

        if (selectedElement.length()<1){
            if (isTtsName()){
                speakFinished();
            }
        } else {
            if (isTtsName()){
                speakName();
            }
        }

    }

    private void speakFinished() {
        textToSpeech = new TextToSpeech(ActivePlanActivity.this
                , new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {
                if (i == TextToSpeech.SUCCESS) {
                    int lang = textToSpeech.setLanguage(Locale.ENGLISH);
                    String name = plan().split("\n",5)[2];
                    int speech = textToSpeech.speak("Workout "+name+ " finished!",TextToSpeech.QUEUE_FLUSH,null);
                }
            }
        });
    }

    private void speakDescription() {
        textToSpeech = new TextToSpeech(ActivePlanActivity.this
                , new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {
                if (i == TextToSpeech.SUCCESS) {
                    TextView textViewElement = findViewById(R.id.textViewElementDescription);
                    int lang = textToSpeech.setLanguage(Locale.ENGLISH);
                    int speech = textToSpeech.speak(textViewElement.getText().toString(),TextToSpeech.QUEUE_FLUSH,null);
                }
            }
        });
    }

    private void speakTime() {
        textToSpeech = new TextToSpeech(ActivePlanActivity.this
                , new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {
                if (i == TextToSpeech.SUCCESS) {
                    TextView textViewElement = findViewById(R.id.textViewTrack);
                    int lang = textToSpeech.setLanguage(Locale.ENGLISH);
                    String text = textViewElement.getText().toString();

                    String elementNames = plan().split("\n",20)[16];
                    String selectedElement = elementNames.split("&",25)[activeElement];

                    if (text.contains("x")) {
                        int speech = textToSpeech.speak(text.replace("x"," times"),TextToSpeech.QUEUE_FLUSH,null);
                    } else { ;
                        int speech = textToSpeech.speak(selectedElement+" seconds",TextToSpeech.QUEUE_FLUSH,null);
                    }
                    if (isTtsDescription()) {
                        speakDescription();
                    }
                }
            }
        });
    }

    private void speakName() {
        textToSpeech = new TextToSpeech(ActivePlanActivity.this
                , new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {
                if (i == TextToSpeech.SUCCESS) {
                    TextView textViewElement = findViewById(R.id.textViewElement);
                    int lang = textToSpeech.setLanguage(Locale.ENGLISH);
                    int speech = textToSpeech.speak(textViewElement.getText().toString(),TextToSpeech.QUEUE_FLUSH,null);


                        speakTime();

                }
            }
        });
    }

    private void planNames(int element) {
        String elementNames = plan().split("\n",20)[13];
        String selectedElement = elementNames.split("&",25)[element];
        TextView textViewElement = findViewById(R.id.textViewElement);
        textViewElement.setText(selectedElement);
    }

    private void planNamesNext(int element) {
        String elementNames = plan().split("\n",20)[13];
        String selectedElement = elementNames.split("&",25)[element+1];
        TextView textViewElement = findViewById(R.id.textViewNext);
        textViewElement.setText("next up will be: "+selectedElement);
    }

    private void planDescription(int element) {
        String elementNames = plan().split("\n",20)[14];
        String selectedElement = elementNames.split("&",25)[element];
        TextView textViewElement = findViewById(R.id.textViewElementDescription);
        textViewElement.setText(selectedElement);
    }

    String advice(int element) {
        String elementNames = plan().split("\n",20)[15];
        String selectedElement = elementNames.split("&",25)[element];
        return selectedElement;
    }

    private void planSeconds(int element) {
        String elementNames = plan().split("\n",20)[16];
        String selectedElement = elementNames.split("&",25)[element];
        TextView textViewElement = findViewById(R.id.textViewTrack);
        textViewElement.setText(selectedElement);
    }

    private void planFormat(int element) {
        String elementNames = plan().split("\n",20)[17];
        String selectedElement = elementNames.split("&",25)[element];
        TextView textViewElement = findViewById(R.id.textViewFormat);
        textViewElement.setText(selectedElement);
    }

    String plan() {
        return thePlan;
    }

    public void openAdvice(View view) {
            pauseTimer();
            TextView textViewStartPause = findViewById(R.id.textViewStart);
            TextView textViewCountdown = findViewById(R.id.textViewTrack);
            String setTime = textViewCountdown.getText().toString();
            textViewStartPause.setText("R Ξ S U M Ξ");
            Toast.makeText(ActivePlanActivity.this, "TRAINING PAUSED", Toast.LENGTH_LONG).show();
            Uri uriUrl = Uri.parse(advice(activeElement));
            Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
            startActivity(launchBrowser);
    }

    public void end(View view) {
            cancelTimer();
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }

    public void skipElement(View view) {
            cancelTimer();
            setCurrentElement();
            nextElement();
    }

    boolean isTtsName() {
        SharedPreferences pref = this.getSharedPreferences("tts", 0); // 0 - for private mode
        boolean tts = pref.getBoolean("name", false);
        return tts;
    }
    boolean isTtsDescription() {
        SharedPreferences pref = this.getSharedPreferences("tts", 0); // 0 - for private mode
        boolean tts = pref.getBoolean("description", false);
        return tts;
    }

    int iterations() {
        int iterationsInt = 0;
        try {
            String firstRow = plan().split("\n",5)[1];
            iterationsInt = Integer.parseInt(firstRow.split(" ",5)[3]);
        } catch (Exception e){
        }
        return iterationsInt;
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