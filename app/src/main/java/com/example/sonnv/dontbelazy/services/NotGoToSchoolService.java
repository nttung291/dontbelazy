package com.example.sonnv.dontbelazy.services;

import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.sonnv.dontbelazy.databases.Subject;
import com.example.sonnv.dontbelazy.databases.SubjectHandleDB;
import com.example.sonnv.dontbelazy.utils.AlarmUtils;

/**
 * Created by SONNV on 10/1/2017.
 */

public class NotGoToSchoolService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        SchedulingService.notificationManager.cancel(1);
        Log.d("Amen", "onStartCommand: ");
        Subject subject = AlarmUtils.preSubject;
        subject.setAttendance(subject.getAttendance() + 1);

        SubjectHandleDB.getInstance(this).updateSubject(subject);

        return super.onStartCommand(intent, flags, startId);
    }
}
