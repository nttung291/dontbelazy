package com.example.sonnv.dontbelazy.services;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Icon;
import android.provider.Settings;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.example.sonnv.dontbelazy.R;
import com.example.sonnv.dontbelazy.activities.StartActivity;
import com.example.sonnv.dontbelazy.adapter.RVSubjectAdapter;
import com.example.sonnv.dontbelazy.databases.Note;
import com.example.sonnv.dontbelazy.databases.Subject;
import com.example.sonnv.dontbelazy.utils.AlarmUtils;
import com.example.sonnv.dontbelazy.utils.Utils;

public class SchedulingService extends IntentService {
    private static final int TIME_VIBRATE = 1000;
    public static NotificationManager notificationManager;

    public SchedulingService() {
        super(SchedulingService.class.getSimpleName());
        Log.d("Start", "SchedulingService: ");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d("Start", "onHandleIntent: ");

        int id = intent.getIntExtra("123", 0);

        if (id == 1) {
            Subject subject = AlarmUtils.subjectNext;

            AlarmUtils.preSubject  = subject;

            String contentTitle = "Go to ";

            if (!AlarmUtils.roomSubject.equals("")) {
                contentTitle = contentTitle + subject.getName() + " class at " + AlarmUtils.roomSubject + "?";
            } else {
                contentTitle = contentTitle + subject.getName() + " class?";
            }

            String contentText = "Absent: " + AlarmUtils.attendence;

            Intent notificationIntent = new Intent(this, StartActivity.class);
            notificationIntent
                    .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            int requestID = (int) System.currentTimeMillis();
            PendingIntent contentIntent = PendingIntent
                    .getActivity(this, requestID, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);


            Intent goToSchoolIntent = new Intent(this, GoToSchoolService.class);
            PendingIntent goToSchoolPendingIntent = PendingIntent
                    .getService(this, requestID, goToSchoolIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            NotificationCompat.Action actionYes = new NotificationCompat.Action.Builder
                    (R.drawable.ic_access_alarm_black_24dp, "Yes", goToSchoolPendingIntent).build();

            Intent notGoToSchoolIntent = new Intent(this, NotGoToSchoolService.class);
            PendingIntent notGoToSchoolPendingIntent = PendingIntent
                    .getService(this, requestID, notGoToSchoolIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            NotificationCompat.Action actionNo = new NotificationCompat.Action.Builder
                    (R.drawable.ic_access_alarm_black_24dp, "No", notGoToSchoolPendingIntent).build();

            NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                    .setSmallIcon(R.drawable.ic_noti)
                    .setContentTitle(contentTitle)
                    .setContentText(contentText)
                    .setSound(Settings.System.DEFAULT_NOTIFICATION_URI)
                    .setDefaults(Notification.DEFAULT_SOUND)
                    .setPriority(6)
                    .setVibrate(new long[]{TIME_VIBRATE, TIME_VIBRATE, TIME_VIBRATE, TIME_VIBRATE,
                            TIME_VIBRATE})
                    .setContentIntent(contentIntent)
                    .setAutoCancel(true)
                    .addAction(actionYes)
                    .addAction(actionNo);

            notificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(id, builder.build());

        } else if (id == 2) {
            Note note = AlarmUtils.noteNext;

            Intent notificationIntent = new Intent(this, StartActivity.class);
            notificationIntent
                    .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            int requestID = (int) System.currentTimeMillis();
            PendingIntent contentIntent = PendingIntent
                    .getActivity(this, requestID, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);


            NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                    .setSmallIcon(R.drawable.ic_noti)
                    .setContentTitle("Note")
                    .setContentText(note.getText())
                    .setSound(Settings.System.DEFAULT_NOTIFICATION_URI)
                    .setDefaults(Notification.DEFAULT_SOUND)
                    .setPriority(6)
                    .setVibrate(new long[]{TIME_VIBRATE, TIME_VIBRATE, TIME_VIBRATE, TIME_VIBRATE,
                            TIME_VIBRATE})
                    .setContentIntent(contentIntent)
                    .setAutoCancel(true);

            notificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(id, builder.build());

        }

        AlarmUtils.create(this, id);
        stopSelf();
    }


}
