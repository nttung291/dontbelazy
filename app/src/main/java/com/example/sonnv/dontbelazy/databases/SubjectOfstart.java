package com.example.sonnv.dontbelazy.databases;

import java.io.Serializable;

/**
 * Created by cuonghx2709 on 9/27/2017.
 */

public class SubjectOfstart implements Serializable {
    private Subject subject;

    public SubjectOfstart(Subject subject, String name, String time, String room) {
        this.subject = subject;
        this.name = name;
        this.time = time;
        this.room = room;
    }

    private String name;
    private String time;
    private String room;

    public Subject getSubject() {
        return subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }
}
