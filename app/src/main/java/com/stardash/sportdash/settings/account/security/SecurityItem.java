package com.stardash.sportdash.settings.account.security;

public class SecurityItem {
    private String mText0;
    private String mText1;
    private String mText2;
    private String mText3;
    private String mText4;



    public SecurityItem(String text0, String text1, String text2, String text3, String text4) {
        mText0 = text0;
        mText1 = text1;
        mText2 = text2;
        mText3 = text3;
        mText4 = text4;
    }

    public void changeText1(String text){
        mText1 = text;
    }

    public String getText0(){
        return mText0;
    }

    public String getText1(){
        return mText1;
    }

    public String getText2(){
        return mText2;
    }

    public String getText3(){
        return mText3;
    }

    public String getText4() {
        return mText4;
    }
}


