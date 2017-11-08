package com.example.sonnv.dontbelazy.databases;

import java.io.Serializable;

/**
 * Created by cuonghx2709 on 9/26/2017.
 */

public class Note implements Serializable{
    private int id;
    private String time;
    private String text;
    private boolean notifi;
    private String mainText;

    public Note() {

    }

    public String getMainText() {
        return mainText;
    }

    public void setMainText(String mainText) {
        this.mainText = mainText;
    }

    public Note(int id, String time, String text, String mainText, boolean notifi) {
        this.id = id;
        this.time = time;
        this.text = text;
        this.notifi = notifi;
        this.mainText = mainText;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public boolean isNotifi() {
        return notifi;
    }

    public void setNotifi(boolean notifi) {
        this.notifi = notifi;
    }

    @Override
    public String toString() {
        return "Note{" +
                "id=" + id +
                ", time='" + time + '\'' +
                ", text='" + text + '\'' +
                ", notifi=" + notifi +
                '}';
    }
}
