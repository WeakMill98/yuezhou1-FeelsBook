package com.example.jerry.feelsbookapp;

import java.util.Date;

public class Love extends Emotion {

    private String emotionName = "Love";

    Love(Date date){
        super(date);
    }

    public String getEmotionName(){
        return this.emotionName;
    }

    @Override
    public String toString(){
        return this.emotionName;
    }

}