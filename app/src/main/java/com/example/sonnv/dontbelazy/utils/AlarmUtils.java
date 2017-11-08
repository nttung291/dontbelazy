package com.example.sonnv.dontbelazy.utils;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import com.example.sonnv.dontbelazy.databases.Note;
import com.example.sonnv.dontbelazy.databases.NoteHandleDB;
import com.example.sonnv.dontbelazy.databases.Subject;
import com.example.sonnv.dontbelazy.databases.SubjectHandleDB;
import com.example.sonnv.dontbelazy.services.SchedulingService;

import java.lang.reflect.Array;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

/**
 * Created by SONNV on 9/28/2017.
 */

public class AlarmUtils {
    private static final String TAG = "Amen";

    public static Subject subjectNext = new Subject();
    public static Subject preSubject = new Subject();
    public static String roomSubject = "";
    public static String attendence = "";

    public static Note noteNext = new Note();

    public static void create(Context context,int id) {
        Log.d(TAG, "create: ");

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, SchedulingService.class);

        long time = Long.MAX_VALUE;
        if (id == 1) {
            List<Subject> subjectList = SubjectHandleDB.getInstance(context).getListSubject();
            time = nextTimeSubject(subjectList);
            intent.putExtra("123",id);
        } else if (id == 2){
                Log.d(TAG, "Ok");
                List<Note> noteList = NoteHandleDB.getInstence(context).getList();
                time = nextTimeNote(noteList);
                intent.putExtra("123", id);
            }

        if (time != Long.MAX_VALUE) {
            PendingIntent pendingIntent =
                    PendingIntent.getService(context, id, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                alarmManager
                        .setExact(AlarmManager.RTC_WAKEUP, time, pendingIntent);
            } else {
                alarmManager
                        .set(AlarmManager.RTC_WAKEUP, time, pendingIntent);
            }
        }

    }

    public static long nextTimeSubject(List<Subject> subjectList) {
        Log.d(TAG, "nextTimeSubject: ");
        long timeCounterMin = Long.MAX_VALUE;

        Calendar pre30MiniuteDate = Calendar.getInstance();
        pre30MiniuteDate.add(Calendar.MINUTE, 30);
        long secondsPre30 = pre30MiniuteDate.getTimeInMillis();

        SimpleDateFormat formatDayOfWeek = new SimpleDateFormat("EEE");
        SimpleDateFormat formatTime = new SimpleDateFormat("HH:mm");
        SimpleDateFormat formatDay = new SimpleDateFormat("dd/MM/yyyy");

        Date date = new Date();
        int[] timeCurrent = Utils.getTime(formatTime.format(date));
        int[] dayCurrent = Utils.getDate(formatDay.format(date));
        Log.d(TAG, "timeCurrent + dayCurrent " + Arrays.toString(timeCurrent) + Arrays.toString(dayCurrent));


        for (Subject subject : subjectList) {
            int[] dayStart = Utils.getDate(subject.getDayStart());
            int[] dayEnd = Utils.getDate(subject.getDayEnd());

            int dateBigger = Utils.date1CompareDate2(dayCurrent, dayEnd);
            Log.d(TAG, "dayCurrent + dayEnd" + Arrays.toString(dayCurrent) + " " + Arrays.toString(dayEnd));

            if (dateBigger != 1) {
                Log.d(TAG, "dateBigger" + dateBigger);
                for (int i = 0; i < subject.getDaysPicked().size(); i++) {


                    String dayOfWeek = subject.getDaysPicked().get(i);

                    Log.d(TAG, "dayOfWeek: " + dayOfWeek);

                    GregorianCalendar gregorianCalendarCurrent = nextDay(dayCurrent, dayOfWeek);
                    GregorianCalendar gregorianCalendarNext = nextDay(dayStart, dayOfWeek);

                    int[] nextDayFromCurrent = Utils.getDate(formatDay.format(gregorianCalendarCurrent.getTime()));

                    Log.d(TAG, "nextDayFromCurrent: " + Arrays.toString(nextDayFromCurrent));
                    int[] nextDayFromStart = Utils.getDate(formatDay.format(gregorianCalendarNext.getTime()));

                    Log.d(TAG, "nextDayFromStart: " + Arrays.toString(nextDayFromStart));
                    dateBigger = Utils.date1CompareDate2(nextDayFromCurrent, dayEnd);
                    Log.d(TAG, "dateBigger: " + dateBigger);
                    if (dateBigger != 1) {
                        dateBigger = Utils.date1CompareDate2(nextDayFromCurrent, nextDayFromStart);
                        Log.d(TAG, "dateBigger: " + dateBigger);
                        int[] nextTimeFromCurrent = Utils.getTime(subject.getTimeStartList().get(i));
                        Log.d(TAG, "nextTimeFromCurrent: " + Arrays.toString(nextTimeFromCurrent));
                        switch (dateBigger) {
                            case 0:
                                dateBigger = Utils.date1CompareDate2(dayCurrent, nextDayFromCurrent);
                                Log.d(TAG, "dateBigger: " + dateBigger);
                                if (dateBigger == 0 && Utils.time1CompareTime2(timeCurrent, nextTimeFromCurrent) == 2) {
                                    Calendar calendar = getCalendar(nextDayFromCurrent[2], nextDayFromCurrent[1], nextDayFromCurrent[0], nextTimeFromCurrent[0], nextTimeFromCurrent[1]);
                                    long seconds = calendar.getTimeInMillis();
                                    if (seconds < timeCounterMin && secondsPre30 < seconds) {
                                        calendar = preCalendar(calendar);
                                        timeCounterMin = calendar.getTimeInMillis();

                                        subjectNext = subject;
                                        if (subject.getRoomList().size() >= i) {
                                            roomSubject = subject.getRoomList().get(i);
                                        } else roomSubject = "";
                                        attendence = subject.getAttendance() + "/" + subject.getMaxAttendance();
                                    }
                                } else if (dateBigger == 2) {
                                    Calendar calendar = getCalendar(nextDayFromCurrent[2], nextDayFromCurrent[1], nextDayFromCurrent[0], nextTimeFromCurrent[0], nextTimeFromCurrent[1]);
                                    long seconds = calendar.getTimeInMillis();
                                    if (seconds < timeCounterMin && secondsPre30 < seconds) {
                                        calendar = preCalendar(calendar);
                                        timeCounterMin = calendar.getTimeInMillis();

                                        subjectNext = subject;
                                        if (subject.getRoomList().size() >= i) {
                                            roomSubject = subject.getRoomList().get(i);
                                        } else roomSubject = "";
                                        attendence = subject.getAttendance() + "/" + subject.getMaxAttendance();
                                    }
                                }
                                break;
                            case 1:
                                dateBigger = Utils.date1CompareDate2(dayCurrent, nextDayFromCurrent);
                                Log.d(TAG, "dateBigger: " + dateBigger);

                                if (dateBigger == 0 && Utils.time1CompareTime2(timeCurrent, nextTimeFromCurrent) == 2) {
                                    Calendar calendar = getCalendar(nextDayFromCurrent[2], nextDayFromCurrent[1], nextDayFromCurrent[0], nextTimeFromCurrent[0], nextTimeFromCurrent[1]);
                                    long seconds = calendar.getTimeInMillis();
                                    if (seconds < timeCounterMin && secondsPre30 < seconds) {
                                        calendar = preCalendar(calendar);
                                        timeCounterMin = calendar.getTimeInMillis();

                                        subjectNext = subject;
                                        if (subject.getRoomList().size() >= i) {
                                            roomSubject = subject.getRoomList().get(i);
                                        } else roomSubject = "";
                                        attendence = subject.getAttendance() + "/" + subject.getMaxAttendance();
                                    }
                                } else if (dateBigger == 2) {
                                    Calendar calendar = getCalendar(nextDayFromCurrent[2], nextDayFromCurrent[1], nextDayFromCurrent[0], nextTimeFromCurrent[0], nextTimeFromCurrent[1]);
                                    long seconds = calendar.getTimeInMillis();
                                    if (seconds < timeCounterMin && secondsPre30 < seconds) {
                                        calendar = preCalendar(calendar);
                                        timeCounterMin = calendar.getTimeInMillis();
                                        subjectNext = subject;
                                        if (subject.getRoomList().size() >= i) {
                                            roomSubject = subject.getRoomList().get(i);
                                        } else roomSubject = "";
                                        attendence = subject.getAttendance() + "/" + subject.getMaxAttendance();
                                    }
                                }
                                break;
                            case 2:
                                Calendar calendar = getCalendar(nextDayFromStart[2], nextDayFromStart[1], nextDayFromStart[0], nextTimeFromCurrent[0], nextTimeFromCurrent[1]);
                                long seconds = calendar.getTimeInMillis();
                                if (seconds < timeCounterMin && secondsPre30 < seconds) {
                                    calendar = preCalendar(calendar);
                                    timeCounterMin = calendar.getTimeInMillis();
                                    subjectNext = subject;
                                    if (subject.getRoomList().size() >= i) {
                                        roomSubject = subject.getRoomList().get(i);
                                    } else roomSubject = "";
                                    attendence = subject.getAttendance() + "/" + subject.getMaxAttendance();
                                }
                                break;
                        }
                    }
                }
            }
        }
        Log.d("Amen", "nextTimeSubject: " + timeCounterMin);
        return timeCounterMin;
    }

    public static long nextTimeNote(List<Note> noteList){
        long timeToNoti = Long.MAX_VALUE;

        DateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.US);
        SimpleDateFormat formatDayOfWeek = new SimpleDateFormat("EEE");
        SimpleDateFormat formatTime = new SimpleDateFormat("HH:mm");
        SimpleDateFormat formatDay = new SimpleDateFormat("dd/MM/yyyy");

        Date currentDate = new Date();
        int[] timeCurrent = Utils.getTime(formatTime.format(currentDate));
        int[] dayCurrent = Utils.getDate(formatDay.format(currentDate));

        for (Note note : noteList) {
            if (note.isNotifi()) {
                Date date = null;
                try {
                    date = format.parse(note.getTime());
                } catch (ParseException e) {
                    Log.d(TAG, "nextTimeNote: " + "Khong parse dc " + note.toString());
                }
                int[] nextTime = Utils.getTime(formatTime.format(date));
                int[] nextDay = Utils.getDate(formatDay.format(date));
                Log.d(TAG, "nextTimeNote: " + Arrays.toString(nextTime) + " " + Arrays.toString(nextDay));
                Calendar calendar = getCalendar(nextDay[2], nextDay[1], nextDay[0], nextTime[0], nextTime[1]);
                long getTime = Long.MAX_VALUE;

                Log.d(TAG, "nextTimeNote: Time Current " + Arrays.toString(timeCurrent) + " " + Arrays.toString(dayCurrent));
                if ((Utils.date1CompareDate2(dayCurrent, nextDay) == 0 && Utils.time1CompareTime2(timeCurrent, nextTime) == 2) ||
                        Utils.date1CompareDate2(dayCurrent, nextDay) == 2) {
                    getTime = calendar.getTimeInMillis();
                    Log.d(TAG, "nextTimeNote: " + "GetTime " + getTime);
                }

                if (getTime < timeToNoti) {
                    timeToNoti = getTime;
                    noteNext = note;
                }
            }
        }
        return  timeToNoti;
    }

    public static GregorianCalendar nextDay(int[] date, String dayOfWeek) {
        GregorianCalendar gregorianCalendar = new GregorianCalendar( date[2], date[1] - 1, date[0]);
        switch (dayOfWeek) {
            case "SUN":
                while( gregorianCalendar.get( Calendar.DAY_OF_WEEK ) != Calendar.SUNDAY )
                    gregorianCalendar.add( Calendar.DATE, 1 );
                break;
            case "MON":
                while( gregorianCalendar.get( Calendar.DAY_OF_WEEK ) != Calendar.MONDAY )
                    gregorianCalendar.add( Calendar.DATE, 1 );
                break;
            case "TUE":
                while( gregorianCalendar.get( Calendar.DAY_OF_WEEK ) != Calendar.TUESDAY )
                    gregorianCalendar.add( Calendar.DATE, 1 );
                break;
            case "WED":
                while( gregorianCalendar.get( Calendar.DAY_OF_WEEK ) != Calendar.WEDNESDAY )
                    gregorianCalendar.add( Calendar.DATE, 1 );
                break;
            case "THU":
                while( gregorianCalendar.get( Calendar.DAY_OF_WEEK ) != Calendar.THURSDAY )
                    gregorianCalendar.add( Calendar.DATE, 1 );
                break;
            case "FRI":
                while( gregorianCalendar.get( Calendar.DAY_OF_WEEK ) != Calendar.FRIDAY )
                    gregorianCalendar.add( Calendar.DATE, 1 );
                break;
            case "SAT":
                while( gregorianCalendar.get( Calendar.DAY_OF_WEEK ) != Calendar.SATURDAY )
                    gregorianCalendar.add( Calendar.DATE, 1 );
                break;
        }
        return gregorianCalendar;
    }



    public static Calendar getCalendar(int year, int month, int day, int hour, int minute) {
        Log.d(TAG, "getCalendar: " + year + " " + month + " " + day + " " + hour + " " + minute);
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month - 1);
        calendar.set(Calendar.DAY_OF_MONTH, day);
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);
        return calendar;
    }

    public static Calendar preCalendar(Calendar calendar) {
        calendar.add(Calendar.MINUTE, -30);
        return calendar;
    }

    public static void cancelAlarm(Context context, int id) {
        Intent myIntent = new Intent(context, SchedulingService.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, id, myIntent, 0);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);
    }
}
