package com.example.sonnv.dontbelazy.databases;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

/**
 * Created by SONNV on 9/22/2017.
 */

public class Subject implements Serializable {
    private int id;
    private String name;
    private int idTeacher;
    private String dayStart;
    private String dayEnd;
    private boolean alarm;
    private int attendance;
    private int maxAttendance;
    private String note;
    private List<String> roomList;
    private List<String> daysPicked;
    private List<String> timeStartList;
    private List<String> timeEndList;

    @Override
    public String toString() {
        return "Subject{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", idTeacher=" + idTeacher +
                ", dayStart='" + dayStart + '\'' +
                ", dayEnd='" + dayEnd + '\'' +
                ", alarm=" + alarm +
                ", attendance=" + attendance +
                ", maxAttendance=" + maxAttendance +
                ", note='" + note + '\'' +
                ", roomList=" + roomList +
                ", daysPicked=" + daysPicked +
                ", timeStartList=" + timeStartList +
                ", timeEndList=" + timeEndList +
                '}';
    }


    public Subject(int id, String name, int idTeacher, String dayStart, String dayEnd, boolean alarm, int attendance, int maxAttendance, String note) {
        this.id = id;
        this.name = name;
        this.idTeacher = idTeacher;
        this.dayStart = dayStart;
        this.dayEnd = dayEnd;
        this.alarm = alarm;
        this.attendance = attendance;
        this.maxAttendance = maxAttendance;
        this.note = note;
    }

    public Subject() {
    }

    public void setDetail(List<String> roomList, List<String> daysPicked, List<String> timeStartList, List<String> timeEndList) {
        this.timeStartList = timeStartList;
        this.timeEndList = timeEndList;
        this.daysPicked = daysPicked;
        this.roomList = roomList;
    }

    public List<String> getRoomList() {
        return roomList;
    }

    public void setRoomList(List<String> roomList) {
        this.roomList = roomList;
    }

    public Subject(int id, String name, int idTeacher, String dayStart, String dayEnd, boolean alarm, int attendance, int maxAttendance, String note, List<String> roomList, List<String> daysPicked, List<String> timeStartList, List<String> timeEndList) {
        this.id = id;
        this.name = name;
        this.idTeacher = idTeacher;
        this.dayStart = dayStart;
        this.dayEnd = dayEnd;
        this.alarm = alarm;
        this.attendance = attendance;
        this.maxAttendance = maxAttendance;
        this.note = note;
        this.timeStartList = timeStartList;
        this.timeEndList = timeEndList;
        this.daysPicked = daysPicked;
        this.roomList = roomList;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIdTeacher() {
        return idTeacher;
    }

    public void setIdTeacher(int idTeacher) {
        this.idTeacher = idTeacher;
    }

    public String getDayStart() {
        return dayStart;
    }

    public void setDayStart(String dayStart) {
        this.dayStart = dayStart;
    }

    public String getDayEnd() {
        return dayEnd;
    }

    public void setDayEnd(String dayEnd) {
        this.dayEnd = dayEnd;
    }

    public boolean isAlarm() {
        return alarm;
    }

    public void setAlarm(boolean alarm) {
        this.alarm = alarm;
    }

    public int getAttendance() {
        return attendance;
    }

    public void setAttendance(int attendance) {
        this.attendance = attendance;
    }

    public int getMaxAttendance() {
        return maxAttendance;
    }

    public void setMaxAttendance(int maxAttendance) {
        this.maxAttendance = maxAttendance;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public List<String> getTimeStartList() {
        return timeStartList;
    }

    public void setTimeStartList(List<String> timeStartList) {
        this.timeStartList = timeStartList;
    }

    public List<String> getTimeEndList() {
        return timeEndList;
    }

    public void setTimeEndList(List<String> timeEndList) {
        this.timeEndList = timeEndList;
    }

    public List<String> getDaysPicked() {
        return daysPicked;
    }

    public void setDaysPicked(List<String> daysPicked) {
        this.daysPicked = daysPicked;
    }
}
