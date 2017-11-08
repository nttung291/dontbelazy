package com.example.sonnv.dontbelazy.databases;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.pdf.PdfDocument;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by SONNV on 9/23/2017.
 */

public class SubjectHandleDB {
    private MyDatabase myDatabase;
    private static SubjectHandleDB subjectdatabaseHandle;
    private SQLiteDatabase sqLiteDatabase;

    public SubjectHandleDB(Context context) {
        myDatabase = new MyDatabase(context);
    }

    public static SubjectHandleDB getInstance(Context context) {
        if (subjectdatabaseHandle == null) {
            subjectdatabaseHandle = new SubjectHandleDB(context);
        }
        return subjectdatabaseHandle;
    }

    public List<Subject> getListSubject() {
        List<Subject> subjectList = new ArrayList<>();
        sqLiteDatabase = myDatabase.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM subject", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            // get data
            int id = cursor.getInt(0);
            String name = cursor.getString(1);
            int idTeacher = cursor.getInt(2);
            String dayStart = cursor.getString(3);
            String dayEnd = cursor.getString(4);
            boolean alarm = cursor.getInt(5) != 0;
            int attendance = cursor.getInt(6);
            int maxAttendance = cursor.getInt(7);
            String note = cursor.getString(8);


            Subject subject = new Subject(id, name, idTeacher, dayStart, dayEnd, alarm, attendance, maxAttendance, note);
            subjectList.add(subject);

            // next cursor
            cursor.moveToNext();
        }

        for (int i = 0; i < subjectList.size(); i++) {
            cursor.close();
            cursor = sqLiteDatabase.rawQuery("SELECT * FROM detail_subject WHERE idSubject = " + subjectList.get(i).getId(), null);
            cursor.moveToFirst();
            List<String> roomList = new ArrayList<>();
            List<String> daysPicked = new ArrayList<>();
            List<String> timeStartList = new ArrayList<>();
            List<String> timeEndList = new ArrayList<>();
            while (!cursor.isAfterLast()) {
                String roomName = cursor.getString(1);
                if (roomName != null) {
                    roomList.add(roomName);
                } else {
                    roomList.add("");
                }
                daysPicked.add(cursor.getString(2));
                timeStartList.add(cursor.getString(3));
                timeEndList.add(cursor.getString(4));
                cursor.moveToNext();
            }
            subjectList.get(i).setDetail(roomList, daysPicked, timeStartList, timeEndList);
            Log.d("DB", "getListSubject: " + subjectList.get(i).toString());
        }
        return subjectList;
    }

    public void insertSubject(Subject subject) {
        Log.d("DB", "insertSubject: " + subject.toString());
        sqLiteDatabase = myDatabase.getWritableDatabase();

        // add to subject
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", subject.getName());
        contentValues.put("idTeacher", subject.getIdTeacher());
        contentValues.put("dayStart", subject.getDayStart());
        contentValues.put("dayEnd", subject.getDayEnd());
        if (subject.isAlarm()) {
            contentValues.put("alarm", 1);
        } else {
            contentValues.put("alarm", 0);
        }
        contentValues.put("attendance", subject.getAttendance());
        contentValues.put("maxAttendance", subject.getMaxAttendance());
        contentValues.put("note", subject.getNote());
        sqLiteDatabase.insert("subject", null, contentValues);

        // add to detail subject
        for (int i = 0; i < subject.getRoomList().size(); i++) {
            contentValues = new ContentValues();
            contentValues.put("idSubject", getListSubject().get(getListSubject().size() - 1).getId());
            contentValues.put("idRoom", subject.getRoomList().get(i));
            contentValues.put("dayOfWeek", subject.getDaysPicked().get(i));
            contentValues.put("timeStart", subject.getTimeStartList().get(i));
            contentValues.put("timeEnd", subject.getTimeEndList().get(i));
            sqLiteDatabase.insert("detail_subject", null, contentValues);
        }
    }

    public Subject getSubjectAtId(int id) {
        sqLiteDatabase = myDatabase.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM subject WHERE id = " + id, null);
        cursor.moveToFirst();
        String name = cursor.getString(1);
        int idTeacher = cursor.getInt(2);
        String dayStart = cursor.getString(3);
        String dayEnd = cursor.getString(4);
        boolean alarm = cursor.getInt(5) != 0;
        int attendance = cursor.getInt(6);
        int maxAttendance = cursor.getInt(7);
        String note = cursor.getString(8);

        Subject subject = new Subject(id, name, idTeacher, dayStart, dayEnd, alarm, attendance, maxAttendance, note);

        cursor.close();
        cursor = sqLiteDatabase.rawQuery("SELECT * FROM detail_subject WHERE idSubject = " + id, null);
        cursor.moveToFirst();
        List<String> roomList = new ArrayList<>();
        List<String> daysPicked = new ArrayList<>();
        List<String> timeStartList = new ArrayList<>();
        List<String> timeEndList = new ArrayList<>();
        while (!cursor.isAfterLast()) {
            String roomName = cursor.getString(1);
            if (roomName != null) {
                roomList.add(roomName);
            } else {
                roomList.add("");
            }
            daysPicked.add(cursor.getString(2));
            timeStartList.add(cursor.getString(3));
            timeEndList.add(cursor.getString(4));
            subject.setDetail(roomList, daysPicked, timeStartList, timeEndList);
            cursor.moveToNext();
        }
        return subject;
    }

    public void deleteSubject(int id) {
        sqLiteDatabase.delete("subject", "id = " + id, null);
        sqLiteDatabase.delete("detail_subject", "idSubject = " + id, null);
    }

    public void updateSubject(Subject subject) {
        Log.d("DB", "updateSubject: " + subject.toString());
        sqLiteDatabase = myDatabase.getWritableDatabase();

        // add to subject
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", subject.getName());
        contentValues.put("idTeacher", subject.getIdTeacher());
        contentValues.put("dayStart", subject.getDayStart());
        contentValues.put("dayEnd", subject.getDayEnd());
        if (subject.isAlarm()) {
            contentValues.put("alarm", 1);
        } else {
            contentValues.put("alarm", 0);
        }
        contentValues.put("attendance", subject.getAttendance());
        contentValues.put("maxAttendance", subject.getMaxAttendance());
        contentValues.put("note", subject.getNote());

        sqLiteDatabase.update("subject", contentValues,
                "id = ?", new String[]{String.valueOf(subject.getId())});

        sqLiteDatabase.delete("detail_subject", "idSubject = " + subject.getId(), null);
        // add to detail subject
        for (int i = 0; i < subject.getRoomList().size(); i++) {
            contentValues = new ContentValues();
            contentValues.put("idSubject", subject.getId());
            contentValues.put("idRoom", subject.getRoomList().get(i));
            contentValues.put("dayOfWeek", subject.getDaysPicked().get(i));
            contentValues.put("timeStart", subject.getTimeStartList().get(i));
            contentValues.put("timeEnd", subject.getTimeEndList().get(i));
            sqLiteDatabase.insert("detail_subject", null, contentValues);
        }
    }

    public void updateTeacher(int idTeacher) {
        sqLiteDatabase = myDatabase.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("idTeacher", 0);
        sqLiteDatabase.update("subject", contentValues,
                "idTeacher = ?", new String[]{String.valueOf(idTeacher)});
    }

}
