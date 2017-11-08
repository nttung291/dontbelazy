package com.example.sonnv.dontbelazy.activities;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.CollapsibleActionView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.CalendarView;
import android.widget.ImageView;

import com.example.sonnv.dontbelazy.EventDecorator;
import com.example.sonnv.dontbelazy.R;
import com.example.sonnv.dontbelazy.activities.calendar.DaynowDecor;
import com.example.sonnv.dontbelazy.databases.CourseMode;
import com.example.sonnv.dontbelazy.databases.Subject;
import com.example.sonnv.dontbelazy.databases.SubjectHandleDB;
import com.example.sonnv.dontbelazy.utils.Utils;
import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.CalendarMode;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Executors;

public class CalendarActivity extends AppCompatActivity implements View.OnClickListener{

    private static final String TAG = CalendarActivity.class.toString();
    public static final String KEY_CALENDAR = "cuonghx";
    private ImageView ivBack;
    private CollapsingToolbarLayout collapsingToolbarLayout;



    //    CompactCalendarView compactCalendarView ;
    MaterialCalendarView materialCalendarView;
//    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMMM - yyyy", Locale.getDefault());


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        materialCalendarView  = (MaterialCalendarView) findViewById(R.id.clv_course);
        materialCalendarView.state().edit().setFirstDayOfWeek(Calendar.MONDAY).setCalendarDisplayMode(CalendarMode.MONTHS).commit();
        ivBack = (ImageView) findViewById(R.id.iv_calender_back);
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar_calender);
        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/Gabriola.ttf");
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar_contact));

        collapsingToolbarLayout.setTitle("Time Table");
        collapsingToolbarLayout.setCollapsedTitleTypeface(typeface);
        collapsingToolbarLayout.setExpandedTitleTypeface(typeface);
        collapsingToolbarLayout.setExpandedTitleColor(getResources().getColor(R.color.titleTextColor));

        materialCalendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                Intent intent = new Intent(CalendarActivity.this , ActivityByDay.class);

                Log.d(TAG, "onDateSelected: " + date.getDate().getTime());

                CourseMode courseMode = new CourseMode();
                Utils.selectDate = date.getDate();
//                courseMode.setDateStart(new Date());

//                Calendar calendar = Calendar.getInstance();
//                calendar.setTimeInMillis(date.getDate().getTimeStart());
//                date1.setTimeStart(Long.parseLong(string));
                ImageView imageView = (ImageView) findViewById(R.id.iv_image_calendar);
                ActivityOptionsCompat optionsubject = ActivityOptionsCompat.makeSceneTransitionAnimation(CalendarActivity.this,
                        new Pair<View, String>(imageView, ViewCompat.getTransitionName(imageView)) );
                courseMode.setCurrentDate(date.getDate());
                Bundle bundle = new Bundle();
                bundle.putSerializable(CalendarActivity.KEY_CALENDAR, courseMode);
                intent.putExtras(bundle);
                CalendarActivity.this.startActivity(intent, optionsubject.toBundle());
            }
        });

        Calendar instance = Calendar.getInstance();
        materialCalendarView.setSelectedDate(instance.getTime());
        materialCalendarView.setSelectionColor(Color.parseColor("#69b86d"));
        materialCalendarView.addDecorators(
                new DaynowDecor(this)
        );

        ivBack.setOnClickListener(this);

    }

    @Override
    protected void onStart() {
        super.onStart();
        new ApiSimulator().executeOnExecutor(Executors.newSingleThreadExecutor());
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.iv_calender_back:
                super.onBackPressed();
        }
    }

    private class ApiSimulator extends AsyncTask<Void, Void, List<CalendarDay>> {


        @Override
        protected List<CalendarDay> doInBackground(@NonNull Void... voids) {

            ArrayList<CalendarDay> dates = new ArrayList<>();
            List<Subject> subjects = SubjectHandleDB.getInstance(CalendarActivity.this).getListSubject();
            SimpleDateFormat sdfDate = new SimpleDateFormat("dd/MM/yyyy");
            for (Subject subject : subjects){

                if (subject.getDayStart() != null && subject.getDayEnd() != null){
                    Calendar end = Calendar.getInstance();
                    end.setTime(Utils.getDate(sdfDate, subject.getDayEnd()));

                    for (String s : subject.getDaysPicked()){
                        Calendar start = Calendar.getInstance();
                        start.setTime(Utils.getDate(sdfDate, subject.getDayStart()));
                        while (true){

                            if (start.getTime().getDay() == Utils.formatDateOfWeek(s)){
                                dates.add(CalendarDay.from(start));
                                start.add(Calendar.DAY_OF_MONTH, + 7);
                            }else {
                                start.add(Calendar.DAY_OF_MONTH, +1);
                            }

                            if (start.getTime().getTime() >= end.getTime().getTime()){
                                break;
                            }

                        }
                    }

                }
            }

            Log.d(TAG, "doInBackground: run");

            return dates;
        }

        @Override
        protected void onPostExecute(@NonNull List<CalendarDay> calendarDays) {
            super.onPostExecute(calendarDays);

            if (isFinishing()) {
                return;
            }
            materialCalendarView.addDecorator(new EventDecorator(Color.RED, calendarDays));
        }
    }
}
