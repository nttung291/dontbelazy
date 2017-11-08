package com.example.sonnv.dontbelazy.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.util.Log;

import com.example.sonnv.dontbelazy.adapter.notes.SpinnerTimeAdapter;
import com.example.sonnv.dontbelazy.databases.Note;
import com.example.sonnv.dontbelazy.databases.Subject;
import com.example.sonnv.dontbelazy.databases.SubjectCalendar;
import com.example.sonnv.dontbelazy.databases.SubjectHandleDB;
import com.example.sonnv.dontbelazy.databases.SubjectOfstart;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static android.content.ContentValues.TAG;

/**
 * Created by cuonghx2709 on 9/22/2017.
 */

public class Utils {

    public static enum BackSubject{ADDING, SHOWING, SHOWLIST, START};


    public static Date selectDate = new Date();
    public static long getLongFromString(String s){
        long l = 0;
        try {
             l = Long.parseLong(s);
        } catch (NumberFormatException nfe) {
            System.out.println("NumberFormatException: " + nfe.getMessage());
        }
        return l;
    }
    public static Date getDate(SimpleDateFormat simpleDateFormat, String s){
        try {
            return simpleDateFormat.parse(s);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static int formatDateOfWeek(String s){
        switch (s){
            case "MON":
                return 1;
            case "TUE":
                return 2;
            case "WED":
                return 3;
            case "THU":
                return 4;
            case  "FRI":
                return 5;
            case "SAT":
                return 6;
            case "SUN" :
                return 0;
        }
        return 0;
    }

    public static int date1CompareDate2(int[] date1, int[] date2) {
        int year1 = date1[2];
        int month1 = date1[1];
        int day1 = date1[0];
        int year2 = date2[2];
        int month2 = date2[1];
        int day2 = date2[0];

        if (year1 > year2) return 1;
        else if (year2 > year1) return 2;

        if (month2 > month1) return 2;
        else if (month1 > month2) return 1;

        if (day2 > day1) return 2;
        else if(day1 > day2) return 1;

        return 0;
    }

    public static int time1CompareTime2(int[] time1, int[] time2) {
        int hour1 = time1[0];
        int hour2 = time2[0];
        int minute1 = time1[1];
        int minute2 = time2[1];

        if (hour1 > hour2) return 1;
        else if (hour2 > hour1) return 2;

        if (minute1 > minute2) return 1;
        else if (minute2 > minute1) return 2;

        return 0;
    }

    public static int[] getTime(String s) {
        int[] timeList = new int[2];
        String hour = "";
        String minute = "";
        int i = 0;
        while (s.charAt(i) != ':') {
            hour += s.charAt(i);
            i++;
        }
        i++;
        while (i < s.length()) {
            minute += s.charAt(i);
            i++;
        }

        timeList[0] = Integer.parseInt(hour);
        timeList[1] = Integer.parseInt(minute);
        return timeList;
    }

    public static int[] getDate(String s) {
        int[] dateList = new int[3];
        String year = "";
        String month = "";
        String day = "";
        int i = 0;
        while (s.charAt(i) != '/') {
            day += s.charAt(i);
            i++;
        }
        dateList[0] = Integer.parseInt(day);
        i++;

        while (s.charAt(i) != '/') {
            month += s.charAt(i);
            i++;
        }
        dateList[1] = Integer.parseInt(month);
        i++;

        while (i < s.length()) {
            year += s.charAt(i);
            i++;
        }
        dateList[2] = Integer.parseInt(year);
        return dateList;
    }

    public static void showWarningDialog(Context context, String message) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setTitle("Warning !!!");
        alertDialogBuilder
                .setMessage(message)
                .setCancelable(false)
                .setNegativeButton("Ok",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        // if this button is clicked, just close
                        // the dialog box and do nothing
                        dialog.cancel();
                    }
                });
        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();
        // show it
        alertDialog.show();
    }
    public static List<Note> sortNoteByDate(List<Note> notes) {
        Collections.sort(notes, new Comparator<Note>() {
            public int compare(Note o1, Note o2) {
                DateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.US);

                Date date1 = null;
                Date date2 = null;

                try {
                    date1=format.parse(o1.getTime());
                    date2=format.parse(o2.getTime());

                } catch (ParseException e) {
                    e.printStackTrace();
                }

                return date1.compareTo(date2);
            }
        });

        return notes;
    }

    public static List<SubjectOfstart> getListStart(Context context){
        List<SubjectOfstart> list = new ArrayList<>();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");


        for (Subject subject : SubjectHandleDB.getInstance(context).getListSubject()){

            for (int i= 0; i < subject.getDaysPicked().size(); i++){
                String s = subject.getDaysPicked().get(i);
                Date now = new Date();
                Date date = new Date();
                Date current = new Date(now.getYear(), now.getMonth(), now.getDate());

                Date end = new Date(3000,1,1);
                Date start = new Date(1000,1,1);
                if (subject.getDayEnd() != null && subject.getDayStart() != null) {
                    end = Utils.getDate(dateFormat, subject.getDayEnd());
                    start = Utils.getDate(dateFormat, subject.getDayStart());
                }

                if (current.getDay() == Utils.formatDateOfWeek(s)
                        && current.getTime() >= start.getTime()
                        && current.getTime() <= end.getTime() ){
                    SubjectOfstart subjectOfstart  = new SubjectOfstart(subject, subject.getName(), subject.getTimeStartList().get(i), subject.getRoomList().get(i));
                    list.add(subjectOfstart);
                }
            }
        }
        return list;
    }
    public static List<SubjectOfstart> sortSubjectOfStartByDate(List<SubjectOfstart> list) {
        Collections.sort(list, new Comparator<SubjectOfstart>() {
            public int compare(SubjectOfstart o1, SubjectOfstart o2) {
                DateFormat format = new SimpleDateFormat("HH:mm", Locale.US);

                Date date1 = null;
                Date date2 = null;

                try {
                    Log.d(TAG, "compare: " + o1.getTime());
                    date1=format.parse(o1.getTime());
                    date2=format.parse(o2.getTime());

                } catch (ParseException e) {
                    e.printStackTrace();
                }

                return date1.compareTo(date2);
            }
        });

        return list;
    }

    public static List<SubjectCalendar> sortSubjectByDate(List<SubjectCalendar> list) {
        Collections.sort(list, new Comparator<SubjectCalendar>() {
            public int compare(SubjectCalendar o1, SubjectCalendar o2) {
                DateFormat format = new SimpleDateFormat("HH:mm", Locale.US);

                Date date1 = null;
                Date date2 = null;

                try {
                    date1=format.parse(o1.getTimeStart());
                    date2=format.parse(o2.getTimeStart());

                } catch (ParseException e) {
                    e.printStackTrace();
                }

                return date1.compareTo(date2);
            }
        });

        return list;
    }

    public static String getNowDayofWeek(){

        Date date = new Date();
        switch (date.getDay()){
            case 0:
                return "Sunday";
            case 1:
                return "Monday";
            case 2:
                return "Tuesday";
            case 3:
                return "Wednesday";
            case  4:
                return "Thursday";
            case 5:
                return "Friday";
            case 6 :
                return "Saturday";
        }
        return "";
    }

    public static String getNowMonth(int month){

        Date date = new Date();
        switch (month){
            case 0:
                return "January";
            case 1:
                return "February";
            case 2:
                return "March";
            case 3:
                return "April";
            case  4:
                return "May";
            case 5:
                return "June";
            case 6 :
                return "July";
            case 7:
                return "August";
            case 8:
                return "September";
            case 9:
                return "October";
            case 10:
                return "November";
            case 11:
                return "December";
        }
        return "";
    }

    public static String getDate(int id){
        Calendar date = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");

        switch (id){
            case 1:
                Date need1 = date.getTime();
                return simpleDateFormat.format(need1);

            case 2:
                date.add(Calendar.DAY_OF_MONTH, +1);
                Date need2 = date.getTime();
                return simpleDateFormat.format(need2);
            case 3:
                date.add(Calendar.DAY_OF_MONTH, +7);
                Date need = date.getTime();
                return simpleDateFormat.format(need);

            case 4:
                break;

        }
        return simpleDateFormat.format(date.getTime());
    }

}
