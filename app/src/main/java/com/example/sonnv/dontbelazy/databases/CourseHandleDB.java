package com.example.sonnv.dontbelazy.databases;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.sonnv.dontbelazy.utils.Utils;

import java.io.Closeable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by cuonghx2709 on 9/21/2017.
 */

public class CourseHandleDB {
    private static final String TAG = CourseHandleDB.class.toString();
    private static MyDatabase myDatabase;
    Context context;

    public CourseHandleDB(Context context){
        this.context = context;
        this.myDatabase = new MyDatabase(context);
    }

    private static  CourseHandleDB courseHandleDB;

    public  static CourseHandleDB getInstence(Context context){
        if (courseHandleDB == null ){
            courseHandleDB = new CourseHandleDB(context);
        }
        return courseHandleDB;
    }
    SQLiteDatabase sqLiteDatabase;

    public  List<CourseMode> getList(){


        List<CourseMode> list = new ArrayList<>();
        sqLiteDatabase = myDatabase.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM tb_course", null);



        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            int id = cursor.getInt(0);
            String name = cursor.getString(1);
            String timeStart = cursor.getString(2);
            String timeEnd = cursor.getString(3);
            String room = cursor.getString(4);

            CourseMode courseMode = new CourseMode(name, new Date(), new Date(), room, id);
            Date start = new Date();
            start.setTime(Utils.getLongFromString(timeStart));
            courseMode.setDateStart(start);

            Date end = new Date();
            end.setTime(Utils.getLongFromString(timeEnd));
            courseMode.setDateEnd(end);

            Log.d(TAG, "getList: " + courseMode.toString());
            cursor.moveToNext();
            list.add(courseMode);
        }

        cursor.close();


        return list;
    }

    public void open() {
        this.sqLiteDatabase = myDatabase.getWritableDatabase();
    }

    /**
     * Close the database connection.
     */
    public void close() {
        if (sqLiteDatabase != null) {
            this.sqLiteDatabase.close();
        }
    }
}
