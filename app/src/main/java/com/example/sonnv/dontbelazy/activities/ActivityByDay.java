package com.example.sonnv.dontbelazy.activities;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sonnv.dontbelazy.R;
import com.example.sonnv.dontbelazy.adapter.CourseAdapter;
import com.example.sonnv.dontbelazy.adapter.RVSubjectAdapter;
import com.example.sonnv.dontbelazy.databases.CourseMode;
import com.example.sonnv.dontbelazy.databases.Subject;
import com.example.sonnv.dontbelazy.databases.SubjectCalendar;
import com.example.sonnv.dontbelazy.databases.SubjectHandleDB;
import com.example.sonnv.dontbelazy.utils.Utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ActivityByDay extends AppCompatActivity {

    private static final String TAG = ActivityByDay.class.toString();
    private RecyclerView listView;
    private TextView tvDay;
    private TextView tvMonth;
    private ImageView back;
    private List<Subject> list;
    private List<SubjectCalendar> listTest;
    private CourseMode courseMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        list = new ArrayList<>();
        listTest = new ArrayList<>();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_by_day);

        tvDay = (TextView) findViewById(R.id.tv_day);
        tvMonth = (TextView) findViewById(R.id.tv_month);
        back = (ImageView) findViewById(R.id.iv_coures_list_back);

        this.courseMode = (CourseMode) getIntent().getExtras().getSerializable(CalendarActivity.KEY_CALENDAR);

        SimpleDateFormat spDateFm = new SimpleDateFormat("dd");

        tvDay.setText(spDateFm.format(courseMode.getCurrentDate()));

        spDateFm = new SimpleDateFormat("MM");
        tvMonth.setText("Th "+ spDateFm.format(courseMode.getCurrentDate()));

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");


        for (Subject subject : SubjectHandleDB.getInstance(this).getListSubject()){
            for (int i = 0; i < subject.getDaysPicked().size(); i ++){
                String s = subject.getDaysPicked().get(i);
                Date current = Utils.getDate(dateFormat, dateFormat.format(courseMode.getCurrentDate()));
                Date end = new Date(3000,1,1);
                Date start = new Date(1000,1,1);
                if (subject.getDayEnd() != null && subject.getDayStart() != null) {
                    end = Utils.getDate(dateFormat, subject.getDayEnd());
                    start = Utils.getDate(dateFormat, subject.getDayStart());
                }
                if (courseMode.getCurrentDate().getDay() == Utils.formatDateOfWeek(s)
                        && current.getTime() >= start.getTime()
                        && current.getTime() <= end.getTime() ){
                    SubjectCalendar subjectCalendar = new SubjectCalendar(subject, subject.getTimeEndList().get(i), subject.getName(), subject.getTimeStartList().get(i), subject.getRoomList().get(i));
                    list.add(subject);
                    Log.d(TAG, "onCreate: " + subjectCalendar.getTimeStart());
                    listTest.add(subjectCalendar);
                }
            }
        }
        listTest = Utils.sortSubjectByDate(listTest);


        listView = (RecyclerView) findViewById(R.id.lv_calendar);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        listView.setLayoutManager(llm);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityByDay.super.onBackPressed();
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

        CourseAdapter rvAdapter = new CourseAdapter(this , list, courseMode.getCurrentDate().getDay(), listTest);
        listView.setAdapter(rvAdapter);
    }
}
