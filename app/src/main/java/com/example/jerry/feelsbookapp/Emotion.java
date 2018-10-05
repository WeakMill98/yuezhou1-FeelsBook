package com.example.jerry.feelsbookapp;

import java.util.Date;

public abstract class Emotion{
    private transient String emotionName = "Base";
    private String comment;
    private Date date;

    Emotion(){
        this.date = new Date();
    }

    Emotion(Date date){
        this.date = date;
    }

    // Setters
    public void setComment(String comment) {
        if (comment.length() <= 100){
            this.comment = comment;
        }
        else{
        }
    }

    public void setDate(Date date){
        this.date = date;
    }

    // Getters
    public Date getDate(){
        return this.date;
    }

    public String getComment(){
        return this.comment;
    }

    public String getEmotionName(){
        return this.emotionName;
    }

    @Override
    public String toString(){
        return this.emotionName;
    }
}
