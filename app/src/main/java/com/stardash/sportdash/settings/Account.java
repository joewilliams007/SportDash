package com.stardash.sportdash.settings;

import android.content.SharedPreferences;

import com.stardash.sportdash.network.tcp.StarsocketConnector;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Account {


    // level --------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    public static int level() {
        int level = xp()/10000+1;
        return level;
    }
    // coins --------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    public static int coins() {
        SharedPreferences pref = MyApplication.getAppContext().getSharedPreferences("account", 0); // 0 - for private mode
        int result = pref.getInt("coins", 0);
        return result;
    }
    public static void setCoins(int coins) {
        SharedPreferences settings = MyApplication.getAppContext().getSharedPreferences("account", 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt("coins", coins);
        editor.apply();
    }
    public static void getCoins(int coins) {
        SharedPreferences settings = MyApplication.getAppContext().getSharedPreferences("account", 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt("coins", coins()+coins);
        editor.apply();
    }
    // reward --------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    public static String rewardDay() {
        SharedPreferences pref = MyApplication.getAppContext().getSharedPreferences("account", 0); // 0 - for private mode
        String result = pref.getString("rewardDay", null);
        return result;
    }
    public static void setRewardDay(String day) {
        SharedPreferences settings = MyApplication.getAppContext().getSharedPreferences("account", 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("rewardDay", day);
        editor.apply();
    }
    // shop new? --------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    public static String isShopNew() {
        SharedPreferences pref = MyApplication.getAppContext().getSharedPreferences("account", 0); // 0 - for private mode
        String result = pref.getString("isShopNew", null);
        return result;
    }
    public static void setShopNew(String day) {
        SharedPreferences settings = MyApplication.getAppContext().getSharedPreferences("account", 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("isShopNew", day);
        editor.apply();
    }
    // progresses --------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    public static String week() {
        SharedPreferences pref = MyApplication.getAppContext().getSharedPreferences("account", 0); // 0 - for private mode
        String result = pref.getString("week", null);
        return result;
    }
    public static String day() {
        SharedPreferences pref = MyApplication.getAppContext().getSharedPreferences("account", 0); // 0 - for private mode
        String result = pref.getString("day", null);
        return result;
    }
    public static int WeekProgress() {
        SharedPreferences pref = MyApplication.getAppContext().getSharedPreferences("account", 0); // 0 - for private mode
        int result = pref.getInt("week_progress", 0);
        return result;
    }
    public static int TodayProgress() {
        SharedPreferences pref = MyApplication.getAppContext().getSharedPreferences("account", 0); // 0 - for private mode
        int result = pref.getInt("today_progress", 0);
        return result;
    }
    public static void setTodayProgress(int progress) {
        SharedPreferences settings = MyApplication.getAppContext().getSharedPreferences("account", 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt("today_progress", progress);
        editor.apply();
    }
    public static void getTodayProgress(int progress) {
        SharedPreferences settings = MyApplication.getAppContext().getSharedPreferences("account", 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt("today_progress", TodayProgress()+progress);
        editor.apply();
    }
    public static void setDay(String day) {
        SharedPreferences settings = MyApplication.getAppContext().getSharedPreferences("account", 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("day", day);
        editor.apply();
    }
    public static void setWeek(String week) {
        SharedPreferences settings = MyApplication.getAppContext().getSharedPreferences("account", 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("week", week);
        editor.apply();
    }
    public static void setWeekProgress(int progress) {
        SharedPreferences settings = MyApplication.getAppContext().getSharedPreferences("account", 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt("week_progress", progress);
        editor.apply();
    }
    public static void getWeekProgress(int progress) {
        SharedPreferences settings = MyApplication.getAppContext().getSharedPreferences("account", 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt("week_progress", WeekProgress()+progress);
        editor.apply();
    }
    // weight --------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    public static int weight() {
        SharedPreferences pref = MyApplication.getAppContext().getSharedPreferences("account", 0); // 0 - for private mode
        int result = pref.getInt("weight", 0);
        return result;
    }

    public static void setWeight(int weight) {
        SharedPreferences settings = MyApplication.getAppContext().getSharedPreferences("account", 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt("weight", weight);
        editor.apply();
    }

    // age --------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    public static int age() {
        SharedPreferences pref = MyApplication.getAppContext().getSharedPreferences("account", 0); // 0 - for private mode
        int result = pref.getInt("age", 0);
        return result;
    }
    public static void setAge(int age) {
        SharedPreferences settings = MyApplication.getAppContext().getSharedPreferences("account", 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt("age", age);
        editor.apply();
    }
    // xp --------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    public static int xp() {
        SharedPreferences pref = MyApplication.getAppContext().getSharedPreferences("account", 0); // 0 - for private mode
        int result = pref.getInt("xp", 0);
        return result;
    }
    public static void setXp(int xp) {
        SharedPreferences settings = MyApplication.getAppContext().getSharedPreferences("account", 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt("xp", xp);
        editor.apply();
    }
    public static void getXp(int xp) {
        try {
            StarsocketConnector.sendMessage("setXp "+userid()+" "+String.valueOf(xp()+xp));
        } catch (Exception e){

        }
        Date cDate = new Date();
        String fDate = new SimpleDateFormat("yyyy-MM-dd").format(cDate);
        Account.setDay(fDate);

        Date d1 = new Date();
        Calendar cl = Calendar.getInstance();
        cl.setTime(d1);

        if (day().equals(fDate)) {
            getTodayProgress(xp);
        } else {
            setTodayProgress(xp);
            setDay(fDate);
        }
        if (week().equals(String.valueOf(cl.WEEK_OF_YEAR))) {
            getWeekProgress(xp);
        } else {
            setWeekProgress(xp);
            setWeek(String.valueOf(cl.WEEK_OF_YEAR));
        }
        try {
            StarsocketConnector.sendMessage("setTodayProgress "+userid()+" "+String.valueOf(Account.TodayProgress()));
            StarsocketConnector.sendMessage("setWeekProgress "+userid()+" "+String.valueOf(Account.WeekProgress()));
        } catch (Exception e){

        }
        SharedPreferences settings = MyApplication.getAppContext().getSharedPreferences("account", 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt("xp", xp()+xp);
        editor.apply();
    }
    // log --------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    public static String log() {
        SharedPreferences pref = MyApplication.getAppContext().getSharedPreferences("account", 0); // 0 - for private mode
        String result = pref.getString("username", null);
        return result;
    }
    public static void setLog(String log) {
        SharedPreferences settings = MyApplication.getAppContext().getSharedPreferences("account", 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("log", log);
        editor.apply();
    }
    public static void addLog(String log) {
        SharedPreferences settings = MyApplication.getAppContext().getSharedPreferences("account", 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("log", log()+"\n\n"+log);
        editor.apply();
    }
    // username --------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    public static String username() {
        SharedPreferences pref = MyApplication.getAppContext().getSharedPreferences("account", 0); // 0 - for private mode
        String result = pref.getString("username", null);
        return result;
    }
    public static void setUsername(String username) {
        SharedPreferences settings = MyApplication.getAppContext().getSharedPreferences("account", 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("username", username);
        editor.apply();
    }
    // id --------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    public static String userid() {
        SharedPreferences pref = MyApplication.getAppContext().getSharedPreferences("account", 0); // 0 - for private mode
        String result = pref.getString("userid", "0");
        return result;
    }
    public static void setId(String id) {
        SharedPreferences settings = MyApplication.getAppContext().getSharedPreferences("account", 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("userid", id);
        editor.apply();
    }
    // email --------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    public static String email() {
        SharedPreferences pref = MyApplication.getAppContext().getSharedPreferences("account", 0); // 0 - for private mode
        String result = pref.getString("email", null);
        return result;
    }
    public static void setEmail(String email) {
        SharedPreferences settings = MyApplication.getAppContext().getSharedPreferences("account", 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("email", email);
        editor.apply();
    }
    // password --------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    public static String password() {
        SharedPreferences pref = MyApplication.getAppContext().getSharedPreferences("account", 0); // 0 - for private mode
        String result = pref.getString("password", "0");
        return result;
    }
    public static void setPassword(String password) {
        SharedPreferences settings = MyApplication.getAppContext().getSharedPreferences("account", 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("password", password);
        editor.apply();
    }

    // is logged in status --------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    public static void setLoggedIn(Boolean loggedIn) {
        SharedPreferences settings = MyApplication.getAppContext().getSharedPreferences("account", 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean("loggedIn", loggedIn);
        editor.apply();
    }
    public static boolean loggedIn() {
        SharedPreferences pref = MyApplication.getAppContext().getSharedPreferences("account", 0); // 0 - for private mode
        Boolean result = pref.getBoolean("loggedIn", false);
        return result;
    }
    // error styles --------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    public static void setErrorStyle(String errorStyle) {
        SharedPreferences settings = MyApplication.getAppContext().getSharedPreferences("account", 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("errorStyle", errorStyle);
        editor.apply();
    }
    public static String errorStyle() { // active user Error style
        SharedPreferences pref = MyApplication.getAppContext().getSharedPreferences("account", 0); // 0 - for private mode
        String result = pref.getString("errorStyle", ">_<");
        return result;
    }
    public static void setMyErrorStyles(String styles) {
        SharedPreferences settings = MyApplication.getAppContext().getSharedPreferences("account", 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("myErrorStyles", styles);
        editor.apply();
    }
    public static String myErrorStyles() { // all error styles user has
        SharedPreferences pref = MyApplication.getAppContext().getSharedPreferences("account", 0); // 0 - for private mode
        String result = pref.getString("myErrorStyles", ">_<");
        return result;
    }
    // energy --------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    public static int energy() {
        SharedPreferences pref = MyApplication.getAppContext().getSharedPreferences("account", 0); // 0 - for private mode
        int result = pref.getInt("energy", 0);
        return result;
    }
    public static void setEnergy(int energy) {
        SharedPreferences settings = MyApplication.getAppContext().getSharedPreferences("account", 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt("energy", energy);
        editor.apply();
    }
    public static void getEnergy(int energy) {
        SharedPreferences settings = MyApplication.getAppContext().getSharedPreferences("account", 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt("energy", energy()+energy);
        editor.apply();
    }
    // create status --------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    public static boolean isCreate() {
        SharedPreferences pref = MyApplication.getAppContext().getSharedPreferences("sport", 0); // 0 - for private mode
        boolean create = pref.getBoolean("create", true);
        return create;
    }
    // active iterations --------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    public static void setActiveIterations(int i) {
        SharedPreferences settings = MyApplication.getAppContext().getSharedPreferences("sport", 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt("activeIterations", 0).commit();
    }
    // plans --------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    public static String plan(int i) {
        SharedPreferences pref = MyApplication.getAppContext().getSharedPreferences("sport", 0); // 0 - for private mode
        String plan = pref.getString(String.valueOf(i)+" plan", "empty");
        if (plan.length()<1) {
            plan = "empty";
        }
        return plan;
    }

    public static void setPlan(int planNumber, String plan) {
        SharedPreferences settings = MyApplication.getAppContext().getSharedPreferences("account", 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(String.valueOf(planNumber)+" plan", plan);
        editor.apply();
    }

    public static String committedPlan() {
        SharedPreferences pref = MyApplication.getAppContext().getSharedPreferences("sport", 0); // 0 - for private mode
        String plan = pref.getString("committed plan", "empty");
        if (plan.length()<1) {
            plan = "empty";
        }
        return plan;
    }
    // plan friend --------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    public static String planFriend(int i) {
        SharedPreferences pref = MyApplication.getAppContext().getSharedPreferences("sport", 0); // 0 - for private mode
        String plan = pref.getString(String.valueOf(i)+" planFriend", "empty");
        if (plan.length()<1) {
            plan = "empty";
        }
        return plan;
    }
    // theme --------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    public static void setAmoled(boolean amoled) {
        SharedPreferences settings = MyApplication.getAppContext().getSharedPreferences("account", 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean("isAmoled", amoled);
        editor.apply();
    }
    public static Boolean isAmoled() {
        SharedPreferences pref = MyApplication.getAppContext().getSharedPreferences("account", 0); // 0 - for private mode
        Boolean amoled = pref.getBoolean("isAmoled", false);
        return amoled;
    }
    // localhost --------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

    public static Boolean localhost() {
        SharedPreferences pref = MyApplication.getAppContext().getSharedPreferences("app", 0); // 0 - for private mode
        Boolean localhost = pref.getBoolean("isLocalhost", false);
        return localhost;
    }

    public static void setLocalhost(boolean localhost) {
        SharedPreferences settings = MyApplication.getAppContext().getSharedPreferences("app", 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean("isLocalhost", localhost);
        editor.apply();
    }

    // is my plan? --------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

    public static void setIsMine(boolean x) {
        SharedPreferences settings = MyApplication.getAppContext().getSharedPreferences("app", 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean("isMine", x);
        editor.apply();
    }
    public static Boolean isMine() {
        SharedPreferences pref = MyApplication.getAppContext().getSharedPreferences("app", 0); // 0 - for private mode
        Boolean x = pref.getBoolean("isMine", false);
        return x;
    }
    // adding friend? --------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

    public static void setAddingFriend(boolean x) {
        SharedPreferences settings = MyApplication.getAppContext().getSharedPreferences("app", 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean("isAddingFriend", x);
        editor.apply();
    }
    public static Boolean isAddingFriend() {
        SharedPreferences pref = MyApplication.getAppContext().getSharedPreferences("app", 0); // 0 - for private mode
        Boolean x = pref.getBoolean("isAddingFriend", false);
        return x;
    }

    public static String friend(int i) {
        SharedPreferences pref = MyApplication.getAppContext().getSharedPreferences("me", 0); // 0 - for private mode
        String plan = pref.getString(String.valueOf(i)+" friend", "");
        if (plan.length()<1) {
            plan = "";
        }
        return plan;
    }
    public static String selectedFriend() {
        SharedPreferences pref = MyApplication.getAppContext().getSharedPreferences("me", 0); // 0 - for private mode
        String plan = pref.getString("selectedFriend", "");

        return plan;
    }

    public static void selectFriend(String friend) {
        SharedPreferences settings = MyApplication.getAppContext().getSharedPreferences("me", 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("selectedFriend", friend);
        editor.apply();
    }

    public static void setFriend(int friendNumber, String friend) {
        SharedPreferences settings = MyApplication.getAppContext().getSharedPreferences("me", 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(String.valueOf(friendNumber)+" friend", friend);
        editor.apply();
    }


}
