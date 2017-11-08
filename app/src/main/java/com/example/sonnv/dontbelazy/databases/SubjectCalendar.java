package com.example.sonnv.dontbelazy.databases;

/**
 * Created by cuonghx on 10/26/2017.
 */

public class SubjectCalendar {
    private Subject subject;
    private String timeEnd;

    public String getTimeEnd() {
        return timeEnd;
    }

    public void setTimeEnd(String timeEnd) {
        this.timeEnd = timeEnd;
    }

    private String name;
    private String timeStart;
    private String room;

    public SubjectCalendar(Subject subject, String timeEnd, String name, String timeStart, String room) {
        this.subject = subject;
        this.timeEnd = timeEnd;
        this.name = name;
        this.timeStart = timeStart;
        this.room = room;
    }

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

    public String getTimeStart() {
        return timeStart;
    }

    public void setTimeStart(String timeStart) {
        this.timeStart = timeStart;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }
}
