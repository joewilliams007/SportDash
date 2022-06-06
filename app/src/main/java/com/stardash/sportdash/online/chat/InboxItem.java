package com.stardash.sportdash.online.chat;

public class InboxItem {
    private String mText1;
    private String mText2;
    private String mText3;

    public String getText4() {
        return mText4;
    }

    public void setText4(String mText4) {
        this.mText4 = mText4;
    }

    private String mText4;

    public InboxItem (String text1, String text2, String text3, String text4) {
        mText1 = text1;
        mText2 = text2;
        mText3 = text3;
        mText4 = text4;
    }

    public void changeText1(String text){
        mText1 = text;
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
}

